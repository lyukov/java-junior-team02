package com.lapushki.chat.server;

public enum Command {

    EXIT("/exit"),
    SEND("/snd"),
    HIST("/hist"),
    CHID("/chid"),
    DEFAULT("");

    private final String message;

    Command(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
