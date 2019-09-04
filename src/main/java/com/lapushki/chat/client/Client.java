package com.lapushki.chat.client;

import com.lapushki.chat.model.RequestMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final int PORT = 8080;
    private static final String HOST = "localhost";
    private static Connection connection;

    private Client() {
        try {
            Scanner scan = new Scanner(System.in);
            String in = "";
            Socket socket = new Socket(HOST, PORT);
            connection = new Connection(socket);
            in = scan.nextLine();
            while (!in.equals("\\exit")) {
                RequestMessage message = connection.formMessageObject(in);
                connection.sendMessage(message);
                in = scan.nextLine();
            }
        } catch (IOException ex) {
            connection.printMessage(ex.getMessage());
        } finally {
            connection.disconnect();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
    }
}
