package client.view.game;

import client.controller.GameController;
import client.model.ClientGameState;
import client.view.style.UiStyles;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Pannello turno sinistro (HTML: {@code .turn.box}).
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
        setPrefWidth(140);
        setMinWidth(140);
        setPrefHeight(400);
        setStyle(UiStyles.GAME_TURN_PANEL);

        Label title = new Label("RISK");
        title.setStyle(UiStyles.GAME_TURN_TITLE);
        playerNameLabel.setId("player-name");
        playerNameLabel.setStyle(UiStyles.GAME_TURN_TEXT);
        playerCountryLabel.setStyle(UiStyles.GAME_TURN_TEXT);
        reserveTitleLabel.setId("reserve-title");
        reserveTitleLabel.setStyle(UiStyles.GAME_TURN_TEXT);
        reserveLabel.setId("reserve");
        reserveLabel.setStyle(UiStyles.GAME_TURN_TEXT + " -fx-font-size: 20px; -fx-font-weight: bold;");
        turnInfoLabel.setStyle(UiStyles.GAME_TURN_TEXT);
        turnMessageLabel.setId("turn-info-message");
        turnMessageLabel.setWrapText(true);
        turnMessageLabel.setMaxWidth(120);
        turnMessageLabel.setStyle(UiStyles.GAME_TURN_TEXT);

        endPhaseButton.setId("end");
        endPhaseButton.setStyle(UiStyles.GAME_END_PHASE_BUTTON);
        endPhaseButton.setOnAction(event -> controller.endPhase());

        // STILE: .turno — indicatore visivo turno (vuoto in bozza, personalizzabile)
        Label turnIndicator = new Label("");
        turnIndicator.getStyleClass().add("turno");

        getChildren().addAll(
                title,
                playerNameLabel,
                playerCountryLabel,
                reserveTitleLabel,
                reserveLabel,
                turnInfoLabel,
                turnMessageLabel,
                turnIndicator,
                endPhaseButton
        );
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

        // STILE: player-country — mostra continente dominante o territorio selezionato (da collegare a MapView)
        playerCountryLabel.setText(me == null ? "—" : me.getTerritories().size() + " territori");
    }
}
