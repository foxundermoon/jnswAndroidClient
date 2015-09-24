package com.jnsw.android.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;
import com.jnsw.android.ui.R;
import com.jnsw.android.ui.widget.event.StopPlayAudioEvent;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.record.audio.MediaPlayerCompletionEvent;
import com.jnsw.core.record.audio.VoicePlayer;
import com.jnsw.core.util.Tip;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fox on 2015/9/24.
 */
public class AudioPlayerView extends LinearLayout implements View.OnClickListener {
    private String audioPath;
    private Context mContext;
    private ImageView voicePlayerBtn;
    private ProgressBar progressBar;
    private TextView textView;
    private VoicePlayer voicePlayer;
    private int voiceTime = 0; //时间
    private static final int REFRESH_PROGROSS = 44401;
    private static final int STOP_PLAY = 44402;
    private static final int START_PLAY = 44403;
    private static final int COMPLETION_PLAY = 44404;

    private static String CURRENT_PROGRRESS;
    Timer timer;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_PROGROSS:
                    progressBar.setProgress(msg.arg1);
                    break;
                case START_PLAY:
                    startPlay();
                    break;
                case STOP_PLAY:
                    stopPlay();
                    break;
                case COMPLETION_PLAY:
                   completionPlay();
                    break;

            }
        }
    };

    public boolean isPlaying() {
        return isPlaying;
    }

    private boolean isPlaying = false;

    public AudioPlayerView(Context context) {
        this(context, null);
    }


    public AudioPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AudioPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCustomView(context, attrs);

    }

    public AudioPlayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initCustomView(context, attrs);
    }

    public void setAudioPath(String path) {
        audioPath = path;
        MediaPlayer m = new MediaPlayer();
        try {
            m.setDataSource(path);
            m.prepare();
            voiceTime = m.getDuration();
            if (textView != null) {
                textView.setText(Math.ceil(voiceTime / 1000) + "″");
            }
            if (progressBar != null) {
                progressBar.setMax(voiceTime);
            }
            m.release();
            m = null;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void inflaterLayout(Context context) {
        LayoutInflater.from(context).inflate(R.layout.audio_player_layout, this, true);
    }

    private void initCustomView(Context context, AttributeSet attrs) {
        CustomApplication.getInstance().eventBus.register(this);

        mContext = context;
        Resources.Theme theme = context.getTheme();
        inflaterLayout(context);
        voicePlayerBtn = (ImageView) findViewById(R.id.voice_display_voice_play);
        textView = (TextView) findViewById(R.id.voice_display_voice_time);
        progressBar = (ProgressBar) findViewById(R.id.voice_display_voice_progressbar);
        voicePlayerBtn.setOnClickListener(this);

    }


    @Subscribe
    public void onrecivePlayTask(StopPlayAudioEvent event) {
        Message message = new Message();
        message.what = STOP_PLAY;
        handler.handleMessage(message);
    }

    @Subscribe
    public void onCompletion(MediaPlayerCompletionEvent event) {
        Message message = new Message();
        message.what = COMPLETION_PLAY;
        handler.handleMessage(message);

    }

    TimerTask currentPlayTask;

    private  void completionPlay() {
        isPlaying = false;
        voicePlayerBtn.setImageResource(R.drawable.globle_player_btn_play);
        progressBar.setProgress(0);
    }
    private void stopPlay() {
        if (isPlaying) {
            VoicePlayer.getInstance().stop();
            isPlaying = false;
        }
        voicePlayerBtn.setImageResource(R.drawable.globle_player_btn_play);
        if (currentPlayTask != null) {
            currentPlayTask.cancel();
        }
        progressBar.setProgress(0);
        if (timer != null) {
            timer.cancel();
        }

    }


    public void startPlay() {
        new StopPlayAudioEvent(this).post();
//        if (isPlaying || VoicePlayer.getInstance().isPlaying())
//            stopPlay();

        if (voicePlayer == null) {
            voicePlayer = VoicePlayer.getInstance();
        }
        File file = new File(audioPath);
        if (!file.exists()) {
            Tip.shortTip("播放文件不存在 " + audioPath);
        } else {
            voicePlayer.play(audioPath);
            isPlaying = true;
            timer = new Timer(true);
            currentPlayTask = new PlayingTask();
            timer.schedule(currentPlayTask, 100, 100);
            voicePlayerBtn.setImageResource(R.drawable.globle_player_btn_stop);
        }
    }


    @Override
    public void onClick(View v) {
        if (v == voicePlayerBtn) {
            if (isPlaying) {
                stopPlay();
            } else {
                startPlay();
            }
        }
    }

    public class PlayingTask extends TimerTask {

        @Override
        public void run() {
            Message message = new Message();
            if (isPlaying) {
                try {
                    int mPlayCurrentPosition = VoicePlayer.getInstance().getmMediaPlayer().getCurrentPosition();
                    message.what = REFRESH_PROGROSS;
                    message.arg1 = mPlayCurrentPosition;
                } catch (Exception e) {
                    message.what = STOP_PLAY;
                }
            } else {
                message.what = STOP_PLAY;
            }
            handler.sendMessage(message);


        }
    }
}
