package com.jnsw.core.xmpp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.jnsw.core.Constants;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.util.EncryptUtil;
import com.jnsw.core.service.XmppService;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

public class SendPacketReceiver extends BroadcastReceiver {
    private XmppService xmppService;
    private CustomApplication customApplication;
    public SendPacketReceiver(XmppService xmppService) {
        this.xmppService = xmppService;
        customApplication = CustomApplication.getInstance();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (customApplication == null)
            customApplication = (CustomApplication) xmppService.getApplication();
        String uuid = intent.getStringExtra(Constants.SEND_PACKET);
        if (uuid != null) {
            Packet packet = (Packet) customApplication.shareMap.get(uuid);
            if(packet !=null) {
                if (packet instanceof Message) {
                    Message message = (Message) packet;
                    String tmpBase64=EncryptUtil.encrBASE64ByGzip(message.getBody());
                    message.removeBody((String) null);
                    message.setBody(tmpBase64);
                    xmppService.getXmppManager().sendPacketAsync(message);
                }else {
                    xmppService.getXmppManager().sendPacketAsync(packet);
                }
                customApplication.shareMap.remove(uuid);
                Log.d(Constants.SEND_PACKET, packet.toXML());
            }else{
                Log.d(Constants.SEND_PACKET,"the packet from intent is null");
            }
        }else{
            Log.d(Constants.SEND_PACKET,"the uuid is null  from intent");
        }
    }
}
