package com.lapushki.chat.server.history.history;

import com.lapushki.chat.server.history.reader.Reader;
import com.lapushki.chat.server.history.reader.SwitchingFileReader;
import com.lapushki.chat.server.history.saver.Saver;
import com.lapushki.chat.server.history.saver.SwitchingFileSaver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * Class that contains reader and saver. Use one instance for all threads. Do NOT clears all previous history
 */
public class HistoryAccessObject implements History {
    private Saver saver;
    private Reader reader;
    private ReadWriteLock readWriteLock;

    public HistoryAccessObject() throws IOException {
        saver = new SwitchingFileSaver();
        reader = new SwitchingFileReader();
        readWriteLock = new ReentrantReadWriteLock();
    }

    public HistoryAccessObject(String folder) throws IOException{
        saver = new SwitchingFileSaver(folder);
        reader = new SwitchingFileReader(folder);
    }

    @Override
    public List<String> getHistory() {
        List<String> history = null;
        try {
            readWriteLock.readLock().lock();
            history = reader.getHistory();
        } finally {
            readWriteLock.readLock().unlock();
        }
        return history;
    }

    @Override
    public void save(String message, LocalDateTime dateTime) throws IOException {
        try {
            readWriteLock.writeLock().lock();
            saver.save(message, dateTime);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void close() throws Exception {
        saver.close();
    }
}