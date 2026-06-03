package client.view;

import client.controller.GameController;
import client.view.game.CardsPanel;
import client.view.game.TurnPanel;
import client.view.game.UsersPanel;
import client.view.layout.ViewScale;
import client.view.style.UiStyles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 * Finestra di gioco responsive.
 */
public class GameView extends BorderPane {
    private static final String DEFAULT_MAP_SVG = "src/client/assets/risk-map.svg";

    private final GameController controller;
    private final MapView mapView;
    private final TurnPanel turnPanel;
    private final UsersPanel usersPanel;
    private final CardsPanel cardsPanel;
    private final Label statusLabel;

    public GameView(GameController controller) {
        this.controller = controller;
        this.mapView = new MapView(controller);
        this.turnPanel = new TurnPanel(controller);
        this.usersPanel = new UsersPanel();
        this.cardsPanel = new CardsPanel();
        this.statusLabel = new Label();

        buildLayout();
        refresh();

        widthProperty().addListener((obs, o, n) -> updateLayoutMargins());
        heightProperty().addListener((obs, o, n) -> updateLayoutMargins());
    }

    public void refresh() {
        turnPanel.refresh();
        usersPanel.refresh(controller.getState());
        cardsPanel.refresh();
        mapView.refresh();
        refreshStatus();
    }

    public void refreshStatus() {
        statusLabel.setText(controller.getState().getStatus());
    }

    private void buildLayout() {
        setStyle(UiStyles.GAME_ROOT);

        mapView.loadSvg(DEFAULT_MAP_SVG);
        StackPane mapHost = new StackPane(mapView);
        mapHost.setAlignment(Pos.CENTER);
        setCenter(mapHost);

        ViewScale.bindPrefWidth(turnPanel, widthProperty(), 140);
        turnPanel.prefHeightProperty().bind(ViewScale.scaleHeight(heightProperty(), 500));
        turnPanel.setMaxHeight(Double.MAX_VALUE);
        setLeft(turnPanel);

        ViewScale.bindPrefWidth(usersPanel, widthProperty(), 120);
        setRight(usersPanel);

        setBottom(cardsPanel);

        statusLabel.setStyle("-fx-text-fill: #333; -fx-font-size: 11px;");
        StackPane statusHost = new StackPane(statusLabel);
        statusHost.setAlignment(Pos.BOTTOM_LEFT);
        setTop(statusHost);

        updateLayoutMargins();
    }

    private void updateLayoutMargins() {
        double w = Math.max(widthProperty().get(), 1);
        double h = Math.max(heightProperty().get(), 1);

        BorderPane.setMargin(getLeft(), new Insets(ViewScale.y(h, 10), 0, 0, ViewScale.x(w, 10)));
        BorderPane.setMargin(getRight(), new Insets(ViewScale.y(h, 100), ViewScale.x(w, 50), 0, 0));
        BorderPane.setMargin(getBottom(), new Insets(0, ViewScale.x(w, 250), ViewScale.y(h, 20), ViewScale.x(w, 250)));
        BorderPane.setMargin(getCenter(), new Insets(ViewScale.y(h, 60), ViewScale.x(w, 200), ViewScale.y(h, 100), ViewScale.x(w, 160)));
        BorderPane.setMargin(getTop(), new Insets(0, 0, 0, ViewScale.x(w, 12)));
    }
}
