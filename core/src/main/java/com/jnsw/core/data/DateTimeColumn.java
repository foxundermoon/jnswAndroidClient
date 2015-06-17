package com.jnsw.core.data;

/**
 * Created by foxundermoon on 2015/6/8.
 */
public class DateTimeColumn extends Column {
    public DateTimeColumn(String name) {
        super(name);
        setDbType("datetime(1)");
    }
}
