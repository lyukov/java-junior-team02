package com.lapushki.chat.model;

/**
 * Message from Client To Server
 */
public class RequestMessage implements Message {
    public String command;
    public String message;

    public RequestMessage(String command, String message) {
        this.command = command;
        this.message = message;
    }

    public RequestMessage(String userInput) {
        // FIXME need to use MessageParser to do it
        String[] parsedInput = userInput.split(" ", 2);
        command = parsedInput[0];
        if (parsedInput.length > 1) {
            message = parsedInput[1];
        }
    }

    @Override
    public String toString() {
        return command + " " + message;
    }
}
