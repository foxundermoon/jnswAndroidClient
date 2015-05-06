package com.jnsw.core.event;

/**
 * Created by foxundermoon on 2015/2/3.
 */
public class SendStringEvent extends XmppEvent<String> {
    public SendStringEvent(String data) {
        setEventData(data);
    }
    public SendStringEvent(){}

    public SendStringEvent(String name, String data) {
        setEventData(data);
        setName(name);
    }
}
