package client.view;

import client.controller.GameController;
import client.view.component.AnimatedLobbyButton;
import client.view.layout.ViewScale;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

/**
 * Lobby host con pulsante START animato.
 */
public class HostLobbyView extends AbstractLobbyView {
    private final AnimatedLobbyButton startButton;

    public HostLobbyView(GameController controller) {
        super(controller);
        this.startButton = new AnimatedLobbyButton("START");
        startButton.setOnAction(v -> controller.startGame());
        ViewScale.bindPrefSize(startButton, widthProperty(), heightProperty(), 200, 60);
        finishLobbyLayout();
    }

    @Override
    protected void buildHostSpecificLayout() {
        getBottomHost().getChildren().add(0, startButton);
    }

    @Override
    protected void refreshHostSpecific() {
        startButton.setButtonDisabled(!controller.getState().isFirstPlayer());
    }
}
