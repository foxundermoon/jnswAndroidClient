package com.jnsw.core.data;

/**
 * Created by foxundermoon on 2015/4/22.
 */
public class Command {
    private String name;
    private String operation;
    private String condition;
    private boolean needBroadcast;
    private boolean needResponse;
    private String sql;

    public String getName() {
        return name;
    }

    public Command setName(String name) {
        this.name = name;
        return this;
    }
    public Command setNeedBroadcast(){
        needBroadcast =true;
        return this;
    }
    public Command setNeedBroadcast(boolean value){
        needBroadcast = value;
        return this;
    }
    public Command setNeedResponse(){
        needResponse=true;
        return this;
    }
    public Command setNeedResponse(boolean value){
        needBroadcast =value;
        return this;
    }
    public  boolean isNeedBroadcast(){
        return needBroadcast;
    }
    public boolean isNeedResponse(){
        return needResponse;
    }

    public String getOperation() {
        return operation;
    }

    public Command setOperation(String operation) {
        this.operation = operation;
        return this;
    }

    public String getCondition() {
        return condition;
    }

    public Command setCondition(String condition) {
        this.condition = condition;
        return this;
    }

    public String getSql() {
        return sql;
    }

    public Command setSql(String sql) {
        this.sql = sql;
        return this;
    }
}
