package com.jnsw.android.xunjian.db.exception;

/**
 * Created by foxundermoon on 2015-8-1.
 */
public class NotDatabaseTableClazzException extends Exception {
    public NotDatabaseTableClazzException() {
        super("this class is not database class");
    }
}
