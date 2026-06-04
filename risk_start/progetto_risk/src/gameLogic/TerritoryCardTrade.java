package gameLogic;

import java.util.List;

public final class TerritoryCardTrade {
    public static final String WRONG_PHASE_MESSAGE = "Non puoi usare carte territorio ora.";
    public static final String ALREADY_TRADED_MESSAGE = "Hai già scambiato carte in questo turno.";
    public static final String INVALID_SET_MESSAGE = "Combinazione carte non valida.";

    private TerritoryCardTrade() {
    }

    public static int bonusArmiesFor(List<TerritoryCardUnit> units) {
        if (units.size() != 3) {
            return -1;
        }
        int infantry = 0;
        int cavalry = 0;
        int artillery = 0;
        for (TerritoryCardUnit unit : units) {
            switch (unit) {
                case INFANTRY -> infantry++;
                case CAVALRY -> cavalry++;
                case ARTILLERY -> artillery++;
            }
        }
        if (infantry == 3) {
            return 6;
        }
        if (cavalry == 3) {
            return 8;
        }
        if (artillery == 3) {
            return 4;
        }
        if (infantry == 1 && cavalry == 1 && artillery == 1) {
            return 10;
        }
        return -1;
    }
}
