package map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapObjects {

    /** Confini ufficiali Risk classico (42 territori), grafo simmetrico. */
    private static final Map<String, List<String>> BORDERS = buildCanonicalBorders();

    public static class Territory {
        String name;
        int armies;
        List<String> borders;
        String owner;
        String color;

        public Territory(String name, int armies, List<String> borders, String owner, String color) {
            this.name = name;
            this.armies = armies;
            this.borders = borders;
            this.owner = owner;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public int getArmies() {
            return armies;
        }

        public List<String> getBorders() {
            return borders;
        }

        public String getOwner() {
            return owner;
        }

        public String getColor() {
            return color;
        }

        public void setArmies(int armies) {
            this.armies = armies;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    public static class Continent {
        String name;
        int bonusArmies;
        List<Territory> territories;

        public Continent(String name, int bonusArmies, List<Territory> territories) {
            this.name = name;
            this.bonusArmies = bonusArmies;
            this.territories = territories;
        }
    }

    public static List<String> getBordersFor(String territoryName) {
        return BORDERS.getOrDefault(territoryName, List.of());
    }

    public static Map<String, List<String>> getAllBorders() {
        return BORDERS;
    }

    private static Territory territory(String name) {
        return new Territory(name, 0, getBordersFor(name), "nickNameX", "#000000");
    }

    public static List<Territory> territories = List.of(
            territory("Alaska"),
            territory("Alberta"),
            territory("Argentina"),
            territory("Afghanistan"),
            territory("Brazil"),
            territory("Central America"),
            territory("China"),
            territory("Congo"),
            territory("Eastern Australia"),
            territory("Eastern United States"),
            territory("Egypt"),
            territory("Great Britain"),
            territory("Greenland"),
            territory("Iceland"),
            territory("India"),
            territory("Indonesia"),
            territory("Japan"),
            territory("Kamchatka"),
            territory("Madagascar"),
            territory("Middle East"),
            territory("Mongolia"),
            territory("New Guinea"),
            territory("North Africa"),
            territory("Northern Europe"),
            territory("Northwest Territories"),
            territory("Ontario"),
            territory("Peru"),
            territory("Quebec"),
            territory("Scandinavia"),
            territory("Siam"),
            territory("Siberia"),
            territory("Southern Europe"),
            territory("South Africa"),
            territory("Ukraine"),
            territory("Ural"),
            territory("Venezuela"),
            territory("Western Australia"),
            territory("Western Europe"),
            territory("Western United States"),
            territory("Yakutsk"),
            territory("East Africa")
    );

    public static List<Continent> continents = Arrays.asList(
            new Continent("Oceania", 2, Arrays.asList(
                    territory("Indonesia"),
                    territory("New Guinea"),
                    territory("Western Australia"),
                    territory("Eastern Australia")
            )),
            new Continent("South America", 2, Arrays.asList(
                    territory("Venezuela"),
                    territory("Brazil"),
                    territory("Peru"),
                    territory("Argentina")
            )),
            new Continent("Europe", 3, Arrays.asList(
                    territory("Iceland"),
                    territory("Scandinavia"),
                    territory("Great Britain"),
                    territory("Northern Europe"),
                    territory("Western Europe"),
                    territory("Southern Europe"),
                    territory("Ukraine")
            )),
            new Continent("Africa", 3, Arrays.asList(
                    territory("North Africa"),
                    territory("Egypt"),
                    territory("East Africa"),
                    territory("Congo"),
                    territory("Madagascar"),
                    territory("South Africa")
            )),
            new Continent("North America", 3, Arrays.asList(
                    territory("Greenland"),
                    territory("Quebec"),
                    territory("Ontario"),
                    territory("Western United States"),
                    territory("Central America"),
                    territory("Alberta"),
                    territory("Northwest Territories"),
                    territory("Alaska"),
                    territory("Eastern United States")
            )),
            new Continent("Asia", 3, Arrays.asList(
                    territory("Kamchatka"),
                    territory("Yakutsk"),
                    territory("Siberia"),
                    territory("Ural"),
                    territory("Afghanistan"),
                    territory("Middle East"),
                    territory("India"),
                    territory("Siam"),
                    territory("Japan"),
                    territory("China"),
                    territory("Mongolia")
            ))
    );

    public static class Card {
        String name;
        String value;

        public Card(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }

    public static List<Card> cards = Arrays.asList(
            new Card("China", "Infantry"),
            new Card("Greenland", "Cavalry"),
            new Card("Quebec", "Artillery"),
            new Card("Ontario", "Infantry"),
            new Card("Western United States", "Cavalry"),
            new Card("Central America", "Artillery"),
            new Card("Alberta", "Infantry"),
            new Card("Northwest Territories", "Cavalry"),
            new Card("Alaska", "Artillery"),
            new Card("Eastern United States", "Infantry"),
            new Card("Venezuela", "Cavalry"),
            new Card("Brazil", "Artillery"),
            new Card("Peru", "Infantry"),
            new Card("Argentina", "Cavalry"),
            new Card("Iceland", "Artillery"),
            new Card("Scandinavia", "Infantry"),
            new Card("Great Britain", "Cavalry"),
            new Card("Northern Europe", "Artillery"),
            new Card("Western Europe", "Infantry"),
            new Card("Southern Europe", "Cavalry"),
            new Card("Ukraine", "Artillery"),
            new Card("North Africa", "Infantry"),
            new Card("Egypt", "Cavalry"),
            new Card("East Africa", "Artillery"),
            new Card("Congo", "Infantry"),
            new Card("Madagascar", "Cavalry"),
            new Card("South Africa", "Artillery"),
            new Card("Kamchatka", "Infantry"),
            new Card("Yakutsk", "Cavalry"),
            new Card("Siberia", "Artillery"),
            new Card("Ural", "Infantry"),
            new Card("Afghanistan", "Cavalry"),
            new Card("Middle East", "Artillery"),
            new Card("India", "Infantry"),
            new Card("Siam", "Cavalry"),
            new Card("Japan", "Artillery"),
            new Card("Mongolia", "Cavalry"),
            new Card("Indonesia", "Artillery"),
            new Card("New Guinea", "Infantry"),
            new Card("Western Australia", "Cavalry"),
            new Card("Eastern Australia", "Artillery")
    );

    private static Map<String, List<String>> buildCanonicalBorders() {
        Map<String, List<String>> raw = new LinkedHashMap<>();
        link(raw, "Alaska", "Alberta", "Kamchatka", "Northwest Territories");
        link(raw, "Alberta", "Alaska", "Northwest Territories", "Ontario", "Western United States");
        link(raw, "Argentina", "Brazil", "Peru");
        link(raw, "Afghanistan", "China", "India", "Middle East", "Ural", "Ukraine");
        link(raw, "Brazil", "Argentina", "North Africa", "Peru", "Venezuela");
        link(raw, "Central America", "Eastern United States", "Venezuela", "Western United States");
        link(raw, "China", "Afghanistan", "India", "Middle East", "Mongolia", "Siberia", "Siam", "Ural");
        link(raw, "Congo", "East Africa", "North Africa", "South Africa");
        link(raw, "Eastern Australia", "New Guinea", "Western Australia");
        link(raw, "Eastern United States", "Central America", "Ontario", "Quebec", "Western United States");
        link(raw, "Egypt", "East Africa", "Middle East", "North Africa", "Southern Europe");
        link(raw, "Great Britain", "Iceland", "Northern Europe", "Scandinavia", "Western Europe");
        link(raw, "Greenland", "Iceland", "Northwest Territories", "Ontario", "Quebec");
        link(raw, "Iceland", "Great Britain", "Greenland", "Scandinavia");
        link(raw, "India", "Afghanistan", "China", "Middle East", "Siam");
        link(raw, "Indonesia", "New Guinea", "Siam", "Western Australia");
        link(raw, "Japan", "Kamchatka", "Mongolia");
        link(raw, "Kamchatka", "Alaska", "Japan", "Mongolia", "Siberia", "Yakutsk");
        link(raw, "Madagascar", "East Africa", "South Africa");
        link(raw, "Middle East", "Afghanistan", "East Africa", "Egypt", "India", "Southern Europe", "Ukraine");
        link(raw, "Mongolia", "China", "Japan", "Kamchatka", "Siberia");
        link(raw, "New Guinea", "Eastern Australia", "Indonesia", "Western Australia");
        link(raw, "North Africa", "Brazil", "Congo", "East Africa", "Egypt", "Southern Europe", "Western Europe");
        link(raw, "Northern Europe", "Great Britain", "Scandinavia", "Southern Europe", "Ukraine", "Western Europe");
        link(raw, "Northwest Territories", "Alaska", "Alberta", "Greenland", "Ontario");
        link(raw, "Ontario", "Alberta", "Eastern United States", "Greenland", "Northwest Territories", "Quebec", "Western United States");
        link(raw, "Peru", "Argentina", "Brazil", "Venezuela");
        link(raw, "Quebec", "Eastern United States", "Greenland", "Ontario");
        link(raw, "Scandinavia", "Great Britain", "Iceland", "Northern Europe", "Ukraine");
        link(raw, "Siam", "China", "India", "Indonesia");
        link(raw, "Siberia", "China", "Kamchatka", "Mongolia", "Ural", "Yakutsk");
        link(raw, "Southern Europe", "Egypt", "Middle East", "North Africa", "Northern Europe", "Ukraine", "Western Europe");
        link(raw, "South Africa", "Congo", "East Africa", "Madagascar");
        link(raw, "Ukraine", "Afghanistan", "Middle East", "Northern Europe", "Scandinavia", "Southern Europe", "Ural");
        link(raw, "Ural", "Afghanistan", "China", "Siberia", "Ukraine");
        link(raw, "Venezuela", "Brazil", "Central America", "Peru");
        link(raw, "Western Australia", "Eastern Australia", "Indonesia", "New Guinea");
        link(raw, "Western Europe", "Great Britain", "North Africa", "Northern Europe", "Southern Europe");
        link(raw, "Western United States", "Alberta", "Central America", "Eastern United States", "Ontario");
        link(raw, "Yakutsk", "Kamchatka", "Siberia");
        link(raw, "East Africa", "Congo", "Egypt", "Madagascar", "Middle East", "North Africa", "South Africa");

        Map<String, List<String>> frozen = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> entry : raw.entrySet()) {
            List<String> neighbors = new ArrayList<>(entry.getValue());
            Collections.sort(neighbors);
            frozen.put(entry.getKey(), Collections.unmodifiableList(neighbors));
        }
        return Collections.unmodifiableMap(frozen);
    }

    private static void link(Map<String, List<String>> graph, String territory, String... neighbors) {
        List<String> list = graph.computeIfAbsent(territory, key -> new ArrayList<>());
        for (String neighbor : neighbors) {
            if (!list.contains(neighbor)) {
                list.add(neighbor);
            }
            List<String> reverse = graph.computeIfAbsent(neighbor, key -> new ArrayList<>());
            if (!reverse.contains(territory)) {
                reverse.add(territory);
            }
        }
    }
}
