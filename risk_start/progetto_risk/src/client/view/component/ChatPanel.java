package client.view.component;

import client.model.ClientGameState;
import client.view.layout.ViewScale;
import client.view.style.UiFonts;
import client.view.style.UiStyles;
import javafx.beans.value.ObservableDoubleValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.util.function.Consumer;

/**
 * Chat lobby (HTML: {@code .chat}) — input in basso, messaggi ancorati in basso, clip senza scroll.
 */
public class ChatPanel extends VBox {
    private final VBox messagesBox;
    private final TextField inputField;
    private final Label title;

    public ChatPanel(Consumer<String> onSend) {
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(8));
        setStyle(UiStyles.LOBBY_CHAT_BOX);
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);

        title = new Label("CHAT");
        title.setStyle(UiStyles.LOBBY_CHAT_TITLE);

        Region separator = new Region();
        separator.setStyle(UiStyles.LOBBY_CHAT_SEPARATOR);
        separator.setMaxWidth(Double.MAX_VALUE);

        messagesBox = new VBox(4);
        messagesBox.setAlignment(Pos.BOTTOM_LEFT);

        Region topSpacer = new Region();
        VBox.setVgrow(topSpacer, Priority.ALWAYS);

        VBox messagesContainer = new VBox(topSpacer, messagesBox);
        messagesContainer.setAlignment(Pos.BOTTOM_LEFT);
        VBox.setVgrow(messagesContainer, Priority.ALWAYS);
        messagesContainer.setMinHeight(0);

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(messagesContainer.widthProperty());
        clip.heightProperty().bind(messagesContainer.heightProperty());
        messagesContainer.setClip(clip);

        inputField = new TextField();
        inputField.setPromptText("Messaggio...");
        inputField.setMaxWidth(Double.MAX_VALUE);
        inputField.setOnAction(event -> {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                onSend.accept(text);
                inputField.clear();
            }
        });

        getChildren().addAll(title, separator, messagesContainer, inputField);
    }

    public void bindToWindow(ObservableDoubleValue windowWidth, ObservableDoubleValue windowHeight) {
        ViewScale.bindFont(title, windowWidth, windowHeight, UiFonts.gunshipBold(18));
        ViewScale.bindFont(inputField, windowWidth, windowHeight, UiFonts.gunship(15));
        inputField.prefHeightProperty().bind(ViewScale.scaleHeight(windowHeight, 34));
    }

    public void refresh(ClientGameState state) {
        messagesBox.getChildren().clear();
        for (ClientGameState.ChatLine line : state.getLobbyChat()) {
            Label label = new Label(formatLine(line));
            label.setWrapText(true);
            label.maxWidthProperty().bind(widthProperty().subtract(24));
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
