package com.jnsw.core.record.audio;

import android.media.MediaRecorder;

import com.jnsw.core.record.audio.event.CompletedVoiceRecordEvent;

import java.io.File;
import java.io.IOException;

/**
 * Created by fox on 2015/9/24.
 */
public class VoiceRecorder {
    private VoiceRecorder() {
    }

    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    private String savePath;

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public VoiceRecordMessage getVoiceRecordMessage() {
        return voiceRecordMessage;
    }

    public void setVoiceRecordMessage(VoiceRecordMessage voiceRecordMessage) {
        this.voiceRecordMessage = voiceRecordMessage;
        setSavePath(voiceRecordMessage.getPath());
    }

    private VoiceRecordMessage voiceRecordMessage;

    public RecordState getRecordState() {
        return recordState;
    }

    public void setRecordState(RecordState recordState) {
        this.recordState = recordState;
    }

    private RecordState recordState;

    private static VoiceRecorder instance;

    public static synchronized VoiceRecorder getInstance() {
        if (instance == null) {
            instance = new VoiceRecorder();
        }
        return instance;
    }

    public int getSampleRateInHz() {
        return sampleRateInHz;
    }

    public void setSampleRateInHz(int sampleRateInHz) {
        this.sampleRateInHz = sampleRateInHz;
    }

    private int sampleRateInHz = 11025;
    private MediaRecorder recorder ;;


    /**
     * 开始录音
     *
     * @throws IOException
     */

    public boolean start(String savePath) throws IOException {
        setSavePath(savePath);
        return start();
    }
    public boolean start() throws IOException {
        String state = android.os.Environment.getExternalStorageState();
        if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
            throw new IOException("SD Card is not mounted,It is  " + state
                    + ".");
        }
        if (recorder == null) {
            setUp();
        }
        if (recordState == RecordState.STARTING) {
            voiceRecordMessage.setRecordState(RecordState.STARTING);
            new CompletedVoiceRecordEvent(voiceRecordMessage).post();
            setErrorMessage("录音被占用，请先停止");
            return false;
        } else {
            File directory = new File(getSavePath()).getParentFile();
            if (!directory.exists() && !directory.mkdirs()) {
                directory.mkdirs();
            }
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.setAudioSamplingRate(sampleRateInHz);
            recorder.setOutputFile(getSavePath());
            recorder.prepare();
            recorder.start();
            if (voiceRecordMessage != null) {
                voiceRecordMessage.setRecordState(RecordState.STARTED);
            }
            recordState = RecordState.STARTING;
            return true;
        }

    }

    private void setUp() {
        recorder = new MediaRecorder();
    }

    /**
     * 结束录音
     *
     * @throws IOException
     */
    public void stop() throws IOException {
        recorder.stop();
        recorder.release();
        recordState = RecordState.FINISHED;
        recorder =null;
    }

    /**
     * 获取录音时间
     *
     * @return
     */
    public double getAmplitude() {
        if (recorder != null) {
            return (recorder.getMaxAmplitude());
        }
        return 0;
    }

    public enum RecordState {
        STARTING,
        FINISHED,
        STARTED,
        ERROR,
    }
}