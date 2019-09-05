package com.lapushki.chat.client;

import com.lapushki.chat.server.Connection;
import com.lapushki.chat.server.ConnectionListener;

import java.io.IOException;
import java.util.Scanner;

public class Client implements ConnectionListener {

    private static final String HOST = "localhost";
    private static final int PORT = 48884;
    private Connection connection;
    private Scanner scan;

    private Client(Scanner scan) {
        this.scan = scan;
    }

    private void start() {
        try {
            connection = new Connection(this, HOST, PORT);
            connection.init();
            while (true) {
                String msg = scan.nextLine();
                if (validateInput(msg)) {
                    connection.sendMessage(msg);
                }
            }
        } catch (IOException ex) {
            printMessage("Connection exception: " + ex);
        } finally {
            connection.disconnect();
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
        if (msg.contains("/hist") || msg.contains("/exit")) {
            return true;
        }
        if (!msg.contains("/chid") && !msg.contains("/snd")) {
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
        printMessage("Hello and welcome to the best chat ever! To quit the chat type \"/exit\"");
    }

    @Override
    public void onReceiveString(Connection connection, String message) {
        if(message == null){
            connection.disconnect();
            //todo: add a shutdown here
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
