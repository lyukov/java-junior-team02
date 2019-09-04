package com.lapushki.server;

import com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage;

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
            while (!in.equals("exit")) {
                in = scan.nextLine();
                RequestMessage message = connection.formMessageObject(in);
                connection.sendMessage(new ResponseMessage(message));
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
