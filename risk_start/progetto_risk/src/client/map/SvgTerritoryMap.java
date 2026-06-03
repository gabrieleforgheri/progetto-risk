package client.map;

import client.model.ClientGameState;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Carica {@code risk-map.svg} e crea shape JavaFX per ogni territorio.
 * Fill = colore giocatore; stroke nero = confini (come nella bozza SVG).
 */
public class SvgTerritoryMap extends StackPane {
    public static final double SVG_WIDTH = 749.81909;
    public static final double SVG_HEIGHT = 519.06781;
    private static final Color NEUTRAL_FILL = Color.web("#e8e8e8");
    private static final Color BORDER_STROKE = Color.BLACK;
    private static final Color SELECTED_STROKE = Color.web("#FFD700");
    private static final double BORDER_WIDTH = 1.2;
    private static final double SELECTED_BORDER_WIDTH = 3.0;

    private final Map<String, TerritoryShape> shapes = new LinkedHashMap<>();
    private final Group mapGroup = new Group();
    private final Pane mapPane = new Pane(mapGroup);
    private boolean loaded;
    private Consumer<String> territoryClickHandler;
    private String selectedTerritory = "";

    public SvgTerritoryMap() {
        mapPane.setMinSize(SVG_WIDTH, SVG_HEIGHT);
        mapPane.setPrefSize(SVG_WIDTH, SVG_HEIGHT);
        getChildren().add(mapPane);
        widthProperty().addListener((obs, oldW, newW) -> fitToBounds(newW.doubleValue(), getHeight()));
        heightProperty().addListener((obs, oldH, newH) -> fitToBounds(getWidth(), newH.doubleValue()));
        fitToBounds(getWidth() > 0 ? getWidth() : 700, getHeight() > 0 ? getHeight() : 500);
    }

    public void loadFromFile(File svgFile) {
        if (svgFile == null || !svgFile.exists()) {
            loaded = false;
            return;
        }
        try {
            Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(svgFile);
            loadPaths(document);
            loaded = true;
        } catch (Exception exception) {
            loaded = false;
            throw new IllegalStateException("Impossibile leggere la mappa SVG: " + svgFile, exception);
        }
    }

    public void loadFromClasspath(String resourcePath) {
        try (InputStream input = getClass().getResourceAsStream(resourcePath)) {
            if (input == null) {
                loaded = false;
                return;
            }
            Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(input);
            loadPaths(document);
            loaded = true;
        } catch (Exception exception) {
            loaded = false;
            throw new IllegalStateException("Impossibile leggere la mappa da classpath: " + resourcePath, exception);
        }
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setOnTerritoryClick(Consumer<String> handler) {
        this.territoryClickHandler = handler;
    }

    public void setSelectedTerritory(String territoryName) {
        this.selectedTerritory = territoryName == null ? "" : territoryName;
        if (loaded) {
            updateSelectionVisuals();
        }
    }

    /** Aggiorna fill (proprietario) e testo armate da {@link ClientGameState}. */
    public void applyState(ClientGameState state) {
        if (!loaded) {
            return;
        }
        mapGroup.applyCss();
        mapGroup.layout();

        for (TerritoryShape territoryShape : shapes.values()) {
            ClientGameState.TerritoryState territory = state.getTerritories().get(territoryShape.gameName());
            if (territory == null) {
                territoryShape.shape().setFill(NEUTRAL_FILL);
                territoryShape.shape().setOpacity(0.85);
                territoryShape.armyLabel().setVisible(false);
                continue;
            }

            territoryShape.shape().setFill(parseColor(territory.getColor(), NEUTRAL_FILL));
            territoryShape.shape().setOpacity(1.0);
            territoryShape.armyLabel().setText(String.valueOf(territory.getArmies()));
            territoryShape.armyLabel().setVisible(territory.getArmies() > 0);
            positionArmyLabel(territoryShape);
        }

        updateSelectionVisuals();
    }

    private void updateSelectionVisuals() {
        for (TerritoryShape territoryShape : shapes.values()) {
            boolean selected = territoryShape.gameName().equals(selectedTerritory);
            SVGPath shape = territoryShape.shape();
            shape.setStroke(selected ? SELECTED_STROKE : BORDER_STROKE);
            shape.setStrokeWidth(selected ? SELECTED_BORDER_WIDTH : BORDER_WIDTH);
        }
    }

    private void loadPaths(Document document) {
        shapes.clear();
        mapGroup.getChildren().clear();

        NodeList pathNodes = document.getElementsByTagName("path");
        for (int i = 0; i < pathNodes.getLength(); i++) {
            if (!(pathNodes.item(i) instanceof Element element)) {
                continue;
            }
            String svgId = element.getAttribute("id");
            String pathData = element.getAttribute("d");
            if (svgId.isBlank() || pathData.isBlank()) {
                continue;
            }

            String gameName = TerritorySvgMapper.toGameName(svgId);
            if (gameName == null) {
                continue;
            }

            SVGPath shape = new SVGPath();
            shape.setContent(pathData);
            shape.setFill(NEUTRAL_FILL);
            shape.setStroke(BORDER_STROKE);
            shape.setStrokeWidth(BORDER_WIDTH);
            shape.setPickOnBounds(true);
            shape.setCursor(Cursor.HAND);
            shape.setOnMouseClicked(event -> {
                if (territoryClickHandler != null) {
                    territoryClickHandler.accept(gameName);
                    event.consume();
                }
            });

            Text armyLabel = new Text();
            armyLabel.setFont(Font.font("Sans-serif", FontWeight.BOLD, 11));
            armyLabel.setFill(Color.web("#111111"));
            armyLabel.setVisible(false);
            armyLabel.setMouseTransparent(true);

            shapes.put(gameName, new TerritoryShape(gameName, svgId, shape, armyLabel));
            mapGroup.getChildren().addAll(shape, armyLabel);
        }
    }

    private void positionArmyLabel(TerritoryShape territoryShape) {
        Bounds bounds = territoryShape.shape().getBoundsInParent();
        if (bounds.getWidth() <= 0 || bounds.getHeight() <= 0) {
            return;
        }
        Text label = territoryShape.armyLabel();
        double x = bounds.getMinX() + bounds.getWidth() / 2 - label.getLayoutBounds().getWidth() / 2;
        double y = bounds.getMinY() + bounds.getHeight() / 2 - label.getLayoutBounds().getHeight() / 2;
        label.setLayoutX(x);
        label.setLayoutY(y);
    }

    private void fitToBounds(double targetWidth, double targetHeight) {
        if (targetWidth <= 0 || targetHeight <= 0) {
            return;
        }
        double scale = Math.min(targetWidth / SVG_WIDTH, targetHeight / SVG_HEIGHT);
        mapGroup.setScaleX(scale);
        mapGroup.setScaleY(scale);
        mapPane.setPrefSize(SVG_WIDTH * scale, SVG_HEIGHT * scale);
    }

    private static Color parseColor(String hex, Color fallback) {
        if (hex == null || hex.isBlank()) {
            return fallback;
        }
        try {
            return Color.web(hex.trim());
        } catch (IllegalArgumentException exception) {
            return fallback;
        }
    }

    private record TerritoryShape(String gameName, String svgId, SVGPath shape, Text armyLabel) {
    }
}
