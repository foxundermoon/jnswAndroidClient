package com.jnsw.core.record.audio;

import com.jnsw.core.CustomApplication;

/**
 * Created by fox on 2015/9/24.
 */
public class VoiceRecordMessage {

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    private String uuid = CustomApplication.getInstance().creatUUID();

    public int getSampleRateInHz() {
        return sampleRateInHz;
    }

    public void setSampleRateInHz(int sampleRateInHz) {
        this.sampleRateInHz = sampleRateInHz;
    }

    private int sampleRateInHz = 11025;

    public VoiceRecorder.RecordState getRecordState() {
        return recordState;
    }

    public void setRecordState(VoiceRecorder.RecordState recordState) {
        this.recordState = recordState;
    }

    private VoiceRecorder.RecordState recordState;



    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isStarting() {
        return isStarting;
    }

    public void setIsStarting(boolean isStarting) {
        this.isStarting = isStarting;
    }

    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    private String path;
    private boolean isStarting;



}
