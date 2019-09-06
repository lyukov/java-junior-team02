package com.lapushki.chat.server.commands;

import com.lapushki.chat.server.Session;
import com.lapushki.chat.server.history.HistoryAccessObject;

import java.io.IOException;
import java.util.List;

public class HistoryCommand implements Command {
    private final Session session;
    private final HistoryAccessObject history;


    public HistoryCommand(Session session, HistoryAccessObject history) {
        this.session = session;
        this.history = history;
    }

    @Override
    public void execute() {
        session.send("Chat history: ");
        List<String> historyList = history.getHistory();
        for (String mess : historyList) {
            session.send(mess);
        }
    }
}
