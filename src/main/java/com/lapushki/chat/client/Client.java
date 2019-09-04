package com.lapushki.chat.client;

import com.lapushki.chat.model.RequestMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final int PORT = 8080;
    private static final String HOST = "localhost";
    private Connection connection;
    private Scanner scanner;
    private Socket socket;

    private Client(Scanner scanner, Connection connection) {
        this.scanner = scanner;
        this.connection = connection;
    }

    private void run() {
        try {
            String in = scanner.nextLine();
            while (!in.equals("\\exit")) {
                RequestMessage message = connection.formMessageObject(in);
                connection.sendMessage(message);
                in = scanner.nextLine();
            }
        } finally {
            connection.disconnect();
        }
    }

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(HOST, PORT);
            Client client = new Client(new Scanner(System.in), new Connection(socket));
            client.run();
        }
        catch (IOException ex) {
            System.out.println("Couldn't connect to server.");
        }
    }
}
