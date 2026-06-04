package client.view.game;

import client.model.ClientGameState;
import client.view.layout.ViewScale;
import client.view.layout.ViewScale;
import client.view.style.UiStyles;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Lista giocatori a destra responsive.
 */
public class UsersPanel extends VBox {
    private final VBox slotsBox;

    public UsersPanel() {
        setSpacing(8);
        setAlignment(Pos.TOP_CENTER);
        setMaxWidth(Double.MAX_VALUE);
        slotsBox = new VBox();
        slotsBox.spacingProperty().bind(ViewScale.scaleHeight(heightProperty(), 30));
        getChildren().add(slotsBox);
    }

    public void refresh(ClientGameState state) {
        slotsBox.getChildren().clear();
        List<String> names = new ArrayList<>(state.getPlayers().keySet());
        if (names.isEmpty()) {
            names.addAll(state.getLobbyPlayers());
        }

        double slotW = getWidth() > 0 ? getWidth() : 120;
        double parentH = getParent() instanceof javafx.scene.layout.Region region && region.getHeight() > 0
                ? region.getHeight() : ViewScale.DESIGN_H;
        double slotH = ViewScale.y(parentH, 80);

        for (String name : names) {
            Label slot = new Label(shortName(name));
            slot.setPrefSize(slotW, slotH);
            slot.setMinSize(slotW, slotH);
            slot.setMaxSize(slotW, slotH);
            slot.setStyle(UiStyles.GAME_USER_SLOT + playerColorStyle(state.getPlayers().get(name)));
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
