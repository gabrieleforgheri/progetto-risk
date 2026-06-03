package client.view.component;

import client.view.style.UiFonts;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.function.Consumer;

/**
 * Pulsante START lobby con animazioni dalla bozza CSS ({@code button:before/after}, {@code :active}).
 */
public class AnimatedLobbyButton extends StackPane {
    private static final Color FACE = Color.web("#e8e8e8");

    private final Label label;
    private final Rectangle face;
    private final Rectangle barHorizontal;
    private final Rectangle barVertical;
    private final ScaleTransition pressTransition;
    private Consumer<Void> onAction;

    public AnimatedLobbyButton(String text) {
        setCursor(Cursor.CROSSHAIR);
        setPickOnBounds(true);
        setMinSize(200, 60);
        setPrefSize(200, 60);

        barHorizontal = new Rectangle(206, 44);
        barHorizontal.setFill(FACE);
        barHorizontal.setMouseTransparent(true);

        barVertical = new Rectangle(184, 66);
        barVertical.setFill(FACE);
        barVertical.setMouseTransparent(true);

        face = new Rectangle(200, 60);
        face.setFill(FACE);
        face.setStroke(Color.BLACK);
        face.setStrokeWidth(2);
        face.setMouseTransparent(true);

        label = new Label(text);
        label.setFont(UiFonts.gunshipBold(20));
        label.setTextFill(Color.BLACK);
        label.setMouseTransparent(true);

        getChildren().addAll(barHorizontal, barVertical, face, label);
        StackPane.setAlignment(barHorizontal, Pos.CENTER);
        StackPane.setAlignment(barVertical, Pos.CENTER);
        StackPane.setAlignment(face, Pos.CENTER);
        StackPane.setAlignment(label, Pos.CENTER);

        pressTransition = new ScaleTransition(Duration.millis(50), this);
        pressTransition.setFromX(1);
        pressTransition.setFromY(1);
        pressTransition.setToX(0.95);
        pressTransition.setToY(0.95);

        setOnMouseEntered(event -> animateHover(true));
        setOnMouseExited(event -> {
            animateHover(false);
            resetScale();
        });
        setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY && !isDisabled()) {
                pressTransition.setRate(1);
                pressTransition.playFromStart();
            }
        });
        setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY && !isDisabled()) {
                resetScale();
                if (onAction != null && contains(event.getX(), event.getY())) {
                    onAction.accept(null);
                }
            }
        });

        widthProperty().addListener((obs, o, n) -> layoutDecorations(n.doubleValue(), getHeight()));
        heightProperty().addListener((obs, o, n) -> layoutDecorations(getWidth(), n.doubleValue()));
    }

    public void setOnAction(Consumer<Void> onAction) {
        this.onAction = onAction;
    }

    public void setButtonDisabled(boolean disabled) {
        setOpacity(disabled ? 0.45 : 1);
        setDisable(disabled);
        setCursor(disabled ? Cursor.DEFAULT : Cursor.CROSSHAIR);
    }

    private void layoutDecorations(double w, double h) {
        if (w <= 0 || h <= 0) {
            return;
        }
        double sx = w / 200;
        double sy = h / 60;
        face.setWidth(200 * sx);
        face.setHeight(60 * sy);
        barHorizontal.setWidth(206 * sx);
        barVertical.setHeight(66 * sy);
        barVertical.setWidth(184 * sx);
        label.setFont(UiFonts.gunshipBold(Math.max(14, 20 * Math.min(sx, sy))));
    }

    private void animateHover(boolean hover) {
        double sy = getHeight() > 0 ? getHeight() / 60 : 1;
        double sx = getWidth() > 0 ? getWidth() / 200 : 1;
        double hHeight = (hover ? 28 : 44) * sy;
        double vWidth = (hover ? 168 : 184) * sx;

        barHorizontal.setHeight(hHeight);
        barVertical.setWidth(vWidth);
    }

    private void resetScale() {
        pressTransition.stop();
        setScaleX(1);
        setScaleY(1);
    }
}
