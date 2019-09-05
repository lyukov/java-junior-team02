package com.lapushki.chat.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lapushki.chat.model.Message;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Connection {
    private Socket socket;
    private ConnectionListener listener;
    private BufferedReader in;
    private BufferedWriter out;
    private ExecutorService executorService;
    private static final String LINE_BREAK = System.lineSeparator();
    private static final Gson gson = new GsonBuilder().create();

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

    public void sendMessage(Message msg) {
        String jsonMsg = gson.toJson(msg);
        try {
            out.write(jsonMsg + LINE_BREAK);
            out.flush();
        } catch (IOException e) {
            listener.onException(this, e);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void disconnect() {
        listener.onDisconnect(this);
        if (!executorService.isTerminated()) {
            executorService.shutdownNow();
        }
        try {
            socket.close();
            out.close();
            in.close();
        } catch (IOException e) {
            listener.onException(Connection.this, e);
        }
    }
}