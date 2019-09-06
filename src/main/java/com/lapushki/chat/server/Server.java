package com.lapushki.chat.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private RoomStore roomStore;
    private ServerSocket connectionListener;
    private ConnectionFactory connectionFactory;
    private ConnectionPool connectionPool;

    public Server(RoomStore roomStore, ConnectionFactory connectionFactory, ConnectionPool connectionPool) {
        this.roomStore = roomStore;
        this.connectionFactory = connectionFactory;
        this.connectionPool = connectionPool;
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
            e.printStackTrace();
        }
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (connectionListener != null) {
                try {
                    roomStore.sendToAll("Server died ;<");
                    connectionPool.closeAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Server closed");
        }));
    }
}
