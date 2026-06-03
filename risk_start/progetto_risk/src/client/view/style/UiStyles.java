package client.view.style;

/**
 * Stili JavaFX mappati da {@code Documenti/projetto finisetre/} — risoluzione 1920×1080.
 */
public final class UiStyles {

    private UiStyles() {
    }

    // --- globali ---
    public static final double WINDOW_WIDTH = 1920;
    public static final double WINDOW_HEIGHT = 1080;
    public static final double GAME_WINDOW_WIDTH = 1920;
    public static final double GAME_WINDOW_HEIGHT = 1080;

    /** Sei colori lobby (HTML: {@code .color_choice .box}). */
    public static final String[] LOBBY_PLAYER_COLORS = {
            "#ff0000",
            "#87cefa",
            "#ffff00",
            "#008000",
            "#ffc0cb",
            "#ffa07a"
    };

    public static final String[] LOBBY_PLAYER_ICON_PATHS = {
            "/client/assets/ui/icons/general.png",
            "/client/assets/ui/icons/hacker.png",
            "/client/assets/ui/icons/chicken.png",
            "/client/assets/ui/icons/man.png",
            "/client/assets/ui/icons/musketeer.png",
            "/client/assets/ui/icons/soldier.png"
    };

    public static final double LOBBY_COLOR_BOX_SIZE = 30;
    public static final double LOBBY_COLOR_GRID_WRAP = 120;

    // --- finestra start (finestra di avvio/style.css) ---
    public static final String START_GRADIENT =
            "-fx-background-color: linear-gradient(to right, "
                    + "rgb(153,44,44) 0%, rgb(173,117,64) 40%, rgb(173,117,64) 60%, rgb(153,44,44) 100%);";

    public static final String START_BUTTON =
            "-fx-background-color: #807745; -fx-text-fill: white; -fx-cursor: hand; "
                    + "-fx-padding: 6 10; -fx-background-radius: 0;";

    public static final String START_INPUT =
            "-fx-background-color: white; -fx-font-size: 16px; -fx-padding: 0 8;";

    // --- lobby (finestra di lobby/style.css) ---
    public static final String LOBBY_GRADIENT =
            "-fx-background-color: linear-gradient(to right, "
                    + "rgb(154,141,76) 0%, rgb(212,199,99) 40%, rgb(212,199,99) 60%, rgb(154,141,76) 100%);";

    public static final String LOBBY_PLAYERS_BOX =
            "-fx-border-color: purple; -fx-border-width: 2; -fx-background-color: rgba(255,255,255,0.15);";

    public static final String LOBBY_PLAYER_NAME =
            "-fx-font-size: 14px; -fx-text-fill: #222222;";

    public static final String LOBBY_CHAT_BOX =
            "-fx-border-color: #9e9e9e; -fx-border-width: 2; -fx-background-color: white;";

    public static final String LOBBY_CHAT_TITLE =
            "-fx-font-size: 18px; -fx-font-weight: bold;";

    public static final String LOBBY_CHAT_SEPARATOR =
            "-fx-background-color: #cccccc; -fx-min-height: 1; -fx-max-height: 1;";

    public static final String LOBBY_CHAT_SERVER_MESSAGE =
            "-fx-font-style: italic; -fx-text-fill: #555555; -fx-font-size: 13px;";

    public static final String LOBBY_CHAT_PLAYER_MESSAGE =
            "-fx-text-fill: #212121; -fx-font-size: 13px;";

    public static final String LOBBY_COLOR_BOX =
            "-fx-border-color: #333333; -fx-border-width: 1; -fx-cursor: hand;";

    public static final String LOBBY_COLOR_BOX_SELECTED =
            "-fx-border-color: #000000; -fx-border-width: 3; -fx-cursor: hand;";

    public static final String LOBBY_COLOR_BOX_TAKEN =
            "-fx-border-color: #666666; -fx-border-width: 1; -fx-opacity: 0.45; -fx-cursor: default;";

    public static final String LOBBY_START_BUTTON =
            "-fx-background-color: #e8e8e8; -fx-text-fill: black; -fx-font-size: 20px; "
                    + "-fx-font-weight: bold; -fx-border-color: black; -fx-border-width: 2; "
                    + "-fx-padding: 8 30; -fx-cursor: hand;";

    // --- finestra game (finestra di gioco/main.css) ---
    public static final String GAME_ROOT = "-fx-background-color: #9a8d4c;";

    public static final String GAME_TURN_PANEL =
            "-fx-background-color: #030f63; -fx-background-radius: 5; "
                    + "-fx-border-color: #222222; -fx-border-width: 3; -fx-padding: 8;";

    public static final String GAME_TURN_TITLE =
            "-fx-text-fill: #d6040e; -fx-font-size: 28px; -fx-font-weight: bold; "
                    + "-fx-effect: dropshadow(gaussian, white, 2, 0.5, 2, 2);";

    public static final String GAME_TURN_TEXT =
            "-fx-text-fill: white; -fx-font-size: 14px; "
                    + "-fx-effect: dropshadow(gaussian, #222, 2, 0.5, 2, 2);";

    public static final String GAME_TURN_INFO =
            "-fx-text-fill: #d6040e; -fx-font-size: 22px; "
                    + "-fx-effect: dropshadow(gaussian, #222, 2, 0.5, 2, 2);";

    public static final String GAME_END_PHASE_BUTTON =
            "-fx-background-color: #d6040e; -fx-text-fill: white; -fx-font-weight: bold; "
                    + "-fx-background-radius: 5; -fx-padding: 6 12; -fx-cursor: hand;";

    public static final String GAME_USER_SLOT =
            "-fx-border-color: black; -fx-border-width: 2; -fx-background-radius: 10; "
                    + "-fx-min-width: 120; -fx-min-height: 80; -fx-alignment: center;";

    public static final String GAME_CARDS_BAR =
            "-fx-background-color: #d6040e; -fx-padding: 8;";

    public static final String GAME_TERRITORY_CARD =
            "-fx-background-color: #2e7d32; -fx-min-width: 90; -fx-min-height: 70; "
                    + "-fx-alignment: center; -fx-text-fill: white;";

    public static final String GAME_OBJECTIVE_CARD =
            "-fx-background-color: black; -fx-text-fill: white; -fx-min-width: 90; "
                    + "-fx-min-height: 70; -fx-alignment: center;";

    public static final String GAME_MAP_PLACEHOLDER =
            "-fx-font-size: 18px; -fx-text-fill: #666666; -fx-alignment: center;";
}
