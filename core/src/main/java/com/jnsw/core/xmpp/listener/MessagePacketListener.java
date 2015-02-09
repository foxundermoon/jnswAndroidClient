package com.jnsw.core.xmpp.listener;

import android.util.Log;
import com.google.common.base.Strings;
import com.jnsw.core.Constants;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.event.ReceivedMessageEvent;
import com.jnsw.core.event.ReceivedStringEvent;
import com.jnsw.core.util.EncryptUtil;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

/**
 * Created by foxundermoon on 2014/11/13.
 */
public class MessagePacketListener implements PacketListener {
    private CustomApplication customApplication;

    public synchronized static MessagePacketListener getInstance() {
        if (instance == null) {
            instance = new MessagePacketListener();
        }
        return instance;
    }

    public static void setInstance(MessagePacketListener instance) {
        MessagePacketListener.instance = instance;
    }

    static private MessagePacketListener instance;

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    private MessageHandler messageHandler;

    private MessagePacketListener() {
        this.customApplication = CustomApplication.getInstance();
    }

    @Override
    public void processPacket(Packet packet) {
        Log.d(Constants.RECEIVER_MESSAGE, "MessagePacketListener.processPacket()...");
        Log.d(Constants.RECEIVER_MESSAGE, "packet.toXML()=" + packet.toXML());
        if (packet instanceof Message) {
            Message message = (Message)packet;
            ReceivedMessageEvent<Message> event = new ReceivedMessageEvent<Message>();
            event.setEventData(message);
            CustomApplication.getInstance().eventBus.post(event);
            if (!Strings.isNullOrEmpty(message.getLanguage())) {
                if (message.getLanguage().toUpperCase().contains("BASE64")) {
                    if (!Strings.isNullOrEmpty(message.getBody())) {
                        ReceivedStringEvent receivedStringEvent = new ReceivedStringEvent();
                        receivedStringEvent.setEventData(EncryptUtil.decryptBASE64ByGzip(message.getBody()));
                        CustomApplication.getInstance()
                                .eventBus
                                .post(receivedStringEvent);
                    }
                }
            }
            if (messageHandler != null) {
                messageHandler.onMessage((Message) packet);
            }
        } else {
            Log.d(Constants.RECEIVER_MESSAGE, "packet is not message...");
        }
    }

    public interface MessageHandler {
        void onMessage(Message message);
    }
}
