package client.view;

import client.controller.GameController;
import client.view.style.UiStyles;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

/**
 * Lobby creatore server (HTML: {@code .window.lobby_server_creator_pov} + {@code #start}).
 */
public class HostLobbyView extends AbstractLobbyView {
    private final Button startButton;

    public HostLobbyView(GameController controller) {
        super(controller);
        this.startButton = new Button("start");
        startButton.setStyle(UiStyles.LOBBY_START_BUTTON);
        startButton.setOnAction(event -> controller.startGame());
        finishLobbyLayout();
    }

    @Override
    protected String lobbyModeLabel() {
        return "Lobby — Host (crea server)";
    }

    @Override
    protected void buildHostSpecificLayout() {
        // STILE: button / #start — top 90%, right 10% in bozza CSS
        AnchorPane.setBottomAnchor(startButton, 24.0);
        AnchorPane.setRightAnchor(startButton, 100.0);
        getChildren().add(startButton);
    }

    @Override
    protected void refreshHostSpecific() {
        startButton.setDisable(!controller.getState().isFirstPlayer());
    }
}
