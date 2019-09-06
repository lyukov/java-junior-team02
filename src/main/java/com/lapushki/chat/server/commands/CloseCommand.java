package com.lapushki.chat.server.commands;

import com.lapushki.chat.server.Connection;
import com.lapushki.chat.server.Room;

public class CloseCommand implements Command {
    private final Connection connection;

    public CloseCommand(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void execute() {
        Room room = connection.getRoom();
        room.remove(connection);
        sendLeaveMessage(room);
    }

    private void sendLeaveMessage(Room room) {
        String nickname = connection.getUsername();
        if (nickname != null) {
            String message = nickname + " has left the chat";
            room.sendToAll(message);
        }
    }
}
