package com.lapushki.chat.server;

import com.lapushki.chat.common.CommandType;
import com.lapushki.chat.server.commands.*;
import com.lapushki.chat.server.history.HistoryAccessObject;
import com.lapushki.chat.server.history.saver.Saver;

import java.time.LocalDateTime;
import java.util.Map;

public class ChatCommandFactory implements CommandFactory {
    private final Parser parser;
    private final Room room;
    private final Saver saver;
    private final Identificator identificator;
    private final HistoryAccessObject history;

    public ChatCommandFactory(Parser parser,
                              Room room,
                              Saver saver,
                              Identificator identificator, HistoryAccessObject history) {
        this.parser = parser;
        this.room = room;
        this.saver = saver;
        this.identificator = identificator;
        this.history = history;
    }

    @Override
    public Command createCommand(Connection connection, String message, LocalDateTime timestamp) {
        Map<String, String> fieldMap = parser.parse(message);
        String type = fieldMap.get("type");
        CommandType commandType = CommandType.fromString(type);
        switch (commandType) {
            case HIST:
                return createHistCommand(connection);
            case SEND:
                return createSendCommand(connection, fieldMap, timestamp);
            case CHID:
                return createChangeIdCommand(connection, fieldMap, timestamp);
            case CLOSE:
                return createCloseCommand(connection);
            default:
                throw new IllegalArgumentException("Unknown command type: " + type);
        }
    }

    private Command createHistCommand(Connection connection) {
        return new HistoryCommand(connection, history);
    }

    private Command createCloseCommand(Connection connection) {
        return new CloseCommand(connection, room);
    }

    private SendCommand createSendCommand(Connection connection, Map<String, String> fieldMap, LocalDateTime timestamp) {
        String message = fieldMap.get("msg");
        return new SendCommand(connection, room, message, saver, timestamp);
    }

    private ChangeIdCommand createChangeIdCommand(Connection connection, Map<String, String> fieldMap, LocalDateTime timestamp) {
        String newNickname = fieldMap.get("msg");
        return new ChangeIdCommand(connection, identificator, newNickname, timestamp, room, saver);
    }
}
