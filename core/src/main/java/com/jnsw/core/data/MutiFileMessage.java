package com.jnsw.core.data;

import com.google.common.eventbus.Subscribe;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.event.MutiUploadedEvent;
import com.jnsw.core.event.MutiUplouadEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by foxundermoon on 2015/6/9.
 */
public class MutiFileMessage {
    private List<FileMessage> files;
    private Callback<MutiFileMessage> callback;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    private String uuid = UUID.randomUUID().toString();

    public MutiFileMessage setCallback(Callback<MutiFileMessage> callback) {
        this.callback = callback;
        return this;
    }

    private boolean uploadSuccess;
    private List<String> errorMessages;

    public MutiFileMessage addError(String error) {
        if (errorMessages == null)
            errorMessages = new ArrayList<String>();
        errorMessages.add(error);
        return this;
    }

    public MutiFileMessage addFile(FileMessage file) {
        if (files == null)
            files = new ArrayList<FileMessage>();
        files.add(file);
        return this;
    }

    public List<String> getErrorMessage() {
        return errorMessages;
    }

    public Callback<MutiFileMessage> getCallback() {
        return callback;
    }

    public boolean isUploadSuccess() {
        return uploadSuccess;
    }

    public List<FileMessage> getFiles() {
        return files;
    }

    public MutiFileMessage setFiles(List<FileMessage> files) {
        this.files = files;
        return this;
    }

    public void sendToUpload() {
        CustomApplication.getInstance().eventBus.register(this);
        new MutiUplouadEvent(this).send();
    }

    @Subscribe
    public void onUploaded(MutiUploadedEvent event) {
        if (uuid.equals(event.getEventData().getUuid())) {
            if (callback != null)
                callback.onCallback(event.getEventData());
            CustomApplication.getInstance().eventBus.unregister(this);
        }
    }

    public void setUploadSuccess(boolean uploadSuccess) {
        this.uploadSuccess = uploadSuccess;
    }
}
