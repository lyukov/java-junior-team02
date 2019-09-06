package com.lapushki.chat.server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ChatRoomStore implements RoomStore {
    private final Map<String, Room> roomsMap;
    private final ReadWriteLock rwl;

    public ChatRoomStore() {
        this.roomsMap = new HashMap<>(100);
        this.rwl = new ReentrantReadWriteLock(false);
    }

    @Override
    public void changeRoom(Connection connection, String newRoomTitle) {
        final Room oldRoom = connection.getRoom();
        if (oldRoom != null) {
            oldRoom.remove(connection);
        }
        final Room newRoom = getRoom(newRoomTitle);
        newRoom.register(connection);
        connection.setRoom(newRoom);
    }

    private Room getRoom(String title) {
        Room room = null;
        try {
            rwl.readLock().lock();
            room = roomsMap.get(title);
        } finally {
            rwl.readLock().unlock();
        }
        if (room != null) return room;
        return createRoom(title);
    }

    private Room createRoom(String title) {
        try {
            rwl.writeLock().lock();
            Room room = roomsMap.get(title);

            // If another stream has already created this room:
            if (room != null) return room;

            Room newRoom = new ChatRoom(title);
            roomsMap.put(title, newRoom);
            return newRoom;
        } finally {
            rwl.writeLock().unlock();
        }
    }
}
