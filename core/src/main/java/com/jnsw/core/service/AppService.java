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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.common.eventbus.Subscribe;
import com.jnsw.core.Constants;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.appmanager.task.MutiUploadTask;
import com.jnsw.core.appmanager.task.DownloadTask;
import com.jnsw.core.appmanager.task.UploadTask;
import com.jnsw.core.config.ClientConfig;
import com.jnsw.core.data.LoginMessage;
import com.jnsw.core.data.LoginedMessage;
import com.jnsw.core.data.LoginStatusMessage;
import com.jnsw.core.data.StatusCode;
import com.jnsw.core.data.Task;
import com.jnsw.core.event.*;
import com.jnsw.core.util.EncryptUtil;
import com.jnsw.core.util.L;
import com.jnsw.core.xmpp.LogUtil;
import com.jnsw.core.appmanager.AppManager;
import com.jnsw.core.xmpp.listener.PhoneStateChangeListener;

import net.qiujuer.genius.nettool.NetModel;
import net.qiujuer.genius.nettool.Ping;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
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
public class AppService extends Service {

    private static final String LOGTAG = LogUtil
            .makeLogTag(AppService.class);

    public static final String SERVICE_NAME = "com.jnsw.core.service.NotificationService";
    private TelephonyManager telephonyManager;
    //    boolean enableBroadcastReceiver = false;
    Timer timer = new Timer();

    //    private WifiManager wifiManager;
    //
    //    private ConnectivityManager connectivityManager;

//    private BroadcastReceiver notificationReceiver;

    private BroadcastReceiver connectivityReceiver;
//    private BroadcastReceiver sendPacketReceiver;

    private PhoneStateListener phoneStateListener;

    private ExecutorService executorService;

    private TaskSubmitter taskSubmitter;

    private TaskTracker taskTracker;

    private AppManager appManager;

    private SharedPreferences sharedPrefs;

    private String deviceId;

    public AppService() {
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

//        sharedPrefs = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,
//                Context.MODE_PRIVATE);

        // Get deviceId
        deviceId = telephonyManager.getDeviceId();
//        enableBroadcastReceiver = sharedPrefs.getBoolean(Constants.ENABLE_BROADCAST, false);
        // Log.d(LOGTAG, "deviceId=" + deviceId);
//        Editor editor = sharedPrefs.edit();
//        editor.putString(Constants.DEVICE_ID, deviceId);
        ClientConfig.putString(Constants.DEVICE_ID, deviceId);

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
//                editor.putString(Constants.EMULATOR_DEVICE_ID, deviceId);
                ClientConfig.putString(Constants.EMULATOR_DEVICE_ID, deviceId);
//                editor.commit();
            }
        }
        Log.d(LOGTAG, "deviceId=" + deviceId);
        appManager = new AppManager(this);
        ((CustomApplication) getApplication()).eventBus.register(this);
//        taskSubmitter.submit(new Runnable() {
//            public void run() {
//                AppService.this.start();
//            }
//        });
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
        Intent intent = new Intent(CustomApplication.getInstance().getApplicationContext(), AppService.class);
//        intent.setPackage("com.jnsw.service");
        return intent;
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

    public AppManager getAppManager() {
        return appManager;
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
                AppService.this.getAppManager().connect();
            }
        });
    }

    public void disconnect() {
        Log.d(LOGTAG, "disconnect()...");
        taskSubmitter.submit(new Runnable() {
            public void run() {
                AppService.this.getAppManager().disconnect();
            }
        });
    }

//    private void registerNotificationReceiver() {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Constants.ACTION_SHOW_NOTIFICATION);
//        filter.addAction(Constants.ACTION_NOTIFICATION_CLICKED);
//        filter.addAction(Constants.ACTION_NOTIFICATION_CLEARED);
//        registerReceiver(notificationReceiver, filter);
//    }

//    private void registerSendPacketReceiver() {
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Constants.SEND_PACKET);
//        registerReceiver(sendPacketReceiver, intentFilter);
//    }

//    private void unRegisterSendPacketReceiver() {
//        sendPacketReceiver = null;
//        unregisterReceiver(sendPacketReceiver);
//    }
//
//    private void unregisterNotificationReceiver() {
//        unregisterReceiver(notificationReceiver);
//    }

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
        try {
            unregisterReceiver(connectivityReceiver);
        } catch (Exception ignore) {
            L.e(this.getClass(), "unregisterReceiver :" + connectivityReceiver);
        }
    }


    private void start() {
        Log.d(LOGTAG, "start()...");
        registerConnectivityReceiver();
//        if (enableBroadcastReceiver) {
//            registerSendPacketReceiver();
//            registerNotificationReceiver();
//        }
        appManager.connect();
    }

    private void stop() {
        Log.d(LOGTAG, "stop()...");
        unregisterConnectivityReceiver();
//        if (enableBroadcastReceiver) {
//            unregisterNotificationReceiver();
//            unRegisterSendPacketReceiver();
//        }
        appManager.disconnect();
//        executorService.shutdown();
    }


    @Subscribe
    public void sendXmppPacketByEventBus(SendXmppPacketEvent event) {
        Packet packet = event.getEventData();
        appManager.sendPacketAsync(packet);
    }

    @Subscribe
    public void onLogin(LoginEvent event) {
        responsedLogin = false;
        LoginMessage loginMessage = event.getEventData();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (!responsedLogin) {
                    CustomApplication.getInstance().eventBus.post(new LoginStatusEvent(new LoginStatusMessage(StatusCode.LOGIN_TIME_OUT)));
                }
            }
        };
        timer.schedule(task, 10 * 1000);
        start();
    }

    private boolean responsedLogin = false;

    @Subscribe
    public void onReceivedLoginStatus(LoginStatusEvent event) {
        boolean canResponse = false;
        LoginStatusMessage loginStatusMessage = event.getEventData();
        LoginedMessage loginedMessage = new LoginedMessage(false);

        if (loginStatusMessage.equalsLoginStatusCode(StatusCode.LOGIN_SUCCESS)) {
            loginedMessage.setLoginStatusCode(StatusCode.LOGIN_SUCCESS);
            canResponse = true;
            loginedMessage.setSuccess(true);
        } else if (loginStatusMessage.equalsLoginStatusCode(StatusCode.LOGIN_ALREADY)) {
            loginedMessage.setSuccess(true);
            canResponse = true;
            loginedMessage.setLoginStatusCode(StatusCode.LOGIN_ALREADY);
        } else if (loginStatusMessage.equalsLoginStatusCode(StatusCode.LOGIN_FAILED) || loginStatusMessage.equalsLoginStatusCode(StatusCode.LOGIN_TIME_OUT)) {
            if (getNetworkType() == StatusCode.NETWORN_NONE) {
                loginStatusMessage.setErrorMessage("当前没有网络，请检查连接");
                loginStatusMessage.setNetStatus(StatusCode.NETWORN_NONE);
                canResponse = true;
            } else {
                String xmppHost = ClientConfig.getXmppHost();
                Ping ping = new Ping(xmppHost);
                ping.start();
                if (ping.getError() == NetModel.SUCCEED) {
                    loginStatusMessage.setNetStatus(StatusCode.NETWORK_CAN_PING_MESSAGE_SERVER);
                    loginStatusMessage.setErrorMessage("当前网络可以ping通消息服务器，但是登录不成功");
                    canResponse = true;
                } else {
                    Ping ping2 = new Ping("www.baidu.com");
                    ping2.start();
                    if (ping2.getError() == NetModel.SUCCEED) {
                        loginStatusMessage.setNetStatus(StatusCode.NETWORK_CAN_PING_BAIDU);
                        loginStatusMessage.setErrorMessage("当前网络无法访问消息服务器，但是可以访问外网");
                        canResponse = true;
                    } else {
                        loginStatusMessage.setNetStatus(StatusCode.NETWORK_CAN_NOT_PING_BAIDU);
                        loginStatusMessage.setErrorMessage("当前网络无法访问消息服务器以及外网");
                        canResponse = true;
                    }
                }
            }
            loginedMessage.setLoginStatusCode(StatusCode.LOGIN_FAILED);
            loginedMessage.setCause(loginStatusMessage.getErrorMessage());
            loginedMessage.setNetStatus(loginStatusMessage.getNetStatus());
//        } else if (loginStatusMessage.equalsLoginStatusCode(StatusCode.LOGIN_TIME_OUT)) {
//            loginedMessage.setNetStatus(loginStatusMessage.getNetStatus());
//            loginedMessage.setLoginStatusCode(loginStatusMessage.getLoginStatus());
//            loginedMessage.setCause("登录超时");
//            canResponse = true;
        }
        if (!responsedLogin && canResponse) {
            postLoginedMessage(loginedMessage);
            responsedLogin = true;
        }
    }


    private void postLoginedMessage(LoginedMessage loginedMessage) {
        CustomApplication.getInstance().eventBus.post(new LoginedEvent(loginedMessage));
    }

    void diagnoseNet(LoginStatusMessage loginStatusMessage) {


    }

    @Subscribe
    public void runTaskEvent(TaskEvent event) {
        Task task = event.getEventData();
        appManager.submitTask(task);
        appManager.runTask();
    }

    @Subscribe
    public void sendMessageEvent(SendMessageEvent event) {
        com.jnsw.core.data.Message message = event.getEventData();
        if (message != null) {
            Message xmppMessage = new Message();
//            xmppMessage.setPacketID(message.getId());
            xmppMessage.setLanguage("BASE64");
            xmppMessage.setSubject(message.getJsonCommand());

            xmppMessage.setBody(EncryptUtil.encrBASE64ByGzip(message.toJson()));
            if (message.getFromUser() == null || message.getFromUser() == "")
                message.setFromUser(ClientConfig.getUsrName());
            if (message.getToUser() == null || message.getToUser() == "")
                message.setToUser("0");
            xmppMessage.setFrom(message.getFromUser() + "@" + ClientConfig.getXmppHost() + "/" + ClientConfig.getXmppResource());
            xmppMessage.setTo(message.getToUser() + "@" + ClientConfig.getXmppHost() + "/" + message.getToResource());
            CustomApplication.getInstance().eventBus.post(new SendXmppPacketEvent(xmppMessage));
        }
    }

    @Subscribe
    public void downloadEvent(DownloadEvent event) {
        appManager.submitTask(new DownloadTask(event.getEventData()));
    }

    @Subscribe
    public void mutiUpload(MutiUplouadEvent event) {
        appManager.submitTask(new MutiUploadTask(event.getEventData()));
    }

    @Subscribe
    public void uploadEvent(UploadEvent event) {
        appManager.submitTask(new UploadTask(event.getEventData()));
    }


    @Subscribe
    public void onReceiveShutdown(ShutdownEvent event) {
        stop();
        stopSelf();
    }

    /**
     * Class for summiting a new runnable task.
     */
    public class TaskSubmitter {

        final AppService appService;

        public TaskSubmitter(AppService appService) {
            this.appService = appService;
        }

        @SuppressWarnings("unchecked")
        public Future submit(Runnable task) {
            Future result = null;
            if (!appService.getExecutorService().isTerminated()
                    && !appService.getExecutorService().isShutdown()
                    && task != null) {
                result = appService.getExecutorService().submit(task);
            }
            return result;
        }

    }

    public int getNetworkType() {
        int netType = StatusCode.NETWORN_NONE;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            netType = StatusCode.NETWORN_MOBILE;
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = StatusCode.NETWORN_WIFI;
        }
        return netType;
    }

    /**
     * Class for monitoring the running task count.
     */
    public class TaskTracker {
        final AppService appService;
        public int count;

        public TaskTracker(AppService appService) {
            this.appService = appService;
            this.count = 0;
        }

        public void increase() {
            synchronized (appService.getTaskTracker()) {
                appService.getTaskTracker().count++;
                Log.d(LOGTAG, "Incremented task count to " + count);
            }
        }

        public void decrease() {
            synchronized (appService.getTaskTracker()) {
                appService.getTaskTracker().count--;
                Log.d(LOGTAG, "Decremented task count to " + count);
            }
        }
    }
}
