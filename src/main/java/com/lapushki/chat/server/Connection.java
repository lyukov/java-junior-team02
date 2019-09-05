package com.lapushki.chat.server;

import java.io.*;
import java.net.Socket;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Connection {
    private Socket socket;
    private ConnectionListener listener;
    private BufferedReader in;
    private BufferedWriter out;
    private ExecutorService executorService;
    private static final String LINE_BREAK = "\r\n";

    public Connection(ConnectionListener listener, String ip, int port) throws IOException {
        this(listener, new Socket(ip, port));
    }

    Connection(ConnectionListener listener, Socket socket) throws IOException {
        this.listener = listener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void init() {
        executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new InputStreamListener(this, listener, in));
    }

    public void sendMessage(String msg) {
        try {
            out.write(msg + LINE_BREAK);
            out.flush();
        } catch (IOException e) {
            listener.onException(Connection.this, e);
            disconnect();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void disconnect() {
        if (!executorService.isTerminated()) {
            executorService.shutdownNow();
        }
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            listener.onException(Connection.this, e);
        }
    }
}