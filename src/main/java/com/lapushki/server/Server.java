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
    public void onReceiveMessage(Connection connection, Message message) {
        switch (message.getCommand()) {
            case EXIT:
                handleExit(connection);
                break;
            case SEND:
                handleMessage(connection, message);
                break;
            case HISTORY:
                handleHistory(connection);
                break;
        }
    }

    private void handleExit(Connection connection) {
        System.out.println("Exit!");
        connection.disconnect();
    }

    private void handleHistory(Connection connection) {
        sendMessage(connection, dao.getHistory());
    }

    private void handleMessage(Connection connection, Message message) {
       if (dao.saveDataBase(connection, message))
           this.sendMessage(message);
    }

    private void sendMessage(Message msg) {
        for (Connection c : connections)
            c.sendMessage(msg);
    }


    private void sendMessage(Connection connection, Collection<Message> msgs) {
        connection.sendMessage(msgs);
    }

    @Override
    public void onDisconnect(Connection connection) {

    }

    @Override
    public void onException(Connection connection, Exception ex) {
        System.out.println("Connection exception: " + ex);
    }
}
