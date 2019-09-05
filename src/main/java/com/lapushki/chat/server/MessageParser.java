package com.lapushki.chat.server;

import com.lapushki.chat.model.RequestMessage;

import java.util.Collection;

class MessageParser {
    private static final MessageHandler messageHandler = new MessageHandler();

    void processMessage(Connection connection, Collection<Connection> connections, RequestMessage message) {
        switch (message.command) {
            //TODO use constants
            case "/exit":
                messageHandler.handleExit(connection);
                break;
            case "/snd":
                messageHandler.handleMessage(connection, connections, message);
                break;
            case "/hist":
                messageHandler.handleHistory(connection);
                break;
            case "/chid":
                messageHandler.handleChid(connection);
                break;
            default:
                //TODO throw exection or responce to client with an error.
                break;
        }
    }

    //TODO don't neet this if use constants
    Command parseCommand(String message) {
        Command newCommand = Command.DEFAULT;
        for (Command command: Command.values()) {
            if (command.getMessage().equals(message))
                newCommand = command;
        }
        return newCommand;
    }
}
