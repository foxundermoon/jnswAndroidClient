package com.jnsw.core.xmpp.listener;

import android.util.Log;
import com.jnsw.core.Constants;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.config.ClientConfig;
import com.jnsw.core.data.Command;
import com.jnsw.core.data.task.DeserilizeAndBroadcastTask;
import com.jnsw.core.event.*;
import com.jnsw.core.util.EncryptUtil;
import com.jnsw.core.util.L;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

/**
 * Created by foxundermoon on 2014/11/13.
 */
public class MessagePacketListener implements PacketListener {
    @Override
    public void processPacket(Packet packet) {
//        Log.d(Constants.RECEIVER_MESSAGE, "MessagePacketListener.processPacket()...");
//        Log.d(Constants.RECEIVER_MESSAGE, "packet.toXML()=" + packet.toXML());
        if (packet instanceof Message) {
            CustomApplication app = CustomApplication.getInstance();
            Message message = (Message) packet;
            String jsonMsg = null;
            if ("BASE64".equalsIgnoreCase(message.getLanguage())) {
                jsonMsg = EncryptUtil.decryptBASE64ByGzip(message.getBody());
            } else {
                jsonMsg = message.getBody();
            }
            com.jnsw.core.data.Message msg = null;
            try {
                msg = com.jnsw.core.data.Message.fromJson(jsonMsg);
                msg.setCommand(CustomApplication.getInstance().gson.fromJson(message.getSubject(),Command.class));
            } catch (Exception e) {
                msg = new com.jnsw.core.data.Message();
                msg.setError(e.getMessage());
            }
            if (msg.getCommand().isNeedResponse()) {
                com.jnsw.core.data.Message responseMsg = new com.jnsw.core.data.Message();
                responseMsg.setId(msg.getId());
                responseMsg.creatCommand().setName(Command.Response);
                responseMsg.setToUser(msg.getFromUser());
                responseMsg.setFromUser(msg.getToUser());
                new SendMessageEvent(responseMsg).post();
            }
            if (Command.UserLogin.equalsIgnoreCase(msg.getCommand().getName())) {
                String userName = (String) msg.getProperty("UserName");
                if (ClientConfig.getUsrName().equalsIgnoreCase(userName)) {
                    app.eventBus.post(new LoginedEvent(true));
                }
                app.eventBus.post(new HasUserOnLineEvent(userName));
            }
            if (Command.UserOffLine.equalsIgnoreCase(msg.getCommand().getName())) {
                String userName = (String) msg.getProperty("UserName");
                app.eventBus.post(new HasUserOffLineEvent(userName));
            }
            if(Command.SendTask.equalsIgnoreCase(msg.getCommand().getName())){
                app.eventBus.post(new SendTaskEvent(msg));
            }
            ReceivedMessageEvent event = new ReceivedMessageEvent(msg);
            app.eventBus.post(event);
        }
    }
}



