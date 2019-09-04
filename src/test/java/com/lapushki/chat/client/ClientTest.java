package com.lapushki.chat.client;

import com.lapushki.chat.model.RequestMessage;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.mockito.Mockito.*;

public class ClientTest {
    @Test
    public void shouldDisconnectWhenExitCommand(){
        final byte[] inputLine = "\\exit".getBytes();
        final ByteArrayInputStream inStream = new ByteArrayInputStream(inputLine);
        System.setIn(inStream);
        Scanner scanner = new Scanner(System.in);

        Connection mockConnection = mock(Connection.class);

        Client sut = new Client(scanner, mockConnection);
        sut.run();

        verify(mockConnection).disconnect();
    }

    @Test
    public void shouldNotDisconnectWhenNotExitCommand(){
        final byte[] inputLine = ("\\snd Hello!" + System.lineSeparator() + "\\exit"  ).getBytes();
        final ByteArrayInputStream inStream = new ByteArrayInputStream(inputLine);
        System.setIn(inStream);
        Scanner scanner = new Scanner(System.in);

        Connection mockConnection = mock(Connection.class);
        RequestMessage requestMessage = new RequestMessage("\\snd", "Hello!");
        when(mockConnection.formMessageObject("\\snd Hello!")).thenReturn(requestMessage);

        Client sut = new Client(scanner, mockConnection);
        sut.run();

        verify(mockConnection).sendMessage(requestMessage);
        verify(mockConnection).disconnect();
    }
}
