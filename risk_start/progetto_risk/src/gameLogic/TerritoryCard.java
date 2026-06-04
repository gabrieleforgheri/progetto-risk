package gameLogic;

import map.MapObjects;

public final class TerritoryCard {
    private final String id;
    private final String territoryName;
    private final TerritoryCardUnit unit;

    public TerritoryCard(String id, String territoryName, TerritoryCardUnit unit) {
        this.id = id;
        this.territoryName = territoryName;
        this.unit = unit;
    }

    public static TerritoryCard fromMapCard(String id, MapObjects.Card card) {
        return new TerritoryCard(id, card.getName(), TerritoryCardUnit.fromWireName(card.getValue()));
    }

    public String getId() {
        return id;
    }

    public String getTerritoryName() {
        return territoryName;
    }

    public TerritoryCardUnit getUnit() {
        return unit;
    }

    public String serialize() {
        return id + ":" + territoryName + ":" + unit.getWireName();
    }

    public static TerritoryCard deserialize(String encoded) {
        String[] parts = encoded.split(":", 3);
        if (parts.length != 3) {
            throw new IllegalArgumentException("Formato carta non valido: " + encoded);
        }
        return new TerritoryCard(parts[0], parts[1], TerritoryCardUnit.fromWireName(parts[2]));
    }
}
