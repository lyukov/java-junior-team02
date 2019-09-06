package com.lapushki.chat.server;

import com.lapushki.chat.server.history.HistoryAccessObject;
import com.lapushki.chat.server.history.saver.Saver;
import com.lapushki.chat.server.history.saver.SwitchingFileSaver;

import java.io.IOException;

public class ServerFramework {
    public static void main(String[] args) throws IOException {
        Parser parser = new Parser("\0", ":");
        Identificator identificator = new Identificator();
        Room room = new ChatRoom(title);
        HistoryAccessObject history = new HistoryAccessObject();
        CommandFactory commandFactory = new ChatCommandFactory(parser, room, identificator, history);
        ConnectionFactory connectionFactory = new ChatConnectionFactory(commandFactory);
        System.out.println("Server starts");
        new Server(connectionFactory, room).startServer();
    }
}
