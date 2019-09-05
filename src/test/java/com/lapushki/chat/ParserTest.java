package com.lapushki.chat.java;

import com.lapushki.chat.db.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void parseRemoveSpaces(){
        String correctFile = "src/test/resources/connection.properties";
        Parser sut = new Parser(correctFile);
        Assert.assertEquals(sut.getPassword(), "password");
    }

    @Test
    public void parseSpaces(){
        String file = "src/test/resources/connectionSpaces.properties";
        Parser sut = new Parser(file);
        Assert.assertEquals(sut.getPassword(), "password");
    }
}
