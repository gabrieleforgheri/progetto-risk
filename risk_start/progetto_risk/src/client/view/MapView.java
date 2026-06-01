package client.view;

import client.controller.GameController;
import client.view.style.UiStyles;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.File;

/**
 * Area mappa centrale (bozza: "SVG FILE MAP").
 * <p>
 * Non usa {@code WebView} / {@code javafx-web}: su JDK 26 il modulo {@code jdk.jsobject}
 * non esiste più e JavaFX Web non parte. STILE: sostituire il placeholder con
 * {@code ImageView} (PNG/SVG rasterizzato) o ripristinare WebView solo su JDK 21–25.
 */
public class MapView extends BorderPane {
    private final GameController controller;
    private final StackPane mapArea;
    private String svgPath;

    public MapView(GameController controller) {
        this.controller = controller;
        this.mapArea = new StackPane();
        buildLayout();
    }

    public void loadSvg(String svgPath) {
        this.svgPath = svgPath;
        File file = new File(svgPath);
        if (!file.exists()) {
            showMapMessage("Mappa non trovata:\n" + svgPath
                    + "\n\nCopia risk-map.svg in src/client/assets/");
            return;
        }

        // STILE: caricare l'immagine (es. SVG→PNG) con Image + ImageView invece del WebView
        showMapMessage("File mappa presente:\n" + file.getAbsolutePath()
                + "\n\n(visualizzazione interattiva da collegare — vedi commenti in MapView.java)");
    }

    public String getSvgPath() {
        return svgPath;
    }

    private void buildLayout() {
        showMapMessage("Mappa — in attesa di risk-map.svg");
        setCenter(mapArea);
    }

    private void showMapMessage(String text) {
        Label label = new Label(text);
        label.setStyle(UiStyles.GAME_MAP_PLACEHOLDER);
        label.setWrapText(true);
        label.setMaxWidth(600);
        label.setAlignment(Pos.CENTER);
        mapArea.getChildren().setAll(label);
    }
}
