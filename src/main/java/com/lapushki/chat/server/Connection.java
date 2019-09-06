package com.lapushki.chat.server;

public interface Connection extends Runnable {
    String getUsername();
    Room getRoom();
    void setUsername(String nickname);
    void run();
    void send(String message);
    void close();
}
