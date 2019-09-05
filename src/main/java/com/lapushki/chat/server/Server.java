package com.lapushki.chat.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lapushki.chat.model.Message;
import com.lapushki.chat.model.RequestMessage;
import com.lapushki.chat.model.ResponseMessage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collection;
import java.util.LinkedList;

public class Server implements ConnectionListener {
    private static final int PORT = 48884;
    private static final Logger log = Logger.getLogger(Server.class);
    private static final MessageParser messageParser = new MessageParser();
    private static final MessageHandler messageHandler = new MessageHandler();
    private final Collection<Connection> connections = new LinkedList<>();
    private static final Gson gson = new GsonBuilder().create();

    private void start() {
        log.info("Server running...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                try {
                    Connection connection = new Connection(this, serverSocket.accept());
                    connection.init();
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
        RequestMessage requestMessage = gson.fromJson(message, RequestMessage.class);
        messageParser.processMessage(connection, connections, requestMessage);
    }

    @Override
    public synchronized void onConnectionReady(Connection connection) {
        connections.add(connection);
        ResponseMessage responseMessage = new ResponseMessage(
                Message.STATUS_OK,
                "New user connected: " + connection,
                "time");
        messageHandler.sendMessageAllClients(responseMessage, connections);
    }

    @Override
    public synchronized void onDisconnect(Connection connection) {
        connections.remove(connection);
        ResponseMessage responseMessage = new ResponseMessage(
                Message.STATUS_OK,
                "User disconnected: " + connection,
                "time");
        messageHandler.sendMessageAllClients(responseMessage, connections);
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
