package com.lapushki.chat.db;

import java.sql.*;

/**
 * Created by kate-c on 04/09/2019.
 */
public class SQLConnector implements AutoCloseable {
    // JDBC variables for opening and managing connection
    private Connection connect = null;
    private ResultSet resultSet = null;
    private PreparedStatement preparedStatement = null;

    public SQLConnector(String fileName) {
        try {
            Parser.parseConfig(fileName);

            connect = DriverManager.getConnection(Parser.getUrl(), Parser.getUser(), Parser.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertMessage(String userName, String message, String time) {
        try {
            preparedStatement = connect
                    .prepareStatement("INSERT INTO messages (user_id, user_name, message, time) " +
                            "VALUES (default, ?, ?, ?)");

            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, message);
            preparedStatement.setString(3, time);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public String getAllMessages() {
        String result;

        try {
            preparedStatement = connect
                    .prepareStatement("SELECT user_name, message, time FROM messages");
            resultSet = preparedStatement.executeQuery();
            result =  getStringFromResultSet(resultSet);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            result = "ERRRRRROR";
        }

        return result;
    }

    void deleteAllMesseges() {
        try {
            preparedStatement = connect
                    .prepareStatement("DELETE FROM messages");
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    int countMessagesSentByGivenPerson(String userName) {
        int count;

        try {
            preparedStatement = connect
                    .prepareStatement("SELECT COUNT(*) as result " +
                            "FROM messages  " +
                            "WHERE user_name = ?");
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();

            count =  resultSet.getInt("result");
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            count = -1;
        }

        return count;
    }

    public void close() {
        try { connect.close(); } catch(SQLException se) { /*can't do anything */ }
        try { resultSet.close(); } catch(SQLException se) { /*can't do anything */ }
    }


    private String getStringFromResultSet(ResultSet resultSet) throws SQLException {
        String result = "";
        while (resultSet.next()) {
            String user_name = resultSet.getString("user_name");
            String message = resultSet.getString("message");
            Date time = resultSet.getDate("time");
            result += "User: " + user_name;
            result += "; message: " + message;
            result += "; date: " + time + "\n";
        }

        return result;
    }

}
