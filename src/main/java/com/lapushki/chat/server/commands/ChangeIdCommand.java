package com.lapushki.chat.server.commands;

import com.lapushki.chat.server.Decorator;
import com.lapushki.chat.server.Identificator;
import com.lapushki.chat.server.Connection;
import com.lapushki.chat.server.Room;
import com.lapushki.chat.server.exceptions.OccupiedNicknameException;
import com.lapushki.chat.server.history.roomed.RoomedHistory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChangeIdCommand implements Command {
    private final Connection connection;
    private final Identificator identificator;
    private final String newNickname;
    private final LocalDateTime timestamp;
    private final RoomedHistory history;

    public ChangeIdCommand(Connection connection,
                           Identificator identificator,
                           String newNickname,
                           LocalDateTime timestamp,
                           RoomedHistory history) {
        this.connection = connection;
        this.identificator = identificator;
        this.newNickname = newNickname;
        this.timestamp = timestamp;
        this.history = history;
    }

    @Override
    public void execute() throws OccupiedNicknameException, IOException {
        String oldNickname = connection.getUsername();
        identificator.changeNickname(oldNickname, newNickname);
        connection.setUsername(newNickname);
        sendChangedNicknameMessage(oldNickname, newNickname);
    }

    private void sendChangedNicknameMessage(String oldNickname, String newNickname) throws IOException {
        String message = "";
        if (oldNickname == null) {
            message = Decorator.joinMessage(newNickname);
        } else {
            message = Decorator.changeNameMessage(oldNickname, newNickname);
        }
        String decoratedMessage = Decorator.decorate(message, timestamp);
        Room room = connection.getRoom();
        room.sendToAll(decoratedMessage);
        history.save(decoratedMessage, timestamp, room.getTitle());
    }
}
