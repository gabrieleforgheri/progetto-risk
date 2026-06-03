package client.view;

import client.controller.GameController;
import client.map.SvgTerritoryMap;
import client.view.style.UiStyles;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.nio.file.Path;

/**
 * Mappa SVG che si adatta all'area disponibile.
 */
public class MapView extends StackPane {
    public static final String DEFAULT_MAP_SVG = "src/client/assets/risk-map.svg";
    private static final String CLASSPATH_MAP = "/client/assets/risk-map.svg";

    private final GameController controller;
    private final SvgTerritoryMap svgMap;
    private boolean mapLoaded;

    public MapView(GameController controller) {
        this.controller = controller;
        this.svgMap = new SvgTerritoryMap();
        this.svgMap.setOnTerritoryClick(controller::onTerritorySelected);
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: transparent;");
        svgMap.prefWidthProperty().bind(widthProperty());
        svgMap.prefHeightProperty().bind(heightProperty());
        svgMap.maxWidthProperty().bind(widthProperty());
        svgMap.maxHeightProperty().bind(heightProperty());
    }

    public void loadSvg(String svgPath) {
        mapLoaded = tryLoadMap(svgPath);
        getChildren().clear();
        if (!mapLoaded) {
            Label error = new Label("Mappa non trovata.\nAtteso: "
                    + Path.of(DEFAULT_MAP_SVG).toAbsolutePath());
            error.setStyle(UiStyles.GAME_MAP_PLACEHOLDER);
            error.setWrapText(true);
            error.setAlignment(Pos.CENTER);
            getChildren().add(error);
            return;
        }
        getChildren().add(svgMap);
        refresh();
    }

    public void refresh() {
        if (mapLoaded) {
            svgMap.applyState(controller.getState());
            svgMap.setSelectedTerritory(controller.getSelectedTerritory());
        }
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
