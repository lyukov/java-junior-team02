package com.lapushki.chat.server.commands;

import com.lapushki.chat.server.Connection;
import com.lapushki.chat.server.Room;

public class CloseCommand implements Command {
    private final Connection connection;
    private final Room room;

    public CloseCommand(Connection connection, Room room) {
        this.connection = connection;
        this.room = room;
    }

    @Override
    public void execute() {
        room.remove(connection);
        sendLeaveMessage();
    }

    private void sendLeaveMessage() {
        String nickname = connection.getUsername();
        if (nickname != null) {
            String message = nickname + " has left the chat";
            room.sendToAll(message);
        }
    }
}
