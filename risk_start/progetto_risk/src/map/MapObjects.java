package map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapObjects {

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

    public static List<Continent> continents = Arrays.asList(
        new Continent("Oceania", 2, Arrays.asList(
            new Territory("Indonesia", 0, Arrays.asList("Siam", "New Guinea", "Western Australia"), "nickNameX", "#000000"),
            new Territory("New Guinea", 0, Arrays.asList("Eastern Australia", "Indonesia", "Western Australia"), "nickNameX", "#000000"),
            new Territory("Western Australia", 0, Arrays.asList("Eastern Australia", "Indonesia", "New Guinea"), "nickNameX", "#000000"),
            new Territory("Eastern Australia", 0, Arrays.asList("Western Australia", "New Guinea"), "nickNameX", "#000000")
        )),
        new Continent("South America", 2, Arrays.asList(
            new Territory("Venezuela", 0, Arrays.asList("Central America", "Brazil", "Peru"), "nickNameX", "#000000"),
            new Territory("Brazil", 0, Arrays.asList("North Africa", "Peru", "Venezuela"), "nickNameX", "#000000"),
            new Territory("Peru", 0, Arrays.asList("Venezuela", "Brazil", "Argentina"), "nickNameX", "#000000"),
            new Territory("Argentina", 0, Arrays.asList("Brazil", "Peru"), "nickNameX", "#000000")
        )),
        new Continent("Europe", 3, Arrays.asList(
            new Territory("Iceland", 0, Arrays.asList("Greenland", "Scandinavia", "Great Britain"), "nickNameX", "#000000"),
            new Territory("Scandinavia", 0, Arrays.asList("Iceland", "Great Britain", "Northern Europe", "Ukraine"), "nickNameX", "#000000"),
            new Territory("Great Britain", 0, Arrays.asList("Iceland", "Scandinavia", "Northern Europe", "Western Europe"), "nickNameX", "#000000"),
            new Territory("Northern Europe", 0, Arrays.asList("Scandinavia", "Great Britain", "Western Europe", "Ukraine", "Southern Europe"), "nickNameX", "#000000"),
            new Territory("Western Europe", 0, Arrays.asList("North Africa", "Great Britain", "Northern Europe", "Southern Europe"), "nickNameX", "#000000"),
            new Territory("Southern Europe", 0, Arrays.asList("Western Europe", "North Africa", "Northern Europe", "Ukraine", "Egypt", "Middle East"), "nickNameX", "#000000"),
            new Territory("Ukraine", 0, Arrays.asList("Northern Europe", "Ural", "Southern Europe", "Scandinavia", "Middle East", "Afghanistan"), "nickNameX", "#000000")
        )),
        new Continent("Africa", 3, Arrays.asList(
            new Territory("North Africa", 0, Arrays.asList("Western Europe", "Egypt", "Congo", "Southern Europe", "East Africa"), "nickNameX", "#000000"),
            new Territory("Egypt", 0, Arrays.asList("Southern Europe", "North Africa", "East Africa", "Middle East", "Congo"), "nickNameX", "#000000"),
            new Territory("East Africa", 0, Arrays.asList("Congo", "North Africa", "Madagascar", "Egypt", "South Africa"), "nickNameX", "#000000"),
            new Territory("Congo", 0, Arrays.asList("North Africa", "East Africa", "South Africa"), "nickNameX", "#000000"),
            new Territory("Madagascar", 0, Arrays.asList("East Africa", "South Africa"), "nickNameX", "#000000"),
            new Territory("South Africa", 0, Arrays.asList("Congo", "East Africa", "Madagascar"), "nickNameX", "#000000")
        )),
        new Continent("North America", 3, Arrays.asList(
            new Territory("Greenland", 0, Arrays.asList("Iceland", "Quebec", "Ontario", "Northwest Territories"), "nickNameX", "#000000"),
            new Territory("Quebec", 0, Arrays.asList("Greenland", "Ontario", "Eastern United States"), "nickNameX", "#000000"),
            new Territory("Ontario", 0, Arrays.asList("Quebec", "Alberta", "Northwest Territories", "Greenland"), "nickNameX", "#000000"),
            new Territory("Western United States", 0, Arrays.asList("Ontario", "Alberta", "Eastern United States", "Central America"), "nickNameX", "#000000"),
            new Territory("Central America", 0, Arrays.asList("Venezuela", "Western United States", "Eastern United States"), "nickNameX", "#000000"),
            new Territory("Alberta", 0, Arrays.asList("Ontario", "Northwest Territories", "Eastern United States", "Alaska"), "nickNameX", "#000000"),
            new Territory("Northwest Territories", 0, Arrays.asList("Ontario", "Greenland", "Alberta", "Alaska"), "nickNameX", "#000000"),
            new Territory("Alaska", 0, Arrays.asList("Kamchatka", "Northwest Territories", "Alberta"), "nickNameX", "#000000"),
            new Territory("Eastern United States", 0, Arrays.asList("Central America", "Ontario", "Western United States", "Quebec"), "nickNameX", "#000000")
        )),
        new Continent("Asia", 3, Arrays.asList(
            new Territory("Kamchatka", 0, Arrays.asList("China", "Japan", "Yakutsk", "Alaska", "Mongolia"), "nickNameX", "#000000"),
            new Territory("Yakutsk", 0, Arrays.asList("China", "Siberia", "Kamchatka"), "nickNameX", "#000000"),
            new Territory("Siberia", 0, Arrays.asList("China", "Ural", "Yakutsk", "China", "Mongolia"), "nickNameX", "#000000"),
            new Territory("Ural", 0, Arrays.asList("Afghanistan", "Ukraine", "Siberia", "China"), "nickNameX", "#000000"),
            new Territory("Afghanistan", 0, Arrays.asList("Ukraine", "Ural", "Middle East", "China"), "nickNameX", "#000000"),
            new Territory("Middle East", 0, Arrays.asList("Southern Europe", "Ukraine", "Yakutsk", "Alaska", "Egypt", "Mongolia"), "nickNameX", "#000000"),
            new Territory("India", 0, Arrays.asList("Middle East", "China", "Siam"), "nickNameX", "#000000"),
            new Territory("Siam", 0, Arrays.asList("India", "China", "Indonesia"), "nickNameX", "#000000"),
            new Territory("Japan", 0, Arrays.asList("Mongolia", "Kamchatka"), "nickNameX", "#000000"),
            new Territory("China", 0, Arrays.asList("Siberia", "Kamchatka", "Yakutsk", "Mongolia"), "nickNameX", "#000000"),
            new Territory("Mongolia", 0, Arrays.asList("Siberia", "Japan", "Kamchatka", "China", "Mongolia"), "nickNameX", "#000000")
        ))
    );

    public static List<Territory> territories = Arrays.asList(
        new Territory("China", 0, Arrays.asList("Mongolia", "Siberia", "Ural", "Afghanistan", "Middle East", "India", "Siam"), "nickNameX", "#000000"),
        new Territory("Greenland", 0, Arrays.asList("Iceland", "Quebec", "Ontario", "Northwest Territories"), "nickNameX", "#000000"),
        new Territory("Quebec", 0, Arrays.asList("Greenland", "Ontario", "Eastern United States"), "nickNameX", "#000000"),
        new Territory("Ontario", 0, Arrays.asList("Quebec", "Alberta", "Northwest Territories", "Greenland"), "nickNameX", "#000000"),
        new Territory("Western United States", 0, Arrays.asList("Ontario", "Alberta", "Eastern United States", "Central America"), "nickNameX", "#000000"),
        new Territory("Central America", 0, Arrays.asList("Venezuela", "Western United States", "Eastern United States"), "nickNameX", "#000000"),
        new Territory("Alberta", 0, Arrays.asList("Ontario", "Northwest Territories", "Eastern United States", "Alaska"), "nickNameX", "#000000"),
        new Territory("Northwest Territories", 0, Arrays.asList("Ontario", "Greenland", "Alberta", "Alaska"), "nickNameX", "#000000"),
        new Territory("Alaska", 0, Arrays.asList("Kamchatka", "Northwest Territories", "Alberta"), "nickNameX", "#000000"),
        new Territory("Eastern United States", 0, Arrays.asList("Central America", "Ontario", "Western United States", "Quebec"), "nickNameX", "#000000"),
        new Territory("Venezuela", 0, Arrays.asList("Central America", "Brazil", "Peru"), "nickNameX", "#000000"),
        new Territory("Brazil", 0, Arrays.asList("North Africa", "Peru", "Venezuela"), "nickNameX", "#000000"),
        new Territory("Peru", 0, Arrays.asList("Venezuela", "Brazil", "Argentina"), "nickNameX", "#000000"),
        new Territory("Argentina", 0, Arrays.asList("Brazil", "Peru"), "nickNameX", "#000000"),
        new Territory("Iceland", 0, Arrays.asList("Greenland", "Scandinavia", "Great Britain"), "nickNameX", "#000000"),
        new Territory("Scandinavia", 0, Arrays.asList("Iceland", "Great Britain", "Northern Europe", "Ukraine"), "nickNameX", "#000000"),
        new Territory("Great Britain", 0, Arrays.asList("Iceland", "Scandinavia", "Northern Europe", "Western Europe"), "nickNameX", "#000000"),
        new Territory("Northern Europe", 0, Arrays.asList("Scandinavia", "Great Britain", "Western Europe", "Ukraine", "Southern Europe"), "nickNameX", "#000000"),
        new Territory("Western Europe", 0, Arrays.asList("North Africa", "Great Britain", "Northern Europe", "Southern Europe"), "nickNameX", "#000000"),
        new Territory("Southern Europe", 0, Arrays.asList("Western Europe", "North Africa", "Northern Europe", "Ukraine", "Egypt", "Middle East"), "nickNameX", "#000000"),
        new Territory("Ukraine", 0, Arrays.asList("Northern Europe", "Ural", "Southern Europe", "Scandinavia", "Middle East", "Afghanistan"), "nickNameX", "#000000"),
        new Territory("North Africa", 0, Arrays.asList("Western Europe", "Egypt", "Congo", "Southern Europe", "Egypt", "East Africa"), "nickNameX", "#000000"),
        new Territory("Egypt", 0, Arrays.asList("Southern Europe", "North Africa", "East Africa", "Middle East", "Congo"), "nickNameX", "#000000"),
        new Territory("East Africa", 0, Arrays.asList("Congo", "North Africa", "Madagascar", "Egypt", "South Africa"), "nickNameX", "#000000"),
        new Territory("Congo", 0, Arrays.asList("North Africa", "East Africa", "South Africa"), "nickNameX", "#000000"),
        new Territory("Madagascar", 0, Arrays.asList("East Africa", "South Africa"), "nickNameX", "#000000"),
        new Territory("South Africa", 0, Arrays.asList("Congo", "East Africa", "Madagascar"), "nickNameX", "#000000"),
        new Territory("Kamchatka", 0, Arrays.asList("China", "Japan", "Yakutsk", "Alaska", "Mongolia"), "nickNameX", "#000000"),
        new Territory("Yakutsk", 0, Arrays.asList("China", "Siberia", "Kamchatka"), "nickNameX", "#000000"),
        new Territory("Siberia", 0, Arrays.asList("China", "Ural", "Yakutsk", "China", "Mongolia"), "nickNameX", "#000000"),
        new Territory("Ural", 0, Arrays.asList("Afghanistan", "Ukraine", "Siberia", "China"), "nickNameX", "#000000"),
        new Territory("Afghanistan", 0, Arrays.asList("Ukraine", "Ural", "Middle East", "China"), "nickNameX", "#000000"),
        new Territory("Middle East", 0, Arrays.asList("Southern Europe", "Ukraine", "Yakutsk", "Alaska", "Egypt", "Mongolia"), "nickNameX", "#000000"),
        new Territory("India", 0, Arrays.asList("Middle East", "China", "Siam"), "nickNameX", "#000000"),
        new Territory("Siam", 0, Arrays.asList("India", "China", "Indonesia"), "nickNameX", "#000000"),
        new Territory("Japan", 0, Arrays.asList("Mongolia", "Kamchatka"), "nickNameX", "#000000"),
        new Territory("China", 0, Arrays.asList("Siberia", "Kamchatka", "Yakutsk", "Mongolia"), "nickNameX", "#000000"),
        new Territory("Mongolia", 0, Arrays.asList("Siberia", "Japan", "Kamchatka", "China", "Mongolia"), "nickNameX", "#000000"),
        new Territory("Indonesia", 0, Arrays.asList("Siam", "New Guinea", "Western Australia"), "nickNameX", "#000000"),
        new Territory("New Guinea", 0, Arrays.asList("Eastern Australia", "Indonesia", "Western Australia"), "nickNameX", "#000000"),
        new Territory("Western Australia", 0, Arrays.asList("Eastern Australia", "Indonesia", "New Guinea"), "nickNameX", "#000000"),
        new Territory("Eastern Australia", 0, Arrays.asList("Western Australia", "New Guinea"), "nickNameX", "#000000")
    );


    public static class Card {
        String name;
        String value;
        public Card(String name, String value) {
            this.name = name;
            this.value = value;
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
            new Card("China", "Infantry"),
            new Card("Mongolia", "Cavalry"),
            new Card("Indonesia", "Artillery"),
            new Card("New Guinea", "Infantry"),
            new Card("Western Australia", "Cavalry"),
            new Card("Eastern Australia", "Artillery")
    );
}
