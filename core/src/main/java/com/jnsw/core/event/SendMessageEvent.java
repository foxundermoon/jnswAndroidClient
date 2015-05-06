package com.jnsw.core.event;

import com.jnsw.core.CustomApplication;
import com.jnsw.core.data.Message;


/**
 * Created by foxundermoon on 2015/4/27.
 */
public class SendMessageEvent extends AppEvent<Message> {
    public SendMessageEvent(Message message) {
        setEventData(message);
    }

    public SendMessageEvent(String name, Message message) {
        setEventData(message);
        setName(name);
    }

    public SendMessageEvent(String name) {
        setName(name);
    }
    public void post() {
        CustomApplication.getInstance().eventBus.post(this);
    }
}
