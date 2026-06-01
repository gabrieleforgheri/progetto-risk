package client.map;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Collega i nomi territorio di {@link map.MapObjects} agli {@code id} dei path nel file
 * {@code risk-map.svg} (snake_case, es. {@code eastern_united_states}).
 */
public final class TerritorySvgMapper {

    private static final Map<String, String> GAME_TO_SVG = new HashMap<>();
    private static final Map<String, String> SVG_TO_GAME = new HashMap<>();

    static {
        for (String gameName : MapObjectsTerritoryNames.ALL) {
            GAME_TO_SVG.put(gameName, defaultSvgId(gameName));
        }
        // Eccezioni rispetto alla regola "spazi → underscore"
        GAME_TO_SVG.put("Northwest Territories", "northwest_territory");
        GAME_TO_SVG.put("Yakutsk", "yakursk"); // id nel file SVG (refuso inkscape)

        for (Map.Entry<String, String> entry : GAME_TO_SVG.entrySet()) {
            SVG_TO_GAME.put(entry.getValue(), entry.getKey());
        }
    }

    private TerritorySvgMapper() {
    }

    public static String toSvgId(String gameTerritoryName) {
        return GAME_TO_SVG.getOrDefault(gameTerritoryName, defaultSvgId(gameTerritoryName));
    }

    public static String toGameName(String svgElementId) {
        return SVG_TO_GAME.get(svgElementId);
    }

    private static String defaultSvgId(String gameTerritoryName) {
        return gameTerritoryName.trim()
                .toLowerCase(Locale.ROOT)
                .replace(' ', '_');
    }

    /** Elenco nomi dalla lista piatta di MapObjects (evita dipendenza circolare in build). */
    static final class MapObjectsTerritoryNames {
        static final String[] ALL = {
                "Afghanistan", "Alaska", "Alberta", "Argentina", "Brazil", "Central America",
                "China", "Congo", "East Africa", "Eastern Australia", "Eastern United States",
                "Egypt", "Great Britain", "Greenland", "Iceland", "India", "Indonesia", "Japan",
                "Kamchatka", "Madagascar", "Middle East", "Mongolia", "New Guinea", "North Africa",
                "Northern Europe", "Northwest Territories", "Ontario", "Peru", "Quebec",
                "Scandinavia", "Siam", "Siberia", "South Africa", "Southern Europe", "Ukraine",
                "Ural", "Venezuela", "Western Australia", "Western Europe", "Western United States",
                "Yakutsk"
        };

        private MapObjectsTerritoryNames() {
        }
    }
}
