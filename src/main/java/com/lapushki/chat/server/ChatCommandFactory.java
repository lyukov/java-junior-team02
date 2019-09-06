package com.lapushki.chat.server;

import com.lapushki.chat.Common.CommandType;
import com.lapushki.chat.server.commands.*;
import com.lapushki.chat.server.history.roomed.RoomedHistory;

import java.time.LocalDateTime;
import java.util.Map;

public class ChatCommandFactory implements CommandFactory {
    private final Parser parser;
    private final RoomStore roomStore;
    private final Identificator identificator;
    private final RoomedHistory history;

    public ChatCommandFactory(Parser parser,
                              RoomStore roomStore,
                              Identificator identificator, RoomedHistory history) {
        this.parser = parser;
        this.roomStore = roomStore;
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
            case CHROOM:
                return createChangeRoomCommand(connection, fieldMap, timestamp);
            default:
                throw new IllegalArgumentException("Unknown command type: " + type);
        }
    }

    private Command createChangeRoomCommand(Connection connection, Map<String, String> fieldMap, LocalDateTime timestamp) {
        String newRoomTitle = fieldMap.get("msg");
        return new ChangeRoomCommand(roomStore, connection, newRoomTitle);
    }

    private Command createHistCommand(Connection connection) {
        return new HistoryCommand(connection, history);
    }

    private Command createCloseCommand(Connection connection) {
        return new CloseCommand(connection);
    }

    private SendCommand createSendCommand(Connection connection, Map<String, String> fieldMap, LocalDateTime timestamp) {
        String message = fieldMap.get("msg");
        return new SendCommand(connection, message, timestamp, history);
    }

    private ChangeIdCommand createChangeIdCommand(Connection connection, Map<String, String> fieldMap, LocalDateTime timestamp) {
        String newNickname = fieldMap.get("msg");
        return new ChangeIdCommand(connection, identificator, newNickname, timestamp, history);
    }
}
