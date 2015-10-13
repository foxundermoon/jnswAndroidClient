package com.jnsw.core.record.audio;

import android.media.MediaRecorder;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.jnsw.core.R;
import com.jnsw.core.record.audio.event.CompletedVoiceRecordEvent;
import com.jnsw.core.util.FileUtil;
import com.jnsw.core.util.SDCardUtils;
import com.jnsw.core.util.StickTip;
import com.jnsw.core.util.Tip;
import com.jnsw.core.view.RecordVoiceView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fox on 2015/9/24.
 */
public class VoiceRecorder {
    View rootView;
    View prepareView;
    View recordingView;
    RecordVoiceView recordVoiceView;
    TextView recordingText;
    Timer timer;

    private VoiceRecorder() {
        stickTip = new StickTip(R.layout.record_tip_layout);
        rootView = stickTip.getContentView();
        prepareView = rootView.findViewById(R.id.prepare_mic);
        recordingView = rootView.findViewById(R.id.recording_mic);
        recordVoiceView = (RecordVoiceView) rootView.findViewById(R.id.record_voice_volume);
        recordingText = (TextView) rootView.findViewById(R.id.recording_text);
    }

    private StickTip stickTip;

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
    private MediaRecorder recorder;
    ;


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
//        String state = android.os.Environment.getExternalStorageState();
//        if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
//            throw new IOException("SD Card is not mounted,It is  " + state
//                    + ".");
//        }
        rootView.post(new Runnable() {
            @Override
            public void run() {
                recordingView.setVisibility(View.GONE);
                prepareView.setVisibility(View.VISIBLE);
            }
        });
        stickTip.show();
        if (recorder == null) {
            setUp();
        }
        if (recordState == RecordState.STARTING) {
            voiceRecordMessage.setRecordState(RecordState.STARTING);
            new CompletedVoiceRecordEvent(voiceRecordMessage).post();
            setErrorMessage("录音被占用，请先停止");
            recordingText.setText("录音器被占用 请先停止");
            return false;
        } else {
            File directory = new File(getSavePath()).getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
                FileUtils.forceMkdir(directory);
            }
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.setAudioSamplingRate(sampleRateInHz);
            recorder.setOutputFile(getSavePath());
            recorder.prepare();
            recorder.start();
            timer = new Timer();
            timer.scheduleAtFixedRate(new UpdateVoiceTask(), 0, 100);
            rootView.post(new Runnable() {
                @Override
                public void run() {
                    recordingView.setVisibility(View.VISIBLE);
                    prepareView.setVisibility(View.GONE);
                }
            });
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
        if (recorder != null) {
            try {
                recorder.stop();
            } catch (Exception ignore) {
                Tip.shortTipCenter(ignore.getMessage());
                ignore.printStackTrace();
            } finally {
                timer.cancel();
                timer = null;
                recorder.release();
                recorder = null;
                if (stickTip != null)
                    stickTip.hide();
            }
        }
        recordState = RecordState.FINISHED;
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

    private static ArrayList<String> getDevMountList() {
        String[] toSearch = new String[0];
        try {
            toSearch = FileUtils.readFileToString(new File("/etc/vold.fstab")).split(" ");
            ArrayList<String> out = new ArrayList<String>();
            for (int i = 0; i < toSearch.length; i++) {
                if (toSearch[i].contains("dev_mount")) {
                    if (new File(toSearch[i + 2]).exists()) {
                        out.add(toSearch[i + 2]);
                    }
                }
            }
            return out;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String getExternalSdCardPath() {

        if (SDCardUtils.isMounted()) {
            File sdCardFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            return sdCardFile.getAbsolutePath();
        }

        String path = null;

        File sdCardFile = null;

        ArrayList<String> devMountList = getDevMountList();

        for (String devMount : devMountList) {
            File file = new File(devMount);

            if (file.isDirectory() && file.canWrite()) {
                path = file.getAbsolutePath();

                String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
                File testWritable = new File(path, "test_" + timeStamp);

                if (testWritable.mkdirs()) {
                    testWritable.delete();
                } else {
                    path = null;
                }
            }
        }

        if (path != null) {
            sdCardFile = new File(path);
            return sdCardFile.getAbsolutePath();
        }

        return null;
    }

    class UpdateVoiceTask extends TimerTask {

        @Override
        public void run() {
            if (recordVoiceView != null) {
                recordVoiceView.setMaxAmplitude(getAmplitude());
            }
        }
    }
}
