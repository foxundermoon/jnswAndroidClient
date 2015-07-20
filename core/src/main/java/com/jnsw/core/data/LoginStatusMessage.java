package com.jnsw.core.data;

/**
 * Created by foxundermoon on 2015/7/16.
 */
public class LoginStatusMessage {
    private int loginStatus;
    private  int netStatus;

    public LoginStatusMessage(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public LoginStatusMessage(int loginStatus, int netStatus) {

        this.loginStatus = loginStatus;
        this.netStatus = netStatus;
    }

    public boolean equalsLoginStatusCode(int code) {
        return code == loginStatus;
    }
    public boolean equalsNetStatusCode(int code) {
        return code == netStatus;
    }
    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public int getNetStatus() {
        return netStatus;
    }

    public void setNetStatus(int netStatus) {
        this.netStatus = netStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    private String errorMessage;
}
