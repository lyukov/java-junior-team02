package com.lapushki.chat.server.history.roomed;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface RoomedHistory extends AutoCloseable{
    List<String> getHistory(String roomName);

    void save(String message, LocalDateTime dateTime, String roomName) throws IOException;

    @Override
    void close() throws Exception;

}
