package com.jnsw.core.event;

import com.jnsw.core.data.FileMessage;


/**
 * Created by foxundermoon on 2015/5/9.
 */
public class DownloadedEvent  extends AppEvent<FileMessage> {
    public DownloadedEvent(FileMessage fileMessage) {
        setEventData(fileMessage);
    }
}
