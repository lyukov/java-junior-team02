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

public class ChatConnection implements Connection {
    private String username;
    private Socket socket;
    private BufferedReader socketIn;
    private PrintWriter socketOut;
    private CommandFactory commandFactory;
    private Room room;
    private boolean isClosed = false;

    ChatConnection(String username, Socket socket, BufferedReader socketIn, PrintWriter socketOut, CommandFactory commandFactory) {
        this.username = username;
        this.socket = socket;
        this.socketIn = socketIn;
        this.socketOut = socketOut;
        this.commandFactory = commandFactory;
        this.room = null;
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
            //TODO add logger
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
        System.out.printf("Debug: %s session closed%n", username);
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
        System.out.printf("Debug: %s %s %s%n", username, timeStamp, message);
    }

    private void processException(ChatException e, String message) {
        send(message);
    }
}
