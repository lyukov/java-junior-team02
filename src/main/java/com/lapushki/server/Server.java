package com.lapushki.server;



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

    @Override
    public void onReceivedMessage(Connection connection, ResponseMessage responseMessage){
        switch (responseMessage.message) {
            case EXIT:
                handleExit(connection);
                break;
            case SEND:
                handleMessage(connection, responseMessage);
                break;
            case HISTORY:
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

    private void handleMessage(Connection connection, ResponseMessage responseMessage) {
       if (dao.saveDataBase(connection, responseMessage))
           this.sendMessageAllClients(responseMessage);
    }

    private void sendMessageAllClients(ResponseMessage msg) {
        for (Connection connection: connections)
            connection.sendMessage(msg);
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
