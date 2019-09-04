package com.lapushki.chat.server;

import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collection;
import java.util.LinkedList;

public class Server implements ConnectionListener {
    private static final int PORT = 8081;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Server.class);
    private final Collection<Connection> connections = new LinkedList<>();
    private static DaoDumomi dao = new DaoDumomi();

    private Server() {
       log.info("Server running...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                try {
                    Connection connection = new Connection(this, serverSocket.accept());
                    connections.add(connection);
                    log.info("New client: " + connection);
                } catch (IOException e) {
                    log.error("Connection exception: " + e);
                }
            }
        } catch (IOException ex) {
            log.error("Server exception: " + ex);
            throw new RuntimeException(ex);
        }
    }


    @Override
    public synchronized void onReceiveString(Connection connection, String message) {
        log.info("New message: " + message + " from client: " + connection.toString());
        String[] mess = message.split(" ");
        System.out.println(mess[0] + mess[1]);
        switch (mess[0]) {
            case "/exit":
                handleExit(connection);
                break;
            case "/snd":
                handleMessage(connection, mess[1]);
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

    private void handleMessage(Connection connection, String message) {
        log.info("New message: " + message + " from client: " + connection.toString());
        if (dao.saveDataBase(connection, message))
            this.sendMessageAllClients(message);
    }

    private void sendMessageAllClients(String msg) {
        for (Connection connection : connections)
            connection.sendMessage(connection.getSocket().getInetAddress() + ": " + msg);
    }


    @Override
    public synchronized void onConnectionReady(Connection connection) {
        connections.add(connection);
        sendToAllConnections("New user connected: " + connection);
    }

    @Override
    public synchronized void onDisconnect(Connection connection) {
        connections.remove(connection);
        sendToAllConnections("User disconnected: " + connection);
    }

    @Override
    public synchronized void onException(Connection connection, Exception ex) {
        System.out.println("Connection exception: " + ex);
    }

    private void sendToAllConnections(String msg) {
        for (Connection c : connections)
            c.sendMessage(msg);
    }


    public static void main(String[] args) {
        new Server();

    }
}
