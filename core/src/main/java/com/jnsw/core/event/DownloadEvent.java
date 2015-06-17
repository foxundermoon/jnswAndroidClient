package com.jnsw.core.event;

import com.jnsw.core.data.FileMessage;

/**
 * Created by foxundermoon on 2015/5/7.
 */
public class DownloadEvent extends AppEvent<FileMessage> {
    public DownloadEvent(FileMessage fileMessage) {
        super();
        setEventData(fileMessage);
    }
}
