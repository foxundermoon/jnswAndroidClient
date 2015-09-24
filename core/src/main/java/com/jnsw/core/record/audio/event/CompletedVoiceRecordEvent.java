package com.jnsw.core.record.audio.event;

import com.jnsw.core.event.AppEvent;
import com.jnsw.core.record.audio.VoiceRecordMessage;

/**
 * Created by fox on 2015/9/24.
 */
public class CompletedVoiceRecordEvent extends AppEvent<VoiceRecordMessage> {
    public CompletedVoiceRecordEvent(VoiceRecordMessage message) {
        setEventData(message);
    }
}
