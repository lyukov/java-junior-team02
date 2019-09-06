package com.lapushki.chat.server.commands;

import com.lapushki.chat.server.Connection;
import com.lapushki.chat.server.RoomStore;
import com.lapushki.chat.server.exceptions.ChatException;

import java.io.IOException;

public class ChangeRoomCommand implements Command  {
    private final RoomStore roomStore;
    private final Connection connection;
    private final String newRoomTitle;

    public ChangeRoomCommand(RoomStore roomStore, Connection connection, String newRoomTitle) {
        this.roomStore = roomStore;
        this.connection = connection;
        this.newRoomTitle = newRoomTitle;
    }

    @Override
    public void execute() throws ChatException, IOException {
        roomStore.changeRoom(connection, newRoomTitle);
    }
}
