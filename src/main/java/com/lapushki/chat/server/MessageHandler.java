package com.lapushki.chat.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class MessageHandler {
    private static final DaoDumomi dao = new DaoDumomi();
    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    void handleChid(Connection connection) {
        //todo
    }

    void handleExit(Connection connection) {
        log.info("Client disconnected: " + connection.toString());
        connection.sendMessage("Goodbye!");
        connection.disconnect();
    }

    void handleHistory(Connection connection, String[] messageArray) {
        int pageNum = 0;
        if (messageArray.length == 2){
            pageNum = Integer.parseInt(messageArray[1]);
        }
        //todo
        //connection.sendMessage(dao.getHistory());
    }

    void handleMessage(Connection connection, Collection<Connection> connections, String message) {
        if (dao.saveDataBase(connection, message))
            sendMessageAllClients(message, connections);
    }

    void sendMessageAllClients(String msg, Collection<Connection> connections) {
        for (Connection connection : connections) {
            connection.sendMessage(connection.getSocket().getInetAddress() + ": " + msg);
        }
    }
}
