package com.lapushki.chat.server;

import java.io.BufferedReader;
import java.io.IOException;

public class InputStreamListener implements Runnable {

    private Connection connection;
    private ConnectionListener listener;
    private BufferedReader reader;

    InputStreamListener(Connection connection, ConnectionListener listener, BufferedReader reader) {
        this.connection = connection;
        this.listener = listener;
        this.reader = reader;
    }

    @Override
    public void run() {
        try {
            listener.onConnectionReady(connection);
            while (true) {
                String msg = reader.readLine();
                listener.onReceiveString(connection, msg);
            }
        } catch (IOException ex) {
            listener.onException(connection, ex);
        } finally {
            listener.onDisconnect(connection);
        }
    }
}
