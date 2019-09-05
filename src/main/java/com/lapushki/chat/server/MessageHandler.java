package com.lapushki.chat.server;

import com.lapushki.chat.model.RequestMessage;
import com.lapushki.chat.model.ResponseMessage;
import org.apache.log4j.Logger;

import java.util.Collection;

import static com.lapushki.chat.model.ResponseMessage.okResponseMessageWithCurrentTime;

class MessageHandler {
    private static final Logger log = Logger.getLogger(MessageHandler.class);

    void handleChid(Connection connection) {
        //todo
    }

    void handleExit(Connection connection) {
        log.info("Client disconnected: " + connection.toString());
        connection.sendMessage(okResponseMessageWithCurrentTime("Goodbye!"));
        connection.disconnect();
    }

    void handleHistory(Connection connection) {
        //todo
        //connection.sendMessage(dao.getHistory());
    }

    void handleMessage(Connection connection, Collection<Connection> connections, RequestMessage message) {
        ResponseMessage responseMessage = new ResponseMessage(message);
        //todo
        //if (dao.saveDataBase(connection, message))
        sendMessageAllClients(responseMessage, connections);
    }

    void sendMessageAllClients(ResponseMessage responseMessage, Collection<Connection> connections) {
        for (Connection connection : connections) {
            // TODO add IP adress to ResponseMessage
            //connection.sendMessage(connection.getSocket().getInetAddress() + ": " + msg);

            connection.sendMessage(responseMessage);
        }
    }
}