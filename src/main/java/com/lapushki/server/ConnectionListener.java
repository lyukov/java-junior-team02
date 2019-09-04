package com.lapushki.server;

public interface ConnectionListener {
    void onReceivedMessage(Connection connection, Message message);

    void onDisconnect(Connection connection);

    void onException(Connection connection, Exception ex);
}
