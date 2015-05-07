package com.jnsw.core.event;

/**
 * Created by foxundermoon on 2015/5/6.
 */
public class LoginEvent extends AppEvent<LoginMessage> {
    public LoginEvent(boolean success){
        setEventData(new LoginMessage(success));
    }
    public LoginEvent(boolean success,String cause){
        setEventData(new LoginMessage(success,cause));
    }
}
