package com.lapushki.chat.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket connectionListener;
    private ConnectionFactory connectionFactory;
    private Room room;

    public Server(ConnectionFactory connectionFactory, Room room) {
        this.connectionFactory = connectionFactory;
        this.room = room;
    }

    void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(8082)) {
            connectionListener = serverSocket;
            registerShutdownHook();
            while (true) {
                Socket socket = connectionListener.accept();
                Connection connection = connectionFactory.createConnection(socket);
                room.register(connection);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (connectionListener != null) {
                try {
                    room.sendToAll("Server died ;<");
                    room.closeAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Server closed");
        }));
    }
}
