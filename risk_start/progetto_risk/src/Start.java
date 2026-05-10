import network.RiskServer;

import java.io.IOException;

public class Start {
    public static void main(String[] args) {
        // The first command-line argument can override the default server port.
        int port = args.length > 0 ? Integer.parseInt(args[0]) : RiskServer.DEFAULT_PORT;
        RiskServer server = new RiskServer(port);

        // These messages describe the lobby rules before the blocking server loop starts.
        System.out.println("Starting Risk server on port " + port + ".");
        System.out.println("Waiting for 3 to 6 players.");
        System.out.println("The first player that joins can write \"Start\" to begin the game.");

        try {
            // start() keeps accepting clients until the server is closed or an error stops it.
            server.start();
        } catch (IOException exception) {
            System.err.println("Server stopped: " + exception.getMessage());
        }
    }
}
