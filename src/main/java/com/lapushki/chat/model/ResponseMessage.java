package com.lapushki.chat.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Message from Server to Client
 */
public class ResponseMessage implements Message {
    public String status;
    public String message;
    private String time;

    public ResponseMessage(String status, String message) {
        this.status = status;
        this.message = message;
        this.time = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date());
    }

    public ResponseMessage(RequestMessage message) {
        this.status = Message.STATUS_OK;
        this.message = message.message;
        this.time = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date());
    }

    public static ResponseMessage okResponseMessageWithCurrentTime(String message) {
        return new ResponseMessage(Message.STATUS_OK, message);
    }

    public static ResponseMessage failResponseMessageWithCurrentTime(String message) {
        return new ResponseMessage(Message.STATUS_ERROR, message);
    }

    @Override
    public String toString() {
        return status.equals("OK") ? String.format("[%s] %s", time, message) : String.format("[ERROR] %s", message);
    }
}
