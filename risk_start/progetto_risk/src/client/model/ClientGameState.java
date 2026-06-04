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
    private String stage = "";
    private String winner = "";
    private String winnerObjective = "";
    private String myObjectiveId = "";
    private String myObjectiveDescription = "";
    private final List<String> lobbyPlayers = new ArrayList<>();
    private final Map<String, String> lobbyPlayerColors = new LinkedHashMap<>();
    private final List<ChatLine> lobbyChat = new ArrayList<>();
    private final List<String> events = new ArrayList<>();
    private final Map<String, PlayerState> players = new LinkedHashMap<>();
    private final Map<String, TerritoryState> territories = new LinkedHashMap<>();

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

    public String getStage() {
        return stage;
    }

    public String getWinner() {
        return winner;
    }

    public String getWinnerObjective() {
        return winnerObjective;
    }

    public String getMyObjectiveId() {
        return myObjectiveId;
    }

    public String getMyObjectiveDescription() {
        return myObjectiveDescription;
    }

    public void setMyObjective(String objectiveId, String description) {
        myObjectiveId = objectiveId == null ? "" : objectiveId;
        myObjectiveDescription = description == null ? "" : description;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer == null ? "" : currentPlayer;
    }

    public List<String> getLobbyPlayers() {
        return Collections.unmodifiableList(lobbyPlayers);
    }

    public Map<String, String> getLobbyPlayerColors() {
        return Collections.unmodifiableMap(lobbyPlayerColors);
    }

    public String getLobbyPlayerColor(String nickName) {
        return lobbyPlayerColors.getOrDefault(nickName, "");
    }

    public List<String> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public List<ChatLine> getLobbyChat() {
        return Collections.unmodifiableList(lobbyChat);
    }

    public void addLobbyChat(String sender, String text) {
        boolean serverMessage = "server".equalsIgnoreCase(sender);
        lobbyChat.add(new ChatLine(sender, text, serverMessage));
        if (lobbyChat.size() > 200) {
            lobbyChat.remove(0);
        }
    }

    public void clearLobbyChat() {
        lobbyChat.clear();
    }

    public Map<String, PlayerState> getPlayers() {
        return Collections.unmodifiableMap(players);
    }

    public Map<String, TerritoryState> getTerritories() {
        return Collections.unmodifiableMap(territories);
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
        lobbyPlayerColors.clear();
        for (String nickName : lobbyPlayers) {
            String color = valueOrEmpty(data.get("player." + nickName + ".color"));
            if (!color.isEmpty()) {
                lobbyPlayerColors.put(nickName, color);
            }
        }
    }

    public void updateStartedGame(Map<String, String> data) {
        updateFromGameState(data);
    }

    /** Legge snapshot server: giocatori + territori ({@code territory.<nome>.owner/armies/color}). */
    public void updateFromGameState(Map<String, String> data) {
        players.clear();
        territories.clear();
        currentPlayer = valueOrEmpty(data.get("currentPlayer"));
        stage = valueOrEmpty(data.get("stage"));
        winner = valueOrEmpty(data.get("winner"));
        winnerObjective = valueOrEmpty(data.get("winnerObjective"));

        for (String nickName : splitList(data.get("players"))) {
            String prefix = "player." + nickName + ".";
            int armies = parseInt(data.get(prefix + "armies"));
            int remainingArmies = parseInt(data.get(prefix + "remainingArmies"));
            String color = valueOrEmpty(data.get(prefix + "color"));
            List<String> ownedTerritories = splitList(data.get(prefix + "territories"));
            players.put(nickName, new PlayerState(nickName, armies, remainingArmies, color, ownedTerritories));
        }

        for (String territoryName : splitList(data.get("territories"))) {
            String prefix = "territory." + territoryName + ".";
            String owner = valueOrEmpty(data.get(prefix + "owner"));
            int armies = parseInt(data.get(prefix + "armies"));
            String color = valueOrEmpty(data.get(prefix + "color"));
            territories.put(territoryName, new TerritoryState(territoryName, owner, armies, color));
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

    public record ChatLine(String sender, String text, boolean serverMessage) {
    }

    public static class TerritoryState {
        private final String name;
        private final String owner;
        private final int armies;
        private final String color;

        public TerritoryState(String name, String owner, int armies, String color) {
            this.name = name;
            this.owner = owner;
            this.armies = armies;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public String getOwner() {
            return owner;
        }

        public int getArmies() {
            return armies;
        }

        public String getColor() {
            return color;
        }
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
