package com.jnsw.core.event;

import java.util.List;

/**
 * Created by foxundermoon on 2015/5/7.
 */
public class UploadEvent extends AppEvent<FileMessage> {
    public UploadEvent(FileMessage fileMessage){
        super();
        setEventData(fileMessage);
    }
}
