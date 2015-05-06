package com.jnsw.core.event;

/**
 * Created by foxundermoon on 2015/2/3.
 */
public class ReceivedStringEvent extends ReceivedEvent<String> {
    public ReceivedStringEvent() {
    }

    public ReceivedStringEvent(String data) {
        setEventData(data);
    }

    public ReceivedStringEvent(String name, String data) {
        setEventData(data);
        setName(name);
    }
}
