package com.jnsw.core.event;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

/**
 * Created by foxundermoon on 2015/2/3.
 */
public class SendXmppPacketEvent extends XmppEvent<Packet> {
    public SendXmppPacketEvent(Packet packet) {
        setEventData(packet);
    }
}
