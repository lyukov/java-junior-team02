package com.lapushki.chat.client;

import com.lapushki.chat.server.ConnectionListener;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client implements ConnectionListener {
    private static final int PORT = 5001;
    private static final String HOST = "localhost";

    private Client() {
        Scanner scan = new Scanner(System.in);
        try {
            String in = "";
            Socket socket = new Socket(HOST, PORT);
            Connection connection = new Connection(this, socket);
            while(!in.equals("exit")) {
                in = scan.nextLine();
                Message message = formMessageObject(in);
                connection.sendMessage(message);
            }
            connection.disconnect();
        }catch (IOException ex) {
            printMessage(ex.getMessage());
        }
    }

    private Message formMessageObject(String in) {
        String command = in.substring(0, in.indexOf(" "));
        String message = in.substring(in.indexOf(" ") + 1);
        return new Message(command, message);
    }

    @Override
    public void onReceivedMessage(String message) {
        printMessage(message);
    }

    @Override
    public void onDisconnect() {
        printMessage("Connection closed! Have a good day!");
    }

    @Override
    public void onException(Exception ex) {
        printMessage(ex.getMessage());

    }

    private void printMessage(String message) {
        System.out.println(message);
    }

    public static void main(String[] args) {
        Client client = new Client();
    }
}
