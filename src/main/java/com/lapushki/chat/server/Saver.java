package com.lapushki.chat.server;

import java.time.LocalDateTime;

public interface Saver {
    void save(String message, LocalDateTime timestamp);
}
