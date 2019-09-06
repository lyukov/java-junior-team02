package com.lapushki.chat.server;

public interface Group {
    void sendToAll(String message);
    void register(Connection connection);
    void remove(Connection connection);
    void closeAll();
}
