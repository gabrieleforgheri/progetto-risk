package client.controller;

import client.map.TerritoryBorders;
import client.model.ClientGameState;
import client.view.ClientLobbyView;
import client.view.GameView;
import client.view.HostLobbyView;
import client.view.StartView;
import javafx.application.Platform;
import network.ClientConnection;
import network.GameMessage;
import network.MessageListener;
import network.MessageType;
import network.RiskServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;

public class GameController implements MessageListener {
    private static final int SERVER_READY_TIMEOUT_MS = 5000;
    private static final int SERVER_POLL_INTERVAL_MS = 50;

    private final ClientGameState state;
    private ClientConnection connection;
    private RiskServer localServer;
    private Thread localServerThread;
    private StartView startView;
    private HostLobbyView hostLobbyView;
    private ClientLobbyView clientLobbyView;
    private GameView gameView;
    private Runnable onShowStart;
    private Runnable onShowHostLobby;
    private Runnable onShowClientLobby;
    private Runnable onShowGame;
    private String selectedTerritory = "";

    public GameController() {
        this.state = new ClientGameState();
    }

    public void setStartView(StartView startView) {
        this.startView = startView;
    }

    public void setHostLobbyView(HostLobbyView hostLobbyView) {
        this.hostLobbyView = hostLobbyView;
    }

    public void setClientLobbyView(ClientLobbyView clientLobbyView) {
        this.clientLobbyView = clientLobbyView;
    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    public void setOnShowStart(Runnable onShowStart) {
        this.onShowStart = onShowStart;
    }

    public void setOnShowHostLobby(Runnable onShowHostLobby) {
        this.onShowHostLobby = onShowHostLobby;
    }

    public void setOnShowClientLobby(Runnable onShowClientLobby) {
        this.onShowClientLobby = onShowClientLobby;
    }

    public void setOnShowGame(Runnable onShowGame) {
        this.onShowGame = onShowGame;
    }

    public ClientGameState getState() {
        return state;
    }

    public String getSelectedTerritory() {
        return selectedTerritory;
    }

    /**
     * Gestisce il click su un territorio della mappa in base alla fase di gioco corrente.
     */
    public void onTerritorySelected(String territoryName) {
        if (territoryName == null || territoryName.isBlank()) {
            return;
        }

        String myName = state.getMyNickName();
        if (myName.isEmpty()) {
            setStatus("Connettiti prima di giocare.");
            return;
        }
        if (!myName.equals(state.getCurrentPlayer())) {
            setStatus("Non è il tuo turno.");
            return;
        }
        if (state.getWinner() != null && !state.getWinner().isEmpty()) {
            setStatus("La partita è terminata.");
            return;
        }

        ClientGameState.TerritoryState territory = state.getTerritories().get(territoryName);
        if (territory == null) {
            setStatus("Territorio sconosciuto: " + territoryName + ".");
            return;
        }

        String stage = state.getStage();
        if ("reinforcement".equalsIgnoreCase(stage)) {
            handleReinforcementClick(territory);
        } else if ("attack".equalsIgnoreCase(stage)) {
            handleAttackClick(territory);
        } else if ("movement".equalsIgnoreCase(stage)) {
            handleMovementClick(territory);
        } else {
            setStatus("Azione non disponibile in questa fase.");
        }
    }

    private void handleReinforcementClick(ClientGameState.TerritoryState territory) {
        String myName = state.getMyNickName();
        if (!myName.equals(territory.getOwner())) {
            setStatus("Puoi rinforzare solo i tuoi territori.");
            return;
        }

        ClientGameState.PlayerState me = state.getPlayers().get(myName);
        if (me == null || me.getRemainingArmies() <= 0) {
            setStatus("Non hai armate da posizionare.");
            return;
        }

        clearTerritorySelection();
        sendReinforcement(territory.getName(), 1);
    }

    private void handleAttackClick(ClientGameState.TerritoryState territory) {
        String myName = state.getMyNickName();
        String territoryName = territory.getName();

        if (selectedTerritory.isEmpty()) {
            if (!myName.equals(territory.getOwner())) {
                setStatus("Seleziona uno dei tuoi territori come origine dell'attacco.");
                return;
            }
            if (territory.getArmies() < 2) {
                setStatus("Servono almeno 2 armate sul territorio di origine.");
                return;
            }
            selectedTerritory = territoryName;
            setStatus("Origine: " + territoryName + ". Seleziona un territorio nemico confinante.");
            refreshGameView();
            return;
        }

        if (selectedTerritory.equals(territoryName)) {
            clearTerritorySelection();
            setStatus("Selezione annullata.");
            refreshGameView();
            return;
        }

        ClientGameState.TerritoryState fromTerritory = state.getTerritories().get(selectedTerritory);
        if (fromTerritory == null) {
            clearTerritorySelection();
            refreshGameView();
            return;
        }

        if (myName.equals(territory.getOwner())) {
            if (territory.getArmies() >= 2) {
                selectedTerritory = territoryName;
                setStatus("Origine: " + territoryName + ". Seleziona un territorio nemico confinante.");
                refreshGameView();
            } else {
                setStatus("Servono almeno 2 armate sul territorio di origine.");
            }
            return;
        }

        if (!TerritoryBorders.areAdjacent(selectedTerritory, territoryName)) {
            setStatus(selectedTerritory + " non confina con " + territoryName + ".");
            return;
        }
        if (fromTerritory.getArmies() < 2) {
            setStatus("Servono almeno 2 armate sul territorio di origine.");
            clearTerritorySelection();
            refreshGameView();
            return;
        }

        String from = selectedTerritory;
        clearTerritorySelection();
        sendAttack(from, territoryName, 1);
    }

    private void handleMovementClick(ClientGameState.TerritoryState territory) {
        String myName = state.getMyNickName();
        String territoryName = territory.getName();

        if (selectedTerritory.isEmpty()) {
            if (!myName.equals(territory.getOwner())) {
                setStatus("Seleziona uno dei tuoi territori come origine dello spostamento.");
                return;
            }
            if (territory.getArmies() < 2) {
                setStatus("Servono almeno 2 armate per spostare truppe.");
                return;
            }
            selectedTerritory = territoryName;
            setStatus("Origine: " + territoryName + ". Seleziona un tuo territorio confinante.");
            refreshGameView();
            return;
        }

        if (selectedTerritory.equals(territoryName)) {
            clearTerritorySelection();
            setStatus("Selezione annullata.");
            refreshGameView();
            return;
        }

        ClientGameState.TerritoryState fromTerritory = state.getTerritories().get(selectedTerritory);
        if (fromTerritory == null) {
            clearTerritorySelection();
            refreshGameView();
            return;
        }

        if (!myName.equals(territory.getOwner())) {
            setStatus("Puoi spostare armate solo tra i tuoi territori.");
            return;
        }

        if (TerritoryBorders.areAdjacent(selectedTerritory, territoryName)) {
            if (fromTerritory.getArmies() < 2) {
                setStatus("Devi lasciare almeno un'armata sul territorio di origine.");
                clearTerritorySelection();
                refreshGameView();
                return;
            }
            String from = selectedTerritory;
            clearTerritorySelection();
            sendArmyMovement(from, territoryName, 1);
            return;
        }

        if (territory.getArmies() >= 2) {
            selectedTerritory = territoryName;
            setStatus("Origine: " + territoryName + ". Seleziona un tuo territorio confinante.");
            refreshGameView();
            return;
        }

        setStatus(selectedTerritory + " non confina con " + territoryName + ".");
    }

    private void clearTerritorySelection() {
        selectedTerritory = "";
    }

    /**
     * Flusso host: avvia {@link RiskServer} in background, apre {@link HostLobbyView} e connette il creatore.
     */
    public void connectAsHost(String nickName) {
        state.setMyNickName(nickName);
        state.clearLobbyChat();
        navigateToHostLobby(nickName);
        setStatus("Avvio server sulla porta " + RiskServer.DEFAULT_PORT + "...");

        Thread hostSetupThread = new Thread(() -> {
            try {
                ensureLocalServerRunning();
                if (!waitForServer("localhost", RiskServer.DEFAULT_PORT, SERVER_READY_TIMEOUT_MS)) {
                    Platform.runLater(() -> setStatus(
                            "Il server non risponde sulla porta " + RiskServer.DEFAULT_PORT + "."));
                    return;
                }
                Platform.runLater(() -> {
                    setStatus("Server attivo. Connessione alla lobby...");
                    state.addLobbyChat("server", "Server avviato su localhost:" + RiskServer.DEFAULT_PORT);
                });
                connectInternal("localhost", RiskServer.DEFAULT_PORT, nickName);
            } catch (IOException exception) {
                Platform.runLater(() -> setStatus("Impossibile avviare il server: " + exception.getMessage()));
            }
        }, "risk-host-setup");

        hostSetupThread.setDaemon(true);
        hostSetupThread.start();
    }

    public void connectAsClient(String host, int port, String nickName) {
        state.setMyNickName(nickName);
        state.clearLobbyChat();
        navigateToClientLobby(nickName);
        connect(host, port, nickName);
    }

    public void reportUserError(String message) {
        setStatus(message);
    }

    private void navigateToHostLobby(String nickName) {
        if (onShowHostLobby != null) {
            onShowHostLobby.run();
        }
        if (hostLobbyView != null) {
            hostLobbyView.refresh();
        }
    }

    private void navigateToClientLobby(String nickName) {
        if (onShowClientLobby != null) {
            onShowClientLobby.run();
        }
        if (clientLobbyView != null) {
            clientLobbyView.refresh();
        }
    }

    /** Avvia {@link RiskServer} in un thread daemon se non è già in esecuzione. */
    private synchronized void ensureLocalServerRunning() throws IOException {
        if (localServerThread != null && localServerThread.isAlive()) {
            return;
        }
        if (isPortListening("localhost", RiskServer.DEFAULT_PORT)) {
            return;
        }

        localServer = new RiskServer(RiskServer.DEFAULT_PORT);
        localServerThread = new Thread(() -> {
            try {
                localServer.start();
            } catch (IOException exception) {
                Platform.runLater(() -> setStatus("Server terminato: " + exception.getMessage()));
            }
        }, "risk-local-server");
        localServerThread.setDaemon(true);
        localServerThread.start();
    }

    private synchronized void stopLocalServer() {
        if (localServer != null) {
            try {
                localServer.close();
            } catch (IOException ignored) {
            }
            localServer = null;
        }
        localServerThread = null;
    }

    private static boolean isPortListening(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 300);
            return true;
        } catch (IOException exception) {
            return false;
        }
    }

    private static boolean waitForServer(String host, int port, int timeoutMs) {
        long deadline = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadline) {
            if (isPortListening(host, port)) {
                return true;
            }
            try {
                Thread.sleep(SERVER_POLL_INTERVAL_MS);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }

    private void connect(String host, int port, String nickName) {
        setStatus("Connessione a " + host + ":" + port + "...");

        Thread connectionThread = new Thread(
                () -> connectInternal(host, port, nickName),
                "risk-client-connect");
        connectionThread.setDaemon(true);
        connectionThread.start();
    }

    private void connectInternal(String host, int port, String nickName) {
        try {
            connection = new ClientConnection(host, port, this);
            connection.connect(nickName);
            Platform.runLater(() -> {
                setStatus("Connesso come " + nickName + ".");
                refreshLobbyViews();
            });
        } catch (IOException exception) {
            Platform.runLater(() -> setStatus("Connessione fallita: " + exception.getMessage()));
        }
    }

    /** Chiude client e server locale (alla chiusura dell'app JavaFX). */
    public void shutdown() {
        disconnect();
        stopLocalServer();
    }

    public void startGame() {
        sendChat("Start");
    }

    public void sendChat(String text) {
        if (connection == null) {
            setStatus("Connettiti prima di inviare messaggi.");
            return;
        }

        try {
            connection.sendChat(text);
        } catch (IOException exception) {
            setStatus("Invio fallito: " + exception.getMessage());
        }
    }

    public void chooseColor(String color) {
        if (connection == null) {
            setStatus("Connettiti prima di scegliere un colore.");
            return;
        }

        try {
            connection.sendChooseColor(color);
        } catch (IOException exception) {
            setStatus("Selezione colore fallita: " + exception.getMessage());
        }
    }

    public void sendAttack(String fromTerritory, String toTerritory, int armies) {
        if (connection == null) {
            setStatus("Connettiti prima di attaccare.");
            return;
        }

        try {
            connection.sendAttack(fromTerritory, toTerritory, armies);
        } catch (IOException exception) {
            setStatus("Attacco non inviato: " + exception.getMessage());
        }
    }

    public void sendReinforcement(String territory, int armies) {
        if (connection == null) {
            setStatus("Connettiti prima di rinforzare.");
            return;
        }

        try {
            connection.sendReinforcement(territory, armies);
        } catch (IOException exception) {
            setStatus("Rinforzo non inviato: " + exception.getMessage());
        }
    }

    public void sendArmyMovement(String fromTerritory, String toTerritory, int armies) {
        if (connection == null) {
            setStatus("Connettiti prima di spostare armate.");
            return;
        }

        try {
            connection.sendArmyMovement(fromTerritory, toTerritory, armies);
        } catch (IOException exception) {
            setStatus("Movimento non inviato: " + exception.getMessage());
        }
    }

    public void endPhase() {
        if (connection == null) {
            setStatus("Connettiti prima di cambiare fase.");
            return;
        }

        try {
            connection.sendEndPhase();
        } catch (IOException exception) {
            setStatus("Cambio fase fallito: " + exception.getMessage());
        }
    }

    public void disconnect() {
        if (connection == null) {
            return;
        }

        try {
            connection.sendLeave();
            connection.close();
        } catch (IOException ignored) {
        }
        connection = null;
    }

    @Override
    public void onMessage(GameMessage message) {
        Platform.runLater(() -> handleMessage(message));
    }

    @Override
    public void onConnectionClosed(Exception exception) {
        Platform.runLater(() -> {
            if (exception == null) {
                setStatus("Connessione chiusa.");
            } else {
                setStatus("Connessione chiusa: " + exception.getMessage());
            }
        });
    }

    private void handleMessage(GameMessage message) {
        if (message.getType() == MessageType.GAME_STATE) {
            handleGameState(message.getData());
        } else if (message.getType() == MessageType.TURN_CHANGE) {
            state.setCurrentPlayer(message.get("currentPlayer"));
            refreshGameView();
        } else if (message.getType() == MessageType.CHAT) {
            String text = message.get("text");
            if (text != null) {
                state.addLobbyChat(message.getSender(), text);
            }
            addEvent(message.getSender() + ": " + text);
            refreshLobbyViews();
        } else if (message.getType() == MessageType.ATTACK_RESULT) {
            addEvent("Risultato attacco: " + message.getData());
        } else if (message.getType() == MessageType.PLAYER_OBJECTIVE) {
            state.setMyObjective(message.get("objectiveId"), message.get("description"));
            refreshGameView();
        } else if (message.getType() == MessageType.ERROR) {
            setStatus("Errore server: " + message.get("text"));
        } else if (message.getType() == MessageType.JOIN || message.getType() == MessageType.LEAVE) {
            addEvent(message.getType() + " — " + message.getSender());
            refreshLobbyViews();
        } else {
            addEvent(message.getType() + " da " + message.getSender() + " " + message.getData());
        }
    }

    private void handleGameState(Map<String, String> data) {
        String phase = data.get("phase");
        if ("lobby".equals(phase)) {
            state.updateLobby(data);
            refreshLobbyViews();
            return;
        }

        if ("started".equals(phase)) {
            state.updateFromGameState(data);
            showGame();
            return;
        }

        if ("playing".equals(phase) || "gameOver".equals(phase)) {
            String previousStage = state.getStage();
            String previousPlayer = state.getCurrentPlayer();
            state.updateFromGameState(data);
            if (!previousStage.equalsIgnoreCase(state.getStage())
                    || !previousPlayer.equals(state.getCurrentPlayer())) {
                clearTerritorySelection();
            }
            refreshGameView();
        }
    }

    private void showGame() {
        if (onShowGame != null) {
            onShowGame.run();
        }
        refreshGameView();
    }

    private void refreshGameView() {
        if (gameView != null) {
            gameView.refresh();
        }
    }

    private void refreshLobbyViews() {
        if (hostLobbyView != null) {
            hostLobbyView.refresh();
        }
        if (clientLobbyView != null) {
            clientLobbyView.refresh();
        }
        if (startView != null) {
            startView.refreshStatus();
        }
    }

    private void addEvent(String text) {
        state.addEvent(text);
        if (gameView != null) {
            gameView.refresh();
        }
    }

    private void setStatus(String text) {
        state.setStatus(text);
        refreshLobbyViews();
        if (gameView != null) {
            gameView.refreshStatus();
        }
    }
}
