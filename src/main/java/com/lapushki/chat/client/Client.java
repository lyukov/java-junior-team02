package com.lapushki.chat.client;

import com.lapushki.chat.server.Connection;
import com.lapushki.chat.server.ConnectionListener;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client implements ConnectionListener {

    private static final Pattern REGEX_PATTERN = Pattern.compile("(^\\/(snd|chid)\\s+[A-z|0-9|А-я]+)|^(\\/(hist|exit))");
    private static final String INCORRECT_MESSAGE = "Incorrect!\nMin 4 and max 150 symbols!\nAvailable command:\n\"/snd [message]\"\n\"/chid [message]\"\n\"/hist\"\n\"/exit\"";
    private static final int MIN_LENGTH_MESSAGE = 4;
    private static final int MAX_LENGTH_MESSAGE = 150;
    private static final String HOST = "localhost";
    private static final int PORT = 48884;
    private Connection connection;
    private Scanner scanner;

    private Client(Scanner scanner) { this.scanner = scanner; }

    private void start() {
        try {
            connection = new Connection(this, HOST, PORT);
            connection.init();
            while (true) {
                String msg = scanner.nextLine();
                if (validateInput(msg)) {
                    connection.sendMessage(msg);
                }
                else {
                    printMessage(INCORRECT_MESSAGE);
                }
            }
        } catch (IOException ex) {
            printMessage("Connection exception: " + ex);
        } finally {
            if (connection!=null) connection.disconnect();
        }
    }

    private boolean validateInput(String msg) {
        return msg.length() >= MIN_LENGTH_MESSAGE && msg.length() <= MAX_LENGTH_MESSAGE && REGEX_PATTERN.matcher(msg).find();
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
