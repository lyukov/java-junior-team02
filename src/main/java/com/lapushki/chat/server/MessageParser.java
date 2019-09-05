package com.lapushki.chat.server;

import java.util.Collection;

class MessageParser {
    private static final MessageHandler messageHandler = new MessageHandler();

    void processMessage(Connection connection, Collection<Connection> connections, String message) {
        String[] mess = message.split(" ");
        switch (mess[0]) {
            case "/exit":
                messageHandler.handleExit(connection);
                break;
            case "/snd":
                messageHandler.handleMessage(connection, connections, mess[1]);
                break;
            case "/hist":
                messageHandler.handleHistory(connection, mess);
                break;
            case "/chid":
                messageHandler.handleChid(connection);
                break;
        }
    }
}
