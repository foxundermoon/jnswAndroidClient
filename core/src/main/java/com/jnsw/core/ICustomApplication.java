package com.jnsw.core;

import com.google.common.eventbus.EventBus;
import org.jivesoftware.smack.packet.Packet;

import java.util.Map;

/**
 * Created by foxundermoon on 2015/1/29.
 */
public interface ICustomApplication {
    void sendPacketByXmppAsync(Packet packet);
    void sendStringByXmppAsync(String stringData);
    EventBus defaultEventBus = null;
}
