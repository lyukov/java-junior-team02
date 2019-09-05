package com.lapushki.chat.db;

import com.lapushki.chat.db.SQLConnector;
import org.junit.Test;

import java.sql.SQLException;

public class connTest {

    @Test
    public void shouldGetNoExceptionsWhenDbConnect() {
        SQLConnector sqlConnector = new SQLConnector();
    }

    @Test
    public void shouldDeleteRecordWhenSimpleQuery() throws SQLException {
      /*  try (
                final Connection connection = DriverManager.getConnection(dbUrl);
                final Statement statement = connection.createStatement()
        ) {

            final int rowsAffected = statement.executeUpdate("delete from CLIENT where LOGIN = 'disabled@acme.com'");
            //NEVER do such SQL queries!
        }*/
    }
}
