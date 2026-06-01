package client.view.game;

import client.view.style.UiStyles;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Barra carte in basso (HTML: {@code .cards.box}).
 * Placeholder finché la logica carte non è collegata al server.
 */
public class CardsPanel extends HBox {
    public CardsPanel() {
        setAlignment(Pos.CENTER);
        setSpacing(10);
        setStyle(UiStyles.GAME_CARDS_BAR);
        setPrefHeight(86);

        // STILE: sostituire Label con componenti carta cliccabili (territorio / obiettivo)
        getChildren().addAll(
                cardPlaceholder("territorio", UiStyles.GAME_TERRITORY_CARD),
                cardPlaceholder("territorio", UiStyles.GAME_TERRITORY_CARD),
                cardPlaceholder("territorio", UiStyles.GAME_TERRITORY_CARD),
                cardPlaceholder("obbiettivo", UiStyles.GAME_OBJECTIVE_CARD)
        );
    }

    public void refresh() {
        // STILE: aggiornare carte da ClientGameState quando il server le invierà
    }

    private static Label cardPlaceholder(String text, String style) {
        Label card = new Label(text);
        card.setStyle(style);
        card.getStyleClass().add("carta_territorio");
        return card;
    }
}
