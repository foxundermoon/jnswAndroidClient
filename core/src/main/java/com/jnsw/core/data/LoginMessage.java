package com.jnsw.core.data;

/**
 * Created by foxundermoon on 2015/5/6.
 */
public class LoginMessage {
    private boolean success;
    private String cause;

    public LoginMessage(boolean success, String cause) {
        this.success=success;
        this.cause = cause;
    }

    public LoginMessage(boolean success) {

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
}
