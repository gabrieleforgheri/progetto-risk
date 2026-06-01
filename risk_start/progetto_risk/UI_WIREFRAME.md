# UI — mappa bozza HTML → JavaFX

Bozza originale: `Documenti/projetto finisetre/finestre.html` + `style.css`.

## Finestre

| Bozza HTML (classe) | Classe JavaFX | File stili |
|---------------------|---------------|------------|
| `.window.start` | `StartView` | `UiStyles.START_*` |
| `.window.lobby_server_creator_pov` | `HostLobbyView` | `UiStyles.LOBBY_*` |
| `.window.lobby_client_join` | `ClientLobbyView` | `UiStyles.LOBBY_*` |
| `.window.game` | `GameView` + pannelli in `view/game/` | `UiStyles.GAME_*` |

## Dove modificare lo stile

1. **Centralizzato:** `src/client/view/style/UiStyles.java` — cerca `STILE:`.
2. **Per componente:** commenti `STILE:` nei file view (es. `StartView`, `TurnPanel`, `CardsPanel`).
3. **Bordo rosso debug:** `UiStyles.WINDOW_DEBUG_BORDER` — rimuovere quando la bozza è definitiva.

## Flusso app

1. **Start** → JOIN (`IP:PORTA` + nickname) → `ClientLobbyView` + connessione.
2. **Start** → CREATE SERVER → `HostLobbyView` + connessione localhost (server in background: stub in `GameController.startLocalServerInBackground()`).
3. **Lobby host** → pulsante `start` → partita → `GameView`.
4. **Lobby client** → attende → `GameView` quando l'host avvia.

## Server locale (host)

Su **CREATE SERVER**, `GameController.connectAsHost()` avvia `RiskServer` in un thread daemon,
attende che la porta sia in ascolto e connette il creatore alla `HostLobbyView`.
Alla chiusura dell'app viene chiamato `GameController.shutdown()`.
