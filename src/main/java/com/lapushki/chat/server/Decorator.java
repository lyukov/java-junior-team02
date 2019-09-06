package com.lapushki.chat.server;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Decorator {
    private static String leaveMessage = " has left the chat";
    private static String changeNameMessage = " has changed name to ";
    private static String joinMessage = " joined to chat";
    private static String chatHistoryStartMessage = "Chat history: ";
    private static String chatHistoryEndMessage = "Chat history end. ";
    private static String openBracket = "[";
    private static String closeBracket = "] ";
    private static String delim = ": ";
    private static String startServerMessage = "Server starts";
    private static String serverDied = "Server died ;<";
    private static String serverClosed = "Server closed";
    private static String shuttDownEx = "Exception in Shutdown Hook";
    private static String Exception = "Exception is thrown";

    public static String leftMessage(String message) {
        return message + leaveMessage;
    }
    public static String changeNameMessage(String oldnickname, String newnickname){
        return oldnickname + changeNameMessage + newnickname;
    }
    public static String joinMessage(String nickname){
        return nickname + joinMessage;
    }
    public static String formatTimestamp(LocalDateTime timestamp){
        return openBracket + timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + closeBracket;
    }
    public static String decorate(String message, LocalDateTime timestamp){
        return formatTimestamp(timestamp) + message;
    }
    public static String decorate(String message,LocalDateTime timestamp, String nickname) {
        return formatTimestamp(timestamp) +
                nickname + delim +
                message;
    }

    public static String getchatHistoryStartMessage(){
        return chatHistoryStartMessage;
    }
    public static String getchatHistoryEndMessage(){
        return chatHistoryEndMessage;
    }
    public static String startServerMess(){
        return startServerMessage;
    }

    public static String getServerDied() {
        return serverDied;
    }

    public static String getServerClosed() {
        return serverClosed;
    }

    public static String getShuttDownEx() {
        return shuttDownEx;
    }

    public static String getException() {
        return Exception;
    }
}