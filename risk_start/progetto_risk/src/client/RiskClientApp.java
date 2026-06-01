package client;

import client.controller.GameController;
import client.view.ClientLobbyView;
import client.view.GameView;
import client.view.HostLobbyView;
import client.view.StartView;
import client.view.style.UiStyles;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RiskClientApp extends Application {
    private GameController controller;
    private StartView startView;
    private HostLobbyView hostLobbyView;
    private ClientLobbyView clientLobbyView;
    private GameView gameView;
    private Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        controller = new GameController();
        startView = new StartView(controller);
        hostLobbyView = new HostLobbyView(controller);
        clientLobbyView = new ClientLobbyView(controller);
        gameView = new GameView(controller);

        controller.setStartView(startView);
        controller.setHostLobbyView(hostLobbyView);
        controller.setClientLobbyView(clientLobbyView);
        controller.setGameView(gameView);

        controller.setOnShowStart(() -> showRoot(startView, UiStyles.WINDOW_WIDTH, UiStyles.WINDOW_HEIGHT));
        controller.setOnShowHostLobby(() -> showRoot(hostLobbyView, UiStyles.WINDOW_WIDTH, UiStyles.WINDOW_HEIGHT));
        controller.setOnShowClientLobby(() -> showRoot(clientLobbyView, UiStyles.WINDOW_WIDTH, UiStyles.WINDOW_HEIGHT));
        controller.setOnShowGame(() -> showRoot(gameView, UiStyles.GAME_WINDOW_WIDTH, UiStyles.GAME_WINDOW_HEIGHT));

        stage.setTitle("Risk");
        stage.setMinWidth(800);
        stage.setMinHeight(480);
        scene = new Scene(startView, UiStyles.WINDOW_WIDTH, UiStyles.WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        if (controller != null) {
            controller.shutdown();
        }
    }

    private void showRoot(javafx.scene.Parent root, double width, double height) {
        scene.setRoot(root);
        if (scene.getWidth() < width || scene.getHeight() < height) {
            scene.getWindow().setWidth(width);
            scene.getWindow().setHeight(height);
        }
    }
}
