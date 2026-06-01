package client.view.game;

import client.model.ClientGameState;
import client.view.style.UiStyles;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Lista giocatori a destra (HTML: {@code .users.box} / {@code .utente}).
 */
public class UsersPanel extends VBox {
    private final VBox slotsBox;

    public UsersPanel() {
        setSpacing(8);
        setAlignment(Pos.TOP_CENTER);
        slotsBox = new VBox(12);
        getChildren().add(slotsBox);
    }

    public void refresh(ClientGameState state) {
        slotsBox.getChildren().clear();
        List<String> names = new ArrayList<>(state.getPlayers().keySet());
        if (names.isEmpty()) {
            names.addAll(state.getLobbyPlayers());
        }

        for (String name : names) {
            Label slot = new Label(shortName(name));
            slot.setStyle(UiStyles.GAME_USER_SLOT
                    + playerColorStyle(state.getPlayers().get(name)));
            slotsBox.getChildren().add(slot);
        }
    }

    private static String shortName(String name) {
        if (name == null || name.length() <= 4) {
            return name == null ? "?" : name.toUpperCase();
        }
        return name.substring(0, 4).toUpperCase();
    }

    private static String playerColorStyle(ClientGameState.PlayerState player) {
        if (player == null || player.getColor() == null || player.getColor().isBlank()) {
            return " -fx-background-color: #eeeeee;";
        }
        return " -fx-background-color: " + player.getColor() + "55;";
    }
}
