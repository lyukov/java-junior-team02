package com.lapushki.chat.server;

import com.lapushki.chat.server.commands.Command;
import com.lapushki.chat.server.exceptions.ChatException;
import com.lapushki.chat.server.exceptions.NotInTheRoomException;
import com.lapushki.chat.server.exceptions.OccupiedNicknameException;
import com.lapushki.chat.server.exceptions.UnidentifiedUserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatConnection implements Connection {
    private String username;
    private Socket socket;
    private BufferedReader socketIn;
    private PrintWriter socketOut;
    private CommandFactory commandFactory;
    private Room room;
    private final Logger logger;
    private boolean isClosed = false;

    ChatConnection(String username, Socket socket,
                   BufferedReader socketIn, PrintWriter socketOut,
                   CommandFactory commandFactory, Logger logger) {
        this.username = username;
        this.socket = socket;
        this.socketIn = socketIn;
        this.socketOut = socketOut;
        this.commandFactory = commandFactory;
        this.room = null;
        this.logger = logger;
    }

    @Override
    public void run() {
        try (
                BufferedReader myIn = socketIn
        ) {
            while (!isClosed) {
                String message = myIn.readLine();
                processRequest(message);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, Decorator.getExceptionInSession(), e);
        }
    }

    @Override
    public void send(String message) {
        socketOut.println(message);
        socketOut.flush();
    }

    @Override
    public void close() {
        isClosed = true;
        logger.log(Level.INFO, String.format("Debug: %s session closed%n", username));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Room getRoom() {
        return room;
    }

    @Override
    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    private void processRequest(String message) throws IOException {
        LocalDateTime timeStamp = LocalDateTime.now();
        Command command = commandFactory.createCommand(this, message, timeStamp);
        try {
            command.execute();
        } catch (UnidentifiedUserException e) {
            processException(e, "First command should be /chid");
        } catch (OccupiedNicknameException e) {
            processException(e, "This nickname is occupied, try another one");
        } catch (NotInTheRoomException e) {
            processException(e, "Please enter the room before writing");
        } catch (ChatException e) {
            processException(e, "Some error has occurred");
        }
        String logLine = String.format("%s %s %s%n", username, timeStamp, message);
        System.out.println(logLine);
        logger.log(Level.INFO, "Debug: " + logLine);
    }

    private void processException(ChatException e, String message) {
        logger.log(Level.SEVERE, Decorator.getExceptionInSession(), e);
        send(message);
    }
}
