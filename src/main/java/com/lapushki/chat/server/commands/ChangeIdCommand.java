package com.lapushki.chat.server.commands;

import com.lapushki.chat.server.Identificator;
import com.lapushki.chat.server.Connection;
import com.lapushki.chat.server.Room;
import com.lapushki.chat.server.exceptions.OccupiedNicknameException;
import com.lapushki.chat.server.Saver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChangeIdCommand implements Command {
    private final Connection connection;
    private final Identificator identificator;
    private final String newNickname;
    private final LocalDateTime timestamp;
    private final Saver saver;

    public ChangeIdCommand(Connection connection, Identificator identificator, String newNickname,
                           LocalDateTime timestamp, Saver saver) {
        this.connection = connection;
        this.identificator = identificator;
        this.newNickname = newNickname;
        this.timestamp = timestamp;
        this.saver = saver;
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
            message = newNickname + " joined the chat";
        } else {
            message = oldNickname + " has changed name to " + newNickname;
        }
        String decoratedMessage = "[" + timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "] " + message;
        Room room = connection.getRoom();
        room.sendToAll(decoratedMessage);
        saver.save(decoratedMessage, timestamp);
    }
}
