package com.jnsw.core.data;

/**
 * Created by foxundermoon on 2015/4/22.
 */
public class Command {
    public static String GetOnlineUsers = "GetOnlineUsers"; //向服务获取全部在线用户
    public static String GetOnlineUsersResponse = "GetOnlineUsersResponse";  //接受全部在线任务
    //public static String GetUserTask = "GetUserTask";
    //public static String GetUserTaskResponse = "GetUserTaskResponse";
    //public static String GetUpLine = "GetUpLine";
    //public static String GetUpLineResponse = "GetUpLineResponse";
    //public static String GetDownLine = "GetDownLine";
    //public static String GetDownLineResponse = "GetDownLineResponse";
    public static String UserLogin = "UserLogin";  //有新用户登录
    public static String OnLineAtOtherPlace = "OnLineAtOtherPlace"; //用户被顶下线
    public static String UserOffLine = "UserOffLine"; //用户离线
    public static String ErrorMessage = "ErrorMessage";  //错误相关消息

    public static String SendTask = "SendTask";   // 下发任务
    public static String SendTaskResponse = "SendTaskResponse";  //收到下发任务
    public static String DataTable = "DataTable";  //数据库/表相关操作
    public static String Response = "Response";  //返回消息
    public static String SqlDataTable = "SqlDataTable";  //数据库/表相关操作
    public static String MySqlDataTable = "MySqlDataTable";  //数据库/表相关操作

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
