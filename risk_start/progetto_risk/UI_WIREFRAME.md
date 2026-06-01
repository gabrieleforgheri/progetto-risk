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

## Mappa SVG e colori territori

- File: `src/client/assets/risk-map.svg` — ogni territorio è un `<path id="snake_case">`.
- Mapping nomi gioco → id SVG: `client.map.TerritorySvgMapper` (es. `Northwest Territories` → `northwest_territory`, `Yakutsk` → `yakursk`).
- Rendering: `client.map.SvgTerritoryMap` — `fill` = colore proprietario dal server, `stroke` nero = confini.
- Stato client: `ClientGameState.updateFromGameState()` legge `territory.<nome>.owner/armies/color` dai messaggi `GAME_STATE`.
- Aggiornamento live: ogni `playing` / `started` dal server → `MapView.refresh()`.

### Personalizzare i colori

1. **In partita:** colori assegnati in `Setup` (`PLAYER_COLORS`) e inviati dal server in `territory.*.color`.
2. **Stile mappa:** `SvgTerritoryMap` — costanti `NEUTRAL_FILL`, `BORDER_STROKE`, `BORDER_WIDTH`.
3. **Stile UI generale:** `UiStyles.GAME_*`.
