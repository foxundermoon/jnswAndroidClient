package com.jnsw.core.data;

/**
 * Created by foxundermoon on 2015/6/8.
 */
public class MessageFactory {
    public static Message creatInsertMessage(){
        Message msg = new Message();
        msg.creatCommand()
                .setName(Command.DataTable)
                .setOperation(Operation.insert);
        return msg;
    }
   public static Message creatQueryMessage(String sql) {
       Message msg = new Message();
       msg.creatCommand().setName(Command.DataTable)
               .setOperation(Operation.query)
               .setSql(sql);
       return msg;
   }
    public static Message creatRunsqlMessage(String sql) {
        Message msg = new Message();
        msg.creatCommand().setName(Command.DataTable)
                .setOperation(Operation.runsql)
                .setSql(sql);
        return msg;
    }
    public  static Message creatUpdateMessage(){
      Message msg = new Message();
        msg.creatCommand().setName(Command.DataTable)
                .setOperation(Operation.update);
        return msg;
    }
    public  static Message creatDeleteMessage(){
        Message msg = new Message();
        msg.creatCommand().setName(Command.DataTable)
                .setOperation(Operation.delete);
        return msg;
    }
}
