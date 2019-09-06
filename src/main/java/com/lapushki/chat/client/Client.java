package com.lapushki.chat.client;

import com.google.gson.Gson;
import com.lapushki.chat.model.Constants;
import com.lapushki.chat.model.Message;
import com.lapushki.chat.model.RequestMessage;
import com.lapushki.chat.model.ResponseMessage;
import com.lapushki.chat.server.Connection;
import com.lapushki.chat.server.ConnectionListener;

import java.io.IOException;
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
    private boolean nameChosen = false;

    private Client(Scanner scan) {
        this.scan = scan;
        this.userMessage = "";
    }

    private void start() {
        try {
            connection = new Connection(this, HOST, PORT);
            connection.init();
            askForInput();
        } catch (IOException ex) {
            printMessage("Connection exception: " + ex);
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    private void askForInput(){
        boolean firstChangeId = false;
        while (true) {
            while (!nameChosen) {
                userMessage = scan.nextLine();
                if(nameChosen){
                    firstChangeId = true;
                    break;
                }
                if(!userMessage.contains("/chid")) {
                    printMessage("to set nickname start with /chid");
                }
                else {
                    connection.sendMessage(new RequestMessage(userMessage));
                }
            }
            if(!firstChangeId) {
                userMessage = scan.nextLine();
            }
            firstChangeId = false;
            if (validateInput(userMessage)) {
                try {
                    connection.sendMessage(new RequestMessage(userMessage));
                } catch (IllegalArgumentException ex) {
                    printMessage("Message exception: " + ex);
                }
            } else
                printMessage("Incorrect!\nMin 4 and max 150 symbols!\nAvailable command:\n\"/snd [message]\"\n\"/chid [message]\"\n\"/hist\"\n\"/exit\"");
        }
    }

    private boolean validateInput(String msg) {
        return msg != null && msg.length() >= MIN_LENGTH_MESSAGE &&
                msg.length() <= MAX_LENGTH_MESSAGE && REGEX_PATTERN.matcher(msg).find();
    }

    @Override
    public void onConnectionReady(Connection connection) {
        printMessage("Hello and welcome to the best chat ever! Choose unique nickname for session. To quit the chat type \"/exit\"");
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
        ResponseMessage responseMessage = new Gson().fromJson(message, ResponseMessage.class);
        if(!nameChosen &&
                responseMessage.status.equals(Message.STATUS_OK) &&
                responseMessage.message.contains("nickname")
        ){
            nameChosen = true;
        }
        printMessage("\r" + new Gson().fromJson(message, ResponseMessage.class));
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
