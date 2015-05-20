package com.jnsw.core.event;

/**
 * Created by foxundermoon on 2015/5/18.
 */
public class HasUserOnLineEvent extends AppEvent<String> {
    public HasUserOnLineEvent(String userName) {
        setEventData(userName);
    }
}
