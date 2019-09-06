package com.lapushki.chat.server;

import com.lapushki.chat.server.commands.Command;

import java.time.LocalDateTime;

public interface CommandFactory {
    Command createCommand(Connection senderConnection, String message, LocalDateTime timeStamp);
}
