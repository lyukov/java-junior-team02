package com.lapushki.chat.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

class MessageProcessor {
    private static DaoDumomi dao = new DaoDumomi();
    private static final Logger log = LoggerFactory.getLogger(MessageProcessor.class);

    static void processMessage(Connection connection, Collection<Connection> connections, String message) {
        String[] mess = message.split(" ");
        switch (mess[0]) {
            case "/exit":
                handleExit(connection);
                break;
            case "/snd":
                handleMessage(connection, connections, mess[1]);
                break;
            case "/hist":
                handleHistory(connection);
                break;
            case "/chid":
                handleChid(connection);
                break;
        }
    }

    private static void handleChid(Connection connection) {
        //todo
    }

    private static void handleExit(Connection connection) {
        log.info("Client disconnected: " + connection.toString());
        connection.sendMessage("Goodbye!");
        connection.disconnect();
    }

    private static void handleHistory(Connection connection) {
        //todo
        //connection.sendMessage(dao.getHistory());
    }

    private static void handleMessage(Connection connection, Collection<Connection> connections, String message) {
        if (dao.saveDataBase(connection, message))
            sendMessageAllClients(message, connections);
    }

    static void sendMessageAllClients(String msg, Collection<Connection> connections) {
        for (Connection connection : connections) {
            connection.sendMessage(connection.getSocket().getInetAddress() + ": " + msg);
        }
    }
}
