package com.lapushki.chat.server;

public interface ConnectionListener {

    void onReceivedMessage(String message);

    void onDisconnect();

    void onException(Exception ex);

}
