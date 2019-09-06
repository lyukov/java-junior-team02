package com.lapushki.chat.server;

import com.lapushki.chat.server.commands.Command;

import java.time.LocalDateTime;

public interface CommandFactory {
    Command createCommand(Session senderSession, String message, LocalDateTime timeStamp);
}
