package com.lapushki.chat.server;

import java.io.IOException;
import java.net.Socket;

public interface ConnectionFactory {
    Connection createConnection(Socket socket) throws IOException;
}
