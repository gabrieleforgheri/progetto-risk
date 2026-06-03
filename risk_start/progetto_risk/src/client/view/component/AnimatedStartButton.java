package client.view.component;

import client.view.style.UiFonts;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.util.Duration;

/**
 * Pulsante finestra start con leggero effetto hover/press.
 */
public final class AnimatedStartButton {
    private AnimatedStartButton() {
    }

    public static Button create(String text, Runnable action) {
        Button button = new Button(text);
        button.setFont(UiFonts.gunshipBold(18));
        button.setCursor(Cursor.HAND);

        ScaleTransition hoverIn = new ScaleTransition(Duration.millis(120), button);
        hoverIn.setToX(1.04);
        hoverIn.setToY(1.04);

        ScaleTransition hoverOut = new ScaleTransition(Duration.millis(120), button);
        hoverOut.setToX(1);
        hoverOut.setToY(1);

        ScaleTransition press = new ScaleTransition(Duration.millis(50), button);
        press.setToX(0.96);
        press.setToY(0.96);

        button.setOnMouseEntered(event -> hoverIn.playFromStart());
        button.setOnMouseExited(event -> hoverOut.playFromStart());
        button.setOnMousePressed(event -> press.playFromStart());
        button.setOnMouseReleased(event -> hoverIn.playFromStart());
        button.setOnAction(event -> action.run());
        return button;
    }
}
