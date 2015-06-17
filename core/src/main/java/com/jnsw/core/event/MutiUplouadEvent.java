package com.jnsw.core.event;

import com.jnsw.core.CustomApplication;
import com.jnsw.core.data.FileMessage;
import com.jnsw.core.data.MutiFileMessage;

import java.util.List;

/**
 * Created by foxundermoon on 2015/6/9.
 */
public class MutiUplouadEvent extends AppEvent<MutiFileMessage> {
    public MutiUplouadEvent() {}
    public MutiUplouadEvent(MutiFileMessage files) {
        setEventData(files);
    }
    public void send(){
        CustomApplication.getInstance().eventBus.post(this);
    }
}
