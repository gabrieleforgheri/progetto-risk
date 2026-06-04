package client.view;

import client.controller.GameController;
import client.view.component.ChatPanel;
import client.view.component.ColorChoicePanel;
import client.view.component.PlayerSlotsPanel;
import client.view.layout.ViewScale;
import client.view.style.UiFonts;
import client.view.style.UiStyles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Layout lobby responsive (design 1920×1080).
 */
public abstract class AbstractLobbyView extends BorderPane {
    protected final GameController controller;
    protected final PlayerSlotsPanel playersPanel;
    protected final ChatPanel chatPanel;
    protected final ColorChoicePanel colorChoicePanel;
    protected final Label statusLabel;
    protected final VBox bottomHost;

    protected AbstractLobbyView(GameController controller) {
        this.controller = controller;
        this.playersPanel = new PlayerSlotsPanel();
        this.chatPanel = new ChatPanel(controller::sendChat);
        this.colorChoicePanel = new ColorChoicePanel();
        this.statusLabel = new Label();
        this.bottomHost = new VBox(12);

        setStyle(UiStyles.LOBBY_GRADIENT);
        colorChoicePanel.setOnColorSelected(controller::chooseColor);
        colorChoicePanel.bindToWindow(widthProperty(), heightProperty());
        chatPanel.bindToWindow(widthProperty(), heightProperty());
        buildSharedLayout();

        widthProperty().addListener((obs, o, n) -> updateLayoutMargins());
        heightProperty().addListener((obs, o, n) -> updateLayoutMargins());
        playersPanel.widthProperty().addListener((obs, o, n) -> refresh());
    }

    public void refresh() {
        playersPanel.setPlayers(
                controller.getState().getLobbyPlayers(),
                controller.getState().getLobbyPlayerColors());
        chatPanel.refresh(controller.getState());
        colorChoicePanel.refresh(controller.getState());
        statusLabel.setText(controller.getState().getStatus());
        refreshHostSpecific();
    }

    protected void refreshHostSpecific() {
    }

    protected abstract void buildHostSpecificLayout();

    protected VBox getBottomHost() {
        return bottomHost;
    }

    private void buildSharedLayout() {
        Label title = new Label("GAME LOBBY");
        title.setTextFill(Color.BLACK);
        ViewScale.bindFont(title, widthProperty(), heightProperty(), UiFonts.gunshipBold(160));
        StackPane titleBox = new StackPane(title);
        setTop(titleBox);

        ViewScale.bindPrefSize(playersPanel, widthProperty(), heightProperty(), 800, 250);
        StackPane playersHost = new StackPane(playersPanel);
        playersHost.setAlignment(Pos.TOP_CENTER);
        setCenter(playersHost);

        VBox rightColumn = new VBox(16, chatPanel, colorChoicePanel);
        rightColumn.setFillWidth(true);
        rightColumn.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(chatPanel, Priority.ALWAYS);
        ViewScale.bindPrefWidth(rightColumn, widthProperty(), 300);
        ViewScale.bindPrefWidth(chatPanel, widthProperty(), 300);
        ViewScale.bindPrefWidth(colorChoicePanel, widthProperty(), 300);
        chatPanel.prefHeightProperty().bind(ViewScale.scaleHeight(heightProperty(), 648));
        chatPanel.setMaxHeight(Double.MAX_VALUE);

        StackPane rightHost = new StackPane(rightColumn);
        rightHost.setAlignment(Pos.TOP_RIGHT);
        setRight(rightHost);

        ViewScale.bindFont(statusLabel, widthProperty(), heightProperty(), UiFonts.gunship(12));
        statusLabel.setPadding(new Insets(8));
        statusLabel.setAlignment(Pos.CENTER_LEFT);
        bottomHost.setAlignment(Pos.CENTER);
        bottomHost.getChildren().add(statusLabel);
        setBottom(bottomHost);

        updateLayoutMargins();
    }

    private void updateLayoutMargins() {
        double w = Math.max(widthProperty().get(), 1);
        double h = Math.max(heightProperty().get(), 1);

        BorderPane.setMargin(getTop(), new Insets(ViewScale.y(h, 24), 0, 0, 0));
        BorderPane.setMargin((StackPane) getCenter(), new Insets(ViewScale.y(h, 100), 0, 0, 0));
        BorderPane.setMargin((StackPane) getRight(), new Insets(
                ViewScale.y(h, 100),
                ViewScale.x(w, 96),
                ViewScale.y(h, 108),
                0));
        BorderPane.setMargin(bottomHost, new Insets(0, 0, ViewScale.y(h, 8), ViewScale.x(w, 16)));
    }

    protected final void finishLobbyLayout() {
        buildHostSpecificLayout();
    }
}
