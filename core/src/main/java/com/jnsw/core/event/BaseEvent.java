package com.jnsw.core.event;

import java.util.Objects;

/**
 * Created by foxundermoon on 2015/2/3.
 */
public abstract class BaseEvent<DataType> {
    private String name;
    private DataType eventData;

    public BaseEvent(String name) {
        this.name = name;
    }

    public BaseEvent(DataType eventData) {
        this.eventData = eventData;
    }

    public BaseEvent(String name, DataType eventData) {
        this.name = name;
        this.eventData = eventData;
    }
    public BaseEvent(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getEventData() {
        return eventData;
    }

    public void setEventData(DataType eventData) {
        this.eventData = eventData;
    }
}
