package com.lapushki.chat.server;

import java.io.*;
import java.net.Socket;
import java.util.Collection;

public class Connection {
    private Socket socket;
    private Thread thread;
    private ConnectionListener listener;
    private BufferedReader in;
    private BufferedWriter out;

    public Connection(ConnectionListener listener, String ip, int port) throws IOException {
        this(listener, new Socket(ip, port));
    }

    Connection(ConnectionListener listener, Socket socket) throws IOException {
        this.listener = listener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        //TODO вынести в отдельный метод
        thread = new Thread((() -> {
            try {
                listener.onConnectionReady(Connection.this);
                while (!thread.isInterrupted()) {
                    String msg = in.readLine();
                    listener.onReceiveString(Connection.this, msg);
                }
            } catch (IOException ex) {
                listener.onException(Connection.this, ex);
            } finally {
                listener.onDisconnect(Connection.this);
            }
        }));
        thread.start();
    }

    public void sendMessage(String msg) {
        try {
            out.write(msg + "\r\n");
            out.flush();
        } catch (IOException e) {
            listener.onException(Connection.this, e);
            disconnect();
        }
    }

    public synchronized void sendMessage(Collection<String> msgs) {
        try {
            out.write(msgs + "\r\n");
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
        thread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            listener.onException(Connection.this, e);
        }
    }
}