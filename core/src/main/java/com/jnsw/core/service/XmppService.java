/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jnsw.core.service;

import android.app.Service;
import android.content.*;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.google.common.eventbus.Subscribe;
import com.jnsw.core.Constants;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.config.ClientConfig;
import com.jnsw.core.event.SendPacketEvent;
import com.jnsw.core.event.SendStringEvent;
import com.jnsw.core.util.EncryptUtil;
import com.jnsw.core.xmpp.LogUtil;
import com.jnsw.core.xmpp.XmppManager;
import com.jnsw.core.xmpp.daemon.HeartThreadRunnable;
import com.jnsw.core.xmpp.listener.PhoneStateChangeListener;
import com.jnsw.core.xmpp.receiver.ConnectivityReceiver;
import com.jnsw.core.xmpp.receiver.NotificationReceiver;
import com.jnsw.core.xmpp.receiver.SendPacketReceiver;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Service that continues to run in background and respond to the push
 * notification events from the server. This should be registered as service
 * in AndroidManifest.xml.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class XmppService extends Service {

    private static final String LOGTAG = LogUtil
            .makeLogTag(XmppService.class);

    public static final String SERVICE_NAME = "com.jnsw.core.service.NotificationService";
    private TelephonyManager telephonyManager;
    boolean enableBroadcastReceiver = false;
    //    private WifiManager wifiManager;
    //
    //    private ConnectivityManager connectivityManager;

    private BroadcastReceiver notificationReceiver;

    private BroadcastReceiver connectivityReceiver;
    private BroadcastReceiver sendPacketReceiver;

    private PhoneStateListener phoneStateListener;

    private ExecutorService executorService;

    private TaskSubmitter taskSubmitter;

    private TaskTracker taskTracker;

    private XmppManager xmppManager;

    private SharedPreferences sharedPrefs;

    private String deviceId;

    public XmppService() {
        notificationReceiver = new NotificationReceiver();
        connectivityReceiver = new ConnectivityReceiver(this);
        phoneStateListener = new PhoneStateChangeListener(this);
//        sendPacketReceiver = new SendPacketReceiver(this);
        executorService = Executors.newSingleThreadExecutor();
        taskSubmitter = new TaskSubmitter(this);
        taskTracker = new TaskTracker(this);
    }

    @Override
    public void onCreate() {
        Log.d(LOGTAG, "onCreate()...");
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        // wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        // connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        sharedPrefs = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);

        // Get deviceId
        deviceId = telephonyManager.getDeviceId();
        enableBroadcastReceiver = sharedPrefs.getBoolean(Constants.ENABLE_BROADCAST, false);
        // Log.d(LOGTAG, "deviceId=" + deviceId);
        Editor editor = sharedPrefs.edit();
        editor.putString(Constants.DEVICE_ID, deviceId);
        editor.commit();

        // If running on an emulator
        if (deviceId == null || deviceId.trim().length() == 0
                || deviceId.matches("0+")) {
            if (sharedPrefs.contains("EMULATOR_DEVICE_ID")) {
                deviceId = sharedPrefs.getString(Constants.EMULATOR_DEVICE_ID,
                        "");
            } else {
                deviceId = (new StringBuilder("EMU")).append(
                        (new Random(System.currentTimeMillis())).nextLong())
                        .toString();
                editor.putString(Constants.EMULATOR_DEVICE_ID, deviceId);
                editor.commit();
            }
        }
        Log.d(LOGTAG, "deviceId=" + deviceId);
        xmppManager = new XmppManager(this);
        ((CustomApplication) getApplication()).eventBus.register(this);
        taskSubmitter.submit(new Runnable() {
            public void run() {
                XmppService.this.start();
            }
        });
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d(LOGTAG, "onStart()...");
    }

    @Override
    public void onDestroy() {
        Log.d(LOGTAG, "onDestroy()...");
        stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOGTAG, "onBind()...");
        return null;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(LOGTAG, "onRebind()...");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(LOGTAG, "onUnbind()...");
        return true;
    }

    public static Intent getIntent() {
        return new Intent(SERVICE_NAME);
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public TaskSubmitter getTaskSubmitter() {
        return taskSubmitter;
    }

    public TaskTracker getTaskTracker() {
        return taskTracker;
    }

    public XmppManager getXmppManager() {
        return xmppManager;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPrefs;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void connect() {
        Log.d(LOGTAG, "connect()...");
        taskSubmitter.submit(new Runnable() {
            public void run() {
                XmppService.this.getXmppManager().connect();
            }
        });
    }

    public void disconnect() {
        Log.d(LOGTAG, "disconnect()...");
        taskSubmitter.submit(new Runnable() {
            public void run() {
                XmppService.this.getXmppManager().disconnect();
            }
        });
    }

    private void registerNotificationReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_SHOW_NOTIFICATION);
        filter.addAction(Constants.ACTION_NOTIFICATION_CLICKED);
        filter.addAction(Constants.ACTION_NOTIFICATION_CLEARED);
        registerReceiver(notificationReceiver, filter);
    }

    private void registerSendPacketReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.SEND_PACKET);
        sendPacketReceiver = new SendPacketReceiver(this);
        registerReceiver(sendPacketReceiver, intentFilter);
    }

    private void unRegisterSendPacketReceiver() {
        sendPacketReceiver = null;
        unregisterReceiver(sendPacketReceiver);
    }

    private void unregisterNotificationReceiver() {
        unregisterReceiver(notificationReceiver);
    }

    private void registerConnectivityReceiver() {
        Log.d(LOGTAG, "registerConnectivityReceiver()...");
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
        IntentFilter filter = new IntentFilter();
        // filter.addAction(android.net.wifi.WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, filter);
    }

    private void unregisterConnectivityReceiver() {
        Log.d(LOGTAG, "unregisterConnectivityReceiver()...");
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_NONE);
        unregisterReceiver(connectivityReceiver);
    }

    private void start() {
        Log.d(LOGTAG, "start()...");
        registerConnectivityReceiver();
        if (enableBroadcastReceiver) {
            registerSendPacketReceiver();
            registerNotificationReceiver();
        }
        // Intent intent = getIntent();
        // startService(intent);
        xmppManager.connect();
//        HeartThreadRunnable.setup(xmppManager);
//        if(xmppManager.getConnection().isConnected()){
//            xmppManager.getConnection().addPacketListener(new MessagePacketListener((MyApplication)getApplication()),new PacketFilter(){
//                @Override
//                public boolean accept(Packet packet) {
//                    return packet instanceof Message;
//                }
//            });
//            Log.d(Constants.RECEIVER_MESSAGE,"run ataddPacketListener( MessagePacketListener) ");
//        }
    }

    private void stop() {
        Log.d(LOGTAG, "stop()...");
        unregisterConnectivityReceiver();
        if (enableBroadcastReceiver) {
            unregisterNotificationReceiver();
            unRegisterSendPacketReceiver();
        }
        HeartThreadRunnable.shutDown();
        xmppManager.disconnect();
        executorService.shutdown();
    }


    @Subscribe
    public void sendPacketByEventBus(SendPacketEvent<Packet> event) {
        Packet packet = event.getEventData();
        xmppManager.sendPacketAsync(packet);

    }

    @Subscribe
    public void sendStringByEventBus(SendStringEvent event) {
        String msg = event.getEventData();
        String base64msg = EncryptUtil.encrBASE64ByGzip(msg);
        Message message = new Message();
        message.setLanguage("BASE64");
        message.setFrom(ClientConfig.getServerJid());
        message.setType(Message.Type.normal);
        message.setTo(ClientConfig.getLocalJid());
        message.setBody(base64msg);
        xmppManager.sendPacketAsync(message);
    }

    /**
     * Class for summiting a new runnable task.
     */
    public class TaskSubmitter {

        final XmppService xmppService;

        public TaskSubmitter(XmppService xmppService) {
            this.xmppService = xmppService;
        }

        @SuppressWarnings("unchecked")
        public Future submit(Runnable task) {
            Future result = null;
            if (!xmppService.getExecutorService().isTerminated()
                    && !xmppService.getExecutorService().isShutdown()
                    && task != null) {
                result = xmppService.getExecutorService().submit(task);
            }
            return result;
        }

    }

    /**
     * Class for monitoring the running task count.
     */
    public class TaskTracker {

        final XmppService xmppService;

        public int count;

        public TaskTracker(XmppService xmppService) {
            this.xmppService = xmppService;
            this.count = 0;
        }

        public void increase() {
            synchronized (xmppService.getTaskTracker()) {
                xmppService.getTaskTracker().count++;
                Log.d(LOGTAG, "Incremented task count to " + count);
            }
        }

        public void decrease() {
            synchronized (xmppService.getTaskTracker()) {
                xmppService.getTaskTracker().count--;
                Log.d(LOGTAG, "Decremented task count to " + count);
            }
        }

    }

}
