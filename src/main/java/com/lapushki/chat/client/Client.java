package com.lapushki.chat.client;

import com.lapushki.chat.model.Constants;
import com.lapushki.chat.model.RequestMessage;
import com.lapushki.chat.model.RequestMessage;
import com.lapushki.chat.server.Connection;
import com.lapushki.chat.server.ConnectionListener;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Client implements ConnectionListener {
    private static final Pattern REGEX_PATTERN =
            Pattern.compile("(^\\/(snd|chid)\\s[A-z|0-9|А-я]+)|^(\\/(hist|exit))");
    private static final int MIN_LENGTH_MESSAGE = 4;
    private static final int MAX_LENGTH_MESSAGE = 150;
    private static final String HOST = "localhost";
    private static final int PORT = 48884;
    private Connection connection;
    private Scanner scan;
    private String userMessage;
    private String name;
    private boolean kostilForNicknameSetting = false;
    private  boolean answered = true;

    private Client(Scanner scan) {
        this.scan = scan;
        this.userMessage = "";
    }

    private void start() {
        try {
            connection = new Connection(this, HOST, PORT);
            connection.init();

            printMessage("Choose unique nickname for session");
            String nick = "";
            while (!kostilForNicknameSetting) {
                if (answered && !kostilForNicknameSetting) {
                    answered = false;
                    nick = scan.nextLine();
                    setNickname(nick);
                    try {
                        Thread.sleep(1000);
                    } catch(InterruptedException ex) {}
                }
            }
            name = nick.replaceAll("/chid ", "");

            while (true) {
                userMessage = scan.nextLine();
                if (validateInput(userMessage)) {
                    try {
                        connection.sendMessage(new RequestMessage(userMessage));
                    } catch (IllegalArgumentException ex) {
                        printMessage("Message exception: " + ex);
                    }
                } else
                    printMessage("Incorrect!\nMin 4 and max 150 symbols!\nAvailable command:\n\"/snd [message]\"\n\"/chid [message]\"\n\"/hist\"\n\"/exit\"");
            }
        } catch (IOException ex) {
            printMessage("Connection exception: " + ex);
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    private void setNickname(String name) {
        if(!name.contains("/chid")) {
            printMessage("to set nickname start with /chid");
        }
        else {
            connection.sendMessage(name);
        }
    }

    private boolean validateInput(String msg) {
        return msg != null && msg.length() >= MIN_LENGTH_MESSAGE && msg.length() <= MAX_LENGTH_MESSAGE && REGEX_PATTERN.matcher(msg).find();
    }

    @Override
    public void onConnectionReady(Connection connection) {
        printMessage("Hello and welcome to the best chat ever! To quit the chat type \"/exit\"");
    }

    @Override
    public void onReceiveString(Connection connection, String message) {
        if (message == null) {
            if (!userMessage.equals(Constants.EXIT)) {
                printMessage("Server is down, try again later!");
            }
            System.exit(0);
            return;
        }
        if(Objects.equals(message, "server code 1234567")){
            System.out.println("name changed");
            kostilForNicknameSetting = true;
            return;
        }
        answered = true;
        printMessage("\r" + message); //todo change message format, do not need to show all the info
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
