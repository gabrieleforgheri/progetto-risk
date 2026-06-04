package gameLogic;

import map.MapObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TerritoryCardDeck {
    private final List<MapObjects.Card> drawPile;
    private final List<MapObjects.Card> discardPile;
    private int nextCardId;

    public TerritoryCardDeck() {
        this.drawPile = new ArrayList<>(MapObjects.cards);
        this.discardPile = new ArrayList<>();
        Collections.shuffle(drawPile);
        this.nextCardId = 0;
    }

    public TerritoryCard draw() {
        if (drawPile.isEmpty()) {
            if (discardPile.isEmpty()) {
                throw new IllegalStateException("Il mazzo carte territorio è esaurito.");
            }
            drawPile.addAll(discardPile);
            discardPile.clear();
            Collections.shuffle(drawPile);
        }
        MapObjects.Card card = drawPile.remove(drawPile.size() - 1);
        return TerritoryCard.fromMapCard("tc-" + (nextCardId++), card);
    }

    public void discard(TerritoryCard card) {
        discardPile.add(new MapObjects.Card(card.getTerritoryName(), card.getUnit().getWireName()));
    }
}
