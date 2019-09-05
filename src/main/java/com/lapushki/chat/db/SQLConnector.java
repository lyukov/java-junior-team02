package com.lapushki.chat.db;

import java.sql.*;

public class SQLConnector implements AutoCloseable {
    private Connection connect = null;
    private ResultSet resultSet = null;
    private PreparedStatement preparedStatement = null;
    private String sourceTable;
    private Parser parser;

    public SQLConnector() {
        this("src/main/resources/connection.properties");
    }

    public SQLConnector(String fileName) {
        try {
            parser = new Parser(fileName);
            sourceTable = parser.getDatabase() + "." + parser.getTable();
            connect = DriverManager.getConnection(parser.getUrl(), parser.getUser(), parser.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException(e);
        }
    }

    public Connection getConnect() {
        return connect;
    }

    public Parser getParser() {
        return parser;
    }

    public void close() {
        try {
            connect.close();
        } catch (SQLException se) {
            se.printStackTrace();
            throw new DatabaseException(se);
        }
        try {
            resultSet.close();
        } catch (SQLException se) {
            se.printStackTrace();
            throw new DatabaseException(se);
        }
    }
}
