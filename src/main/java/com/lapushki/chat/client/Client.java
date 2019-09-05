package com.lapushki.chat.client;

import com.lapushki.chat.server.Connection;
import com.lapushki.chat.server.ConnectionListener;

import java.io.IOException;
import java.util.Scanner;

public class Client implements ConnectionListener {

    private static final String HOST = "localhost";
    private static final int PORT = 8081;

    private Client() {
        Connection connection;
        Scanner scan = new Scanner(System.in);
        try {
            String msg = "";
            connection = new Connection(this, HOST, PORT);
            while (!msg.equals("\\exit")) {
                msg = scan.nextLine();
                if (validateInput(msg)) {
                    connection.sendMessage(msg);
                }
            }
            connection.disconnect();
        } catch (IOException ex) {
            printMessage("Connection exception: " + ex);
        }
    }

    private boolean validateInput(String msg) {
        if (msg.length() == 0 ||
                (msg.contains("/snd") && !msg.trim().contains(" ")) ||
                (msg.contains("/chid") && !msg.trim().contains(" "))
        ) {
            printMessage("Message is empty");
            return false;
        }
        if (msg.contains("/hist")) {
            return true;
        }
        if (!msg.contains("/chid") & !msg.contains("/snd")){
            printMessage("Unknown command");
            return false;
        }
        if (msg.substring(msg.indexOf(" ") + 1).length() > 150) {
            printMessage("Message is too big");
            return false;
        }
        return true;
    }

    @Override
    public void onConnectionReady(Connection connection) {
        printMessage("Connection opened");
    }

    @Override
    public void onReceiveString(Connection connection, String message) {
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
        new Client();
    }
}