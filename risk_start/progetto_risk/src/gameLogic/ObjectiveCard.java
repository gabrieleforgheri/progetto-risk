package gameLogic;

import menu.Setup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Carte obiettivo segrete (14 tipi). Le missioni "Elimina il giocatore …" entrano nel mazzo
 * solo se quel colore è stato scelto in lobby; se tocchi il tuo colore diventa "24 territori".
 */
public final class ObjectiveCard {
    public static final int TERRITORIES_WITH_TWO_ARMIES = 18;
    public static final int TERRITORIES_UNRESTRICTED = 24;

    private final String id;
    private final Type type;
    private final String description;
    private final String targetColor;

    private ObjectiveCard(String id, Type type, String description, String targetColor) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.targetColor = targetColor == null ? "" : targetColor;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isFulfilled(PlayerSnapshot player, Map<String, PlayerSnapshot> allPlayers) {
        return switch (type) {
            case ASIA_SOUTH_AMERICA -> controlsPair(player, Continents.ASIA, Continents.SOUTH_AMERICA);
            case AFRICA_NORTH_AMERICA -> controlsPair(player, Continents.AFRICA, Continents.NORTH_AMERICA);
            case EUROPE_SOUTH_AMERICA_PLUS_ONE -> controlsPair(player, Continents.EUROPE, Continents.SOUTH_AMERICA)
                    && Continents.playerControlsExtraContinent(player,
                    Set.of(Continents.EUROPE, Continents.SOUTH_AMERICA));
            case ASIA_AFRICA -> controlsPair(player, Continents.ASIA, Continents.AFRICA);
            case EUROPE_OCEANIA_PLUS_ONE -> controlsPair(player, Continents.EUROPE, Continents.OCEANIA)
                    && Continents.playerControlsExtraContinent(player,
                    Set.of(Continents.EUROPE, Continents.OCEANIA));
            case NORTH_AMERICA_OCEANIA -> controlsPair(player, Continents.NORTH_AMERICA, Continents.OCEANIA);
            case EIGHTEEN_TERRITORIES_TWO_ARMIES -> countTerritoriesWithMinArmies(player, TERRITORIES_WITH_TWO_ARMIES) >= 18;
            case TWENTY_FOUR_TERRITORIES -> player.getTerritories().size() >= TERRITORIES_UNRESTRICTED;
            case ELIMINATE_COLOR -> isEliminateTargetDefeated(targetColor, allPlayers);
        };
    }

    public static Map<String, ObjectiveCard> dealToPlayers(List<String> turnOrder,
                                                         Map<String, String> playerColors) {
        Set<String> usedColors = new LinkedHashSet<>(playerColors.values());
        List<ObjectiveCard> deck = buildDeck(usedColors);
        Collections.shuffle(deck, new Random());

        Map<String, ObjectiveCard> dealt = new LinkedHashMap<>();
        int index = 0;
        for (String nickName : turnOrder) {
            if (index >= deck.size()) {
                break;
            }
            ObjectiveCard card = deck.get(index++);
            String playerColor = playerColors.getOrDefault(nickName, "");
            if (card.type == Type.ELIMINATE_COLOR && card.targetColor.equalsIgnoreCase(playerColor)) {
                card = substituteTwentyFourTerritories(card.id, playerColor);
            }
            dealt.put(nickName, card);
        }
        return dealt;
    }

    private static boolean controlsPair(PlayerSnapshot player, String first, String second) {
        return Continents.playerControlsContinent(player, first)
                && Continents.playerControlsContinent(player, second);
    }

    private static int countTerritoriesWithMinArmies(PlayerSnapshot player, int minArmies) {
        int count = 0;
        for (String territory : player.getTerritories()) {
            if (player.getArmiesOnTerritory(territory) >= minArmies) {
                count++;
            }
        }
        return count;
    }

    private static boolean isEliminateTargetDefeated(String color, Map<String, PlayerSnapshot> allPlayers) {
        if (color == null || color.isEmpty()) {
            return false;
        }
        for (PlayerSnapshot snapshot : allPlayers.values()) {
            if (color.equalsIgnoreCase(snapshot.getColor())) {
                return snapshot.getTerritories().isEmpty();
            }
        }
        return false;
    }

    private static List<ObjectiveCard> buildDeck(Set<String> usedColors) {
        List<ObjectiveCard> deck = new ArrayList<>();
        deck.add(new ObjectiveCard("obj_01", Type.ASIA_SOUTH_AMERICA,
                "Conquista Asia e Sud America", ""));
        deck.add(new ObjectiveCard("obj_02", Type.AFRICA_NORTH_AMERICA,
                "Conquista Africa e America del Nord", ""));
        deck.add(new ObjectiveCard("obj_03", Type.EUROPE_SOUTH_AMERICA_PLUS_ONE,
                "Conquista Europa e America del Sud, e un continente a tua scelta", ""));
        deck.add(new ObjectiveCard("obj_10", Type.ASIA_AFRICA,
                "Conquista Asia e Africa", ""));
        deck.add(new ObjectiveCard("obj_11", Type.EUROPE_OCEANIA_PLUS_ONE,
                "Conquista Europa e Oceania, e un continente a tua scelta", ""));
        deck.add(new ObjectiveCard("obj_12", Type.NORTH_AMERICA_OCEANIA,
                "Conquista America del Nord e Oceania", ""));
        deck.add(new ObjectiveCard("obj_13", Type.EIGHTEEN_TERRITORIES_TWO_ARMIES,
                "Conquista 18 territori con almeno 2 armate su ciascuno", ""));
        deck.add(new ObjectiveCard("obj_14", Type.TWENTY_FOUR_TERRITORIES,
                "Conquista 24 territori", ""));

        for (int colorIndex = 0; colorIndex < Setup.LOBBY_COLORS.length; colorIndex++) {
            String color = Setup.LOBBY_COLORS[colorIndex];
            if (!usedColors.contains(color)) {
                continue;
            }
            String colorName = Setup.displayNameForColor(color);
            deck.add(new ObjectiveCard(
                    "obj_elim_" + colorName.toLowerCase(),
                    Type.ELIMINATE_COLOR,
                    "Elimina il giocatore " + colorName,
                    color));
        }
        return deck;
    }

    private static ObjectiveCard substituteTwentyFourTerritories(String originalId, String playerColor) {
        String colorName = Setup.displayNameForColor(playerColor);
        return new ObjectiveCard(
                originalId + "_sub24",
                Type.TWENTY_FOUR_TERRITORIES,
                "Conquista 24 territori a tua scelta (al posto di eliminare "
                        + (colorName.isEmpty() ? "il tuo colore" : colorName) + ")",
                "");
    }

    public static final class PlayerSnapshot {
        private final String nickName;
        private final String color;
        private final List<String> territories;
        private final Map<String, Integer> armiesByTerritory;

        public PlayerSnapshot(String nickName, String color, List<String> territories,
                              Map<String, Integer> armiesByTerritory) {
            this.nickName = nickName;
            this.color = color;
            this.territories = List.copyOf(territories);
            this.armiesByTerritory = Map.copyOf(armiesByTerritory);
        }

        public String getNickName() {
            return nickName;
        }

        public String getColor() {
            return color;
        }

        public List<String> getTerritories() {
            return territories;
        }

        public int getArmiesOnTerritory(String territoryName) {
            return armiesByTerritory.getOrDefault(territoryName, 0);
        }
    }

    private enum Type {
        ASIA_SOUTH_AMERICA,
        AFRICA_NORTH_AMERICA,
        EUROPE_SOUTH_AMERICA_PLUS_ONE,
        ELIMINATE_COLOR,
        ASIA_AFRICA,
        EUROPE_OCEANIA_PLUS_ONE,
        NORTH_AMERICA_OCEANIA,
        EIGHTEEN_TERRITORIES_TWO_ARMIES,
        TWENTY_FOUR_TERRITORIES
    }
}
