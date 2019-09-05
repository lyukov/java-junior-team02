package com.lapushki.chat.client;

import com.lapushki.chat.model.RequestMessage;
import com.lapushki.chat.server.Connection;
import com.lapushki.chat.server.ConnectionListener;

import java.io.IOException;
import java.util.Scanner;

public class Client implements ConnectionListener {
    private static final String HOST = "localhost";
    private static final int PORT = 48884;
    private Connection connection;
    private Scanner scan;
    private String userMessage;

    private Client(Scanner scan) {
        this.scan = scan;
        this.userMessage = "";
    }

    private void start() {
        try {
            connection = new Connection(this, HOST, PORT);
            connection.init();
            while (true) {
                userMessage = scan.nextLine();
                String msg = scan.nextLine();
                //TODO catch exception form creating requestMessage
                connection.sendMessage(new RequestMessage(userMessage));
            }
        } catch (IOException ex) {
            printMessage("Connection exception: " + ex);
        } finally {
            connection.disconnect();
        }
    }

    @Override
    public void onConnectionReady(Connection connection) {
        printMessage("Hello and welcome to the best chat ever! To quit the chat type \"/exit\"");
    }

    @Override
    public void onReceiveString(Connection connection, String message) {
        if (message == null) {
            if(!userMessage.equals("/exit")) {
                printMessage("Server is down, try again later!");
            }
            connection.disconnect();
            System.exit(0);
            return;
        }
        printMessage(message);
    }

    @Override
    public synchronized void onDisconnect(Connection connection) {
        printMessage("Connection closed");
    }

    @Override
    public synchronized void onException(Connection connection, Exception ex) {
        printMessage("Connection exception: " + ex);
    }

    private void printMessage(String msg) {
        System.out.println(msg);
    }

    public static void main(String[] args) {
        Client client = new Client(new Scanner(System.in));
        client.start();
    }
}
