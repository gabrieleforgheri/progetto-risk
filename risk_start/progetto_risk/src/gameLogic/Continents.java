package gameLogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Territori per continente (allineato alla mappa Risk del progetto). */
public final class Continents {
    public static final String OCEANIA = "Oceania";
    public static final String SOUTH_AMERICA = "South America";
    public static final String EUROPE = "Europe";
    public static final String AFRICA = "Africa";
    public static final String NORTH_AMERICA = "North America";
    public static final String ASIA = "Asia";

    public static final List<String> ALL = List.of(
            OCEANIA, SOUTH_AMERICA, EUROPE, AFRICA, NORTH_AMERICA, ASIA
    );

    private static final Map<String, List<String>> TERRITORIES_BY_CONTINENT = buildTerritoriesByContinent();

    private Continents() {
    }

    public static String findContinentName(String territoryName) {
        for (Map.Entry<String, List<String>> entry : TERRITORIES_BY_CONTINENT.entrySet()) {
            if (entry.getValue().contains(territoryName)) {
                return entry.getKey();
            }
        }
        return ASIA;
    }

    public static List<String> getTerritories(String continentName) {
        return TERRITORIES_BY_CONTINENT.getOrDefault(continentName, List.of());
    }

    public static boolean playerControlsContinent(ObjectiveCard.PlayerSnapshot player, String continentName) {
        List<String> required = getTerritories(continentName);
        return !required.isEmpty() && player.getTerritories().containsAll(required);
    }

    public static boolean playerControlsExtraContinent(ObjectiveCard.PlayerSnapshot player,
                                                       Set<String> excludedContinents) {
        for (String continent : ALL) {
            if (excludedContinents.contains(continent)) {
                continue;
            }
            if (playerControlsContinent(player, continent)) {
                return true;
            }
        }
        return false;
    }

    private static Map<String, List<String>> buildTerritoriesByContinent() {
        Map<String, List<String>> map = new LinkedHashMap<>();
        map.put(OCEANIA, List.of(
                "Indonesia", "New Guinea", "Western Australia", "Eastern Australia"));
        map.put(SOUTH_AMERICA, List.of(
                "Venezuela", "Brazil", "Peru", "Argentina"));
        map.put(EUROPE, List.of(
                "Iceland", "Scandinavia", "Great Britain", "Northern Europe",
                "Western Europe", "Southern Europe", "Ukraine"));
        map.put(AFRICA, List.of(
                "North Africa", "Egypt", "East Africa", "Congo", "Madagascar", "South Africa"));
        map.put(NORTH_AMERICA, List.of(
                "Greenland", "Quebec", "Ontario", "Western United States", "Central America",
                "Alberta", "Northwest Territories", "Alaska", "Eastern United States"));
        map.put(ASIA, List.of(
                "Kamchatka", "Yakutsk", "Siberia", "Ural", "Afghanistan", "Middle East",
                "India", "Siam", "Japan", "China", "Mongolia"));
        return map;
    }

    public static List<String> allTerritoryNames() {
        List<String> names = new ArrayList<>();
        for (List<String> territories : TERRITORIES_BY_CONTINENT.values()) {
            names.addAll(territories);
        }
        return Collections.unmodifiableList(names);
    }
}
