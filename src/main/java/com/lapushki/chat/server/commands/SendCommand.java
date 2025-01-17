package com.lapushki.chat.server.commands;

import com.lapushki.chat.server.Connection;
import com.lapushki.chat.server.Decorator;
import com.lapushki.chat.server.Room;
import com.lapushki.chat.server.exceptions.ChatException;
import com.lapushki.chat.server.exceptions.NotInTheRoomException;
import com.lapushki.chat.server.exceptions.UnidentifiedUserException;

import com.lapushki.chat.server.history.roomed.RoomedHistory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SendCommand implements Command {
    private final Connection connection;
    private final String message;
    private final LocalDateTime timestamp;
    private final RoomedHistory history;

    public SendCommand(Connection connection, String message, LocalDateTime timestamp, RoomedHistory history) {
        this.connection = connection;
        this.message = message;
        this.timestamp = timestamp;
        this.history = history;
    }

    @Override
    public void execute() throws ChatException, IOException {
        checkUsername();
        String decoratedMessage = Decorator.decorate(message,timestamp,connection.getUsername());
        Room room = connection.getRoom();
        if (room == null) {
            throw new NotInTheRoomException();
        }
        room.sendToAll(decoratedMessage);
        history.save(decoratedMessage, timestamp, room.getTitle());
    }

    private void checkUsername() throws UnidentifiedUserException {
        String nickname = connection.getUsername();
        if (nickname == null) {
            throw new UnidentifiedUserException();
        }
    }
}
