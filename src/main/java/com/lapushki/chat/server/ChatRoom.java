package com.lapushki.chat.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ChatRoom implements Room {
    private Collection<Connection> connections;
    private final ExecutorService executorService;
    private final ReadWriteLock rwl;

    public ChatRoom() {
        connections = new ArrayList<>();
        this.executorService = Executors.newCachedThreadPool();
        rwl = new ReentrantReadWriteLock(false);
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
    public void register(Connection connection) {
        try {
            rwl.writeLock().lock();
            connections.add(connection);
        } finally {
            rwl.writeLock().unlock();
        }
        executorService.execute(connection);
    }

    @Override
    public void remove(Connection connection) {
        try {
            rwl.writeLock().lock();
            connections.remove(connection);
        } finally {
            rwl.writeLock().unlock();
        }
        connection.close();
    }

    @Override
    public void closeAll() {
        try {
            rwl.readLock().lock();
            connections.forEach(Connection::close);
        } finally {
            rwl.readLock().unlock();
        }
        executorService.shutdown();
    }
}
