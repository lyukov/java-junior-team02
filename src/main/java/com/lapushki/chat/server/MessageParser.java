package com.lapushki.chat.server;

import com.lapushki.chat.model.Constants;
import com.lapushki.chat.model.RequestMessage;

import java.util.Collection;

class MessageParser {
    private static final MessageHandler messageHandler = new MessageHandler();

    void processMessage(Connection connection, Collection<Connection> connections, RequestMessage message) {
        switch (message.command) {
            case Constants.EXIT:
                messageHandler.handleExit(connection);
                break;
            case Constants.SND:
                messageHandler.handleMessage(connection, connections, message);
                break;
            case Constants.HIST:
                messageHandler.handleHistory(connection);
                break;
            case Constants.CHID:
                messageHandler.handleChid(connection, message.message);
                break;
            default:
                //todo throw exception or response to client with an error
                break;
        }
    }
}
