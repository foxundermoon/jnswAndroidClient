package com.jnsw.core.event;

import com.jnsw.core.data.MutiFileMessage;

/**
 * Created by foxundermoon on 2015/6/9.
 */
public class MutiUploadedEvent extends AppEvent<MutiFileMessage> {
    public MutiUploadedEvent(MutiFileMessage eventData) {
        setEventData(eventData);
    }
}
