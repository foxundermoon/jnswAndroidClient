package com.jnsw.core.xmpp.listener;

import com.jnsw.core.CustomApplication;
import com.jnsw.core.event.ReceivedXmppIQEvent;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;

/**
 * Created by foxundermoon on 2015/1/15.
 */
@Deprecated
public class IqPacketListener implements PacketListener{
    @Override
    public void processPacket(Packet packet) {
        if(packet instanceof IQ){
            IQ iq = (IQ)packet;
            ReceivedXmppIQEvent<IQ> event = new ReceivedXmppIQEvent<IQ>();
            event.setEventData(iq);
            CustomApplication.getInstance().eventBus.post(event);
        }
    }
}
