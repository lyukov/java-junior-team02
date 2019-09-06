package com.lapushki.chat.server.commands;

import com.lapushki.chat.server.Connection;
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
    public void execute() {
        connection.send("Chat history: ");
        List<String> historyList = history.getHistory();
        for (String mess : historyList) {
            connection.send(mess);
        }
    }
}
