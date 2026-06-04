package client.view;

import client.controller.GameController;

/**
 * Lobby client (HTML: {@code lobbyviewclient.html}) — senza pulsante Start.
 */
public class ClientLobbyView extends AbstractLobbyView {

    public ClientLobbyView(GameController controller) {
        super(controller);
        finishLobbyLayout();
    }

    @Override
    protected void buildHostSpecificLayout() {
    }
}
