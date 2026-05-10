package client.view;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.List;

public class EventLogView extends VBox {
    private final ListView<String> eventList;

    public EventLogView() {
        Label title = new Label("Event Log");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        this.eventList = new ListView<>();
        setSpacing(8);
        getChildren().addAll(title, eventList);
    }

    public void setEvents(List<String> events) {
        eventList.getItems().setAll(events);
        if (!events.isEmpty()) {
            eventList.scrollTo(events.size() - 1);
        }
    }
}
