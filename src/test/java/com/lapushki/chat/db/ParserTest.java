package com.lapushki.chat.db;

import com.lapushki.chat.db.Parser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void parseBrokenFile(){
        String correctFile = "src/test/resources/connectionWrong.properties";
        Parser sut = new Parser(correctFile);
        Assert.assertEquals(sut.getPassword(), "password");
    }

    @Test
    public void parseMissingParametersFile(){
        String correctFile = "src/test/resources/connectionMiss.properties";
        Parser sut = new Parser(correctFile);
        Assert.assertEquals(sut.getPassword(), "password");
    }

    @Test
    public void parseCorrectFile(){
        String correctFile = "src/test/resources/connection.properties";
        Parser sut = new Parser(correctFile);
        Assert.assertEquals(sut.getPassword(), "password");
    }

    @Test
    public void parseWithSpaces(){
        String file = "src/test/resources/connectionSpaces.properties";
        Parser sut = new Parser(file);
        Assert.assertEquals(sut.getPassword(), "password");
    }
}
