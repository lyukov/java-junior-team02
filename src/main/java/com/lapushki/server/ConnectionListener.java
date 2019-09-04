package com.lapushki.server;

public interface ConnectionListener {
    void onReceivedMessage(Connection connection, ResponseMessage responseMessage);

    void onDisconnect(Connection connection);

    void onException(Connection connection, Exception ex);
}
