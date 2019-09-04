package com.lapushki.server;



import com.google.gson.Gson;
import com.lapushki.chat.model.RequestMessage;
import com.lapushki.chat.model.ResponseMessage;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;


public class Server implements ConnectionListener {
    private static final int PORT = 8080;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Server.class);
    private final Collection<Connection> connections = new LinkedList<>();
    private static DaoDumomi dao = new DaoDumomi();
    private static final Gson gson = new Gson();

    private Server() {
        log.info("Server running...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    connections.add(new Connection(this, socket));
                    log.info("New user connected: " + socket);
                } catch (IOException e) {
                    log.error("Connection exception: " + e);
                }
            }
        } catch (IOException ex) {
            log.error("Server: " + ex);
            throw new RuntimeException(ex);
        }
    }

    //message to json

    @Override
    public void onReceivedMessage(Connection connection, String message){
        RequestMessage requestMessage = gson.fromJson(message, RequestMessage.class);
        switch (requestMessage.command) {
            case "/exit":
                handleExit(connection);
                break;
            case "/snd":
                handleMessage(connection, requestMessage);
                break;
            case "/hist":
                handleHistory(connection);
                break;
        }
    }

    private void handleExit(Connection connection) {
        log.info("Client disconnected: " + connection.toString());
        connection.disconnect();
    }

    private void handleHistory(Connection connection) {
        connection.sendMessage(dao.getHistory());
    }

    private void handleMessage(Connection connection, RequestMessage requestMessage) {
       if (dao.saveDataBase(connection, requestMessage))
           this.sendMessageAllClients(requestMessage);
    }

    private void sendMessageAllClients(RequestMessage msg) {
        ResponseMessage responseMessage = new ResponseMessage(
                "OK",
                msg.message,
                String.valueOf(System.currentTimeMillis())
        );
        for (Connection connection: connections)
            connection.sendMessage(responseMessage);
    }

    @Override
    public void onDisconnect(Connection connection) {
        connections.remove(connection);
        log.info("Client disconnected: " + connection.toString());
    }

    @Override
    public void onException(Connection connection, Exception ex) {
        log.error("Connection exception: " + ex);
    }
}
