package gameLogic;

import map.MapObjects;
import menu.Setup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GameLogic {
    public enum Phase {
        REINFORCEMENT,
        ATTACK,
        MOVEMENT,
        GAME_OVER
    }

    private final Map<String, PlayerState> players;
    private final Map<String, TerritoryState> territories;
    private final List<String> turnOrder;
    private final Random random;
    private int currentPlayerIndex;
    private int playersWaitingForInitialPlacement;
    private Phase phase;
    private String winner;
    private String winnerObjectiveDescription;
    private final Map<String, ObjectiveCard> playerObjectives;
    private final TerritoryCardDeck territoryCardDeck;

    private GameLogic(Map<String, PlayerState> players,
                      Map<String, TerritoryState> territories,
                      List<String> turnOrder,
                      Map<String, ObjectiveCard> playerObjectives,
                      TerritoryCardDeck territoryCardDeck) {
        this.players = players;
        this.territories = territories;
        this.turnOrder = turnOrder;
        this.playerObjectives = playerObjectives;
        this.territoryCardDeck = territoryCardDeck;
        this.random = new Random();
        this.currentPlayerIndex = 0;
        this.playersWaitingForInitialPlacement = turnOrder.size();
        this.phase = Phase.REINFORCEMENT;
        this.winner = "";
        this.winnerObjectiveDescription = "";
    }

    public static GameLogic fromSetup(Setup.GameSetup setup) {
        Map<String, MapObjects.Territory> mapTerritories = new LinkedHashMap<>();
        for (MapObjects.Territory territory : MapObjects.territories) {
            mapTerritories.putIfAbsent(territory.getName(), territory);
        }

        Map<String, PlayerState> players = new LinkedHashMap<>();
        Map<String, TerritoryState> territories = new LinkedHashMap<>();
        List<String> turnOrder = new ArrayList<>();

        for (Setup.PlayerSetup playerSetup : setup.getPlayers()) {
            PlayerState player = new PlayerState(
                    playerSetup.getNickName(),
                    playerSetup.getColor(),
                    playerSetup.getRemainingArmies()
            );
            players.put(player.getNickName(), player);
            turnOrder.add(player.getNickName());

            for (String territoryName : playerSetup.getTerritories()) {
                if (territories.containsKey(territoryName)) {
                    continue;
                }

                MapObjects.Territory mapTerritory = mapTerritories.get(territoryName);
                List<String> borders = mapTerritory == null ? Collections.emptyList() : mapTerritory.getBorders();
                TerritoryState territory = new TerritoryState(
                        territoryName,
                        player.getNickName(),
                        player.getColor(),
                        1,
                        borders
                );
                territories.put(territoryName, territory);
                player.addTerritory(territoryName);
            }
        }

        Map<String, String> playerColors = new LinkedHashMap<>();
        for (PlayerState player : players.values()) {
            playerColors.put(player.getNickName(), player.getColor());
        }
        Map<String, ObjectiveCard> objectives = ObjectiveCard.dealToPlayers(turnOrder, playerColors);
        return new GameLogic(players, territories, turnOrder, objectives, new TerritoryCardDeck());
    }

    public String serializeTerritoryCards(String playerNickName) {
        PlayerState player = players.get(playerNickName);
        if (player == null) {
            return "";
        }
        return player.serializeTerritoryCards();
    }

    public boolean hasTradedTerritoryCardsThisTurn(String playerNickName) {
        PlayerState player = players.get(playerNickName);
        return player != null && player.hasTradedTerritoryCardsThisTurn();
    }

    public Result tradeTerritoryCards(String playerNickName, List<String> cardIds) {
        if (phase != Phase.REINFORCEMENT) {
            return Result.rejected(TerritoryCardTrade.WRONG_PHASE_MESSAGE);
        }
        Result validation = validateActivePlayer(playerNickName, Phase.REINFORCEMENT);
        if (!validation.isAccepted()) {
            return validation;
        }

        PlayerState player = players.get(playerNickName);
        if (player.hasTradedTerritoryCardsThisTurn()) {
            return Result.rejected(TerritoryCardTrade.ALREADY_TRADED_MESSAGE);
        }
        if (cardIds == null || cardIds.size() != 3) {
            return Result.rejected("Devi selezionare esattamente 3 carte.");
        }
        if (new LinkedHashSet<>(cardIds).size() != 3) {
            return Result.rejected("Le 3 carte devono essere diverse.");
        }

        List<TerritoryCard> selected = player.findTerritoryCards(cardIds);
        if (selected.size() != 3) {
            return Result.rejected("Una o più carte selezionate non sono nella tua mano.");
        }

        List<TerritoryCardUnit> units = new ArrayList<>();
        for (TerritoryCard card : selected) {
            units.add(card.getUnit());
        }
        int bonus = TerritoryCardTrade.bonusArmiesFor(units);
        if (bonus < 0) {
            return Result.rejected(TerritoryCardTrade.INVALID_SET_MESSAGE);
        }

        player.removeTerritoryCards(cardIds);
        for (TerritoryCard card : selected) {
            territoryCardDeck.discard(card);
        }
        player.addRemainingArmies(bonus);
        player.setTradedTerritoryCardsThisTurn(true);

        return Result.accepted(playerNickName + " ha scambiato 3 carte e riceve " + bonus + " armate.");
    }

    public ObjectiveCard getObjective(String playerNickName) {
        return playerObjectives.get(playerNickName);
    }

    public Result reinforce(String playerNickName, String territoryName, int armies) {
        Result validation = validateActivePlayer(playerNickName, Phase.REINFORCEMENT);
        if (!validation.isAccepted()) {
            return validation;
        }

        PlayerState player = players.get(playerNickName);
        TerritoryState territory = territories.get(territoryName);
        if (territory == null) {
            return Result.rejected("Territory does not exist: " + territoryName + ".");
        }
        if (!playerNickName.equals(territory.getOwner())) {
            return Result.rejected("You can only reinforce your own territories.");
        }
        if (armies <= 0) {
            return Result.rejected("Reinforcement armies must be greater than zero.");
        }
        if (armies > player.getRemainingArmies()) {
            return Result.rejected("You only have " + player.getRemainingArmies() + " armies to place.");
        }

        territory.addArmies(armies);
        player.removeRemainingArmies(armies);
        if (player.getRemainingArmies() == 0) {
            phase = Phase.ATTACK;
        }
        checkObjectiveVictory(playerNickName);

        return Result.accepted(playerNickName + " reinforced " + territoryName + " with " + armies + " armies.");
    }

    public Result attack(String playerNickName, String fromTerritoryName, String toTerritoryName) {
        Result validation = validateActivePlayer(playerNickName, Phase.ATTACK);
        if (!validation.isAccepted()) {
            return validation;
        }

        TerritoryState fromTerritory = territories.get(fromTerritoryName);
        TerritoryState toTerritory = territories.get(toTerritoryName);
        if (fromTerritory == null || toTerritory == null) {
            return Result.rejected("Both attack territories must exist.");
        }
        if (!playerNickName.equals(fromTerritory.getOwner())) {
            return Result.rejected("You can only attack from your own territories.");
        }
        if (playerNickName.equals(toTerritory.getOwner())) {
            return Result.rejected("You cannot attack your own territory.");
        }
        if (!fromTerritory.isBordering(toTerritoryName)) {
            return Result.rejected(fromTerritoryName + " does not border " + toTerritoryName + ".");
        }
        if (fromTerritory.getArmies() <= 1) {
            return Result.rejected("An attack needs at least two armies on the source territory.");
        }

        int attackerLosses = 0;
        int defenderLosses = 0;
        while (toTerritory.getArmies() > 0 && fromTerritory.getArmies() > 1) {
            if (rollAttackerLoses()) {
                fromTerritory.removeArmies(1);
                attackerLosses++;
            } else {
                toTerritory.removeArmies(1);
                defenderLosses++;
            }
        }

        boolean conquered = toTerritory.getArmies() == 0;
        if (conquered) {
            conquerTerritory(playerNickName, fromTerritory, toTerritory);
            checkObjectiveVictory(null);
        }

        String message = conquered
                ? playerNickName + " conquered " + toTerritoryName + " from " + fromTerritoryName + "."
                : playerNickName + " attacked " + toTerritoryName + " from " + fromTerritoryName + ".";
        return Result.attackAccepted(message, fromTerritoryName, toTerritoryName,
                attackerLosses, defenderLosses, conquered);
    }

    public Result moveArmies(String playerNickName, String fromTerritoryName, String toTerritoryName, int armies) {
        Result validation = validateActivePlayer(playerNickName, Phase.MOVEMENT);
        if (!validation.isAccepted()) {
            return validation;
        }

        TerritoryState fromTerritory = territories.get(fromTerritoryName);
        TerritoryState toTerritory = territories.get(toTerritoryName);
        if (fromTerritory == null || toTerritory == null) {
            return Result.rejected("Both movement territories must exist.");
        }
        if (!playerNickName.equals(fromTerritory.getOwner()) || !playerNickName.equals(toTerritory.getOwner())) {
            return Result.rejected("You can only move armies between your own territories.");
        }
        if (!fromTerritory.isBordering(toTerritoryName)) {
            return Result.rejected(fromTerritoryName + " does not border " + toTerritoryName + ".");
        }
        if (armies <= 0) {
            return Result.rejected("Movement armies must be greater than zero.");
        }
        if (fromTerritory.getArmies() - armies < 1) {
            return Result.rejected("You must leave at least one army behind.");
        }

        fromTerritory.removeArmies(armies);
        toTerritory.addArmies(armies);
        checkObjectiveVictory(playerNickName);
        return Result.accepted(playerNickName + " moved " + armies + " armies from "
                + fromTerritoryName + " to " + toTerritoryName + ".");
    }

    public Result advancePhase(String playerNickName) {
        if (phase == Phase.GAME_OVER) {
            return Result.rejected("The game is already over.");
        }
        if (!getCurrentPlayer().equals(playerNickName)) {
            return Result.rejected("It is " + getCurrentPlayer() + "'s turn.");
        }

        if (phase == Phase.REINFORCEMENT) {
            PlayerState player = players.get(playerNickName);
            if (player.getRemainingArmies() > 0) {
                return Result.rejected("Place all reinforcement armies before changing phase.");
            }
            phase = Phase.ATTACK;
            return Result.accepted(playerNickName + " started the attack phase.");
        }

        if (phase == Phase.ATTACK) {
            phase = Phase.MOVEMENT;
            return Result.accepted(playerNickName + " started the movement phase.");
        }

        PlayerState endingPlayer = players.get(playerNickName);
        boolean drewCard = false;
        if (endingPlayer != null && endingPlayer.hasConqueredThisTurn()) {
            TerritoryCard drawn = territoryCardDeck.draw();
            endingPlayer.addTerritoryCard(drawn);
            endingPlayer.setConqueredThisTurn(false);
            drewCard = true;
        }

        advanceTurn();
        String message = drewCard
                ? playerNickName + " ha pescato una carta territorio. Turno di " + getCurrentPlayer() + "."
                : "Turn changed to " + getCurrentPlayer() + ".";
        return Result.accepted(message);
    }

    public String getCurrentPlayer() {
        if (turnOrder.isEmpty()) {
            return "";
        }
        return turnOrder.get(currentPlayerIndex);
    }

    public boolean isGameOver() {
        return phase == Phase.GAME_OVER;
    }

    public String getWinner() {
        return winner;
    }

    public String getWinnerObjectiveDescription() {
        return winnerObjectiveDescription;
    }

    public String getWinAnnouncement() {
        if (winner == null || winner.isEmpty()) {
            return "";
        }
        if (winnerObjectiveDescription == null || winnerObjectiveDescription.isEmpty()) {
            return winner + " ha vinto la partita!";
        }
        return winner + " ha vinto la partita! Missione completata: " + winnerObjectiveDescription;
    }

    public Map<String, String> toMessageData(String messagePhase) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("phase", messagePhase);
        data.put("stage", phase.name().toLowerCase());
        data.put("currentPlayer", getCurrentPlayer());
        data.put("winner", winner);
        data.put("winnerObjective", winnerObjectiveDescription);
        data.put("players", String.join(",", turnOrder));

        for (PlayerState player : players.values()) {
            String prefix = "player." + player.getNickName() + ".";
            data.put(prefix + "armies", String.valueOf(player.getTotalArmies(territories)));
            data.put(prefix + "remainingArmies", String.valueOf(player.getRemainingArmies()));
            data.put(prefix + "color", player.getColor());
            data.put(prefix + "territories", String.join(",", player.getTerritories()));
        }

        data.put("territories", String.join(",", territories.keySet()));
        for (TerritoryState territory : territories.values()) {
            String prefix = "territory." + territory.getName() + ".";
            data.put(prefix + "owner", territory.getOwner());
            data.put(prefix + "armies", String.valueOf(territory.getArmies()));
            data.put(prefix + "color", territory.getColor());
        }

        return data;
    }

    private Result validateActivePlayer(String playerNickName, Phase expectedPhase) {
        if (phase == Phase.GAME_OVER) {
            return Result.rejected("The game is over.");
        }
        if (!players.containsKey(playerNickName)) {
            return Result.rejected("Unknown player: " + playerNickName + ".");
        }
        if (!getCurrentPlayer().equals(playerNickName)) {
            return Result.rejected("It is " + getCurrentPlayer() + "'s turn.");
        }
        if (phase != expectedPhase) {
            return Result.rejected("Current phase is " + phase.name().toLowerCase() + ".");
        }
        return Result.accepted("");
    }

    private void conquerTerritory(String attackerNickName,
                                  TerritoryState fromTerritory,
                                  TerritoryState conqueredTerritory) {
        PlayerState attacker = players.get(attackerNickName);
        PlayerState defender = players.get(conqueredTerritory.getOwner());

        if (defender != null) {
            defender.removeTerritory(conqueredTerritory.getName());
        }

        conqueredTerritory.setOwner(attackerNickName);
        conqueredTerritory.setColor(attacker.getColor());
        attacker.addTerritory(conqueredTerritory.getName());

        int movingArmies = Math.max(1, fromTerritory.getArmies() / 2);
        fromTerritory.removeArmies(movingArmies);
        conqueredTerritory.setArmies(movingArmies);
        attacker.setConqueredThisTurn(true);
    }

    private void advanceTurn() {
        playersWaitingForInitialPlacement--;
        currentPlayerIndex = (currentPlayerIndex + 1) % turnOrder.size();
        phase = Phase.REINFORCEMENT;

        PlayerState currentPlayer = players.get(getCurrentPlayer());
        currentPlayer.setTradedTerritoryCardsThisTurn(false);
        currentPlayer.setConqueredThisTurn(false);
        if (playersWaitingForInitialPlacement <= 0) {
            currentPlayer.setRemainingArmies(calculateReinforcements(currentPlayer));
        }
    }

    private int calculateReinforcements(PlayerState player) {
        int reinforcements = Math.max(3, player.getTerritories().size() / 3);

        for (ContinentState continent : buildContinents()) {
            if (player.getTerritories().containsAll(continent.getTerritories())) {
                reinforcements += continent.getBonusArmies();
            }
        }

        return reinforcements;
    }

    private List<ContinentState> buildContinents() {
        Map<String, ContinentState> continents = new LinkedHashMap<>();
        for (MapObjects.Territory territory : MapObjects.territories) {
            String continentName = Continents.findContinentName(territory.getName());
            int bonusArmies = getContinentBonus(continentName);
            continents.computeIfAbsent(continentName, name -> new ContinentState(name, bonusArmies))
                    .addTerritory(territory.getName());
        }
        return new ArrayList<>(continents.values());
    }

    private int getContinentBonus(String continentName) {
        return switch (continentName) {
            case Continents.OCEANIA, Continents.SOUTH_AMERICA -> 2;
            case Continents.AFRICA -> 3;
            case Continents.EUROPE, Continents.NORTH_AMERICA -> 5;
            case Continents.ASIA -> 7;
            default -> 0;
        };
    }

    private void checkObjectiveVictory(String preferredPlayerNickName) {
        if (phase == Phase.GAME_OVER) {
            return;
        }

        if (preferredPlayerNickName != null && trySetWinner(preferredPlayerNickName)) {
            return;
        }

        for (String nickName : turnOrder) {
            if (trySetWinner(nickName)) {
                return;
            }
        }
    }

    private boolean trySetWinner(String playerNickName) {
        ObjectiveCard objective = playerObjectives.get(playerNickName);
        if (objective == null) {
            return false;
        }
        if (!objective.isFulfilled(toPlayerSnapshot(playerNickName), buildAllPlayerSnapshots())) {
            return false;
        }
        winner = playerNickName;
        winnerObjectiveDescription = objective.getDescription();
        phase = Phase.GAME_OVER;
        return true;
    }

    private ObjectiveCard.PlayerSnapshot toPlayerSnapshot(String playerNickName) {
        PlayerState player = players.get(playerNickName);
        if (player == null) {
            return new ObjectiveCard.PlayerSnapshot(playerNickName, "", List.of(), Map.of());
        }
        Map<String, Integer> armiesByTerritory = new LinkedHashMap<>();
        for (String territoryName : player.getTerritories()) {
            TerritoryState territory = territories.get(territoryName);
            if (territory != null) {
                armiesByTerritory.put(territoryName, territory.getArmies());
            }
        }
        return new ObjectiveCard.PlayerSnapshot(
                player.getNickName(),
                player.getColor(),
                player.getTerritories(),
                armiesByTerritory
        );
    }

    private Map<String, ObjectiveCard.PlayerSnapshot> buildAllPlayerSnapshots() {
        Map<String, ObjectiveCard.PlayerSnapshot> snapshots = new LinkedHashMap<>();
        for (String nickName : turnOrder) {
            snapshots.put(nickName, toPlayerSnapshot(nickName));
        }
        return snapshots;
    }

    private boolean rollAttackerLoses() {
        return random.nextInt(6) + 1 > random.nextInt(6) + 1;
    }

    public static class Result {
        private final boolean accepted;
        private final String message;
        private final String fromTerritory;
        private final String toTerritory;
        private final int attackerLosses;
        private final int defenderLosses;
        private final boolean conquered;

        private Result(boolean accepted, String message, String fromTerritory, String toTerritory,
                       int attackerLosses, int defenderLosses, boolean conquered) {
            this.accepted = accepted;
            this.message = message;
            this.fromTerritory = fromTerritory;
            this.toTerritory = toTerritory;
            this.attackerLosses = attackerLosses;
            this.defenderLosses = defenderLosses;
            this.conquered = conquered;
        }

        public static Result accepted(String message) {
            return new Result(true, message, "", "", 0, 0, false);
        }

        public static Result attackAccepted(String message, String fromTerritory, String toTerritory,
                                            int attackerLosses, int defenderLosses, boolean conquered) {
            return new Result(true, message, fromTerritory, toTerritory,
                    attackerLosses, defenderLosses, conquered);
        }

        public static Result rejected(String message) {
            return new Result(false, message, "", "", 0, 0, false);
        }

        public boolean isAccepted() {
            return accepted;
        }

        public String getMessage() {
            return message;
        }

        public String getFromTerritory() {
            return fromTerritory;
        }

        public String getToTerritory() {
            return toTerritory;
        }

        public int getAttackerLosses() {
            return attackerLosses;
        }

        public int getDefenderLosses() {
            return defenderLosses;
        }

        public boolean isConquered() {
            return conquered;
        }
    }

    private static class PlayerState {
        private final String nickName;
        private final String color;
        private final Set<String> territories;
        private final List<TerritoryCard> territoryCards;
        private int remainingArmies;
        private boolean conqueredThisTurn;
        private boolean tradedTerritoryCardsThisTurn;

        private PlayerState(String nickName, String color, int remainingArmies) {
            this.nickName = nickName;
            this.color = color;
            this.remainingArmies = remainingArmies;
            this.territories = new LinkedHashSet<>();
            this.territoryCards = new ArrayList<>();
            this.conqueredThisTurn = false;
            this.tradedTerritoryCardsThisTurn = false;
        }

        private String getNickName() {
            return nickName;
        }

        private String getColor() {
            return color;
        }

        private int getRemainingArmies() {
            return remainingArmies;
        }

        private void setRemainingArmies(int remainingArmies) {
            this.remainingArmies = remainingArmies;
        }

        private void removeRemainingArmies(int armies) {
            remainingArmies -= armies;
        }

        private void addRemainingArmies(int armies) {
            remainingArmies += armies;
        }

        private boolean hasConqueredThisTurn() {
            return conqueredThisTurn;
        }

        private void setConqueredThisTurn(boolean conqueredThisTurn) {
            this.conqueredThisTurn = conqueredThisTurn;
        }

        private boolean hasTradedTerritoryCardsThisTurn() {
            return tradedTerritoryCardsThisTurn;
        }

        private void setTradedTerritoryCardsThisTurn(boolean tradedTerritoryCardsThisTurn) {
            this.tradedTerritoryCardsThisTurn = tradedTerritoryCardsThisTurn;
        }

        private void addTerritoryCard(TerritoryCard card) {
            territoryCards.add(card);
        }

        private List<TerritoryCard> findTerritoryCards(List<String> cardIds) {
            List<TerritoryCard> found = new ArrayList<>();
            for (String cardId : cardIds) {
                for (TerritoryCard card : territoryCards) {
                    if (card.getId().equals(cardId)) {
                        found.add(card);
                        break;
                    }
                }
            }
            return found;
        }

        private void removeTerritoryCards(List<String> cardIds) {
            territoryCards.removeIf(card -> cardIds.contains(card.getId()));
        }

        private String serializeTerritoryCards() {
            if (territoryCards.isEmpty()) {
                return "";
            }
            List<String> encoded = new ArrayList<>();
            for (TerritoryCard card : territoryCards) {
                encoded.add(card.serialize());
            }
            return String.join(";", encoded);
        }

        private List<String> getTerritories() {
            return new ArrayList<>(territories);
        }

        private void addTerritory(String territoryName) {
            territories.add(territoryName);
        }

        private void removeTerritory(String territoryName) {
            territories.remove(territoryName);
        }

        private int getTotalArmies(Map<String, TerritoryState> allTerritories) {
            int total = remainingArmies;
            for (String territoryName : territories) {
                TerritoryState territory = allTerritories.get(territoryName);
                if (territory != null) {
                    total += territory.getArmies();
                }
            }
            return total;
        }
    }

    private static class TerritoryState {
        private final String name;
        private final List<String> borders;
        private String owner;
        private String color;
        private int armies;

        private TerritoryState(String name, String owner, String color, int armies, List<String> borders) {
            this.name = name;
            this.owner = owner;
            this.color = color;
            this.armies = armies;
            this.borders = new ArrayList<>(borders);
        }

        private String getName() {
            return name;
        }

        private String getOwner() {
            return owner;
        }

        private void setOwner(String owner) {
            this.owner = owner;
        }

        private String getColor() {
            return color;
        }

        private void setColor(String color) {
            this.color = color;
        }

        private int getArmies() {
            return armies;
        }

        private void setArmies(int armies) {
            this.armies = armies;
        }

        private void addArmies(int armies) {
            this.armies += armies;
        }

        private void removeArmies(int armies) {
            this.armies -= armies;
        }

        private boolean isBordering(String territoryName) {
            return borders.contains(territoryName);
        }
    }

    private static class ContinentState {
        private final String name;
        private final int bonusArmies;
        private final Set<String> territories;

        private ContinentState(String name, int bonusArmies) {
            this.name = name;
            this.bonusArmies = bonusArmies;
            this.territories = new LinkedHashSet<>();
        }

        private int getBonusArmies() {
            return bonusArmies;
        }

        private Set<String> getTerritories() {
            return territories;
        }

        private void addTerritory(String territoryName) {
            territories.add(territoryName);
        }
    }
}
