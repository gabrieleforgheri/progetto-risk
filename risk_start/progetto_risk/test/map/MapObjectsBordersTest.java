package map;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Garantisce che i confini territorio restino simmetrici, senza duplicati e allineati tra lista e mappa canonica.
 */
class MapObjectsBordersTest {

    private static final int EXPECTED_TERRITORY_COUNT = 41;

    @Test
    void territoriesHaveUniqueNames() {
        Set<String> names = new HashSet<>();
        for (MapObjects.Territory territory : MapObjects.territories) {
            assertTrue(names.add(territory.getName()),
                    "Nome territorio duplicato: " + territory.getName());
        }
        assertEquals(EXPECTED_TERRITORY_COUNT, names.size());
    }

    @Test
    void canonicalBorderGraphIsSymmetricAndComplete() {
        Map<String, List<String>> borders = MapObjects.getAllBorders();
        assertEquals(EXPECTED_TERRITORY_COUNT, borders.size(),
                "La mappa canonica deve contenere tutti i territori");

        for (Map.Entry<String, List<String>> entry : borders.entrySet()) {
            String territory = entry.getKey();
            List<String> neighbors = entry.getValue();

            assertFalse(neighbors.isEmpty(), territory + " deve avere almeno un confine");
            assertEquals(neighbors.size(), new HashSet<>(neighbors).size(),
                    territory + " ha confini duplicati nella lista");

            for (String neighbor : neighbors) {
                assertTrue(borders.containsKey(neighbor),
                        territory + " confina con territorio sconosciuto: " + neighbor);
                assertTrue(borders.get(neighbor).contains(territory),
                        "Confine asimmetrico: " + territory + " -> " + neighbor
                                + " ma non " + neighbor + " -> " + territory);
            }
        }
    }

    @Test
    void territoryListUsesCanonicalBorders() {
        Map<String, List<String>> canonical = MapObjects.getAllBorders();

        for (MapObjects.Territory territory : MapObjects.territories) {
            assertEquals(canonical.get(territory.getName()), territory.getBorders(),
                    "Confini non allineati per " + territory.getName());
            assertEquals(MapObjects.getBordersFor(territory.getName()), territory.getBorders(),
                    "getBordersFor non coerente per " + territory.getName());
        }

        Set<String> fromList = new HashSet<>();
        for (MapObjects.Territory territory : MapObjects.territories) {
            fromList.add(territory.getName());
        }
        assertEquals(canonical.keySet(), fromList,
                "Lista territori e mappa canonica devono avere le stesse chiavi");
    }

    @Test
    void continentTerritoriesUseCanonicalBorders() {
        for (MapObjects.Continent continent : MapObjects.continents) {
            for (MapObjects.Territory territory : continent.territories) {
                assertEquals(MapObjects.getBordersFor(territory.getName()), territory.getBorders(),
                        "Continente " + continent.name + ": confini errati per " + territory.getName());
            }
        }
    }
}
