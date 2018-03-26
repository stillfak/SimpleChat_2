import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ServerChat implements TCPConnectionListener{
    public static void main(String[] args) {
        new ServerChat();
    }

    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    private ServerChat() {
        System.out.println("Server running");


        try (ServerSocket serverSocket = new ServerSocket(8289)) {
            System.out.println(serverSocket.getInetAddress());
            while (true) {

                new TCPConnection(this, serverSocket.accept());

            }
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToAllConnections("Client connected: " + tcpConnection);
    }

    private void sendToAllConnections(String value) {
        System.out.println(value);
        for (TCPConnection connection: connections) {
            connection.sendString(value);

        }
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String str) {
        sendToAllConnections(str);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendToAllConnections("Client connected: " + tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, IOException e) {
        System.out.println("TCPConnection exception: " + e);
    }
}
