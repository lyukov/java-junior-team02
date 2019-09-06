package com.lapushki.chat.server.history.roomed;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ejb.Local;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class RoomedHistoryTest {
    LocalDateTime time = null;
    RoomedHistory sut;

    @Test
    public void shouldLogInRoomSaver() throws IOException{
        String message1 = "test1";
        String message2 = "test2";

        String roomName1 = "room1";
        String roomName2 = "room2";

        sut.save(message1, time, roomName1);
        sut.save(message2, time, roomName2);



    }













    private static void cleanRecursivelyDFS(File directory) {
        for(File currentFile: directory.listFiles()) {
            if(!currentFile.exists()) return;
            if(currentFile.isDirectory()) {
                cleanRecursivelyDFS(currentFile);
                currentFile.delete();
            } else {
                currentFile.delete();
            }
        }
    }

    @Before
    public void beforeTest() throws IOException {
        time = LocalDateTime.now();
        sut = new RoomedFileSwitchingHistoryAccessObject();

        String directoryName = Paths.get(".","resources", "History").toString();
        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        else {
            cleanRecursivelyDFS(new File("./resources"));
        }
    }

    @After
    public void afterTest() throws  IOException {
        cleanRecursivelyDFS(new File("./resources"));
    }

}
