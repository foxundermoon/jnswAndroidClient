package com.jnsw.core.record.audio;

import com.jnsw.core.event.AppEvent;

/**
 * Created by fox on 2015/9/24.
 */
public class CompletedVoiceRecordEvent extends AppEvent<VoiceRecordMessage> {
    public CompletedVoiceRecordEvent(VoiceRecordMessage message) {
        setEventData(message);
    }
}
