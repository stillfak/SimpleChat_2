import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static com.sun.javafx.fxml.expression.Expression.add;

public class ClientWindows_v2 extends Application implements TCPConnectionListener {
    private static final String IP_ADDRESS = "0.0.0.0";
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

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane root = new GridPane();

        add(textField, BorderLayout.NORTH);

        log.setEditable(false);
        log.setLineWrap(true);
        add(log, BorderLayout.CENTER);

//        filedInput.addActionListener(this);
        add(filedInput, BorderLayout.SOUTH);

        String msg = filedInput.getText();
        if (msg.equals("")) return;
        filedInput.setText(null);
        connection.sendString(textField.getText() + ": " + msg);

        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.setResizable(false); // block the window size change

        primaryStage.show();

    }

    private final JTextField textField = new JTextField("Гость");
    private final JTextArea log = new JTextArea();
    private final JTextField filedInput = new JTextField();
    private TCPConnection connection;

    public ClientWindows_v2(String[] args) {
//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        setSize(WIDTH, HEIGHT);
//        setLocationRelativeTo(null);
//        setAlwaysOnTop(true);


//        setVisible(true);
        launch(args);
        try {
            connection = new TCPConnection(this, IP_ADDRESS, PORT);
        } catch (IOException e) {
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


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindows_v2(args);
            }
        });
    }
}