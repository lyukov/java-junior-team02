package com.lapushki.chat.server;

import com.lapushki.chat.model.ResponseMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

class MessageParser {
    private static final MessageHandler messageHandler = new MessageHandler();

    void processMessage(Connection connection, Collection<Connection> connections, String message) {
        String[] mess = message.split(" ");
        switch (parseCommand(mess[0])) {
            case EXIT:
                messageHandler.handleExit(connection);
                break;
            case SEND:
                messageHandler.handleMessage(connection, connections, createResponseMessage(mess[1]));
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

    private ResponseMessage createResponseMessage(String message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return new ResponseMessage("OK", message, LocalDateTime.now().format(formatter));
    }

    private Command parseCommand(String message) {
        Command newCommand = Command.DEFAULT;
        for (Command command: Command.values()) {
            if (command.getMessage().equals(message))
                newCommand = command;
        }
        return newCommand;
    }
}
