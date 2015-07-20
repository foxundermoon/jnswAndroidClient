package com.jnsw.core.data;

import com.jnsw.core.CustomApplication;
import com.jnsw.core.event.LoginEvent;

/**
 * Created by foxundermoon on 2015/7/16.
 */
public class LoginMessage {
    private String password;

    public LoginMessage() {
    }

    public LoginMessage(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    private String userName;
    public void post(){
        CustomApplication.getInstance().eventBus.post(new LoginEvent(this));
    }
}
