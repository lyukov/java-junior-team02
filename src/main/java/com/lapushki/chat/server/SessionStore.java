package com.lapushki.chat.server;

public interface SessionStore {
    void sendToAll(String message);
    void register(Session session);
    void remove(Session session);
    void closeAll();
}
