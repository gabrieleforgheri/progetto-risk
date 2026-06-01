package client.view;

import client.controller.GameController;
import client.map.SvgTerritoryMap;
import client.view.style.UiStyles;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.nio.file.Path;

/**
 * Mappa territori da {@code risk-map.svg}: fill = colore proprietario, stroke = confini.
 */
public class MapView extends BorderPane {
    public static final String DEFAULT_MAP_SVG = "src/client/assets/risk-map.svg";
    private static final String CLASSPATH_MAP = "/client/assets/risk-map.svg";

    private final GameController controller;
    private final SvgTerritoryMap svgMap;
    private final ScrollPane scrollPane;
    private boolean mapLoaded;

    public MapView(GameController controller) {
        this.controller = controller;
        this.svgMap = new SvgTerritoryMap();
        this.scrollPane = new ScrollPane(svgMap);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setStyle("-fx-background-color: #b8d4e8;");
        setCenter(scrollPane);
    }

    public void loadSvg(String svgPath) {
        mapLoaded = tryLoadMap(svgPath);
        if (!mapLoaded) {
            Label error = new Label("Mappa non trovata.\nAtteso: "
                    + Path.of(DEFAULT_MAP_SVG).toAbsolutePath());
            error.setStyle(UiStyles.GAME_MAP_PLACEHOLDER);
            error.setWrapText(true);
            error.setAlignment(Pos.CENTER);
            setCenter(error);
            return;
        }
        setCenter(scrollPane);
        refresh();
    }

    public void refresh() {
        if (mapLoaded) {
            svgMap.applyState(controller.getState());
        }
    }

    public String getSvgPath() {
        return DEFAULT_MAP_SVG;
    }

    private boolean tryLoadMap(String svgPath) {
        File file = Path.of(svgPath).toFile();
        if (file.exists()) {
            try {
                svgMap.loadFromFile(file);
                return svgMap.isLoaded();
            } catch (IllegalStateException exception) {
                return false;
            }
        }

        try {
            svgMap.loadFromClasspath(CLASSPATH_MAP);
            return svgMap.isLoaded();
        } catch (IllegalStateException exception) {
            return false;
        }
    }
}
