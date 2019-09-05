package com.lapushki.chat.db;

import com.lapushki.chat.model.RequestMessage;

import java.sql.*;

import static java.lang.System.exit;

/**
 * Created by kate-c on 04/09/2019.
 */
public class SQLConnector implements AutoCloseable {
    private Connection connect = null;
    private ResultSet resultSet = null;
    private PreparedStatement preparedStatement = null;
    private String sourceTable;
    private Parser parser;

    public Connection getConnect() {
        return connect;
    }

    public Parser getParser() {
        return parser;
    }

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
            System.out.println("Can't connect to the database!");
            exit(1);
        }
    }

    public void close() {
        try { connect.close(); } catch(SQLException se) { /*can't do anything */ }
        try { resultSet.close(); } catch(SQLException se) { /*can't do anything */ }
    }

}
