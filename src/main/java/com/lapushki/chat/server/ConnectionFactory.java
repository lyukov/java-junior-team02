package com.lapushki.chat.server;

import java.io.IOException;
import java.net.Socket;

public interface SessionFactory {
    Connection createSession(Socket socket) throws IOException;
}
