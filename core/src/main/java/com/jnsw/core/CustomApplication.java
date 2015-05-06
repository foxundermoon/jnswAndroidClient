package com.jnsw.core;

import android.app.Application;
import android.content.Intent;
import com.google.common.eventbus.EventBus;
import com.jnsw.core.http.MyHttpClient;
import org.jivesoftware.smack.packet.Packet;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by foxundermoon on 2014/10/30.
 */
public class CustomApplication extends Application implements ICustomApplication {
    public Map<String, Object> shareMap;
    public  boolean isLogin = false;
    public EventBus eventBus;
    private static CustomApplication applicationInstance;
    public MyHttpClient httpClient;

    @Deprecated
    @Override
    public void sendPacketByXmppAsync(Packet packet) {
        String uuid = creatUUID();
        shareMap.put(uuid, packet);
        Intent intent = new Intent();
        intent.setAction(Constants.SEND_PACKET);
        intent.putExtra(Constants.SEND_PACKET,uuid);
        sendBroadcast(intent);
    }

    @Deprecated
    @Override
    public void sendStringByXmppAsync(String stringData) {
        String uuid = creatUUID();
        shareMap.put(uuid, stringData);
        Intent intent = new Intent();
        intent.setAction(Constants.SEND_STRING);
        intent.putExtra(Constants.SEND_STRING, uuid);
        sendBroadcast(intent);

    }

    private String creatUUID() {
        return UUID.randomUUID().toString().replace("-","");
    }

    public static CustomApplication getInstance(){
        return applicationInstance;
    }

//    public void sendBroadcast(String action,String shareMapKey) {
//        Intent intent = new Intent();
//        intent.setAction(action);
//        intent.putExtra(action,shareMapKey);
//        sendBroadcast(intent);
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        shareMap = new HashMap<String, Object>();
        Map<String,Objects> xmppStatus = new HashMap<String, Objects>();
        shareMap.put(Constants.XMPP_STATUS,xmppStatus);
        eventBus = new EventBus(Constants.CUSTOM_EVENT_BUS);
        applicationInstance = this;
        httpClient = MyHttpClient.getInstance();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public CustomApplication() {
        super();
    }
}
