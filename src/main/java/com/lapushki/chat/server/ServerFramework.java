package com.lapushki.chat.server;

import com.lapushki.chat.server.history.HistoryAccessObject;

import java.io.IOException;

public class ServerFramework {
    public static void main(String[] args) throws IOException {
        Parser parser = new Parser("\0", ":");
        Identificator identificator = new Identificator();
        RoomStore roomStore = new ChatRoomStore();
        ConnectionPool connectionPool = new ConnectionPool();
        HistoryAccessObject history = new HistoryAccessObject();
        CommandFactory commandFactory = new ChatCommandFactory(parser, roomStore, identificator, history);
        ConnectionFactory connectionFactory = new ChatConnectionFactory(commandFactory);
        System.out.println("Server starts");
        new Server(roomStore, connectionFactory, connectionPool).startServer();
    }
}
