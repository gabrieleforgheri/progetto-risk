package client.view;

import client.controller.GameController;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TerritoryPanel extends VBox {
    private final GameController controller;
    private final Label titleLabel;
    private final TextField fromField;
    private final TextField toField;
    private final Spinner<Integer> armiesSpinner;
    private final Button attackButton;
    private final Button moveButton;

    public TerritoryPanel(GameController controller) {
        this.controller = controller;
        this.titleLabel = new Label("Territory Action");
        this.fromField = new TextField();
        this.toField = new TextField();
        this.armiesSpinner = new Spinner<>(1, 99, 1);
        this.attackButton = new Button("Attack");
        this.moveButton = new Button("Move");
        buildLayout();
        bindActions();
    }

    public void refresh() {
        // Future step: update this panel from the selected territory in MapView.
    }

    private void buildLayout() {
        setSpacing(10);
        setPadding(new Insets(8, 0, 8, 0));

        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        fromField.setPromptText("From territory");
        toField.setPromptText("To territory");
        armiesSpinner.setEditable(true);

        GridPane form = new GridPane();
        form.setHgap(8);
        form.setVgap(8);
        form.add(new Label("From"), 0, 0);
        form.add(fromField, 1, 0);
        form.add(new Label("To"), 0, 1);
        form.add(toField, 1, 1);
        form.add(new Label("Armies"), 0, 2);
        form.add(armiesSpinner, 1, 2);

        HBox buttons = new HBox(8, attackButton, moveButton);
        getChildren().addAll(titleLabel, form, buttons);
    }

    private void bindActions() {
        attackButton.setOnAction(event -> controller.sendAttack(
                fromField.getText().trim(),
                toField.getText().trim(),
                armiesSpinner.getValue()
        ));

        moveButton.setOnAction(event -> controller.sendArmyMovement(
                fromField.getText().trim(),
                toField.getText().trim(),
                armiesSpinner.getValue()
        ));
    }
}
