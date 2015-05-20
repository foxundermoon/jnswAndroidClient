package com.jnsw.core.event;

import com.jnsw.core.data.Message;

/**
 * Created by foxundermoon on 2015/5/19.
 */
public class SendTaskEvent extends AppEvent<Message>{
    public SendTaskEvent(Message msg) {
        setEventData(msg);
    }
}
