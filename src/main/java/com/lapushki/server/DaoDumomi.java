package com.lapushki.server;

import com.lapushki.chat.model.RequestMessage;
import com.lapushki.chat.model.ResponseMessage;

import java.util.Collection;

public class DaoDumomi {
    public boolean saveDataBase(Connection connection, RequestMessage responseMessage) {
        return true;
    }

    public Collection<ResponseMessage>  getHistory() {
        return null;
    }
}
