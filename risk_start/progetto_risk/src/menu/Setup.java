package menu;

import map.MapObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Builds the initial Risk match state after the lobby has enough players.
public class Setup {
    // Official project lobby limits: game cannot start below 3 players or above 6.
    public static final int MIN_PLAYERS = 3;
    public static final int MAX_PLAYERS = 6;

    // Temporary fixed colors assigned in join order until a UI color picker exists.
    private static final String[] PLAYER_COLORS = {
            "#ff0000",
            "#0000ff",
            "#00aa00",
            "#ffff00",
            "#ff8800",
            "#aa00ff"
    };

    private Setup() {
    }

    // Creates the first game snapshot: player colors, initial armies, and territory ownership.
    public static GameSetup createGame(List<String> playerNickNames) {
        if (playerNickNames.size() < MIN_PLAYERS || playerNickNames.size() > MAX_PLAYERS) {
            throw new IllegalArgumentException("Risk needs between 3 and 6 players.");
        }

        int initialArmies = getInitialArmies(playerNickNames.size());
        Map<String, PlayerSetup> players = new LinkedHashMap<>();

        // LinkedHashMap preserves the lobby join order, which also defines the first turn.
        for (int i = 0; i < playerNickNames.size(); i++) {
            String nickName = playerNickNames.get(i);
            players.put(nickName, new PlayerSetup(nickName, initialArmies, PLAYER_COLORS[i]));
        }

        // Shuffle once so territory distribution changes from match to match.
        List<MapObjects.Territory> territories = new ArrayList<>(MapObjects.territories);
        Collections.shuffle(territories);

        // Deal territories round-robin and place one army on each assigned territory.
        for (int i = 0; i < territories.size(); i++) {
            String owner = playerNickNames.get(i % playerNickNames.size());
            PlayerSetup player = players.get(owner);
            MapObjects.Territory territory = territories.get(i);

            territory.setOwner(owner);
            territory.setColor(player.getColor());
            territory.setArmies(1);
            player.addTerritory(territory.getName());
        }

        return new GameSetup(initialArmies, new ArrayList<>(players.values()));
    }

    // Risk starting armies by player count.
    public static int getInitialArmies(int playerCount) {
        switch (playerCount) {
            case 3:
                return 35;
            case 4:
                return 30;
            case 5:
                return 25;
            case 6:
                return 20;
            default:
                throw new IllegalArgumentException("Risk needs between 3 and 6 players.");
        }
    }

    // Immutable-style setup result sent to all clients when the game starts.
    public static class GameSetup {
        private final int initialArmies;
        private final List<PlayerSetup> players;

        public GameSetup(int initialArmies, List<PlayerSetup> players) {
            this.initialArmies = initialArmies;
            this.players = players;
        }

        public int getInitialArmies() {
            return initialArmies;
        }

        public List<PlayerSetup> getPlayers() {
            return Collections.unmodifiableList(players);
        }

        // Converts setup state to the string payload format used by GameMessage.
        public Map<String, String> toMessageData() {
            Map<String, String> data = new LinkedHashMap<>();
            data.put("initialArmies", String.valueOf(initialArmies));
            data.put("players", String.join(",", getPlayerNickNames()));

            java.util.Set<String> allTerritories = new java.util.LinkedHashSet<>();
            for (PlayerSetup player : players) {
                String prefix = "player." + player.getNickName() + ".";
                data.put(prefix + "armies", String.valueOf(player.getInitialArmies()));
                data.put(prefix + "remainingArmies", String.valueOf(player.getRemainingArmies()));
                data.put(prefix + "color", player.getColor());
                data.put(prefix + "territories", String.join(",", player.getTerritories()));

                for (String territoryName : player.getTerritories()) {
                    allTerritories.add(territoryName);
                    String territoryPrefix = "territory." + territoryName + ".";
                    data.put(territoryPrefix + "owner", player.getNickName());
                    data.put(territoryPrefix + "armies", "1");
                    data.put(territoryPrefix + "color", player.getColor());
                }
            }
            data.put("territories", String.join(",", allTerritories));
            return data;
        }

        // Keeps player order stable in serialized messages.
        private List<String> getPlayerNickNames() {
            List<String> nickNames = new ArrayList<>();
            for (PlayerSetup player : players) {
                nickNames.add(player.getNickName());
            }
            return nickNames;
        }
    }

    // Setup data for one player: color, assigned territories, and armies available to place.
    public static class PlayerSetup {
        private final String nickName;
        private final int initialArmies;
        private final String color;
        private final List<String> territories;

        public PlayerSetup(String nickName, int initialArmies, String color) {
            this.nickName = nickName;
            this.initialArmies = initialArmies;
            this.color = color;
            this.territories = new ArrayList<>();
        }

        public String getNickName() {
            return nickName;
        }

        public int getInitialArmies() {
            return initialArmies;
        }

        public int getRemainingArmies() {
            // One army is already placed on every starting territory.
            return initialArmies - territories.size();
        }

        public String getColor() {
            return color;
        }

        public List<String> getTerritories() {
            return Collections.unmodifiableList(territories);
        }

        private void addTerritory(String territoryName) {
            territories.add(territoryName);
        }
    }
}
