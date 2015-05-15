package com.jnsw.core.xmpp.listener;

import android.util.Log;
import com.jnsw.core.Constants;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.data.Command;
import com.jnsw.core.data.task.DeserilizeAndBroadcastTask;
import com.jnsw.core.event.ReceivedMessageEvent;
import com.jnsw.core.event.SendMessageEvent;
import com.jnsw.core.util.EncryptUtil;
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
            Message message = (Message) packet;
            String body = null;
            if (message.getLanguage() != null && message.getLanguage().toUpperCase().contains("BASE64")) {
                body = EncryptUtil.decryptBASE64ByGzip(message.getBody());
            } else {
                body = message.getBody();
            }
            String jsonMsg = null;
            if ("BASE64".equalsIgnoreCase(message.getLanguage())) {
                jsonMsg = EncryptUtil.decryptBASE64ByGzip(body);
            } else {
                jsonMsg = body;
            }
            com.jnsw.core.data.Message msg = null;
            try {
                msg = com.jnsw.core.data.Message.fromJson(jsonMsg);
            } catch (Exception e) {
               msg = new com.jnsw.core.data.Message();
                msg.setError(e.getMessage());
            }
            if(msg.getCommand().isNeedResponse()){
                com.jnsw.core.data.Message responseMsg = new com.jnsw.core.data.Message();
                responseMsg.setId(msg.getId());
                responseMsg.creatCommand().setName(Command.Response);
                responseMsg.setToUser(msg.getFromUser());
                responseMsg.setFromUser(msg.getToUser());
                new SendMessageEvent(responseMsg).post();
            }
            new ReceivedMessageEvent(msg).post();
        }
    }
}



