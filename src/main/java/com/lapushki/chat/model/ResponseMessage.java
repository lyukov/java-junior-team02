package com.lapushki.chat.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Message from Server to Client
 */
public class ResponseMessage implements Message {
    private String status;
    public String message;
    private String time; //FIXME why is time string?

    public ResponseMessage(String status, String message, String time) {
        this.status = status;
        this.message = message;
        this.time = time;
    }

    public ResponseMessage(RequestMessage message) {
        this.status = Message.STATUS_OK;
        this.message = message.message;
        this.time = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date());
    }

    public static ResponseMessage okResponseMessageWithCurrentTime(String message) {
        return new ResponseMessage(Message.STATUS_OK, message, "time");
    }

    @Override
    public String toString() {
        return status.equals("OK") ? String.format("[%s] %s", time, message) : String.format("[ERROR] %s", message);
    }
}
