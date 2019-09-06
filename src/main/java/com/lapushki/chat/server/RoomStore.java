package com.lapushki.chat.server;

public interface RoomStore {
    void changeRoom(Connection connection, String newRoomTitle);
}
