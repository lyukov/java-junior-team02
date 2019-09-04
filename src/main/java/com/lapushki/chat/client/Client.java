package com.lapushki.chat.client;

import com.lapushki.chat.server.Connection;
import com.lapushki.chat.server.ConnectionListener;

import java.io.IOException;
import java.util.Scanner;

public class Client implements ConnectionListener {
    private static final String HOST = "localhost";
    private static final int PORT = 8081;
    private Connection connection;

    Client() {
        Scanner scan = new Scanner(System.in);
        try {
            String msg = "";
            connection = new Connection(this, HOST, PORT);
            while(!msg.equals("exit")) {
                msg = scan.nextLine();
                connection.sendMessage(msg);
            }
            connection.disconnect();
        }catch (IOException ex) {
            printMessage("Connection exception: "+ex);
        }
    }

    @Override
    public synchronized void onConnectionReady(Connection connection) {
        printMessage("Connection opened");
    }

    @Override
    public synchronized void onReceiveString(Connection connection, String message) {
        printMessage(message);
    }

    @Override
    public synchronized void onDisconnect(Connection connection) {
        printMessage("Connection closed");
    }

    @Override
    public synchronized void onException(Connection connection, Exception ex) {
        printMessage("Connection exception: "+ex);
    }

    private synchronized void printMessage(String msg) {
        System.out.println(msg);
    }

    public static void main(String[] args) {
        new Client();
    }
}