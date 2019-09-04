package com.lapushki.server;

public interface ConnectionListener {
    void onReceiveMessage(Connection connection, Message message);

    void onDisconnect(Connection connection);

    void onException(Connection connection, Exception ex);
}
