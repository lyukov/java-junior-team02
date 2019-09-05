package com.lapushki.chat.server;

import org.apache.log4j.Logger;

import java.util.Collection;

public class MessageHandler {
    private static final Logger log = Logger.getLogger(MessageHandler.class);

    void handleChid(Connection connection) {
        //todo
    }

    void handleExit(Connection connection) {
        log.info("Client disconnected: " + connection.toString());
        connection.disconnect();
    }

    void handleHistory(Connection connection) {
        //todo
        //connection.sendMessage(dao.getHistory());
    }

    void handleMessage(Connection connection, Collection<Connection> connections, String message) {
        //todo
        //if (dao.saveDataBase(connection, message))
            sendMessageAllClients(message, connections);
    }

    void sendMessageAllClients(String msg, Collection<Connection> connections) {
        for (Connection connection : connections) {
            connection.sendMessage(connection.getSocket().getInetAddress() + ": " + msg);
        }
    }
}