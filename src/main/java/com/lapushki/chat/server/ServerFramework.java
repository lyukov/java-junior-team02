package com.lapushki.chat.server;

import com.lapushki.chat.server.history.roomed.RoomedFileSwitchingHistoryAccessObject;
import com.lapushki.chat.server.history.roomed.RoomedHistory;

import java.io.IOException;

public class ServerFramework {
    public static void main(String[] args) throws IOException {
        Parser parser = new Parser("\0", ":");
        Identificator identificator = new Identificator();
        Room room = new ChatRoom(title);
        //HistoryAccessObject history = new HistoryAccessObject();
        RoomedHistory history = new RoomedFileSwitchingHistoryAccessObject();

        CommandFactory commandFactory = new ChatCommandFactory(parser, room, identificator, history);
        ConnectionFactory connectionFactory = new ChatConnectionFactory(commandFactory);
        System.out.println("Server starts");
        new Server(connectionFactory, room).startServer();
    }
}
