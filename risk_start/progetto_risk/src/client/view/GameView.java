package client.view;

import client.controller.GameController;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class GameView extends BorderPane {
    private static final String DEFAULT_MAP_SVG = "src/client/assets/risk-map.svg";

    private final GameController controller;
    private final MapView mapView;
    private final PlayerPanel playerPanel;
    private final TerritoryPanel territoryPanel;
    private final EventLogView eventLogView;
    private final Label statusLabel;

    public GameView(GameController controller) {
        this.controller = controller;
        this.mapView = new MapView(controller);
        this.playerPanel = new PlayerPanel(controller);
        this.territoryPanel = new TerritoryPanel(controller);
        this.eventLogView = new EventLogView();
        this.statusLabel = new Label();

        buildLayout();
        refresh();
    }

    public void refresh() {
        playerPanel.refresh();
        territoryPanel.refresh();
        refreshEvents();
        refreshStatus();
    }

    public void refreshEvents() {
        eventLogView.setEvents(controller.getState().getEvents());
    }

    public void refreshStatus() {
        statusLabel.setText(controller.getState().getStatus());
    }

    private void buildLayout() {
        mapView.loadSvg(DEFAULT_MAP_SVG);
        setCenter(mapView);
        setRight(buildSideBar());
        setBottom(statusLabel);
    }

    private VBox buildSideBar() {
        VBox sideBar = new VBox(14, playerPanel, territoryPanel, eventLogView);
        sideBar.setPadding(new Insets(16));
        sideBar.setPrefWidth(340);
        VBox.setVgrow(eventLogView, Priority.ALWAYS);
        return sideBar;
    }
}
