package com.jnsw.core.data;

/**
 * Created by foxundermoon on 2015/5/6.
 */
public class LoginedMessage {
    private boolean success;
    private String cause;

    public int getLoginStatusCode() {
        return loginStatusCode;
    }

    public void setLoginStatusCode(int loginStatusCode) {
        this.loginStatusCode = loginStatusCode;
    }

    public int getNetStatus() {
        return netStatus;
    }

    public void setNetStatus(int netStatus) {
        this.netStatus = netStatus;
    }

    private int loginStatusCode;
    private  int netStatus;
    public LoginedMessage(boolean success, String cause) {
        this.success=success;
        this.cause = cause;
    }

    public LoginedMessage(boolean success) {

        this.success = success;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "------success:"+success  +"  ;\n"
                +"login status code:"+loginStatusCode +"   \n"
                +"nets tatus code:"+ netStatus  +"\n"
                +"cause:"+ cause;
    }
}
