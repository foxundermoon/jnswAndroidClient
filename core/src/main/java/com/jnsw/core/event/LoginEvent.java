package com.jnsw.core.event;

import com.jnsw.core.CustomApplication;
import com.jnsw.core.data.LoginMessage;
import com.jnsw.core.data.LoginedMessage;

/**
 * Created by foxundermoon on 2015/7/16.
 */
public class LoginEvent extends AppEvent<LoginMessage> {
   public LoginEvent(LoginMessage loginMessage){
       setEventData(loginMessage);
   }
}
