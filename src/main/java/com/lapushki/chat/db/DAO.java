package com.lapushki.chat.db;

import com.lapushki.chat.model.RequestMessage;

import java.sql.*;

public class DAO {
    private SQLConnector connector;
    private String sourceTable;
    private Parser parser;
    private Connection connect = null;
    private ResultSet resultSet = null;
    private PreparedStatement preparedStatement = null;

    public DAO(){
        connector = new SQLConnector();
        connect = connector.getConnect();
        parser = connector.getParser();
        sourceTable = parser.getDatabase() + "." + parser.getTable();
    }

    public DAO(String configFile) {
        connector = new SQLConnector(configFile);
        connect = connector.getConnect();
        parser = connector.getParser();
        sourceTable = parser.getDatabase() + "." + parser.getTable();
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
            throw new DatabaseException(sqlEx);
        }
        return true;
    }

    public void insertMessage(RequestMessage message) {
        try {
            preparedStatement = connect
                    .prepareStatement("INSERT INTO " + sourceTable + " (user_id, user_name, message, time) " +
                            "VALUES (default, ?, ?, ?)");

            preparedStatement.setString(1, "admin");
            preparedStatement.setString(2, message.message);
            preparedStatement.setString(3, "time");
            preparedStatement.executeUpdate();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            throw new DatabaseException(sqlEx);
        }
    }

    public String getAllMessages() {
        String result;
        try {
            preparedStatement = connect
                    .prepareStatement("SELECT user_name, message, time FROM " + sourceTable);
            resultSet = preparedStatement.executeQuery();
            result =  getStringFromResultSet(resultSet);
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
            preparedStatement.executeUpdate();
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
                            "FROM " + sourceTable  + " " +
                            "WHERE user_name = ?");
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();
            count =  resultSet.getInt("result");
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            throw new DatabaseException(sqlEx);
        }
        return count;
    }

    String getStringFromResultSet(ResultSet resultSet) throws SQLException {
        StringBuilder result = new StringBuilder();
        while (resultSet.next()) {
            String user_name = resultSet.getString("user_name");
            String message = resultSet.getString("message");
            String time = resultSet.getString("time");
            result.append("User: ").append(user_name);
            result.append("; message: ").append(message);
            result.append("; date: ").append(time).append(System.lineSeparator());
        }
        return result.toString();
    }
}
