package com.lapushki.chat.server;

import com.lapushki.chat.Common.ChatLogger;
import com.lapushki.chat.server.history.roomed.RoomedFileSwitchingHistoryAccessObject;
import com.lapushki.chat.server.history.roomed.RoomedHistory;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerFramework {
    public static void main(String[] args) throws IOException {
        Parser parser = new Parser("\0", ":");
        Logger logger = ChatLogger.createLogger("ServerLogger", "server.log");
        Identificator identificator = new Identificator();
        RoomStore roomStore = new ChatRoomStore();
        ConnectionPool connectionPool = new ConnectionPool();
        RoomedHistory history = new RoomedFileSwitchingHistoryAccessObject();
        CommandFactory commandFactory = new ChatCommandFactory(parser, roomStore, identificator, history);
        ConnectionFactory connectionFactory = new ChatConnectionFactory(commandFactory);
        logger.log(Level.INFO, Decorator.startServerMess());
        new Server(roomStore, connectionFactory, connectionPool, logger).startServer();
    }
}
