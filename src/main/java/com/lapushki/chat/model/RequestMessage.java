package com.lapushki.chat.model;

public class RequestMessage {
    public String command;
    public String message;

    public RequestMessage(String command, String message) {
        this.command = command;
        this.message = message;
    }
}