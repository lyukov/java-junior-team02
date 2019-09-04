package com.lapushki.server;

public class Message {
    private Command command;
    private String message;
    private String ip;

    public Message(Command command, String message, String ip) {
        this.command = command;
        this.message = message;
        this.ip = ip;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String getMessage() {
        return message;
    }

    public Command getCommand() {
        return command;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }
}
