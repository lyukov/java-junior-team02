package com.lapushki.chat.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ChatRoom implements Room {
    private final String title;
    private Collection<Connection> connections;
    private final ReadWriteLock rwl;

    public ChatRoom(String title) {
        this.title = title;
        connections = new ArrayList<>();
        rwl = new ReentrantReadWriteLock(false);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void sendToAll(String message) {
        try {
            rwl.readLock().lock();
            connections.forEach(s -> s.send(message));
        } finally {
            rwl.readLock().unlock();
        }
    }

    @Override
    public void add(Connection connection) {
        try {
            rwl.writeLock().lock();
            connections.add(connection);
        } finally {
            rwl.writeLock().unlock();
        }
    }

    @Override
    public void remove(Connection connection) {
        try {
            rwl.writeLock().lock();
            connections.remove(connection);
        } finally {
            rwl.writeLock().unlock();
        }
    }
}
