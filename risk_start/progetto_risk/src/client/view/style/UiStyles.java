package client.view.style;

/**
 * Punto unico per gli stili JavaFX, mappato dalla bozza CSS in
 * {@code Documenti/projetto finisetre/style.css}.
 * <p>
 * Cerca i commenti {@code STILE:} per sapere dove intervenire su colori, font e bordi.
 */
public final class UiStyles {

    private UiStyles() {
    }

    // --- STILE: globali (.window) — dimensioni finestra bozza 1000x500 ---
    public static final double WINDOW_WIDTH = 1000;
    public static final double WINDOW_HEIGHT = 500;
    public static final double GAME_WINDOW_WIDTH = 1280;
    public static final double GAME_WINDOW_HEIGHT = 820;

    // STILE: bordo debug bozza (border: 2px solid red) — rimuovere in produzione
    public static final String WINDOW_DEBUG_BORDER =
            "-fx-border-color: #cc0000; -fx-border-width: 2;";

    // --- STILE: finestra start (.start) ---
    // STILE: sostituire con immagine di sfondo (finestre.html: "background img and to decide")
    public static final String START_ROOT =
            "-fx-background-color: linear-gradient(to bottom, #1a1a2e, #16213e);";

    // STILE: .join — box verde JOIN
    public static final String START_JOIN_BOX =
            "-fx-background-color: #2e7d32; -fx-background-radius: 4; -fx-cursor: hand;";
    public static final String START_JOIN_LABEL =
            "-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;";

    // STILE: .server_create — box verde CREATE SERVER
    public static final String START_SERVER_BOX =
            "-fx-background-color: #2e7d32; -fx-background-radius: 4; -fx-cursor: hand;";

    // STILE: .start input — campo IP:PORTA
    public static final String START_ADDRESS_FIELD =
            "-fx-background-color: #f5f5f5; -fx-font-size: 14px;";

    // --- STILE: lobby (.players_join, .player_icon, .chat) ---
    // STILE: .players_join — area giocatori (border purple in bozza)
    public static final String LOBBY_PLAYERS_BOX =
            "-fx-border-color: #7b1fa2; -fx-border-width: 2; -fx-background-color: #fafafa;";

    // STILE: .player_icon center — cerchio avatar (border green in bozza)
    public static final String LOBBY_PLAYER_AVATAR =
            "-fx-border-color: #2e7d32; -fx-border-width: 2; -fx-background-radius: 50; "
                    + "-fx-min-width: 80; -fx-min-height: 80; -fx-max-width: 80; -fx-max-height: 80;";

    // STILE: .chat — pannello chat (border grey in bozza)
    public static final String LOBBY_CHAT_BOX =
            "-fx-border-color: #9e9e9e; -fx-border-width: 2; -fx-background-color: white;";

    public static final String LOBBY_CHAT_TITLE =
            "-fx-font-size: 18px; -fx-font-weight: bold;";

    // STILE: messaggi server in corsivo (tag <i> in HTML)
    public static final String LOBBY_CHAT_SERVER_MESSAGE =
            "-fx-font-style: italic; -fx-text-fill: #555555;";

    // STILE: messaggi giocatore (tag <p> in HTML)
    public static final String LOBBY_CHAT_PLAYER_MESSAGE =
            "-fx-text-fill: #212121;";

    // STILE: button / #start — pulsante avvio partita host
    public static final String LOBBY_START_BUTTON =
            "-fx-background-color: #d6040e; -fx-text-fill: white; -fx-font-weight: bold; "
                    + "-fx-font-size: 14px; -fx-padding: 8 24;";

    // --- STILE: finestra game (.turn, .utente, .cards) ---
    // STILE: .turn — pannello turno sinistro
    public static final String GAME_TURN_PANEL =
            "-fx-background-color: #030f63; -fx-background-radius: 5; "
                    + "-fx-border-color: #222222; -fx-border-width: 3; -fx-padding: 8;";

    public static final String GAME_TURN_TITLE =
            "-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold; "
                    + "-fx-effect: dropshadow(gaussian, #222, 2, 0.5, 2, 2);";

    public static final String GAME_TURN_TEXT =
            "-fx-text-fill: white; -fx-font-size: 12px; "
                    + "-fx-effect: dropshadow(gaussian, #222, 2, 0.5, 2, 2);";

    // STILE: .turn button / #end — End Phase
    public static final String GAME_END_PHASE_BUTTON =
            "-fx-background-color: #d6040e; -fx-text-fill: white; -fx-font-weight: bold; "
                    + "-fx-background-radius: 5; -fx-padding: 6 12; -fx-cursor: hand;";

    // STILE: .utente — riquadro giocatore a destra
    public static final String GAME_USER_SLOT =
            "-fx-border-color: black; -fx-border-width: 2; -fx-background-radius: 10; "
                    + "-fx-min-width: 60; -fx-min-height: 40; -fx-alignment: center;";

    // STILE: .cards — barra carte in basso
    public static final String GAME_CARDS_BAR =
            "-fx-background-color: #d6040e; -fx-padding: 8;";

    // STILE: .carta_territorio
    public static final String GAME_TERRITORY_CARD =
            "-fx-background-color: #2e7d32; -fx-min-width: 90; -fx-min-height: 70; "
                    + "-fx-alignment: center; -fx-text-fill: white;";

    // STILE: .carta_obbiettivo
    public static final String GAME_OBJECTIVE_CARD =
            "-fx-background-color: black; -fx-text-fill: white; -fx-min-width: 90; "
                    + "-fx-min-height: 70; -fx-alignment: center;";

    // STILE: area mappa centrale ("SVG FILE MAP" in bozza)
    public static final String GAME_MAP_PLACEHOLDER =
            "-fx-font-size: 18px; -fx-text-fill: #666666; -fx-alignment: center;";
}
