package com.jnsw.core.event;

import com.jnsw.core.data.AppErrorMessage;

/**
 * Created by foxundermoon on 2015/5/9.
 */
public class AppErrorEvent extends AppEvent<AppErrorMessage> {
    public AppErrorEvent(AppErrorMessage message) {
        setEventData(message);
    }
}
