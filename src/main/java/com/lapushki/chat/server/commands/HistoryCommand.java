package com.lapushki.chat.server.commands;

import com.lapushki.chat.server.Connection;
import com.lapushki.chat.server.Decorator;
import com.lapushki.chat.server.Room;
import com.lapushki.chat.server.exceptions.NotInTheRoomException;
import com.lapushki.chat.server.history.HistoryAccessObject;

import java.util.List;

public class HistoryCommand implements Command {
    private final Connection connection;
    private final HistoryAccessObject history;

    public HistoryCommand(Connection connection, HistoryAccessObject history) {
        this.connection = connection;
        this.history = history;
    }

    @Override
    public void execute() throws NotInTheRoomException {
        Room room = connection.getRoom();
        if (room == null) {
            throw new NotInTheRoomException();
        }
        connection.send(Decorator.getchatHistoryStartMessage());
        List<String> historyList = history.getHistory(room.getTitle());
        for (String mess : historyList) {
            connection.send(mess);
        }
        connection.send(Decorator.getchatHistoryEndMessage());
    }
}
