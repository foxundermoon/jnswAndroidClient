package com.jnsw.core.xmpp.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.google.common.base.Strings;
import com.jnsw.core.Constants;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.config.ClientConfig;
import com.jnsw.core.util.EncryptUtil;
import com.jnsw.core.service.AppService;
import org.jivesoftware.smack.packet.Message;

@Deprecated
public class SendStringReceiver extends BroadcastReceiver {
    AppService appService;
    public SendStringReceiver(AppService appService) {
        this.appService = appService;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String uuid = intent.getStringExtra(Constants.SEND_STRING);
        if (!Strings.isNullOrEmpty(uuid)) {
            String data  = (String) CustomApplication.getInstance().shareMap.get(uuid);
            if (!Strings.isNullOrEmpty(data)) {
                Message message = new Message();
                message.setTo(ClientConfig.getServerJid());
                message.setFrom(ClientConfig.getLocalJid());
                message.setLanguage("BASE64");
                message.setBody(EncryptUtil.encrBASE64ByGzip(data));
                message.setPacketID(uuid);
                appService.getAppManager().sendPacketAsync(message);
            }
        }
    }
}
