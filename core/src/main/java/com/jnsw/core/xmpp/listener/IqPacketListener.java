package com.jnsw.core.xmpp.listener;

import com.jnsw.core.CustomApplication;
import com.jnsw.core.event.ReceivedXmppIQEvent;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;

/**
 * Created by foxundermoon on 2015/1/15.
 */
public class IqPacketListener implements PacketListener{
    private IqHandler iqHandler;

    public static synchronized IqPacketListener getInstance() {
        if(instance==null)
            instance = new IqPacketListener();
        return instance;
    }

    public static  void setInstance(IqPacketListener instance) {
        IqPacketListener.instance = instance;
    }

    public IqHandler getIqHandler() {
        return iqHandler;
    }

    public void setIqHandler(IqHandler iqHandler) {
        this.iqHandler = iqHandler;
    }

    private IqPacketListener() {
    }

    private static IqPacketListener instance;
    @Override
    public void processPacket(Packet packet) {
        if(packet instanceof IQ){
            IQ iq = (IQ)packet;
            ReceivedXmppIQEvent<IQ> event = new ReceivedXmppIQEvent<IQ>();
            event.setEventData(iq);
            CustomApplication.getInstance().eventBus.post(event);
        }
    }
    public interface IqHandler{
        void onIQ(IQ iq);
    }
}
