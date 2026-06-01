package client.view;

import client.controller.GameController;
import client.view.component.ChatPanel;
import client.view.component.PlayerSlotsPanel;
import client.view.style.UiStyles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * Layout condiviso lobby host/client (HTML: {@code .window.lobby_*}).
 */
public abstract class AbstractLobbyView extends AnchorPane {
    protected final GameController controller;
    protected final PlayerSlotsPanel playersPanel;
    protected final ChatPanel chatPanel;
    protected final Label statusLabel;
    protected final TextField nickNameField;

    protected AbstractLobbyView(GameController controller) {
        this.controller = controller;
        this.playersPanel = new PlayerSlotsPanel();
        this.chatPanel = new ChatPanel(controller::sendChat);
        this.statusLabel = new Label();
        this.nickNameField = new TextField();

        setPrefSize(UiStyles.WINDOW_WIDTH, UiStyles.WINDOW_HEIGHT);
        setStyle("-fx-background-color: #eceff1;" + UiStyles.WINDOW_DEBUG_BORDER);
        buildSharedLayout();
    }

    public void refresh() {
        playersPanel.setPlayers(controller.getState().getLobbyPlayers());
        chatPanel.refresh(controller.getState());
        statusLabel.setText(controller.getState().getStatus());
        refreshHostSpecific();
    }

    protected void refreshHostSpecific() {
    }

    protected abstract void buildHostSpecificLayout();

    private void buildSharedLayout() {
        // STILE: barra superiore connessione (non in bozza HTML, necessaria per il protocollo)
        nickNameField.setPromptText("Nickname");
        nickNameField.setDisable(true);
        Label modeLabel = new Label(lobbyModeLabel());
        modeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        HBox topBar = new HBox(12, modeLabel, nickNameField, statusLabel);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(8, 12, 8, 12));
        AnchorPane.setTopAnchor(topBar, 8.0);
        AnchorPane.setLeftAnchor(topBar, 8.0);
        AnchorPane.setRightAnchor(topBar, 8.0);

        // STILE: .players_join — left 25%, top 100px (bozza CSS)
        AnchorPane.setTopAnchor(playersPanel, 100.0);
        AnchorPane.setLeftAnchor(playersPanel, 250.0);

        // STILE: .chat — left 80%, top 100px (bozza CSS)
        AnchorPane.setTopAnchor(chatPanel, 100.0);
        AnchorPane.setRightAnchor(chatPanel, 24.0);

        getChildren().addAll(topBar, playersPanel, chatPanel);
    }

    /** Chiamare dalla sottoclasse dopo aver creato i controlli specifici (es. pulsante Start). */
    protected final void finishLobbyLayout() {
        buildHostSpecificLayout();
    }

    protected abstract String lobbyModeLabel();

    public void disableNickNameField() {
        nickNameField.setDisable(true);
    }

    public void setNickName(String nickName) {
        nickNameField.setText(nickName);
    }
}
