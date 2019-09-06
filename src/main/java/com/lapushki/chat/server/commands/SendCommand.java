package com.lapushki.chat.server.commands;

import com.lapushki.chat.server.Session;
import com.lapushki.chat.server.SessionStore;
import com.lapushki.chat.server.exceptions.UnidentifiedUserException;
import com.lapushki.chat.server.history.saver.Saver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SendCommand implements Command {
    private final Session session;
    private final SessionStore sessionStore;
    private final String message;
    private final Saver saver;
    private final LocalDateTime timestamp;

    public SendCommand(Session session, SessionStore sessionStore, String message, Saver saver, LocalDateTime timestamp) {
        this.session = session;
        this.sessionStore = sessionStore;
        this.message = message;
        this.saver = saver;
        this.timestamp = timestamp;
    }

    @Override
    public void execute() throws UnidentifiedUserException, IOException {
        checkUsername();
        String decoratedMessage = decorate(message);
        sessionStore.sendToAll(decoratedMessage);
        saver.save(decoratedMessage, timestamp);
    }

    private String decorate(String message) {
        return "[" + timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "] " +
                session.getUsername() + ": " +
                message;
    }

    private void checkUsername() throws UnidentifiedUserException {
        String nickname = session.getUsername();
        if (nickname == null) {
            throw new UnidentifiedUserException();
        }
    }
}
