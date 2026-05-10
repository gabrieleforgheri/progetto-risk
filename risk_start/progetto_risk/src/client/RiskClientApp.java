package client;

import client.controller.GameController;
import client.view.GameView;
import client.view.LobbyView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RiskClientApp extends Application {
    private static final int DEFAULT_WIDTH = 1280;
    private static final int DEFAULT_HEIGHT = 820;

    private GameController controller;
    private LobbyView lobbyView;
    private GameView gameView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        controller = new GameController();
        lobbyView = new LobbyView(controller);
        gameView = new GameView(controller);

        controller.setLobbyView(lobbyView);
        controller.setGameView(gameView);
        controller.setOnGameStarted(() -> showGame(stage));

        stage.setTitle("Risk - Client");
        stage.setMinWidth(980);
        stage.setMinHeight(640);
        stage.setScene(new Scene(lobbyView, DEFAULT_WIDTH, DEFAULT_HEIGHT));
        stage.show();
    }

    @Override
    public void stop() {
        if (controller != null) {
            controller.disconnect();
        }
    }

    private void showGame(Stage stage) {
        stage.getScene().setRoot(gameView);
    }
}
