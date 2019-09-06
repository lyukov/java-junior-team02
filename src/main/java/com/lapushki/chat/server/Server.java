package com.lapushki.chat.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private RoomStore roomStore;
    private ServerSocket connectionListener;
    private ConnectionFactory connectionFactory;
    private ConnectionPool connectionPool;
    private final Logger logger;

    public Server(RoomStore roomStore, ConnectionFactory connectionFactory,
                  ConnectionPool connectionPool, Logger logger) {
        this.roomStore = roomStore;
        this.connectionFactory = connectionFactory;
        this.connectionPool = connectionPool;
        this.logger = logger;
    }

    void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(8082)) {
            connectionListener = serverSocket;
            registerShutdownHook();
            while (true) {
                Socket socket = connectionListener.accept();
                Connection connection = connectionFactory.createConnection(socket);
                connectionPool.register(connection);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, Decorator.getException(), e);
        }
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (connectionListener != null) {
                try {
                    roomStore.sendToAll(Decorator.getServerDied());
                    connectionPool.closeAll();
                    connectionListener.close();
                } catch (Exception e) {
                    logger.log(Level.SEVERE, Decorator.getShuttDownEx(), e);
                }
            }
            logger.log(Level.INFO, Decorator.getServerClosed());
        }));
    }
}
