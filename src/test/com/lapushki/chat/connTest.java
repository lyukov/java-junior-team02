package com.lapushki.chat;

import com.lapushki.chat.db.SQLConnector;
import org.junit.Test;

public class connTest {

    @Test(expected = java.sql.SQLException.class)
    public void shouldThrowExceptionIfConNotEstablished() {
        String fileName = "src/main/resources/connectionWrong.properties";
        SQLConnector sqlConnector = new SQLConnector(fileName);
    }

    @Test
    public void shouldEstablishConnection() {
        String fileName = "src/main/resources/connection.properties";
        SQLConnector sqlConnector = new SQLConnector(fileName);
    }
}
