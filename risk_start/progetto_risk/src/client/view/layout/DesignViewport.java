package client.view.layout;

import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * Area design fissa ({@value ViewScale#DESIGN_W}×{@value ViewScale#DESIGN_H}) centrata nella finestra.
 * Lo spazio esterno (letterbox / pillarbox) resta nero.
 */
public class DesignViewport extends StackPane {
    private final Region content;

    public DesignViewport(Region content) {
        this.content = content;
        setStyle("-fx-background-color: black;");
        setAlignment(Pos.CENTER);

        content.setPrefSize(ViewScale.DESIGN_W, ViewScale.DESIGN_H);
        content.setMinSize(ViewScale.DESIGN_W, ViewScale.DESIGN_H);
        content.setMaxSize(ViewScale.DESIGN_W, ViewScale.DESIGN_H);

        getChildren().add(content);

        var scale = ViewScale.uniformBinding(widthProperty(), heightProperty());
        content.scaleXProperty().bind(scale);
        content.scaleYProperty().bind(scale);
    }

    public Region getContent() {
        return content;
    }
}
