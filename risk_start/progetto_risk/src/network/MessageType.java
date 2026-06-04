package network;

// Every value represents one kind of message that can travel between clients and the server.
public enum MessageType {
    // A client asks to enter the lobby with its nickname.
    JOIN,
    // A client exits the lobby or match.
    LEAVE,
    // A client picks a lobby color before the match starts.
    CHOOSE_COLOR,
    // Generic text message; the server also uses this for lobby notices.
    CHAT,
    // The active player declares an attack from one territory to another.
    ATTACK,
    // The dice/fight result of an attack, including losses and conquest state.
    ATTACK_RESULT,
    // The active player places reinforcement armies on an owned territory.
    REINFORCEMENT,
    // Armies are moved between two owned territories.
    ARMY_MOVEMENT,
    // The active player advances from attack to movement or ends the turn.
    END_PHASE,
    // The server announces whose turn is active.
    TURN_CHANGE,
    // The server sends lobby state or full game setup data.
    GAME_STATE,
    // Secret objective assigned to one player at match start.
    PLAYER_OBJECTIVE,
    // The server rejects or reports an invalid action/connection.
    ERROR
}
