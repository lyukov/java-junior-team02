package com.lapushki.chat.server;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class ChatConnectionFactory implements ConnectionFactory {
    private CommandFactory commandFactory;
    private Logger logger;

    public ChatConnectionFactory(CommandFactory commandFactory, Logger logger) {
        this.commandFactory = commandFactory;
        this.logger = logger;
    }

    @Override
    public Connection createConnection(Socket socket) throws IOException {
        final BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        new BufferedInputStream(
                                socket.getInputStream())));
        final PrintWriter out = new PrintWriter(
                new OutputStreamWriter(
                        new BufferedOutputStream(
                                socket.getOutputStream())));
        return new ChatConnection(null, socket, in, out, commandFactory, logger);
    }
}
