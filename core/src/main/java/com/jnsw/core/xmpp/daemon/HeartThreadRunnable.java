package com.jnsw.core.xmpp.daemon;

import android.util.Log;
import com.jnsw.core.Constants;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.config.ClientConfig;
import com.jnsw.core.xmpp.XmppManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import java.util.Map;

/**
 * Created by foxundermoon on 2014/11/28.
 */
public class HeartThreadRunnable implements Runnable {
    private static String TAG = HeartThreadRunnable.class.getSimpleName();
    public static boolean interrupted = false;
    private static int defaultDuration = 30 * 1000;
    private XmppManager xmppManager;
    private Presence presence;
    static CustomApplication customApplication;
    static Map<String, Object> xmppstatus;


    public static HeartThreadRunnable getInstance(XmppManager xmppManager) {
        synchronized (HeartThreadRunnable.class) {
            if (instance == null) {
                instance = new HeartThreadRunnable(xmppManager, defaultDuration);
                customApplication = CustomApplication.getInstance();
                xmppstatus = (Map) customApplication.shareMap.get(Constants.XMPP_STATUS);
            }
        }
        return instance;
    }

    private static HeartThreadRunnable instance;

    public boolean isInterrupt() {
        return interrupt;
    }


    public static void setInterrupt(boolean _interrupt) {
        interrupt = _interrupt;
        interrupted = true;

    }

    private static boolean interrupt = false;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    private int duration;

    public HeartThreadRunnable(XmppManager xmppManager, int duration) {
        this.xmppManager = xmppManager;
        presence = new Presence(Presence.Type.available);
        presence.setFrom(ClientConfig.getLocalJid());
        presence.setTo(ClientConfig.getServerJid());
        presence.setStatus("ping");
        setDuration(duration);

    }

    @Override
    public void run() {
        while (!interrupt) {
            heartBeat();
            try {
                Thread.currentThread().sleep((long) duration);
            } catch (InterruptedException ignore) {
                Log.d(TAG, "thread sleep catch exception when net heart beat!");
            }
        }
    }

    private void heartBeat() {
        XMPPConnection xmppConnection = xmppManager.getConnection();
        if (xmppConnection != null && xmppConnection.isConnected()) {
            if (xmppstatus.containsKey("online")) {

                if (!(Boolean) xmppstatus.get("online")) {
                    presence.setStatus("online");
                } else {
                    presence.setStatus("ping");
                }
                synchronized (xmppConnection) {
                    xmppConnection.sendPacket(presence);
                }
            }else{
                xmppstatus.put("online",false);
            }

        }
    }

    public static void setup(XmppManager xmppManager) {
        HeartThreadRunnable runnable = HeartThreadRunnable.getInstance(xmppManager);
        runnable.setInterrupt(false);
        new Thread(runnable).start();
    }

    public static void shutDown() {
        HeartThreadRunnable.setInterrupt(true);
    }
}
