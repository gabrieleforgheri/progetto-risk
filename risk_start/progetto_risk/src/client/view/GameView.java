package client.view;

import client.controller.GameController;
import client.view.game.CardsPanel;
import client.view.game.TurnPanel;
import client.view.game.UsersPanel;
import client.view.style.UiStyles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Finestra di gioco (HTML: {@code .window.game}) — identica per host e client.
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
    }

    public void refresh() {
        turnPanel.refresh();
        usersPanel.refresh(controller.getState());
        cardsPanel.refresh();
        refreshStatus();
    }

    public void refreshStatus() {
        statusLabel.setText(controller.getState().getStatus());
    }

    private void buildLayout() {
        setPadding(new Insets(8));

        // STILE: .turn — sinistra
        setLeft(turnPanel);

        // STILE: centro mappa — "SVG FILE MAP"
        mapView.loadSvg(DEFAULT_MAP_SVG);
        StackPane mapStack = new StackPane(mapView);
        mapStack.setAlignment(Pos.CENTER);
        setCenter(mapStack);

        // STILE: .users — destra (top: 50px, right: 25px in bozza)
        VBox rightColumn = new VBox(usersPanel);
        rightColumn.setPadding(new Insets(50, 25, 0, 0));
        setRight(rightColumn);

        // STILE: .cards — barra in basso
        BorderPane.setMargin(cardsPanel, new Insets(0, 200, 0, 160));
        setBottom(cardsPanel);

        statusLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 11px;");
        // Azioni territorio (non in bozza HTML): tenute per test/debug — STILE: nascondere in release
        // setTop(new TerritoryPanel(controller));

        setStyle("-fx-background-color: #f0f0f0;");
    }
}
