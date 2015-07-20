package com.jnsw.core.event;

import com.jnsw.core.data.LoginStatusMessage;

/**
 * Created by foxundermoon on 2015/7/14.
 */
public class LoginStatusEvent extends AppEvent<LoginStatusMessage> {
    public LoginStatusEvent(LoginStatusMessage loginStatusMessage) {
        setEventData(loginStatusMessage);
    }
}
