package client.view;

import client.controller.GameController;
import javafx.concurrent.Worker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;

public class MapView extends BorderPane {
    private final GameController controller;
    private final WebView webView;
    private String svgPath;

    public MapView(GameController controller) {
        this.controller = controller;
        this.webView = new WebView();
        buildLayout();
    }

    public void loadSvg(String svgPath) {
        this.svgPath = svgPath;
        File file = new File(svgPath);
        if (!file.exists()) {
            showMissingMap(svgPath);
            return;
        }

        WebEngine engine = webView.getEngine();
        engine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                installSvgClickBridge();
            }
        });
        engine.load(file.toURI().toString());
        setCenter(webView);
    }

    public String getSvgPath() {
        return svgPath;
    }

    private void buildLayout() {
        Label placeholder = new Label("Map SVG not loaded yet.");
        placeholder.setStyle("-fx-font-size: 20px; -fx-text-fill: #666666;");
        setCenter(placeholder);
    }

    private void showMissingMap(String path) {
        Label error = new Label("Map SVG not found: " + path);
        error.setStyle("-fx-font-size: 18px; -fx-text-fill: #aa0000;");
        setCenter(error);
    }

    private void installSvgClickBridge() {
        // Future step: expose Java object to JavaScript and map SVG element ids to territory names.
        // The expected SVG convention should be one clickable element per territory, with id=territory name/key.
    }
}
