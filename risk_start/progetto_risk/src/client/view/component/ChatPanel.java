package client.view.component;

import client.model.ClientGameState;
import client.view.style.UiStyles;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

/**
 * Pannello chat lobby (HTML: {@code .chat.box}).
 */
public class ChatPanel extends VBox {
    private final VBox messagesBox;
    private final TextField inputField;

    public ChatPanel(Consumer<String> onSend) {
        setSpacing(6);
        setPadding(new Insets(8));
        getStyleClass().add("chat-panel");
        setStyle(UiStyles.LOBBY_CHAT_BOX);
        setPrefWidth(220);
        setMinWidth(180);

        Label title = new Label("CHAT");
        title.setStyle(UiStyles.LOBBY_CHAT_TITLE);

        messagesBox = new VBox(4);
        ScrollPane scroll = new ScrollPane(messagesBox);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        inputField = new TextField();
        inputField.setPromptText("Scrivi un messaggio...");
        inputField.setOnAction(event -> {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                onSend.accept(text);
                inputField.clear();
            }
        });

        getChildren().addAll(title, scroll, inputField);
    }

    public void refresh(ClientGameState state) {
        messagesBox.getChildren().clear();
        for (ClientGameState.ChatLine line : state.getLobbyChat()) {
            Label label = new Label(formatLine(line));
            label.setWrapText(true);
            if (line.serverMessage()) {
                label.setStyle(UiStyles.LOBBY_CHAT_SERVER_MESSAGE);
            } else {
                label.setStyle(UiStyles.LOBBY_CHAT_PLAYER_MESSAGE);
            }
            messagesBox.getChildren().add(label);
        }
    }

    private static String formatLine(ClientGameState.ChatLine line) {
        if (line.serverMessage()) {
            return line.text();
        }
        return line.sender() + ": " + line.text();
    }
}
