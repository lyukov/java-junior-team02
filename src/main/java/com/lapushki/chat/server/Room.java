package com.lapushki.chat.server;

public interface Room {
    String getTitle();
    void sendToAll(String message);
    void add(Connection connection);
    void remove(Connection connection);
}
