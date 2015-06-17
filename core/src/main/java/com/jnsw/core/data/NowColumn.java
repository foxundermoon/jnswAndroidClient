package com.jnsw.core.data;

/**
 * Created by foxundermoon on 2015/6/8.
 */
public class NowColumn extends Column {
    public NowColumn(String name){
        super(name);
        setDbType("now");
    }
}
