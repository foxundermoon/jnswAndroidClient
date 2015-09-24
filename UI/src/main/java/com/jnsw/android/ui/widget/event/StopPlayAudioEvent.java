package com.jnsw.android.ui.widget.event;

import com.jnsw.android.ui.widget.AudioPlayerView;
import com.jnsw.core.event.AppEvent;

/**
 * Created by fox on 2015/9/24.
 */
public class StopPlayAudioEvent extends AppEvent<AudioPlayerView> {
    public StopPlayAudioEvent(AudioPlayerView message) {
        setEventData(message);
    }

    public StopPlayAudioEvent() {
    }
}
