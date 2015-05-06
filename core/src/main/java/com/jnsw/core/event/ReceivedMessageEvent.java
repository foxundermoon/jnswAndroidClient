package com.jnsw.core.event;

import com.jnsw.core.CustomApplication;
import com.jnsw.core.data.Message;

/**
 * Created by foxundermoon on 2015/4/27.
 */
public class ReceivedMessageEvent extends AppEvent<Message> {
    public ReceivedMessageEvent(Message message) {
        setEventData(message);
    }

    public ReceivedMessageEvent(String name, Message message) {
        setName(name);
        setEventData(message);
    }

    public ReceivedMessageEvent(String name) {
        setName(name);
    }

    public void post() {
        CustomApplication.getInstance().eventBus.post(this);
    }
}
