package client.view.component;

import client.view.style.UiStyles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Area giocatori in lobby (HTML: {@code .players_join.box} con {@code .player_icon}).
 */
public class PlayerSlotsPanel extends VBox {
    private static final String[] SLOT_COLORS = {
            "#7fffd4", "#8a2be2", "#ff0000", "#ffff00", "#ff8800", "#aa00ff"
    };

    private final FlowPane slotsPane;

    public PlayerSlotsPanel() {
        setSpacing(8);
        setPadding(new Insets(12));
        setStyle(UiStyles.LOBBY_PLAYERS_BOX);
        setPrefSize(500, 250);
        setMinSize(400, 200);

        slotsPane = new FlowPane(12, 12);
        slotsPane.setPrefWrapLength(480);
        getChildren().add(slotsPane);
    }

    public void setPlayers(List<String> playerNames) {
        slotsPane.getChildren().clear();
        for (int i = 0; i < playerNames.size(); i++) {
            slotsPane.getChildren().add(buildPlayerSlot(playerNames.get(i), SLOT_COLORS[i % SLOT_COLORS.length]));
        }
    }

    private VBox buildPlayerSlot(String nickName, String colorHex) {
        // STILE: sostituire "icon img" con ImageView / sprite avatar
        Label avatar = new Label(initials(nickName));
        avatar.setAlignment(Pos.CENTER);
        avatar.setStyle(UiStyles.LOBBY_PLAYER_AVATAR + " -fx-background-color: " + colorHex + ";");

        Label name = new Label(nickName);
        name.setStyle("-fx-font-size: 13px;");

        VBox slot = new VBox(6, new StackPane(avatar), name);
        slot.setAlignment(Pos.TOP_CENTER);
        slot.setPrefWidth(140);
        return slot;
    }

    private static String initials(String nickName) {
        if (nickName == null || nickName.isEmpty()) {
            return "?";
        }
        return nickName.substring(0, 1).toUpperCase();
    }
}
