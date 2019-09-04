package com.lapushki.chat.client;

import com.google.gson.Gson;
import com.lapushki.chat.server.ConnectionListener;

import java.io.*;
import java.net.Socket;

public class Connection {
    private final Socket socket;
    private final Thread thread;
    private final ConnectionListener listener;
    private final BufferedReader in;
    private final BufferedWriter out;

    Connection(ConnectionListener listener, Socket socket) throws IOException {
        this.listener = listener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        thread = new Thread((this::onMessageReceive));
        thread.start();
    }

    private void onMessageReceive (){
        try {
            while (!thread.isInterrupted()) {
                String msg = in.readLine();
                listener.onReceivedMessage(msg);
            }
        } catch (IOException ex) {
            listener.onException(ex);
        } finally {
            listener.onDisconnect();
        }
    }

    public synchronized void sendMessage(Message msg) {
        try {
            Gson gson = new Gson();
            out.write(gson.toJson(msg));
            out.flush();
        } catch (IOException e) {
            listener.onException(e);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        try {
            thread.interrupt();
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            listener.onException(e);
        }
    }
}
