package com.lapushki.chat.java;

import com.lapushki.chat.db.SQLConnector;
import jdk.nashorn.internal.AssertsEnabled;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

public class connTest {

    @Test
    public void shouldGetNoExceptionsWhenDbConnect() {
        SQLConnector sqlConnector = new SQLConnector("src/test/resources/connectionTest.properties");
    }

    @Test
    public void shouldDeleteAll(){
        SQLConnector sut = new SQLConnector("src/test/resources/connectionTest.properties");
        sut.insertMessage("name1", "message1", "2019-12-31 23:59:59");
        sut.insertMessage("name2", "message2", "2019-12-31 00:59:59");
        sut.deleteAllMesseges();
        String res = sut.getAllMessages();
        String expectedMessage = "";
        Assert.assertEquals(res, expectedMessage);
    }

    @Test
    public void shouldInsertMessage() throws SQLException {
        SQLConnector sut = new SQLConnector("src/test/resources/connectionTest.properties");
        sut.deleteAllMesseges();
        sut.insertMessage("name", "message", "2019-12-31 23:59:59");
        String res = sut.getAllMessages();
        Assert.assertEquals(res, "User: name; message: message; date: 2019-12-31\n");
    }

    @Test
    public void shouldGetHistory() throws SQLException {
        SQLConnector sut = new SQLConnector("src/test/resources/connectionTest.properties");
        sut.deleteAllMesseges();
        sut.insertMessage("name1", "message1", "2019-12-31 23:59:59");
        sut.insertMessage("name2", "message2", "2019-12-31 00:59:59");
        String res = sut.getAllMessages();
        String expectedMessage = "User: name1; message: message1; date: 2019-12-31\n" +
                "User: name2; message: message2; date: 2019-12-31\n";
        Assert.assertEquals(res, expectedMessage);
    }
}
