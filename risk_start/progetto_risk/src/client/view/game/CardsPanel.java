package client.view.game;

import client.view.style.UiStyles;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Barra carte in basso (HTML: {@code .cards.box}).
 * Le carte territorio restano placeholder; l'obiettivo segreto arriva dal server.
 */
public class CardsPanel extends HBox {
    private final Label objectiveCard;

    public CardsPanel() {
        setAlignment(Pos.CENTER);
        setSpacing(10);
        setStyle(UiStyles.GAME_CARDS_BAR);
        setPrefHeight(86);

        objectiveCard = cardPlaceholder("obbiettivo", UiStyles.GAME_OBJECTIVE_CARD);
        objectiveCard.setWrapText(true);
        objectiveCard.setMaxWidth(320);

        getChildren().addAll(
                cardPlaceholder("territorio", UiStyles.GAME_TERRITORY_CARD),
                cardPlaceholder("territorio", UiStyles.GAME_TERRITORY_CARD),
                cardPlaceholder("territorio", UiStyles.GAME_TERRITORY_CARD),
                objectiveCard
        );
    }

    public void refresh(String objectiveDescription) {
        if (objectiveDescription == null || objectiveDescription.isBlank()) {
            objectiveCard.setText("obbiettivo");
        } else {
            objectiveCard.setText(objectiveDescription);
        }
    }

    private static Label cardPlaceholder(String text, String style) {
        Label card = new Label(text);
        card.setStyle(style);
        card.getStyleClass().add("carta_territorio");
        return card;
    }
}
