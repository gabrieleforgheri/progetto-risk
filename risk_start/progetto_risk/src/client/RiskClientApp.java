package client;

import client.controller.GameController;
import client.view.ClientLobbyView;
import client.view.GameView;
import client.view.HostLobbyView;
import client.view.StartView;
import client.view.layout.DesignViewport;
import client.view.layout.ViewScale;
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
    private DesignViewport gameViewport;
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
        gameViewport = new DesignViewport(gameView);

        controller.setStartView(startView);
        controller.setHostLobbyView(hostLobbyView);
        controller.setClientLobbyView(clientLobbyView);
        controller.setGameView(gameView);

        controller.setOnShowStart(() -> showRoot(startView));
        controller.setOnShowHostLobby(() -> showRoot(hostLobbyView));
        controller.setOnShowClientLobby(() -> showRoot(clientLobbyView));
        controller.setOnShowGame(() -> showRoot(gameViewport));

        stage.setTitle("Risk");
        stage.setMinWidth(ViewScale.DESIGN_W * 0.5);
        stage.setMinHeight(ViewScale.DESIGN_H * 0.5);
        scene = new Scene(startView, UiStyles.WINDOW_WIDTH, UiStyles.WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    @Override
    public void stop() {
        if (controller != null) {
            controller.shutdown();
        }
    }

    private void showRoot(javafx.scene.Parent root) {
        scene.setRoot(root);
    }
}
