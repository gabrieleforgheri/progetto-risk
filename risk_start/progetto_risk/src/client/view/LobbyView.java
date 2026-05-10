package client.view;

import client.controller.GameController;
import client.model.ClientGameState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class LobbyView extends BorderPane {
    private final GameController controller;
    private final TextField hostField;
    private final TextField portField;
    private final TextField nickNameField;
    private final Button connectButton;
    private final Button startButton;
    private final Label statusLabel;
    private final Label firstPlayerLabel;
    private final ListView<String> playersList;
    private final EventLogView eventLogView;

    public LobbyView(GameController controller) {
        this.controller = controller;
        this.hostField = new TextField("localhost");
        this.portField = new TextField("5555");
        this.nickNameField = new TextField();
        this.connectButton = new Button("Connect");
        this.startButton = new Button("Start");
        this.statusLabel = new Label();
        this.firstPlayerLabel = new Label();
        this.playersList = new ListView<>();
        this.eventLogView = new EventLogView();

        buildLayout();
        bindActions();
        refresh();
    }

    public void refresh() {
        ClientGameState state = controller.getState();
        playersList.getItems().setAll(state.getLobbyPlayers());
        firstPlayerLabel.setText("First player: " + emptyLabel(state.getFirstPlayer()));
        startButton.setDisable(!state.isFirstPlayer());
        refreshStatus();
        refreshEvents();
    }

    public void refreshStatus() {
        statusLabel.setText(controller.getState().getStatus());
    }

    public void refreshEvents() {
        eventLogView.setEvents(controller.getState().getEvents());
    }

    private void buildLayout() {
        setPadding(new Insets(24));
        setTop(buildHeader());
        setCenter(buildConnectionBox());
        setRight(buildLobbyBox());
        setBottom(statusLabel);
    }

    private VBox buildHeader() {
        Label title = new Label("Risk Lobby");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        Label subtitle = new Label("Connect to the server, wait for 3 to 6 players, then the first player can start.");
        subtitle.setStyle("-fx-text-fill: #555555;");

        VBox header = new VBox(6, title, subtitle);
        header.setPadding(new Insets(0, 0, 24, 0));
        return header;
    }

    private VBox buildConnectionBox() {
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(12);

        form.add(new Label("Host"), 0, 0);
        form.add(hostField, 1, 0);
        form.add(new Label("Port"), 0, 1);
        form.add(portField, 1, 1);
        form.add(new Label("Nickname"), 0, 2);
        form.add(nickNameField, 1, 2);

        HBox buttons = new HBox(10, connectButton, startButton);
        buttons.setAlignment(Pos.CENTER_LEFT);

        VBox box = new VBox(16, form, buttons, eventLogView);
        box.setPadding(new Insets(18));
        VBox.setVgrow(eventLogView, Priority.ALWAYS);
        return box;
    }

    private VBox buildLobbyBox() {
        Label playersTitle = new Label("Players");
        playersTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        playersList.setPrefWidth(280);
        playersList.setMinWidth(240);

        VBox box = new VBox(10, playersTitle, firstPlayerLabel, playersList);
        box.setPadding(new Insets(18));
        VBox.setVgrow(playersList, Priority.ALWAYS);
        return box;
    }

    private void bindActions() {
        connectButton.setOnAction(event -> {
            String host = hostField.getText().trim();
            int port = Integer.parseInt(portField.getText().trim());
            String nickName = nickNameField.getText().trim();
            controller.connect(host, port, nickName);
        });

        startButton.setOnAction(event -> controller.startGame());
    }

    private String emptyLabel(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "-";
        }
        return value;
    }
}
