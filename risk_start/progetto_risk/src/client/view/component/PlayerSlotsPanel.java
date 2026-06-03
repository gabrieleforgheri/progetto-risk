package client.view.component;

import client.view.style.UiStyles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

/**
 * Area giocatori lobby — dimensioni impostate dal contenitore padre.
 */
public class PlayerSlotsPanel extends StackPane {
    private final HBox slotsRow;

    public PlayerSlotsPanel() {
        setStyle(UiStyles.LOBBY_PLAYERS_BOX);
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);

        slotsRow = new HBox();
        slotsRow.setAlignment(Pos.CENTER);
        slotsRow.setPadding(new Insets(12));
        getChildren().add(slotsRow);
    }

    public void setPlayers(List<String> playerNames, Map<String, String> playerColors) {
        slotsRow.getChildren().clear();
        if (playerNames.isEmpty()) {
            return;
        }

        double panelWidth = getWidth() > 0 ? getWidth() : getPrefWidth();
        if (panelWidth <= 0) {
            panelWidth = 800;
        }
        double panelHeight = getHeight() > 0 ? getHeight() : getPrefHeight();
        if (panelHeight <= 0) {
            panelHeight = 250;
        }

        double slotWidth = panelWidth / Math.min(playerNames.size(), 6);
        double scale = Math.min(panelWidth / 800, panelHeight / 250);
        double avatarSize = 100 * scale;
        double iconSize = avatarSize * 0.8;

        for (int i = 0; i < playerNames.size(); i++) {
            String nickName = playerNames.get(i);
            String color = playerColors.getOrDefault(nickName, "");
            slotsRow.getChildren().add(buildPlayerSlot(nickName, color, slotWidth, avatarSize, iconSize, i));
        }
    }

    private VBox buildPlayerSlot(String nickName, String colorHex, double slotWidth,
                                   double avatarSize, double iconSize, int index) {
        StackPane avatarFrame = new StackPane();
        avatarFrame.setPrefSize(avatarSize, avatarSize);
        avatarFrame.setMinSize(avatarSize, avatarSize);
        avatarFrame.setMaxSize(avatarSize, avatarSize);
        avatarFrame.setStyle("-fx-border-color: #2e7d32; -fx-border-width: 2; -fx-border-radius: 50; "
                + "-fx-background-radius: 50; -fx-background-color: "
                + (colorHex.isEmpty() ? "transparent" : colorHex) + ";");

        ImageView icon = loadIcon(index, iconSize);
        avatarFrame.getChildren().add(icon);

        Label name = new Label(nickName);
        name.setStyle(UiStyles.LOBBY_PLAYER_NAME + " -fx-font-size: " + Math.max(11, 14 * (avatarSize / 100)) + "px;");
        name.setPadding(new Insets(avatarSize * 0.12, 0, 0, avatarSize * 0.2));

        VBox slot = new VBox(6, avatarFrame, name);
        slot.setAlignment(Pos.TOP_CENTER);
        slot.setPrefWidth(slotWidth);
        slot.setMinWidth(slotWidth);
        HBox.setHgrow(slot, Priority.ALWAYS);
        return slot;
    }

    private static ImageView loadIcon(int index, double iconSize) {
        String path = UiStyles.LOBBY_PLAYER_ICON_PATHS[index % UiStyles.LOBBY_PLAYER_ICON_PATHS.length];
        Image image = new Image(
                PlayerSlotsPanel.class.getResourceAsStream(path), iconSize, iconSize, false, false);
        ImageView view = new ImageView(image);
        view.setSmooth(false);
        view.setFitWidth(iconSize);
        view.setFitHeight(iconSize);
        view.setPreserveRatio(true);
        return view;
    }
}
