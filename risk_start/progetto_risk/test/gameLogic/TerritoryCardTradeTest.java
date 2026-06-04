package gameLogic;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TerritoryCardTradeTest {

    @Test
    void threeCavalryGivesEightArmies() {
        assertEquals(8, TerritoryCardTrade.bonusArmiesFor(List.of(
                TerritoryCardUnit.CAVALRY,
                TerritoryCardUnit.CAVALRY,
                TerritoryCardUnit.CAVALRY
        )));
    }

    @Test
    void threeInfantryGivesSixArmies() {
        assertEquals(6, TerritoryCardTrade.bonusArmiesFor(List.of(
                TerritoryCardUnit.INFANTRY,
                TerritoryCardUnit.INFANTRY,
                TerritoryCardUnit.INFANTRY
        )));
    }

    @Test
    void threeArtilleryGivesFourArmies() {
        assertEquals(4, TerritoryCardTrade.bonusArmiesFor(List.of(
                TerritoryCardUnit.ARTILLERY,
                TerritoryCardUnit.ARTILLERY,
                TerritoryCardUnit.ARTILLERY
        )));
    }

    @Test
    void oneOfEachGivesTenArmies() {
        assertEquals(10, TerritoryCardTrade.bonusArmiesFor(List.of(
                TerritoryCardUnit.INFANTRY,
                TerritoryCardUnit.CAVALRY,
                TerritoryCardUnit.ARTILLERY
        )));
    }

    @Test
    void invalidCombinationReturnsNegative() {
        assertEquals(-1, TerritoryCardTrade.bonusArmiesFor(List.of(
                TerritoryCardUnit.INFANTRY,
                TerritoryCardUnit.INFANTRY,
                TerritoryCardUnit.CAVALRY
        )));
    }
}
