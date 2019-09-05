package com.lapushki.chat.server;

import com.lapushki.chat.db.DatabaseException;
import com.lapushki.chat.db.SQLConnector;
import com.lapushki.chat.model.RequestMessage;
import com.lapushki.chat.model.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import static com.lapushki.chat.model.ResponseMessage.failResponseMessageWithCurrentTime;
import static com.lapushki.chat.model.ResponseMessage.okResponseMessageWithCurrentTime;

class MessageHandler {
    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);
    private static SQLConnector connector;

    static {
        try {
            connector = new SQLConnector();
        } catch (DatabaseException e) {
            log.error("Database is dead!!!");
        }
    }

    void handleChid(Connection connection) {
        log.info("Change id request: " + connection.toString());
        //todo add change id
    }

    void handleExit(Connection connection) {
        log.info("Client disconnected: " + connection.toString());
        connection.sendMessage(okResponseMessageWithCurrentTime("Goodbye!"));
        connection.disconnect();
    }

    void handleHistory(Connection connection) {
        log.info("Hist request: " + connection.toString());
        String message = connector.getAllMessages();
        connection.sendMessage(okResponseMessageWithCurrentTime(message));
    }

    void handleMessage(Connection connection, Collection<Connection> connections, RequestMessage requestMessage) {
        log.info("New message request: " + connection.toString() + " " + requestMessage.toString());
        ResponseMessage responseMessage = new ResponseMessage(requestMessage);
        String time = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date());
        if (connector.insertMessage(connection.toString(), requestMessage.message, time)) {
            sendMessageAllClients(responseMessage, connections);
        } else {
            connection.sendMessage(failResponseMessageWithCurrentTime(requestMessage.message));
        }
    }

    void sendMessageAllClients(ResponseMessage responseMessage, Collection<Connection> connections) {
        for (Connection connection : connections) {
            //todo add IP adress to ResponseMessage
            //connection.sendMessage(connection.getSocket().getInetAddress() + ": " + msg);
            connection.sendMessage(responseMessage);
        }
    }
}