package client.util;

/**
 * Parsing del formato richiesto {@code IP:PORTA} (es. {@code 192.168.1.10:5555} o {@code localhost:5555}).
 */
public record ConnectionAddress(String host, int port) {

    public static ConnectionAddress parse(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            throw new IllegalArgumentException("Inserisci un indirizzo nel formato IP:PORTA.");
        }

        String value = raw.trim();
        int colon = value.lastIndexOf(':');
        if (colon <= 0 || colon == value.length() - 1) {
            throw new IllegalArgumentException("Formato non valido. Usa IP:PORTA (es. 127.0.0.1:5555).");
        }

        String host = value.substring(0, colon).trim();
        String portText = value.substring(colon + 1).trim();
        if (host.isEmpty()) {
            throw new IllegalArgumentException("Host mancante.");
        }

        int port;
        try {
            port = Integer.parseInt(portText);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Porta non valida: " + portText);
        }

        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("Porta fuori intervallo (1-65535).");
        }

        return new ConnectionAddress(host, port);
    }
}
