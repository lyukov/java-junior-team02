package com.lapushki.chat.server.history.roomed;

import com.lapushki.chat.server.history.saver.SwitchingFileSaver;
import com.lapushki.chat.server.history.saver.SwitchingSaverTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.*;

import javax.ejb.Local;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

public class RoomedHistoryTest {
    LocalDateTime time = null;
    RoomedHistory sut;

    @Test
    public void isLogInDiferentRoomsShouldLogInDifferentDirectories() throws IOException{
        String message1 = "test1";
        String message2 = "test2";

        String roomName1 = "room1";
        String roomName2 = "room2";

        sut.save(message1, time, roomName1);
        sut.save(message2, time, roomName2);

        BufferedReader reader1 = new BufferedReader(new FileReader("./resources/History/room1"
                + SwitchingFileSaver.fileNameFormat("history", time, 0)));
        BufferedReader reader2 = new BufferedReader(new FileReader("./resources/History/room2"
                + SwitchingFileSaver.fileNameFormat("history", time, 0)));

        assertThat(reader1.readLine()).isEqualTo(message1);
        assertThat(reader1.readLine()).isNull();
        assertThat(reader2.readLine()).isEqualTo(message2);
        assertThat(reader2.readLine()).isNull();


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
