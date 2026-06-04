package network;

import gameLogic.GameLogic;
import gameLogic.ObjectiveCard;
import menu.Setup;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

// Central authoritative server for the Risk match.
public class RiskServer implements Closeable {
    // Default port used by Start.java and by direct RiskServer execution.
    public static final int DEFAULT_PORT = 5555;

    private final int port;
    // CopyOnWriteArrayList lets broadcast iterate safely while clients join or leave.
    private final List<ClientHandler> clients;
    // Protects lobby-only state: first player, player limit checks, and game start.
    private final Object lobbyLock;
    private final Object gameLock;
    private ServerSocket serverSocket;
    // The first joined player is the only player allowed to send the Start command.
    private String firstPlayerNickName;
    // Once true, new players are rejected and setup cannot run again.
    private boolean gameStarted;
    private GameLogic gameLogic;
    private volatile boolean running;
    private final Map<String, String> lobbyPlayerColors;

    public RiskServer() {
        this(DEFAULT_PORT);
    }

    public RiskServer(int port) {
        this.port = port;
        this.clients = new CopyOnWriteArrayList<>();
        this.lobbyLock = new Object();
        this.gameLock = new Object();
        this.lobbyPlayerColors = new LinkedHashMap<>();
    }

    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        RiskServer server = new RiskServer(port);
        server.start();
    }

    // Opens the server socket and creates one ClientHandler thread per accepted client.
    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;
        System.out.println("Risk server started on port " + port);

        while (running) {
            Socket socket = serverSocket.accept();
            // Reject before creating a handler when the lobby is full or already started.
            if (!canAcceptNewClient()) {
                rejectClient(socket, "The game already has 6 players or has already started.");
                continue;
            }

            ClientHandler client = new ClientHandler(socket);
            clients.add(client);
            new Thread(client, "risk-client-" + socket.getRemoteSocketAddress()).start();
        }
    }

    // Sends one message to every connected client, including the original sender.
    public void broadcast(GameMessage message) {
        for (ClientHandler client : clients) {
            client.send(message);
        }
    }

    // Removes disconnected clients and informs the lobby unless LEAVE was already broadcast.
    private void remove(ClientHandler client) {
        clients.remove(client);
        if (client.nickName != null) {
            synchronized (lobbyLock) {
                lobbyPlayerColors.remove(client.nickName);
            }
        }
        if (client.nickName != null && !client.leaveAnnounced) {
            broadcast(GameMessage.leave(client.nickName));
        }
        assignNewFirstPlayerIfNeeded();
        if (!gameStarted) {
            broadcastLobbyState();
        }
    }

    @Override
    public void close() throws IOException {
        // Closing the server stops the accept loop and closes all active client sockets.
        running = false;
        for (ClientHandler client : clients) {
            client.close();
        }
        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    private boolean canAcceptNewClient() {
        synchronized (lobbyLock) {
            // clients.size() includes connected sockets that may not have sent JOIN yet.
            return !gameStarted && clients.size() < Setup.MAX_PLAYERS;
        }
    }

    // Sends a final ERROR message to a socket that cannot enter the lobby.
    private void rejectClient(Socket socket, String reason) {
        try (Socket rejectedSocket = socket;
             ObjectOutputStream output = new ObjectOutputStream(rejectedSocket.getOutputStream())) {
            output.writeObject(GameMessage.error(reason));
            output.flush();
        } catch (IOException ignored) {
        }
    }

    // Routes every incoming client message through the server's lobby/game rules.
    private void handleClientMessage(ClientHandler client, GameMessage message) {
        if (message.getType() == MessageType.JOIN) {
            handleJoin(client, message);
            return;
        }

        if (message.getType() == MessageType.LEAVE) {
            client.leaveAnnounced = true;
            broadcast(message);
            client.closeQuietly();
            return;
        }

        if (message.getType() == MessageType.CHAT && isStartCommand(message)) {
            startGame(client);
            return;
        }

        if (message.getType() == MessageType.CHOOSE_COLOR) {
            handleChooseColor(client, message);
            return;
        }

        if (message.getType() == MessageType.REINFORCEMENT
                || message.getType() == MessageType.ATTACK
                || message.getType() == MessageType.ARMY_MOVEMENT
                || message.getType() == MessageType.END_PHASE) {
            handleGameAction(client, message);
            return;
        }

        broadcast(message);
    }

    // Accepts a player into the lobby if the match has room and the nickname is unique.
    private void handleJoin(ClientHandler client, GameMessage message) {
        synchronized (lobbyLock) {
            if (gameStarted) {
                client.send(GameMessage.error("The game has already started."));
                client.closeQuietly();
                return;
            }

            if (getJoinedPlayers().size() >= Setup.MAX_PLAYERS) {
                client.send(GameMessage.error("The game is full. Maximum players: " + Setup.MAX_PLAYERS + "."));
                client.closeQuietly();
                return;
            }

            if (isNickNameAlreadyUsed(message.getSender())) {
                client.send(GameMessage.error("This nickname is already in use."));
                client.closeQuietly();
                return;
            }

            client.nickName = message.getSender();
            if (firstPlayerNickName == null) {
                // The first accepted nickname becomes the lobby owner/start authority.
                firstPlayerNickName = client.nickName;
                client.send(GameMessage.chat("server", "You are the first player. Write Start to begin when everyone is ready."));
            }
        }

        broadcast(message);
        broadcastLobbyState();
    }

    // The command is intentionally case-insensitive, so "Start", "start", and " START " work.
    private boolean isStartCommand(GameMessage message) {
        String text = message.get("text");
        return text != null && "start".equalsIgnoreCase(text.trim());
    }

    // Validates the Start command and broadcasts the setup snapshot when the game begins.
    private void startGame(ClientHandler client) {
        synchronized (lobbyLock) {
            if (client.nickName == null) {
                client.send(GameMessage.error("Join the game before starting it."));
                return;
            }

            if (!client.nickName.equals(firstPlayerNickName)) {
                client.send(GameMessage.error("Only the first player can start the game."));
                return;
            }

            if (gameStarted) {
                client.send(GameMessage.error("The game has already started."));
                return;
            }

            List<String> players = getJoinedPlayers();
            if (players.size() < Setup.MIN_PLAYERS) {
                client.send(GameMessage.error("At least " + Setup.MIN_PLAYERS + " players are needed to start."));
                return;
            }

            Setup.GameSetup gameSetup = Setup.createGame(players, lobbyPlayerColors);
            synchronized (gameLock) {
                gameLogic = GameLogic.fromSetup(gameSetup);
            }
            gameStarted = true;

            // Broadcast both a human-readable notice and structured setup/turn data.
            broadcast(GameMessage.chat("server", "Game started by " + client.nickName + "."));
            broadcast(GameMessage.gameState(gameLogic.toMessageData("started")));
            broadcast(GameMessage.turnChange(gameLogic.getCurrentPlayer()));
            sendSecretObjectives();
        }
    }

    private void sendSecretObjectives() {
        for (ClientHandler client : clients) {
            if (client.nickName == null || gameLogic == null) {
                continue;
            }
            ObjectiveCard objective = gameLogic.getObjective(client.nickName);
            if (objective != null) {
                client.send(GameMessage.playerObjective(
                        client.nickName,
                        objective.getId(),
                        objective.getDescription()));
            }
        }
    }

    private void handleGameAction(ClientHandler client, GameMessage message) {
        if (client.nickName == null) {
            client.send(GameMessage.error("Join the game before sending game actions."));
            return;
        }

        synchronized (gameLock) {
            if (!gameStarted || gameLogic == null) {
                client.send(GameMessage.error("The game has not started yet."));
                return;
            }

            GameLogic.Result result;
            try {
                result = applyGameAction(client.nickName, message);
            } catch (NumberFormatException exception) {
                client.send(GameMessage.error("Armies must be a valid number."));
                return;
            }

            if (!result.isAccepted()) {
                client.send(GameMessage.error(result.getMessage()));
                return;
            }

            broadcast(GameMessage.chat("server", result.getMessage()));
            if (gameLogic.isGameOver()) {
                broadcast(GameMessage.chat("server", gameLogic.getWinAnnouncement()));
            }
            if (message.getType() == MessageType.ATTACK) {
                broadcast(GameMessage.attackResult(
                        client.nickName,
                        result.getFromTerritory(),
                        result.getToTerritory(),
                        result.getAttackerLosses(),
                        result.getDefenderLosses(),
                        result.isConquered()
                ));
            }

            String phase = gameLogic.isGameOver() ? "gameOver" : "playing";
            broadcast(GameMessage.gameState(gameLogic.toMessageData(phase)));
            broadcast(GameMessage.turnChange(gameLogic.getCurrentPlayer()));
        }
    }

    private GameLogic.Result applyGameAction(String nickName, GameMessage message) {
        if (message.getType() == MessageType.REINFORCEMENT) {
            return gameLogic.reinforce(
                    nickName,
                    message.get("territory"),
                    parseArmies(message)
            );
        }

        if (message.getType() == MessageType.ATTACK) {
            return gameLogic.attack(
                    nickName,
                    message.get("fromTerritory"),
                    message.get("toTerritory")
            );
        }

        if (message.getType() == MessageType.ARMY_MOVEMENT) {
            return gameLogic.moveArmies(
                    nickName,
                    message.get("fromTerritory"),
                    message.get("toTerritory"),
                    parseArmies(message)
            );
        }

        return gameLogic.advancePhase(nickName);
    }

    private int parseArmies(GameMessage message) {
        String armies = message.get("armies");
        if (armies == null || armies.trim().isEmpty()) {
            throw new NumberFormatException("Missing armies value.");
        }
        return Integer.parseInt(armies);
    }

    private void handleChooseColor(ClientHandler client, GameMessage message) {
        synchronized (lobbyLock) {
            if (gameStarted) {
                client.send(GameMessage.error("The game has already started."));
                return;
            }
            if (client.nickName == null) {
                client.send(GameMessage.error("Join the game before choosing a color."));
                return;
            }

            String color = normalizeColor(message.get("color"));
            if (color == null) {
                client.send(GameMessage.error("Invalid color."));
                return;
            }

            for (Map.Entry<String, String> entry : lobbyPlayerColors.entrySet()) {
                if (color.equals(entry.getValue()) && !client.nickName.equals(entry.getKey())) {
                    client.send(GameMessage.error("That color is already taken."));
                    return;
                }
            }

            lobbyPlayerColors.put(client.nickName, color);
        }
        broadcastLobbyState();
    }

    private static String normalizeColor(String color) {
        if (color == null || color.trim().isEmpty()) {
            return null;
        }
        String normalized = color.trim().toLowerCase();
        for (String allowed : Setup.LOBBY_COLORS) {
            if (allowed.equalsIgnoreCase(normalized)) {
                return allowed;
            }
        }
        return null;
    }

    // Sends a small lobby snapshot after joins/leaves so clients can render waiting state.
    private void broadcastLobbyState() {
        Map<String, String> data = new java.util.LinkedHashMap<>();
        List<String> players = getJoinedPlayers();
        data.put("phase", "lobby");
        data.put("players", String.join(",", players));
        data.put("firstPlayer", firstPlayerNickName == null ? "" : firstPlayerNickName);
        data.put("minPlayers", String.valueOf(Setup.MIN_PLAYERS));
        data.put("maxPlayers", String.valueOf(Setup.MAX_PLAYERS));
        for (String player : players) {
            String color = lobbyPlayerColors.get(player);
            if (color != null && !color.isEmpty()) {
                data.put("player." + player + ".color", color);
            }
        }
        broadcast(GameMessage.gameState(data));
    }

    // Returns accepted nicknames in the same order as their ClientHandler entries.
    private List<String> getJoinedPlayers() {
        List<String> players = new ArrayList<>();
        for (ClientHandler client : clients) {
            if (client.nickName != null) {
                players.add(client.nickName);
            }
        }
        return players;
    }

    // Nicknames are unique because they identify player-owned territories in messages.
    private boolean isNickNameAlreadyUsed(String nickName) {
        for (ClientHandler client : clients) {
            if (nickName.equals(client.nickName)) {
                return true;
            }
        }
        return false;
    }

    // If the first player leaves before game start, the next joined player becomes starter.
    private void assignNewFirstPlayerIfNeeded() {
        synchronized (lobbyLock) {
            if (gameStarted || firstPlayerNickName == null || getJoinedPlayers().contains(firstPlayerNickName)) {
                return;
            }

            firstPlayerNickName = null;
            for (ClientHandler client : clients) {
                if (client.nickName != null) {
                    firstPlayerNickName = client.nickName;
                    client.send(GameMessage.chat("server", "You are now the first player. Write Start to begin when everyone is ready."));
                    break;
                }
            }
        }
        broadcastLobbyState();
    }

    // Handles one client's socket streams and forwards valid messages to the server.
    private final class ClientHandler implements Runnable, Closeable {
        private final Socket socket;
        private ObjectOutputStream output;
        private ObjectInputStream input;
        // Null until the JOIN message is accepted.
        private String nickName;
        // Prevents broadcasting duplicate LEAVE messages during normal disconnect.
        private boolean leaveAnnounced;

        private ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // Output is created and flushed first to avoid ObjectStream header deadlocks.
                output = new ObjectOutputStream(socket.getOutputStream());
                output.flush();
                input = new ObjectInputStream(socket.getInputStream());
                readMessages();
            } catch (EOFException ignored) {
                // Normal socket closure; cleanup happens in finally.
            } catch (IOException exception) {
                send(GameMessage.error("Connection error: " + exception.getMessage()));
            } catch (ClassNotFoundException exception) {
                send(GameMessage.error("Unknown message received."));
            } finally {
                remove(this);
                closeQuietly();
            }
        }

        // Blocking read loop for this one client.
        private void readMessages() throws IOException, ClassNotFoundException {
            while (running && !socket.isClosed()) {
                Object object = input.readObject();
                if (object instanceof GameMessage) {
                    handleClientMessage(this, (GameMessage) object);
                } else {
                    send(GameMessage.error("Only GameMessage objects are accepted."));
                }
            }
        }

        private void send(GameMessage message) {
            if (output == null) {
                return;
            }

            try {
                // Synchronization allows broadcasts and direct errors to share the same stream safely.
                synchronized (output) {
                    output.writeObject(message);
                    output.flush();
                    // Sends fresh object values even if the same object reference is reused later.
                    output.reset();
                }
            } catch (IOException exception) {
                closeQuietly();
            }
        }

        @Override
        public void close() throws IOException {
            socket.close();
        }

        private void closeQuietly() {
            try {
                close();
            } catch (IOException ignored) {
            }
        }
    }
}
