package client.view;

import client.controller.GameController;
import client.util.ConnectionAddress;
import client.view.style.UiStyles;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Finestra iniziale app (HTML: {@code .window.start}).
 */
public class StartView extends AnchorPane {
    private final GameController controller;
    private final TextField addressField;
    private final TextField nickNameField;
    private final Label statusLabel;

    public StartView(GameController controller) {
        this.controller = controller;
        this.addressField = new TextField();
        this.nickNameField = new TextField();
        this.statusLabel = new Label();

        setPrefSize(UiStyles.WINDOW_WIDTH, UiStyles.WINDOW_HEIGHT);
        setStyle(UiStyles.START_ROOT + UiStyles.WINDOW_DEBUG_BORDER);
        buildLayout();
    }

    public void refreshStatus() {
        statusLabel.setText(controller.getState().getStatus());
    }

    private void buildLayout() {
        // STILE: overlay titolo / logo sopra lo sfondo start
        Label title = new Label("RISK");
        title.setStyle("-fx-font-size: 42px; -fx-font-weight: bold; -fx-text-fill: white;");
        AnchorPane.setTopAnchor(title, 24.0);
        AnchorPane.setLeftAnchor(title, 24.0);

        // STILE: .join — pulsante JOIN (non in bozza: nickname richiesto dal protocollo)
        StackPane joinBox = clickableBox("JOIN", UiStyles.START_JOIN_BOX, this::onJoinClicked);
        AnchorPane.setTopAnchor(joinBox, 100.0);
        AnchorPane.setLeftAnchor(joinBox, 340.0);
        joinBox.setPrefSize(200, 40);

        // STILE: .start input — formato IP:PORTA
        addressField.setPromptText("IP:PORTA (es. 127.0.0.1:5555)");
        addressField.setStyle(UiStyles.START_ADDRESS_FIELD);
        addressField.setPrefWidth(280);
        AnchorPane.setTopAnchor(addressField, 108.0);
        AnchorPane.setLeftAnchor(addressField, 600.0);

        // Campo nickname (logica rete, assente nella bozza HTML)
        nickNameField.setPromptText("Nickname");
        nickNameField.setPrefWidth(200);
        AnchorPane.setTopAnchor(nickNameField, 150.0);
        AnchorPane.setLeftAnchor(nickNameField, 600.0);

        // STILE: .server_create — crea server locale + lobby host
        StackPane serverBox = clickableBox("CREATE SERVER", UiStyles.START_SERVER_BOX, this::onCreateServerClicked);
        AnchorPane.setTopAnchor(serverBox, 200.0);
        AnchorPane.setLeftAnchor(serverBox, 340.0);
        serverBox.setPrefSize(300, 40);

        statusLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 12px;");
        AnchorPane.setBottomAnchor(statusLabel, 12.0);
        AnchorPane.setLeftAnchor(statusLabel, 24.0);

        getChildren().addAll(title, joinBox, addressField, nickNameField, serverBox, statusLabel);
    }

    private StackPane clickableBox(String text, String style, Runnable action) {
        Label label = new Label(text);
        label.setStyle(UiStyles.START_JOIN_LABEL);
        StackPane box = new StackPane(label);
        box.setStyle(style);
        box.setOnMouseClicked(event -> action.run());
        return box;
    }

    private void onJoinClicked() {
        try {
            String nickName = requireNickName();
            ConnectionAddress address = ConnectionAddress.parse(addressField.getText());
            controller.connectAsClient(address.host(), address.port(), nickName);
        } catch (IllegalArgumentException exception) {
            controller.reportUserError(exception.getMessage());
            refreshStatus();
        }
    }

    private void onCreateServerClicked() {
        try {
            String nickName = requireNickName();
            controller.connectAsHost(nickName);
        } catch (IllegalArgumentException exception) {
            controller.reportUserError(exception.getMessage());
            refreshStatus();
        }
    }

    private String requireNickName() {
        String nickName = nickNameField.getText().trim();
        if (nickName.isEmpty()) {
            throw new IllegalArgumentException("Inserisci un nickname.");
        }
        return nickName;
    }
}
