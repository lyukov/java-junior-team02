package com.lapushki.chat.server.commands;

import com.lapushki.chat.server.exceptions.ChatException;

import java.io.IOException;

public interface Command {
    public void execute() throws ChatException, IOException;
}
