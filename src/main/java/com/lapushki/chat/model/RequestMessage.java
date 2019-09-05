package com.lapushki.chat.model;

/**
 * Message from Client To Server
 */
public class RequestMessage implements Message {
    public String command;
    public String message;

    RequestMessage(String command, String message) {
        this.command = command;
        this.message = message;
    }

    public RequestMessage(String userInput) {
        if (validateInput(userInput)) {
            // FIXME need to use MessageParser to do it
            String[] parsedInput = userInput.split(" ", 2);
            command = parsedInput[0];
            if (parsedInput.length > 1) {
                message = parsedInput[1];
            }
        }
    }

    @Override
    public String toString() {
        return command + " " + message;
    }

    private boolean validateInput(String msg) {
        if (msg.length() == 0 ||
                (msg.startsWith("/snd") && !msg.trim().contains(" ")) ||
                (msg.startsWith("/chid") && !msg.trim().contains(" "))
        ) {
            System.out.println(msg);
            throw new IllegalArgumentException("Message is empty");
        }
        if (msg.startsWith("/hist") || msg.startsWith("/exit")) {
            return true;
        }
        if (!msg.startsWith("/chid") && !msg.startsWith("/snd")) {
            throw new IllegalArgumentException("Unknown command");
        }
        if (msg.substring(msg.indexOf(" ") + 1).length() > 150) {
            throw new IllegalArgumentException("Message is too big");
        }
        return true;
    }
}
