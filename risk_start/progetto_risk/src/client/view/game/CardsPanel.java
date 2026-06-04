package client.view.game;

import client.controller.GameController;
import client.model.ClientGameState;
import client.view.style.UiStyles;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Barra carte: territorio (mano segreta) + obiettivo.
 */
public class CardsPanel extends HBox {
    private final GameController controller;
    private final HBox territoryCardsBox;
    private final Label objectiveCard;
    private final Set<String> selectedCardIds = new LinkedHashSet<>();

    public CardsPanel(GameController controller) {
        this.controller = controller;
        setAlignment(Pos.CENTER);
        setSpacing(10);
        setStyle(UiStyles.GAME_CARDS_BAR);
        setPrefHeight(96);

        territoryCardsBox = new HBox(8);
        territoryCardsBox.setAlignment(Pos.CENTER);

        objectiveCard = cardPlaceholder("obbiettivo", UiStyles.GAME_OBJECTIVE_CARD);
        objectiveCard.setWrapText(true);
        objectiveCard.setMaxWidth(320);

        getChildren().addAll(territoryCardsBox, objectiveCard);
    }

    public void refresh(String objectiveDescription) {
        if (objectiveDescription == null || objectiveDescription.isBlank()) {
            objectiveCard.setText("obbiettivo");
        } else {
            objectiveCard.setText(objectiveDescription);
        }
        refreshTerritoryCards();
    }

    public void clearTerritoryCardSelection() {
        selectedCardIds.clear();
        refreshTerritoryCards();
    }

    private void refreshTerritoryCards() {
        territoryCardsBox.getChildren().clear();
        ClientGameState state = controller.getState();
        if (state.getMyTerritoryCards().isEmpty()) {
            territoryCardsBox.getChildren().add(cardPlaceholder("nessuna carta", UiStyles.GAME_TERRITORY_CARD));
            return;
        }

        for (ClientGameState.TerritoryCardState card : state.getMyTerritoryCards()) {
            territoryCardsBox.getChildren().add(buildTerritoryCard(card));
        }
    }

    private VBox buildTerritoryCard(ClientGameState.TerritoryCardState card) {
        Label nameLabel = new Label(card.getTerritoryName());
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 10px;");
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(84);

        Label valueLabel = new Label(card.getUnitDisplayName());
        valueLabel.setStyle("-fx-text-fill: #e8f5e9; -fx-font-size: 11px;");

        VBox box = new VBox(4, nameLabel, valueLabel);
        box.setAlignment(Pos.CENTER);
        boolean selected = selectedCardIds.contains(card.getId());
        String style = UiStyles.GAME_TERRITORY_CARD
                + (selected ? " -fx-border-color: #ffeb3b; -fx-border-width: 3;" : "");
        box.setStyle(style);
        box.getStyleClass().add("carta_territorio");
        box.setOnMouseClicked(event -> controller.onTerritoryCardClicked(card.getId()));
        return box;
    }

    private static Label cardPlaceholder(String text, String style) {
        Label card = new Label(text);
        card.setStyle(style);
        card.getStyleClass().add("carta_territorio");
        return card;
    }

    public void toggleSelection(String cardId) {
        if (selectedCardIds.contains(cardId)) {
            selectedCardIds.remove(cardId);
        } else if (selectedCardIds.size() < 3) {
            selectedCardIds.add(cardId);
        }
        refreshTerritoryCards();
    }

    public int getSelectionCount() {
        return selectedCardIds.size();
    }

    public java.util.List<String> getSelectedCardIds() {
        return java.util.List.copyOf(selectedCardIds);
    }
}
