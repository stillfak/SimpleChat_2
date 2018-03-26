import java.io.IOException;

public interface TCPConnectionListener {
    void onConnectionReady(TCPConnection tcpConnection);

    void onReceiveString(TCPConnection tcpConnection,String str);

    void onDisconnect(TCPConnection tcpConnection);

    void onException(TCPConnection tcpConnection, IOException e);
}
