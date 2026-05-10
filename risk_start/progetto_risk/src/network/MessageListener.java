package network;

// Implemented by client-side code that wants to react to messages received from the server.
public interface MessageListener {
    // Called for every valid GameMessage read from the socket.
    void onMessage(GameMessage message);

    // Optional hook for UI/game code that needs to react when the server connection ends.
    default void onConnectionClosed(Exception exception) {
    }
}
