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

    public boolean insertMessage(String userName, String message, String time) {
        try {
            preparedStatement = connect
                    .prepareStatement("INSERT INTO " + sourceTable + " (user_id, user_name, message, time) " +
                            "VALUES (default, ?, ?, ?)");

            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, message);
            preparedStatement.setString(3, time);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return false;
        }
        return true;
    }

    public String getAllMessages() {
        String result;
        try {
            preparedStatement = connect
                    .prepareStatement("SELECT user_name, message, time FROM " + sourceTable);
            resultSet = preparedStatement.executeQuery();
            result = getStringFromResultSet(resultSet);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            throw new DatabaseException(sqlEx);
        }
        return result;
    }

    void deleteAllMesseges() {
        try {
            preparedStatement = connect
                    .prepareStatement("DELETE FROM " + sourceTable);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            throw new DatabaseException(sqlEx);
        }
    }

    int countMessagesSentByGivenPerson(String userName) {
        int count;
        try {
            preparedStatement = connect
                    .prepareStatement("SELECT COUNT(*) as result " +
                            "FROM " + sourceTable + " " +
                            "WHERE user_name = ?");
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();
            count = resultSet.getInt("result");
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            count = -1;
            throw new DatabaseException(sqlEx);
        }
        return count;
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


    private String getStringFromResultSet(ResultSet resultSet) throws SQLException {
        StringBuilder result = new StringBuilder();
        while (resultSet.next()) {
            String user_name = resultSet.getString("user_name");
            String message = resultSet.getString("message");
            String time = resultSet.getString("time");
            result.append("User: ").append(user_name);
            result.append("; message: ").append(message);
            result.append("; date: ").append(time).append(System.getProperty("line.separator"));
        }
        return result.toString();
    }
}
