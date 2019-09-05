package com.lapushki.chat.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static com.lapushki.chat.server.Server.userNames;

public class MessageHandler {
    private static final DaoDumomi dao = new DaoDumomi();
    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    void handleChid(Connection connection, String message) {
        if( !userNames.add(message)) {
            connection.sendMessage("nickname " + message + " is taken by another user. Choose another nickname." );
        }
        else {
            connection.sendMessage("server code 1234567");
            connection.sendMessage("your nickname for this session is " + message );
        }
    }

    void handleExit(Connection connection) {
        log.info("Client disconnected: " + connection.toString());
        connection.sendMessage("Goodbye!");
        connection.disconnect();
    }

    void handleHistory(Connection connection) {
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