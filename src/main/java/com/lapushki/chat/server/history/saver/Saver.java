package com.lapushki.chat.server.history.saver;

import java.io.IOException;
import java.time.LocalDateTime;

public interface Saver extends AutoCloseable{
    void save(String message, LocalDateTime dateTime) throws IOException;

    void close();

}
