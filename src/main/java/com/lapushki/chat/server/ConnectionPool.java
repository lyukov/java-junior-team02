package com.lapushki.chat.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConnectionPool {
    private Collection<Connection> connections;
    private final ExecutorService executorService;
    private final ReadWriteLock rwl;

    public ConnectionPool() {
        this.connections = new ArrayList<>();
        this.executorService = Executors.newCachedThreadPool();
        rwl = new ReentrantReadWriteLock(false);
    }

    public void register(Connection connection) {
        executorService.execute(connection);
    }

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
