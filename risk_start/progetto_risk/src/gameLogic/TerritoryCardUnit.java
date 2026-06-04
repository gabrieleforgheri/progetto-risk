package gameLogic;

public enum TerritoryCardUnit {
    INFANTRY("Infantry", "Fantino"),
    CAVALRY("Cavalry", "Cavaliere"),
    ARTILLERY("Artillery", "Cannone");

    private final String wireName;
    private final String displayName;

    TerritoryCardUnit(String wireName, String displayName) {
        this.wireName = wireName;
        this.displayName = displayName;
    }

    public String getWireName() {
        return wireName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static TerritoryCardUnit fromWireName(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Tipo carta mancante.");
        }
        for (TerritoryCardUnit unit : values()) {
            if (unit.wireName.equalsIgnoreCase(value.trim())) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Tipo carta sconosciuto: " + value);
    }
}
