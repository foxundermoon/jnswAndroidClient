package com.jnsw.core.record.audio;

import com.jnsw.core.event.AppEvent;

/**
 * Created by fox on 2015/9/24.
 */
public class OnMediaPlayerError extends AppEvent<VoicePlayer> {
    public OnMediaPlayerError(VoicePlayer voicePlayer) {
        setEventData(voicePlayer);
    }

    public OnMediaPlayerError() {
    }
}
