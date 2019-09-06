package com.lapushki.chat.server;

import java.util.Collection;

public interface RoomStore {
    void sendToAll(String message);
    void changeRoom(Connection connection, String newRoomTitle);
}
