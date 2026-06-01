package client.view;

import client.controller.GameController;

/**
 * Lobby client che si unisce via IP:PORTA (HTML: {@code .window.lobby_client_join}).
 * Nessun pulsante Start: attende che l'host avvii la partita.
 */
public class ClientLobbyView extends AbstractLobbyView {

    public ClientLobbyView(GameController controller) {
        super(controller);
        finishLobbyLayout();
    }

    @Override
    protected String lobbyModeLabel() {
        return "Lobby — Client";
    }

    @Override
    protected void buildHostSpecificLayout() {
        // STILE: eventuale messaggio "In attesa dell'host..." sotto la chat
    }
}
