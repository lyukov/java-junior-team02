package com.lapushki.chat.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

public class Server implements ConnectionListener {
    private static final int PORT = 8081;
    private static final Logger log = LoggerFactory.getLogger(Server.class);
    private static final MessageParser messageParser = new MessageParser();
    private static final MessageHandler messageHandler = new MessageHandler();
    private final Collection<Connection> connections = new LinkedList<>();
    static Collection<String> userNames = new HashSet<>();

    private void start() {
        log.info("Server running...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                try {
                    Connection connection = new Connection(this, serverSocket.accept());
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
        if (message == null || message.isEmpty())
            return;
        log.info("New message: " + message + " from client: " + connection.toString());
        messageParser.processMessage(connection, connections, message);
    }

    @Override
    public synchronized void onConnectionReady(Connection connection) {
        connections.add(connection);
        messageHandler.sendMessageAllClients("New user connected: " + connection, connections);
    }

    @Override
    public synchronized void onDisconnect(Connection connection) {
        connections.remove(connection);
        messageHandler.sendMessageAllClients("User disconnected: " + connection, connections);
    }

    @Override
    public synchronized void onException(Connection connection, Exception ex) {
        log.error("Connection exception: " + ex);
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
