package com.jnsw.core.data;

/**
 * Created by foxundermoon on 2015/5/9.
 */
public class AppErrorMessage{
    public enum ErrorType{
        ConnectError,
        LoginError,
        SendPacketError,
        RegisterError,
        OtherError
    }
    public ErrorType errorType;
    public String message;
    public String cause;
}
