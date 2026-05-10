package client.view;

import client.controller.GameController;
import client.model.ClientGameState;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class PlayerPanel extends VBox {
    private final GameController controller;
    private final Label titleLabel;
    private final Label turnLabel;
    private final ListView<String> playersList;

    public PlayerPanel(GameController controller) {
        this.controller = controller;
        this.titleLabel = new Label("Players");
        this.turnLabel = new Label();
        this.playersList = new ListView<>();
        buildLayout();
    }

    public void refresh() {
        ClientGameState state = controller.getState();
        turnLabel.setText("Current turn: " + emptyLabel(state.getCurrentPlayer()));
        playersList.getItems().clear();

        for (ClientGameState.PlayerState player : state.getPlayers().values()) {
            String row = player.getNickName()
                    + " | armies: " + player.getInitialArmies()
                    + " | remaining: " + player.getRemainingArmies()
                    + " | territories: " + player.getTerritories().size();
            playersList.getItems().add(row);
        }
    }

    private void buildLayout() {
        setSpacing(8);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        playersList.setPrefHeight(180);
        getChildren().addAll(titleLabel, turnLabel, playersList);
    }

    private String emptyLabel(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "-";
        }
        return value;
    }
}
