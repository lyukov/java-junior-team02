package com.lapushki.chat.client;

import com.lapushki.chat.model.RequestMessage;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConnectionTest {
    @Test
    public void shouldGetRequestMessage() throws IOException {
        Socket mockSocket = mock(Socket.class);
        when(mockSocket.getInputStream()).thenReturn(System.in);
        when(mockSocket.getOutputStream()).thenReturn(System.out);


        Connection connection = new Connection(mockSocket);
        RequestMessage msg = connection.formMessageObject("/snd Hello!");

        RequestMessage msg2 = connection.formMessageObject("/hist");

        RequestMessage msg3 = connection.formMessageObject("/chid Vlad");

        assertEquals(msg.message, "Hello!");
        assertEquals(msg.command, "/snd");

        assertEquals(msg2.message, "");
        assertEquals(msg2.command, "/hist");

        assertEquals(msg3.message, "Vlad");
        assertEquals(msg3.command, "/chid");
    }
}
