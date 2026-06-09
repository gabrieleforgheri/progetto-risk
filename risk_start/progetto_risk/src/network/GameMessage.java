package network;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// Serializable protocol object used by both server and clients.
public class GameMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    // Message category used by the receiver to choose how to handle the payload.
    private final MessageType type;
    // Nickname of the player who sent the message; server-generated messages use "server".
    private final String sender;
    // Flexible string payload so new game fields can be added without new message classes.
    private final Map<String, String> data;

    public GameMessage(MessageType type, String sender, Map<String, String> data) {
        this.type = type;
        this.sender = sender;
        // Defensive copy: later changes to the caller's map cannot change this message.
        this.data = new HashMap<>(data);
    }

    // Lobby entry message. The sender nickname is the identity used by the server.
    public static GameMessage join(String nickName) {
        return new GameMessage(MessageType.JOIN, nickName, Collections.emptyMap());
    }

    // Lobby or match exit message. The server broadcasts it to every remaining client.
    public static GameMessage leave(String nickName) {
        return new GameMessage(MessageType.LEAVE, nickName, Collections.emptyMap());
    }

    // Text message. Sending "Start" as chat is also the current command to start the match.
    public static GameMessage chat(String nickName, String text) {
        Map<String, String> data = new HashMap<>();
        data.put("text", text);
        return new GameMessage(MessageType.CHAT, nickName, data);
    }

    public static GameMessage chooseColor(String nickName, String color) {
        Map<String, String> data = new HashMap<>();
        data.put("color", color);
        return new GameMessage(MessageType.CHOOSE_COLOR, nickName, data);
    }

    // Attack declaration before dice/results are resolved.
    public static GameMessage attack(String nickName, String fromTerritory, String toTerritory, int armies) {
        Map<String, String> data = new HashMap<>();
        data.put("fromTerritory", fromTerritory);
        data.put("toTerritory", toTerritory);
        data.put("armies", String.valueOf(armies));
        return new GameMessage(MessageType.ATTACK, nickName, data);
    }

    // Reinforcement placement during the first setup pass or the reinforcement phase.
    public static GameMessage reinforcement(String nickName, String territory, int armies) {
        Map<String, String> data = new HashMap<>();
        data.put("territory", territory);
        data.put("armies", String.valueOf(armies));
        return new GameMessage(MessageType.REINFORCEMENT, nickName, data);
    }

    // Final result of an attack. conquered=true means the defender lost the target territory.
    public static GameMessage attackResult(String nickName, String fromTerritory, String toTerritory,
                                           int attackerLosses, int defenderLosses, boolean conquered) {
        Map<String, String> data = new HashMap<>();
        data.put("fromTerritory", fromTerritory);
        data.put("toTerritory", toTerritory);
        data.put("attackerLosses", String.valueOf(attackerLosses));
        data.put("defenderLosses", String.valueOf(defenderLosses));
        data.put("conquered", String.valueOf(conquered));
        return new GameMessage(MessageType.ATTACK_RESULT, nickName, data);
    }

    // Movement between territories after attack or during fortification.
    public static GameMessage armyMovement(String nickName, String fromTerritory, String toTerritory, int armies) {
        Map<String, String> data = new HashMap<>();
        data.put("fromTerritory", fromTerritory);
        data.put("toTerritory", toTerritory);
        data.put("armies", String.valueOf(armies));
        return new GameMessage(MessageType.ARMY_MOVEMENT, nickName, data);
    }

    // Phase advancement: ATTACK -> MOVEMENT, MOVEMENT -> next player's REINFORCEMENT.
    public static GameMessage endPhase(String nickName) {
        return new GameMessage(MessageType.END_PHASE, nickName, Collections.emptyMap());
    }

    // Server-only message that tells all clients whose turn should be active.
    public static GameMessage turnChange(String currentPlayer) {
        Map<String, String> data = new HashMap<>();
        data.put("currentPlayer", currentPlayer);
        return new GameMessage(MessageType.TURN_CHANGE, "server", data);
    }

    // Server-only state snapshot. Used for both lobby updates and initial game setup.
    public static GameMessage gameState(Map<String, String> data) {
        return new GameMessage(MessageType.GAME_STATE, "server", data);
    }

    // Secret objective for a single client (not broadcast to other players).
    public static GameMessage playerObjective(String nickName, String objectiveId, String description) {
        Map<String, String> data = new HashMap<>();
        data.put("objectiveId", objectiveId);
        data.put("description", description);
        return new GameMessage(MessageType.PLAYER_OBJECTIVE, "server", data);
    }

    public static GameMessage playerTerritoryCards(String nickName, String cards, boolean tradedThisTurn) {
        Map<String, String> data = new HashMap<>();
        data.put("cards", cards == null ? "" : cards);
        data.put("cardsTraded", String.valueOf(tradedThisTurn));
        return new GameMessage(MessageType.PLAYER_TERRITORY_CARDS, "server", data);
    }

    public static GameMessage tradeTerritoryCards(String nickName, String cardIds) {
        Map<String, String> data = new HashMap<>();
        data.put("cardIds", cardIds);
        return new GameMessage(MessageType.TRADE_TERRITORY_CARDS, nickName, data);
    }

    // Server-only error message sent when a request cannot be accepted.
    public static GameMessage error(String text) {
        Map<String, String> data = new HashMap<>();
        data.put("text", text);
        return new GameMessage(MessageType.ERROR, "server", data);
    }

    public MessageType getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public Map<String, String> getData() {
        return Collections.unmodifiableMap(data);
    }

    // Convenience accessor for single payload values, for example "text" in CHAT messages.
    public String get(String key) {
        return data.get(key);
    }
}
