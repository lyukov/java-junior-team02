package com.lapushki.chat.server.commands;

import com.lapushki.chat.server.Connection;
import com.lapushki.chat.server.Room;
import com.lapushki.chat.server.exceptions.ChatException;
import com.lapushki.chat.server.exceptions.NotInTheRoomException;
import com.lapushki.chat.server.exceptions.UnidentifiedUserException;
import com.lapushki.chat.server.history.History;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SendCommand implements Command {
    private final Connection connection;
    private final String message;
    private final LocalDateTime timestamp;
    private final History history;

    public SendCommand(Connection connection, String message, LocalDateTime timestamp, History history) {
        this.connection = connection;
        this.message = message;
        this.timestamp = timestamp;
        this.history = history;
    }

    @Override
    public void execute() throws ChatException, IOException {
        checkUsername();
        String decoratedMessage = decorate(message);
        Room room = connection.getRoom();
        if (room == null) {
            throw new NotInTheRoomException();
        }
        room.sendToAll(decoratedMessage);
        history.save(decoratedMessage, timestamp);
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
