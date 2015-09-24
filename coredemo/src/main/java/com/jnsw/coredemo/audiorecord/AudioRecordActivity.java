package com.jnsw.coredemo.audiorecord;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jnsw.android.ui.widget.AudioPlayerView;
import com.jnsw.core.record.audio.VoiceRecorder;
import com.jnsw.core.util.Tip;
import com.jnsw.coredemo.R;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.UUID;

@EActivity(R.layout.activity_audio_record)
public class AudioRecordActivity extends AppCompatActivity {
    @ViewById(R.id.voice_record_btn)
    Button recordBtn;


    @ViewById
    LinearLayout play_linearlayout;
    private static final int RECORD_NO = 0; // 不在录音
    private static final int RECORD_ING = 1; // 正在录音
    private static final int RECORD_ED = 2; // 完成录音
    private int mRecord_State = 0; // 录音的状态

    private int mMAXVolume;// 最大音量高度
    private int mMINVolume;// 最小音量高度
    private static final String PATH = "/sdcard/Record/";// 录音存储路径
    private String mRecordPath;// 录音的存储名称
    VoiceRecorder voiceRecorder;

    @Touch(R.id.voice_record_btn)
    boolean onTouch(View btn, MotionEvent event) {
//        if (event.isButtonPressed(R.id.voice_record_btn)) {
//            Tip.shortTip("pressed");
//        }
//        if(event.)
        switch (event.getAction()) {
            // 开始录音
            case MotionEvent.ACTION_DOWN:
                if (mRecord_State != RECORD_ING) {
                    // 开始动画效果
                    startRecordLightAnimation();
                    // 修改录音状态
                    mRecord_State = RECORD_ING;
                    // 设置录音保存路径
                    mRecordPath = PATH + UUID.randomUUID().toString()
                            + ".acc";
                    // 实例化录音工具类
                    voiceRecorder = VoiceRecorder.getInstance();
                    try {
                        // 开始录音
//                        voiceRecorder.setSavePath(mRecordPath);
                        if (!voiceRecorder.start(mRecordPath)) {
                            Tip.shortTip(voiceRecorder.getErrorMessage());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    new Thread(new Runnable() {
//
//                        public void run() {
//                            // 初始化录音时间
//                            mRecord_Time = 0;
//                            while (mRecord_State == RECORD_ING) {
//                                // 大于最大录音时间则停止录音
//                                if (mRecord_Time >= MAX_TIME) {
//                                    mRecordHandler.sendEmptyMessage(0);
//                                } else {
//                                    try {
//                                        // 每隔200毫秒就获取声音音量并更新界面显示
//                                        Thread.sleep(200);
//                                        mRecord_Time += 0.2;
//                                        if (mRecord_State == RECORD_ING) {
//                                            mRecord_Volume = mRecordUtil
//                                                    .getAmplitude();
//                                            mRecordHandler
//                                                    .sendEmptyMessage(1);
//                                        }
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        }
//                    }).start();
                }
                break;
            // 停止录音
            case MotionEvent.ACTION_UP:
                if (mRecord_State == RECORD_ING) {
                    // 停止动画效果
                    stopRecordLightAnimation();
                    // 修改录音状态
                    mRecord_State = RECORD_ED;
                    try {
                        // 停止录音
                       VoiceRecorder.getInstance().stop();
                        // 初始录音音量
//                        mRecord_Volume = 0;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    AudioPlayerView playerView = new AudioPlayerView(this);
                    playerView.setAudioPath(mRecordPath);
                    play_linearlayout.addView(playerView);
                    playerView.startPlay();

                    // 如果录音时间小于最短时间
//                    if (mRecord_Time <= MIN_TIME) {
//                        // 显示提醒
//                        Toast.makeText(VoiceActivity.this, "录音时间过短",
//                                Toast.LENGTH_SHORT).show();
//                        // 修改录音状态
//                        mRecord_State = RECORD_NO;
//                        // 修改录音时间
//                        mRecord_Time = 0;
//                        // 修改显示界面
//                        mRecordTime.setText("0″");
//                        mRecordProgressBar.setProgress(0);
//                        // 修改录音声音界面
//                        ViewGroup.LayoutParams params = mRecordVolume
//                                .getLayoutParams();
//                        params.height = 0;
//                        mRecordVolume.setLayoutParams(params);
//                    } else {
//                        // 录音成功,则显示录音成功后的界面
//                        mRecordLayout.setVisibility(View.GONE);
//                        mRecord.setVisibility(View.GONE);
//                        mDisplayVoiceLayout.setVisibility(View.VISIBLE);
//                        mDisplayVoicePlay
//                                .setImageResource(R.drawable.globle_player_btn_play);
//                        mDisplayVoiceProgressBar.setMax((int) mRecord_Time);
//                        mDisplayVoiceProgressBar.setProgress(0);
//                        mDisplayVoiceTime.setText((int) mRecord_Time + "″");
//                        send.setVisibility(View.VISIBLE);
//                    }
                }
                break;
        }
        return false;
    }

    Handler mRecordLightHandler = new Handler() {

        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    if (mRecord_State == RECORD_ING) {
//                        mRecordLight_1.setVisibility(View.VISIBLE);
//                        mRecordLight_1_Animation = AnimationUtils.loadAnimation(
//                                VoiceActivity.this, R.anim.voice_anim);
//                        mRecordLight_1.setAnimation(mRecordLight_1_Animation);
//                        mRecordLight_1_Animation.startNow();
//                    }
//                    break;
//
//                case 1:
//                    if (mRecord_State == RECORD_ING) {
//                        mRecordLight_2.setVisibility(View.VISIBLE);
//                        mRecordLight_2_Animation = AnimationUtils.loadAnimation(
//                                VoiceActivity.this, R.anim.voice_anim);
//                        mRecordLight_2.setAnimation(mRecordLight_2_Animation);
//                        mRecordLight_2_Animation.startNow();
//                    }
//                    break;
//                case 2:
//                    if (mRecord_State == RECORD_ING) {
//                        mRecordLight_3.setVisibility(View.VISIBLE);
//                        mRecordLight_3_Animation = AnimationUtils.loadAnimation(
//                                VoiceActivity.this, R.anim.voice_anim);
//                        mRecordLight_3.setAnimation(mRecordLight_3_Animation);
//                        mRecordLight_3_Animation.startNow();
//                    }
//                    break;
//                case 3:
//                    if (mRecordLight_1_Animation != null) {
//                        mRecordLight_1.clearAnimation();
//                        mRecordLight_1_Animation.cancel();
//                        mRecordLight_1.setVisibility(View.GONE);
//
//                    }
//                    if (mRecordLight_2_Animation != null) {
//                        mRecordLight_2.clearAnimation();
//                        mRecordLight_2_Animation.cancel();
//                        mRecordLight_2.setVisibility(View.GONE);
//                    }
//                    if (mRecordLight_3_Animation != null) {
//                        mRecordLight_3.clearAnimation();
//                        mRecordLight_3_Animation.cancel();
//                        mRecordLight_3.setVisibility(View.GONE);
//                    }
//
//                    break;
//            }
        }
    };
    /**
     * 开始动画效果
     */
    private void startRecordLightAnimation() {
        mRecordLightHandler.sendEmptyMessageDelayed(0, 0);
        mRecordLightHandler.sendEmptyMessageDelayed(1, 1000);
        mRecordLightHandler.sendEmptyMessageDelayed(2, 2000);
    }

    /**
     * 停止动画效果
     */
    private void stopRecordLightAnimation() {
        mRecordLightHandler.sendEmptyMessage(3);
    }
}
