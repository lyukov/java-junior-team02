package com.lapushki.chat.client;

import com.google.gson.Gson;
import com.lapushki.chat.model.RequestMessage;
import com.lapushki.chat.model.ResponseMessage;
import com.lapushki.chat.server.ConnectionListener;

import java.io.*;
import java.net.Socket;

public class Connection implements ConnectionListener {
    private final Socket socket;
    private Thread thread;
    private final ConnectionListener listener;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final Gson gson;

    Connection(Socket socket) throws IOException {
        listener = this;
        gson = new Gson();
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void init() {
        thread = new Thread((() -> {
            try {
                while (!thread.isInterrupted() && !socket.isClosed()) {
                    String jsonResponse = in.readLine();
                    ResponseMessage responseMessage = gson.fromJson(jsonResponse, ResponseMessage.class);
                    listener.onReceivedMessage(responseMessage.toString());
                }
            } catch (IOException ex) {
                listener.onException(ex);
            } finally {
                disconnect();
            }
        }));
        thread.start();
    }

    public void sendMessage(RequestMessage msg) {
        try {
            out.write(gson.toJson(msg));
            out.flush();
        } catch (IOException e) {
            listener.onException(e);
            disconnect();
        }
    }

    public void disconnect() {
        try {
            thread.interrupt();
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            listener.onException(e);
        }
    }

    RequestMessage formMessageObject(String in) {
        String command = in.substring(0, in.indexOf(" "));
        String message = in.substring(in.indexOf(" ") + 1);
        return new RequestMessage(command, message);
    }

    @Override
    public void onReceivedMessage(String message) {
        printMessage(message);
    }

    @Override
    public void onDisconnect() {
        printMessage("Connection closed! Have a good day!");
    }

    @Override
    public void onException(Exception ex) {
        printMessage(ex.getMessage());

    }

    void printMessage(String message) {
        System.out.println(message);
    }
}
