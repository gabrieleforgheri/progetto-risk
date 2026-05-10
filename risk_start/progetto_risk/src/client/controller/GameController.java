package client.controller;

import client.model.ClientGameState;
import client.view.GameView;
import client.view.LobbyView;
import javafx.application.Platform;
import network.ClientConnection;
import network.GameMessage;
import network.MessageListener;
import network.MessageType;

import java.io.IOException;
import java.util.Map;

public class GameController implements MessageListener {
    private final ClientGameState state;
    private ClientConnection connection;
    private LobbyView lobbyView;
    private GameView gameView;
    private Runnable onGameStarted;

    public GameController() {
        this.state = new ClientGameState();
    }

    public void setLobbyView(LobbyView lobbyView) {
        this.lobbyView = lobbyView;
    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    public void setOnGameStarted(Runnable onGameStarted) {
        this.onGameStarted = onGameStarted;
    }

    public ClientGameState getState() {
        return state;
    }

    public void connect(String host, int port, String nickName) {
        state.setMyNickName(nickName);
        setStatus("Connecting to " + host + ":" + port + "...");

        Thread connectionThread = new Thread(() -> {
            try {
                connection = new ClientConnection(host, port, this);
                connection.connect(nickName);
                Platform.runLater(() -> setStatus("Connected as " + nickName + "."));
            } catch (IOException exception) {
                Platform.runLater(() -> setStatus("Connection failed: " + exception.getMessage()));
            }
        }, "risk-client-connect");

        connectionThread.setDaemon(true);
        connectionThread.start();
    }

    public void startGame() {
        sendChat("Start");
    }

    public void sendChat(String text) {
        if (connection == null) {
            setStatus("Connect before sending messages.");
            return;
        }

        try {
            connection.sendChat(text);
        } catch (IOException exception) {
            setStatus("Send failed: " + exception.getMessage());
        }
    }

    public void sendAttack(String fromTerritory, String toTerritory, int armies) {
        if (connection == null) {
            setStatus("Connect before attacking.");
            return;
        }

        try {
            connection.sendAttack(fromTerritory, toTerritory, armies);
        } catch (IOException exception) {
            setStatus("Attack message failed: " + exception.getMessage());
        }
    }

    public void sendArmyMovement(String fromTerritory, String toTerritory, int armies) {
        if (connection == null) {
            setStatus("Connect before moving armies.");
            return;
        }

        try {
            connection.sendArmyMovement(fromTerritory, toTerritory, armies);
        } catch (IOException exception) {
            setStatus("Movement message failed: " + exception.getMessage());
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
    }

    @Override
    public void onMessage(GameMessage message) {
        Platform.runLater(() -> handleMessage(message));
    }

    @Override
    public void onConnectionClosed(Exception exception) {
        Platform.runLater(() -> {
            if (exception == null) {
                setStatus("Connection closed.");
            } else {
                setStatus("Connection closed: " + exception.getMessage());
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
            addEvent(message.getSender() + ": " + message.get("text"));
        } else if (message.getType() == MessageType.ERROR) {
            setStatus("Server error: " + message.get("text"));
        } else {
            addEvent(message.getType() + " from " + message.getSender() + " " + message.getData());
        }
    }

    private void handleGameState(Map<String, String> data) {
        String phase = data.get("phase");
        if ("lobby".equals(phase)) {
            state.updateLobby(data);
            if (lobbyView != null) {
                lobbyView.refresh();
            }
            return;
        }

        if ("started".equals(phase)) {
            state.updateStartedGame(data);
            refreshGameView();
            if (onGameStarted != null) {
                onGameStarted.run();
            }
        }
    }

    private void refreshGameView() {
        if (gameView != null) {
            gameView.refresh();
        }
    }

    private void addEvent(String text) {
        state.addEvent(text);
        if (gameView != null) {
            gameView.refreshEvents();
        }
        if (lobbyView != null) {
            lobbyView.refreshEvents();
        }
    }

    private void setStatus(String text) {
        state.setStatus(text);
        if (lobbyView != null) {
            lobbyView.refreshStatus();
        }
        if (gameView != null) {
            gameView.refreshStatus();
        }
    }
}
