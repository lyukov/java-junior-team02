package com.lapushki.chat.server;

import com.lapushki.chat.server.history.HistoryAccessObject;
import com.lapushki.chat.server.history.saver.Saver;
import com.lapushki.chat.server.history.saver.SwitchingFileSaver;

import java.io.IOException;

public class ServerLauncher {
    public static void main(String[] args) throws IOException {
        Parser parser = new Parser("\0", ":");
        Saver saver = new SwitchingFileSaver();
        Identificator identificator = new Identificator();
        SessionStore sessionStore = new ChatSessionStore();
        HistoryAccessObject history = new HistoryAccessObject();
        CommandFactory commandFactory = new ChatCommandFactory(parser, sessionStore, saver, identificator, history);
        SessionFactory sessionFactory = new ChatSessionFactory(commandFactory);
        System.out.println("Server starts");
        new Server(sessionFactory, sessionStore).startServer();
    }
}
