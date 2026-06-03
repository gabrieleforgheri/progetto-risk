package client.view.layout;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableDoubleValue;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

/**
 * Conversione proporzionale rispetto al design 1920×1080.
 */
public final class ViewScale {
    public static final double DESIGN_W = 1920;
    public static final double DESIGN_H = 1080;

    private ViewScale() {
    }

    public static double uniform(double width, double height) {
        if (width <= 0 || height <= 0) {
            return 1;
        }
        return Math.min(width / DESIGN_W, height / DESIGN_H);
    }

    public static double x(double windowWidth, double designPx) {
        return windowWidth * designPx / DESIGN_W;
    }

    public static double y(double windowHeight, double designPx) {
        return windowHeight * designPx / DESIGN_H;
    }

    public static void bindPrefSize(Region region, ObservableDoubleValue width, ObservableDoubleValue height,
                                    double designW, double designH) {
        region.prefWidthProperty().bind(scaleWidth(width, designW));
        region.prefHeightProperty().bind(scaleHeight(height, designH));
        region.minWidthProperty().bind(region.prefWidthProperty());
        region.minHeightProperty().bind(region.prefHeightProperty());
        region.maxWidthProperty().bind(region.prefWidthProperty());
        region.maxHeightProperty().bind(region.prefHeightProperty());
    }

    public static void bindPrefWidth(Region region, ObservableDoubleValue width, double designW) {
        region.prefWidthProperty().bind(scaleWidth(width, designW));
        region.maxWidthProperty().bind(region.prefWidthProperty());
    }

    public static void bindPrefHeight(Region region, ObservableDoubleValue height, double designH) {
        region.prefHeightProperty().bind(scaleHeight(height, designH));
        region.maxHeightProperty().bind(region.prefHeightProperty());
    }

    public static void bindSquareSize(Region region, ObservableDoubleValue width, ObservableDoubleValue height,
                                        double designPx) {
        DoubleBinding scale = uniformBinding(width, height);
        region.prefWidthProperty().bind(Bindings.multiply(scale, designPx));
        region.prefHeightProperty().bind(Bindings.multiply(scale, designPx));
        region.minWidthProperty().bind(region.prefWidthProperty());
        region.minHeightProperty().bind(region.prefHeightProperty());
        region.maxWidthProperty().bind(region.prefWidthProperty());
        region.maxHeightProperty().bind(region.prefHeightProperty());
    }

    public static void bindFont(Labeled labeled, ObservableDoubleValue width, ObservableDoubleValue height,
                                Font baseFont) {
        uniformBinding(width, height).addListener((obs, oldScale, newScale) ->
                labeled.setFont(Font.font(baseFont.getFamily(), baseFont.getSize() * newScale.doubleValue())));
    }

    public static void bindFont(TextInputControl field, ObservableDoubleValue width, ObservableDoubleValue height,
                                Font baseFont) {
        uniformBinding(width, height).addListener((obs, oldScale, newScale) ->
                field.setFont(Font.font(baseFont.getFamily(), baseFont.getSize() * newScale.doubleValue())));
    }

    public static void bindControlSize(Control control, ObservableDoubleValue width, ObservableDoubleValue height,
                                       double designW, double designH) {
        if (control instanceof Region region) {
            bindPrefSize(region, width, height, designW, designH);
        }
    }

    public static void bindImageSize(javafx.scene.image.ImageView imageView,
                                     ObservableDoubleValue width, ObservableDoubleValue height,
                                     double designW, double designH) {
        imageView.fitWidthProperty().bind(scaleWidth(width, designW));
        imageView.fitHeightProperty().bind(scaleHeight(height, designH));
    }

    public static DoubleBinding scaleWidth(ObservableDoubleValue width, double designW) {
        return Bindings.createDoubleBinding(() -> width.get() * designW / DESIGN_W, width);
    }

    public static DoubleBinding scaleHeight(ObservableDoubleValue height, double designH) {
        return Bindings.createDoubleBinding(() -> height.get() * designH / DESIGN_H, height);
    }

    public static DoubleBinding uniformBinding(ObservableDoubleValue width, ObservableDoubleValue height) {
        return Bindings.createDoubleBinding(() -> uniform(width.get(), height.get()), width, height);
    }

    public static void bindScale(Node node, ObservableDoubleValue width, ObservableDoubleValue height) {
        uniformBinding(width, height).addListener((obs, oldScale, newScale) -> {
            node.setScaleX(newScale.doubleValue());
            node.setScaleY(newScale.doubleValue());
        });
    }
}
