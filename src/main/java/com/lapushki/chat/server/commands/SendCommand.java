package com.lapushki.chat.server.commands;

import com.lapushki.chat.server.Connection;
import com.lapushki.chat.server.Room;
import com.lapushki.chat.server.exceptions.UnidentifiedUserException;
import com.lapushki.chat.server.Saver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SendCommand implements Command {
    private final Connection connection;
    private final String message;
    private final Saver saver;
    private final LocalDateTime timestamp;

    public SendCommand(Connection connection, String message, Saver saver, LocalDateTime timestamp) {
        this.connection = connection;
        this.message = message;
        this.saver = saver;
        this.timestamp = timestamp;
    }

    @Override
    public void execute() throws UnidentifiedUserException, IOException {
        checkUsername();
        String decoratedMessage = decorate(message);
        Room room = connection.getRoom();
        room.sendToAll(decoratedMessage);
        saver.save(decoratedMessage, timestamp);
    }

    private String decorate(String message) {
        return "[" + timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "] " +
                connection.getUsername() + ": " +
                message;
    }

    private void checkUsername() throws UnidentifiedUserException {
        String nickname = connection.getUsername();
        if (nickname == null) {
            throw new UnidentifiedUserException();
        }
    }
}
