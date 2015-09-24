package com.jnsw.core.record.audio;

import android.media.MediaPlayer;

import com.jnsw.core.record.audio.event.MediaPlayerCompletionEvent;
import com.jnsw.core.record.audio.event.OnMediaPlayerErrorEvent;

import java.io.IOException;

/**
 * Created by fox on 2015/9/24.
 */
public class VoicePlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private static VoicePlayer instance;

    public int getErrorWhat() {
        return errorWhat;
    }

    public void setErrorWhat(int errorWhat) {
        this.errorWhat = errorWhat;
    }

    private  int errorWhat =-1;

    public static synchronized VoicePlayer getInstance() {
        if (instance == null) {
            instance = new VoicePlayer();
        }
        return instance;
    }

    public MediaPlayer getmMediaPlayer() {
        return mMediaPlayer;
    }

    public void setmMediaPlayer(MediaPlayer mMediaPlayer) {
        this.mMediaPlayer = mMediaPlayer;
    }

    private MediaPlayer mMediaPlayer;

    private VoicePlayer() {
        if (mMediaPlayer == null) {
            setUp();
        }
    }

    public void setUp() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnErrorListener(this);
        }

    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public boolean isPlaying() {
        if (mMediaPlayer == null) {
            return false;
        }
        return mMediaPlayer.isPlaying();
    }

    public boolean play(String path) {

        if (isPlaying()) {
            stop();
        }
        release();
        setUp();
        try {
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;

    }

    public void stop() {
        if (isPlaying()) {
            mMediaPlayer.stop();
            release();
        }

    }

    public void pause() {
        if (isPlaying()) {
            mMediaPlayer.pause();
        }

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
            new MediaPlayerCompletionEvent(this).post();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        setErrorWhat(what);
        new OnMediaPlayerErrorEvent(this).post();
        return false;
    }
}
