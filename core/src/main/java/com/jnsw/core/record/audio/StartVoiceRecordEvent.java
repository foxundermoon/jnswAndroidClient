package com.jnsw.core.record.audio;

import com.jnsw.core.event.AppEvent;

/**
 * Created by fox on 2015/9/24.
 */
public class StartVoiceRecordEvent extends AppEvent<VoiceRecordMessage> {
    public StartVoiceRecordEvent(VoiceRecordMessage message) {
        setEventData(message);
    }
}
