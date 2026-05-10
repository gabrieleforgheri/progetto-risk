package network;

import java.io.Serializable;
import java.time.Instant;
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
    // Creation timestamp in epoch milliseconds, useful for logging and replay ordering.
    private final long createdAt;
    // Flexible string payload so new game fields can be added without new message classes.
    private final Map<String, String> data;

    public GameMessage(MessageType type, String sender, Map<String, String> data) {
        this.type = type;
        this.sender = sender;
        this.createdAt = Instant.now().toEpochMilli();
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

    // Attack declaration before dice/results are resolved.
    public static GameMessage attack(String nickName, String fromTerritory, String toTerritory, int armies) {
        Map<String, String> data = new HashMap<>();
        data.put("fromTerritory", fromTerritory);
        data.put("toTerritory", toTerritory);
        data.put("armies", String.valueOf(armies));
        return new GameMessage(MessageType.ATTACK, nickName, data);
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

    public long getCreatedAt() {
        return createdAt;
    }

    public Map<String, String> getData() {
        return Collections.unmodifiableMap(data);
    }

    // Convenience accessor for single payload values, for example "text" in CHAT messages.
    public String get(String key) {
        return data.get(key);
    }
}
