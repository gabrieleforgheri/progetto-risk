package client.view.game;

import client.controller.GameController;
import client.model.ClientGameState;
import client.view.layout.ViewScale;
import client.view.style.UiFonts;
import client.view.style.UiStyles;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Pannello turno sinistro con animazione evidenziazione ({@code .evidenzia} in main.css).
 */
public class TurnPanel extends VBox {
    private final GameController controller;
    private final Label playerNameLabel;
    private final Label playerCountryLabel;
    private final Label reserveTitleLabel;
    private final Label reserveLabel;
    private final Label turnInfoLabel;
    private final Label turnMessageLabel;
    private final Button endPhaseButton;
    private FadeTransition highlightTransition;

    public TurnPanel(GameController controller) {
        this.controller = controller;
        this.playerNameLabel = new Label("PLAYER");
        this.playerCountryLabel = new Label("—");
        this.reserveTitleLabel = new Label("Rinforzo:");
        this.reserveLabel = new Label("0");
        this.turnInfoLabel = new Label("turno info");
        this.turnMessageLabel = new Label("messaggio del turno");
        this.endPhaseButton = new Button("End Phase");

        setAlignment(Pos.TOP_CENTER);
        setSpacing(8);
        setMaxWidth(Double.MAX_VALUE);
        setStyle(UiStyles.GAME_TURN_PANEL);

        Label title = new Label("RISK");
        ViewScale.bindFont(title, widthProperty(), heightProperty(), UiFonts.gunshipBold(28));
        title.setStyle(UiStyles.GAME_TURN_TITLE);

        playerNameLabel.setStyle(UiStyles.GAME_TURN_TEXT);
        playerCountryLabel.setStyle(UiStyles.GAME_TURN_TEXT);
        reserveTitleLabel.setStyle(UiStyles.GAME_TURN_TEXT);
        reserveLabel.setStyle(UiStyles.GAME_TURN_TEXT + " -fx-font-size: 20px; -fx-font-weight: bold;");
        turnInfoLabel.setStyle(UiStyles.GAME_TURN_INFO);
        turnMessageLabel.setWrapText(true);
        turnMessageLabel.setMaxWidth(120);
        turnMessageLabel.setStyle(UiStyles.GAME_TURN_TEXT);

        endPhaseButton.setStyle(UiStyles.GAME_END_PHASE_BUTTON);
        endPhaseButton.setOnAction(event -> controller.endPhase());

        getChildren().addAll(
                title,
                playerNameLabel,
                playerCountryLabel,
                reserveTitleLabel,
                reserveLabel,
                turnInfoLabel,
                turnMessageLabel,
                endPhaseButton
        );

        highlightTransition = new FadeTransition(Duration.seconds(2.5), this);
        highlightTransition.setFromValue(1);
        highlightTransition.setToValue(0.55);
        highlightTransition.setAutoReverse(true);
        highlightTransition.setCycleCount(FadeTransition.INDEFINITE);
    }

    public void refresh() {
        ClientGameState state = controller.getState();
        String myName = state.getMyNickName();
        playerNameLabel.setText(myName.isEmpty() ? "—" : myName.toUpperCase());

        ClientGameState.PlayerState me = state.getPlayers().get(myName);
        int remaining = me == null ? 0 : me.getRemainingArmies();
        reserveLabel.setText(String.valueOf(remaining));

        String current = state.getCurrentPlayer();
        String stage = state.getStage();
        turnInfoLabel.setText("Turno: " + (current.isEmpty() ? "—" : current));
        turnMessageLabel.setText("Fase: " + (stage.isEmpty() ? "—" : stage));

        boolean myTurn = myName.equals(current);
        endPhaseButton.setDisable(!myTurn || state.getWinner() != null && !state.getWinner().isEmpty());
        playerCountryLabel.setText(me == null ? "—" : me.getTerritories().size() + " territori");

        if (myTurn && (state.getWinner() == null || state.getWinner().isEmpty())) {
            if (highlightTransition.getStatus() != javafx.animation.Animation.Status.RUNNING) {
                highlightTransition.play();
            }
        } else {
            highlightTransition.stop();
            setOpacity(1);
        }
    }
}
