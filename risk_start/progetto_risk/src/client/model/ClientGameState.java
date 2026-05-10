package client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClientGameState {
    private String myNickName = "";
    private String status = "Disconnected.";
    private String firstPlayer = "";
    private String currentPlayer = "";
    private final List<String> lobbyPlayers = new ArrayList<>();
    private final List<String> events = new ArrayList<>();
    private final Map<String, PlayerState> players = new LinkedHashMap<>();

    public String getMyNickName() {
        return myNickName;
    }

    public void setMyNickName(String myNickName) {
        this.myNickName = myNickName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer == null ? "" : currentPlayer;
    }

    public List<String> getLobbyPlayers() {
        return Collections.unmodifiableList(lobbyPlayers);
    }

    public List<String> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public Map<String, PlayerState> getPlayers() {
        return Collections.unmodifiableMap(players);
    }

    public boolean isFirstPlayer() {
        return myNickName != null && myNickName.equals(firstPlayer);
    }

    public void addEvent(String event) {
        events.add(event);
        if (events.size() > 200) {
            events.remove(0);
        }
    }

    public void updateLobby(Map<String, String> data) {
        lobbyPlayers.clear();
        lobbyPlayers.addAll(splitList(data.get("players")));
        firstPlayer = valueOrEmpty(data.get("firstPlayer"));
    }

    public void updateStartedGame(Map<String, String> data) {
        players.clear();
        currentPlayer = valueOrEmpty(data.get("currentPlayer"));

        for (String nickName : splitList(data.get("players"))) {
            String prefix = "player." + nickName + ".";
            int armies = parseInt(data.get(prefix + "armies"));
            int remainingArmies = parseInt(data.get(prefix + "remainingArmies"));
            String color = valueOrEmpty(data.get(prefix + "color"));
            List<String> territories = splitList(data.get(prefix + "territories"));
            players.put(nickName, new PlayerState(nickName, armies, remainingArmies, color, territories));
        }
    }

    private static List<String> splitList(String value) {
        List<String> result = new ArrayList<>();
        if (value == null || value.trim().isEmpty()) {
            return result;
        }

        String[] parts = value.split(",");
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                result.add(trimmed);
            }
        }
        return result;
    }

    private static int parseInt(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    private static String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }

    public static class PlayerState {
        private final String nickName;
        private final int initialArmies;
        private final int remainingArmies;
        private final String color;
        private final List<String> territories;

        public PlayerState(String nickName, int initialArmies, int remainingArmies,
                           String color, List<String> territories) {
            this.nickName = nickName;
            this.initialArmies = initialArmies;
            this.remainingArmies = remainingArmies;
            this.color = color;
            this.territories = new ArrayList<>(territories);
        }

        public String getNickName() {
            return nickName;
        }

        public int getInitialArmies() {
            return initialArmies;
        }

        public int getRemainingArmies() {
            return remainingArmies;
        }

        public String getColor() {
            return color;
        }

        public List<String> getTerritories() {
            return Collections.unmodifiableList(territories);
        }
    }
}
