package com.jnsw.core.event;

import com.jnsw.core.data.LoginedMessage;

/**
 * Created by foxundermoon on 2015/5/6.
 */
public class LoginedEvent extends AppEvent<LoginedMessage> {
    public LoginedEvent(boolean success){
        setEventData(new LoginedMessage(success));
    }
    public LoginedEvent(boolean success,String cause){
        setEventData(new LoginedMessage(success,cause));
    }
    public LoginedEvent(LoginedMessage loginedMessage){
        setEventData(loginedMessage);
    }
}
