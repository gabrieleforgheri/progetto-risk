package client.view.component;

import client.model.ClientGameState;
import client.view.layout.ViewScale;
import client.view.style.UiFonts;
import client.view.style.UiStyles;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ObservableDoubleValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Selettore colori lobby (HTML: {@code .colors} + {@code .color_choice .box} — 30×30px).
 */
public class ColorChoicePanel extends VBox {
    private static final double BOX_DESIGN_PX = UiStyles.LOBBY_COLOR_BOX_SIZE;
    private static final double GRID_WRAP_DESIGN_PX = UiStyles.LOBBY_COLOR_GRID_WRAP;

    private final Map<String, Region> colorBoxes = new HashMap<>();
    private final FlowPane grid;
    private final Label title;
    private Consumer<String> onColorSelected;
    private ObservableDoubleValue windowWidth;
    private ObservableDoubleValue windowHeight;

    public ColorChoicePanel() {
        setAlignment(Pos.TOP_CENTER);
        setMaxWidth(Double.MAX_VALUE);
        setPadding(new Insets(8, 0, 0, 0));

        title = new Label("CHOOSE YOUR COLOR");
        title.setFont(UiFonts.gunshipBold(16));

        grid = new FlowPane(8, 8);
        grid.setAlignment(Pos.CENTER);

        for (String colorHex : UiStyles.LOBBY_PLAYER_COLORS) {
            Region box = buildColorBox(colorHex);
            colorBoxes.put(colorHex.toLowerCase(), box);
            grid.getChildren().add(box);
        }

        getChildren().addAll(title, grid);
    }

    /** Scala i box rispetto alla finestra (design 1920×1080), non al pannello. */
    public void bindToWindow(ObservableDoubleValue windowWidth, ObservableDoubleValue windowHeight) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

        ViewScale.bindFont(title, windowWidth, windowHeight, UiFonts.gunshipBold(16));
        grid.hgapProperty().bind(ViewScale.scaleWidth(windowWidth, 8));
        grid.vgapProperty().bind(ViewScale.scaleWidth(windowWidth, 8));
        grid.prefWrapLengthProperty().bind(ViewScale.scaleWidth(windowWidth, GRID_WRAP_DESIGN_PX));

        for (Region box : colorBoxes.values()) {
            bindColorBoxSize(box);
        }
    }

    private void bindColorBoxSize(Region box) {
        if (windowWidth == null) {
            return;
        }
        box.prefWidthProperty().bind(ViewScale.scaleWidth(windowWidth, BOX_DESIGN_PX));
        box.prefHeightProperty().bind(ViewScale.scaleWidth(windowWidth, BOX_DESIGN_PX));
        box.minWidthProperty().bind(box.prefWidthProperty());
        box.minHeightProperty().bind(box.prefHeightProperty());
        box.maxWidthProperty().bind(box.prefWidthProperty());
        box.maxHeightProperty().bind(box.prefHeightProperty());
    }

    public void setOnColorSelected(Consumer<String> onColorSelected) {
        this.onColorSelected = onColorSelected;
    }

    public void refresh(ClientGameState state) {
        String myNickName = state.getMyNickName();
        String myColor = state.getLobbyPlayerColor(myNickName).toLowerCase();

        Map<String, String> takenByOther = new HashMap<>();
        for (String player : state.getLobbyPlayers()) {
            String color = state.getLobbyPlayerColor(player);
            if (!color.isEmpty() && !player.equals(myNickName)) {
                takenByOther.put(color.toLowerCase(), player);
            }
        }

        for (String colorHex : UiStyles.LOBBY_PLAYER_COLORS) {
            String key = colorHex.toLowerCase();
            Region box = colorBoxes.get(key);
            boolean taken = takenByOther.containsKey(key);
            boolean selected = key.equals(myColor);
            box.setStyle(baseStyle(colorHex, selected, taken));
            box.setDisable(taken);
        }
    }

    private Region buildColorBox(String colorHex) {
        Region box = new Region();
        box.setStyle(baseStyle(colorHex, false, false));
        box.setOnMouseClicked(event -> {
            if (box.isDisabled() || onColorSelected == null) {
                return;
            }
            ScaleTransition pulse = new ScaleTransition(Duration.millis(120), box);
            pulse.setFromX(1);
            pulse.setFromY(1);
            pulse.setToX(1.15);
            pulse.setToY(1.15);
            pulse.setAutoReverse(true);
            pulse.setCycleCount(2);
            pulse.setOnFinished(e -> onColorSelected.accept(colorHex));
            pulse.play();
        });
        return box;
    }

    private static String baseStyle(String colorHex, boolean selected, boolean taken) {
        String border = selected
                ? UiStyles.LOBBY_COLOR_BOX_SELECTED
                : taken ? UiStyles.LOBBY_COLOR_BOX_TAKEN : UiStyles.LOBBY_COLOR_BOX;
        return border + " -fx-background-color: " + colorHex + ";";
    }
}
