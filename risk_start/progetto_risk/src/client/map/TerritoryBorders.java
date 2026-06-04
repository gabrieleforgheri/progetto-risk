package client.map;

import map.MapObjects;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Confini territorio per validazione client-side (attacco / spostamento).
 */
public final class TerritoryBorders {

    private static final Map<String, List<String>> BORDERS = buildBorders();

    private TerritoryBorders() {
    }

    public static boolean areAdjacent(String fromTerritory, String toTerritory) {
        List<String> borders = BORDERS.get(fromTerritory);
        return borders != null && borders.contains(toTerritory);
    }

    private static Map<String, List<String>> buildBorders() {
        Map<String, List<String>> result = new LinkedHashMap<>();
        for (MapObjects.Territory territory : MapObjects.territories) {
            result.put(territory.getName(), List.copyOf(territory.getBorders()));
        }
        return Collections.unmodifiableMap(result);
    }
}
