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
        Group group = new ChatGroup();
        HistoryAccessObject history = new HistoryAccessObject();
        CommandFactory commandFactory = new ChatCommandFactory(parser, group, saver, identificator, history);
        ConnectionFactory connectionFactory = new ChatConnectionFactory(commandFactory);
        System.out.println("Server starts");
        new Server(connectionFactory, group).startServer();
    }
}
