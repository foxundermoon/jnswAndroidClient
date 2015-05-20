package com.jnsw.core.event;

/**
 * Created by foxundermoon on 2015/5/18.
 */
public class HasUserOffLineEvent extends AppEvent<String> {
    public HasUserOffLineEvent(String userName) {
        setEventData(userName);
    }
}
