package client.view;

import client.controller.GameController;
import client.util.ConnectionAddress;
import client.view.component.AnimatedStartButton;
import client.view.layout.ViewScale;
import client.view.style.UiFonts;
import client.view.style.UiStyles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Finestra iniziale — immagine 1920×1080 nitida, layout da {@code finestra di avvio/}.
 */
public class StartView extends StackPane {
    private static final String BACKGROUND_IMAGE = "/client/assets/ui/start-background.png";
    private static final double BG_NATIVE_W = 1920;
    private static final double BG_NATIVE_H = 1080;

    private final GameController controller;
    private final TextField addressField;
    private final TextField nickNameField;
    private final Label statusLabel;
    private final ImageView background;

    public StartView(GameController controller) {
        this.controller = controller;
        this.addressField = new TextField();
        this.nickNameField = new TextField();
        this.statusLabel = new Label();
        this.background = new ImageView(loadBackgroundImage());

        buildLayout();
        widthProperty().addListener((obs, o, n) -> fitBackground());
        heightProperty().addListener((obs, o, n) -> fitBackground());
    }

    public void refreshStatus() {
        statusLabel.setText(controller.getState().getStatus());
    }

    private static Image loadBackgroundImage() {
        return new Image(
                StartView.class.getResourceAsStream(BACKGROUND_IMAGE),
                BG_NATIVE_W,
                BG_NATIVE_H,
                false,
                false);
    }

    private void fitBackground() {
        double w = getWidth();
        double h = getHeight();
        if (w <= 0 || h <= 0) {
            return;
        }
        // Copre la finestra mantenendo aspect ratio (come CSS width:100%), senza smoothing.
        double scale = Math.max(w / BG_NATIVE_W, h / BG_NATIVE_H);
        background.setFitWidth(BG_NATIVE_W * scale);
        background.setFitHeight(BG_NATIVE_H * scale);
        background.setSmooth(false);
    }

    private void buildLayout() {
        background.setPreserveRatio(true);
        background.setSmooth(false);
        StackPane.setAlignment(background, Pos.CENTER);

        Label title = new Label("RISK!");
        title.setTextFill(Color.web("#f5f5dc"));
        ViewScale.bindFont(title, widthProperty(), heightProperty(), UiFonts.gunshipBold(100));

        nickNameField.setPromptText("NICKNAME");
        nickNameField.setStyle(UiStyles.START_INPUT);
        ViewScale.bindControlSize(nickNameField, widthProperty(), heightProperty(), 300, 54);
        ViewScale.bindFont(nickNameField, widthProperty(), heightProperty(), UiFonts.gunship(16));

        addressField.setPromptText("INSERT IP:PORT");
        addressField.setStyle(UiStyles.START_INPUT);
        ViewScale.bindControlSize(addressField, widthProperty(), heightProperty(), 300, 54);
        ViewScale.bindFont(addressField, widthProperty(), heightProperty(), UiFonts.gunship(16));

        Button joinButton = AnimatedStartButton.create("JOIN GAME", this::onJoinClicked);
        joinButton.setStyle(UiStyles.START_BUTTON);
        ViewScale.bindControlSize(joinButton, widthProperty(), heightProperty(), 300, 60);

        Button createButton = AnimatedStartButton.create("CREATE GAME", this::onCreateServerClicked);
        createButton.setStyle(UiStyles.START_BUTTON);
        ViewScale.bindControlSize(createButton, widthProperty(), heightProperty(), 300, 60);

        HBox joinRow = new HBox(16, joinButton, addressField);
        joinRow.setAlignment(Pos.CENTER);
        joinRow.prefWidthProperty().bind(ViewScale.scaleWidth(widthProperty(), 800));
        joinRow.setMaxWidth(Double.MAX_VALUE);

        HBox createRow = new HBox(createButton);
        createRow.setAlignment(Pos.CENTER);
        createRow.prefWidthProperty().bind(ViewScale.scaleWidth(widthProperty(), 500));

        statusLabel.setTextFill(Color.web("#eeeeee"));
        ViewScale.bindFont(statusLabel, widthProperty(), heightProperty(), UiFonts.gunship(12));

        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        content.maxWidthProperty().bind(widthProperty());
        content.getChildren().addAll(title, nickNameField, joinRow, createRow, statusLabel);
        content.spacingProperty().bind(ViewScale.scaleHeight(heightProperty(), 50));
        content.paddingProperty().bind(new javafx.beans.binding.ObjectBinding<>() {
            {
                bind(heightProperty());
            }

            @Override
            protected Insets computeValue() {
                double top = ViewScale.y(heightProperty().get(), 80);
                double bottom = ViewScale.y(heightProperty().get(), 40);
                return new Insets(top, 0, bottom, 0);
            }
        });

        // Solo immagine + contenuti UI — niente overlay gradiente che la offusca.
        getChildren().addAll(background, content);
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
