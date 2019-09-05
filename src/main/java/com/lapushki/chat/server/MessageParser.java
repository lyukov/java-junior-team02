package com.lapushki.chat.server;

import java.util.Collection;

class MessageParser {
    private static final MessageHandler messageHandler = new MessageHandler();

    void processMessage(Connection connection, Collection<Connection> connections, String message) {
        String[] mess = message.split(" ");
        Command newCommand = Command.DEFAULT;
        for (Command command: Command.values()){
            if (command.getMessage().equals(mess[0]))
                newCommand = command;
        }
        switch (newCommand) {
            case EXIT:
                messageHandler.handleExit(connection);
                break;
            case SEND:
                messageHandler.handleMessage(connection, connections, mess[1]);
                break;
            case HIST:
                messageHandler.handleHistory(connection);
                break;
            case CHID:
                messageHandler.handleChid(connection);
                break;
            default:
                break;
        }
    }
}
