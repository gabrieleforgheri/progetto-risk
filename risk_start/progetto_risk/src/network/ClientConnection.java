package network;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

// Client-side socket wrapper used by the future UI/game code to talk to RiskServer.
public class ClientConnection implements Closeable {
    private final Socket socket;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;
    private final MessageListener listener;
    private volatile boolean running;
    private String nickName;

    public ClientConnection(String host, int port, MessageListener listener) throws IOException {
        // ObjectOutputStream is flushed before ObjectInputStream so both sides exchange stream headers.
        this.socket = new Socket(host, port);
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.output.flush();
        this.input = new ObjectInputStream(socket.getInputStream());
        this.listener = listener;
        this.running = true;
    }

    public void connect(String nickName) throws IOException {
        // Store the nickname once, then reuse it as the sender for every game action.
        this.nickName = nickName;
        send(GameMessage.join(nickName));

        // Reader runs in the background so the client can keep sending actions.
        Thread readerThread = new Thread(this::readMessages, "risk-client-reader");
        readerThread.setDaemon(true);
        readerThread.start();
    }

    public void sendChat(String text) throws IOException {
        send(GameMessage.chat(nickName, text));
    }

    public void sendChooseColor(String color) throws IOException {
        send(GameMessage.chooseColor(nickName, color));
    }

    public void sendAttack(String fromTerritory, String toTerritory, int armies) throws IOException {
        send(GameMessage.attack(nickName, fromTerritory, toTerritory, armies));
    }

    public void sendReinforcement(String territory, int armies) throws IOException {
        send(GameMessage.reinforcement(nickName, territory, armies));
    }

    public void sendArmyMovement(String fromTerritory, String toTerritory, int armies) throws IOException {
        send(GameMessage.armyMovement(nickName, fromTerritory, toTerritory, armies));
    }

    public void sendEndPhase() throws IOException {
        send(GameMessage.endPhase(nickName));
    }

    public void sendTradeTerritoryCards(String cardIds) throws IOException {
        send(GameMessage.tradeTerritoryCards(nickName, cardIds));
    }

    public void sendLeave() throws IOException {
        send(GameMessage.leave(nickName));
    }

    public void send(GameMessage message) throws IOException {
        // Synchronization prevents mixed bytes if multiple UI/game threads send at the same time.
        synchronized (output) {
            output.writeObject(message);
            output.flush();
            // reset() avoids ObjectOutputStream reusing old object references across messages.
            output.reset();
        }
    }

    private void readMessages() {
        Exception failure = null;
        try {
            // All valid server updates are forwarded to the listener.
            while (running) {
                Object object = input.readObject();
                if (object instanceof GameMessage) {
                    listener.onMessage((GameMessage) object);
                }
            }
        } catch (EOFException exception) {
            failure = exception;
        } catch (IOException exception) {
            if (running) {
                failure = exception;
            }
        } catch (ClassNotFoundException exception) {
            failure = exception;
        } finally {
            running = false;
            // Always notify the caller, even when the socket closes normally.
            listener.onConnectionClosed(failure);
            closeQuietly();
        }
    }

    @Override
    public void close() throws IOException {
        running = false;
        socket.close();
    }

    private void closeQuietly() {
        try {
            close();
        } catch (IOException ignored) {
        }
    }
}
