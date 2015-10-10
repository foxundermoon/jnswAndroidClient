package com.jnsw.core.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jnsw.core.R;

/**
 * Created by fox on 2015/10/10.
 */
public class RecordVoiceView extends ImageView {
    public RecordVoiceView(Context context) {
        this(context, null);
    }

    public RecordVoiceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordVoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.drawable.voice_volume_1);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RecordVoiceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setVoiceVolume(final int voiceVolume) {
        Handler handler = getHandler();
        if (handler != null)
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (voiceVolume <= 1) {
                        setImageResource(R.drawable.voice_volume_1);
                    } else if (voiceVolume == 2) {
                        setImageResource(R.drawable.voice_volume_2);

                    } else if (voiceVolume == 3) {
                        setImageResource(R.drawable.voice_volume_3);
                    } else if (voiceVolume == 4) {
                        setImageResource(R.drawable.voice_volume_4);
                    } else if (voiceVolume == 5) {
                        setImageResource(R.drawable.voice_volume_5);
                    } else {
                        setImageResource(R.drawable.voice_volume_6);
                    }
                }
            });
    }

    public void setDecibel(double db) {
        if (db < 30) {
            setVoiceVolume(1);
        } else if (db >= 30 && db < 60) {
            setVoiceVolume(2);
        } else if (db >= 60 && db < 70) {
            setVoiceVolume(3);
        } else if (db >= 70 && db < 80) {
            setVoiceVolume(4);
        } else if (db >= 80 && db < 90) {
            setVoiceVolume(5);
        } else if (db >= 90) {
            setVoiceVolume(6);
        }
    }

    public void setMaxAmplitude(double maxAmplitude) {
        int BASE = 1;
        double ratio = (double) maxAmplitude / BASE;
        double db = 0;
        if (ratio > 1)
            db = 20 * Math.log10(ratio);
        setDecibel(db);
    }

}
