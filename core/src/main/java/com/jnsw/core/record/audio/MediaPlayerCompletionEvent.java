package com.jnsw.core.record.audio;

import com.jnsw.core.event.AppEvent;

/**
 * Created by fox on 2015/9/24.
 */
public class MediaPlayerCompletionEvent extends AppEvent<VoicePlayer> {
    public MediaPlayerCompletionEvent(VoicePlayer voicePlayer) {
        setEventData(voicePlayer);
    }

    public MediaPlayerCompletionEvent() {
    }
}
