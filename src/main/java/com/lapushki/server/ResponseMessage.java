package com.lapushki.server;

public class ResponseMessage {
    public String status;
    public String message;
    public String time;

    public ResponseMessage(String status, String message, String time) {
        this.status = status;
        this.message = message;
        this.time = time;
    }

    @Override
    public String toString() {
        return status.equals("OK") ? String.format("[%s] %s", time, message) : String.format("[ERROR] %s", message);
    }
}
