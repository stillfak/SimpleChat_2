

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class ClientWindow extends JFrame implements ActionListener, TCPConnectionListener{
    private static final String IP_ADDRESS ="0.0.0.0";
    private static final int PORT = 8289;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;



    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMessage("Connection ready...");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String str) {
    printMessage(str);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
    printMessage("Connection close");
    }

    @Override
    public void onException(TCPConnection tcpConnection, IOException e) {
    printMessage("Connection exception " + e);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });
    }
    private final  JTextField textField = new JTextField("Гость");
    private final JTextArea log = new JTextArea();
    private final JTextField filedInput = new JTextField();

    private TCPConnection connection;

    public ClientWindow() throws HeadlessException {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
//        setAlwaysOnTop(true);

        add(textField,BorderLayout.NORTH);

        log.setEditable(false);
        log.setLineWrap(true);
        add(log, BorderLayout.CENTER);

        filedInput.addActionListener(this);
        add(filedInput, BorderLayout.SOUTH);

        setVisible(true);

        try{
            connection = new TCPConnection(this , IP_ADDRESS,PORT);
        } catch (IOException e){
            printMessage("Connection: " + e);
        }
    }

    private synchronized void printMessage(String s) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(s + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = filedInput.getText();
        if (msg.equals("")) return;
        filedInput.setText(null);
        connection.sendString(textField.getText() + ": " + msg);
    }
}
