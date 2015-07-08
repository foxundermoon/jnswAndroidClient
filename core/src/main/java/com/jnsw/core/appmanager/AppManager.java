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
package com.jnsw.core.appmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.util.Log;
import com.jnsw.core.Constants;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.data.AppErrorMessage;
import com.jnsw.core.event.AppErrorEvent;
import com.jnsw.core.event.LoginEvent;
import com.jnsw.core.service.AppService;
import com.jnsw.core.util.L;
import com.jnsw.core.xmpp.LogUtil;
import com.jnsw.core.xmpp.daemon.ReconnectionThread;
import com.jnsw.core.xmpp.listener.*;
import com.jnsw.core.xmpp.packet.NotificationIQ;
import com.jnsw.core.xmpp.provider.NotificationIQProvider;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.*;
import org.jivesoftware.smack.provider.ProviderManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * This class is to manage the XMPP connection between client and server.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class AppManager {

    private static final String LOGTAG = LogUtil.makeLogTag(AppManager.class);

    private String XmppResourceName;
    private final String password;
    private final String username;

    private Context context;/**/

    private AppService.TaskSubmitter taskSubmitter;

    private AppService.TaskTracker taskTracker;

    private SharedPreferences sharedPrefs;

    private String xmppHost;

    private int xmppPort;

    private XMPPConnection connection;
    private ConnectionListener connectionListener;
    private Handler handler;
    private List<Runnable> taskList;
    private boolean running = false;
    private Future<?> futureTask;
    private Thread reconnection;
    private NotificationPacketListener notificationPacketListener;
    private MessagePacketListener messagePacketListener;
    private PresencePacketListener presenceListener;

    public AppManager(AppService appService) {
        context = appService;
        taskSubmitter = appService.getTaskSubmitter();
        taskTracker = appService.getTaskTracker();
        sharedPrefs = appService.getSharedPreferences();

        xmppHost = sharedPrefs.getString(Constants.XMPP_HOST, "localhost");
        xmppPort = sharedPrefs.getInt(Constants.XMPP_PORT, 5222);
        username = sharedPrefs.getString(Constants.XMPP_USERNAME, "-1");
        password = sharedPrefs.getString(Constants.XMPP_PASSWORD, "");
        XmppResourceName = sharedPrefs.getString(Constants.XMPP_RESOURCE, "android");
//        username = sharedPrefs.getString(Constants.XMPP_USERNAME, "");
//        password = sharedPrefs.getString(Constants.XMPP_PASSWORD, "");

        connectionListener = new PersistentConnectionListener(this);
        handler = new Handler();
        taskList = new ArrayList<Runnable>();
        reconnection = new ReconnectionThread(this);
    }

    public Context getContext() {
        return context;
    }

    public void connect() {
//        Log.d(LOGTAG, "connect()...");
        submitLoginTask();
    }

    public void disconnect() {
        Log.d(LOGTAG, "disconnect()...");
        terminatePersistentConnection();
    }

    public void terminatePersistentConnection() {
        Log.d(LOGTAG, "terminatePersistentConnection()...");
        Runnable runnable = new Runnable() {

            final AppManager xmppManager = AppManager.this;

            public void run() {
                if (xmppManager.isConnected()) {
                    Log.d(LOGTAG, "terminatePersistentConnection()... run()");
                    xmppManager.unregisterPacketListener();
//                    appManager.getConnection().removePacketListener(
//                            appManager.ionPacketListener());
                    xmppManager.getConnection().disconnect();
                }
                xmppManager.runTask();
            }

        };
        addTask(runnable);
    }

    public XMPPConnection getConnection() {
        return connection;
    }

    public void setConnection(XMPPConnection connection) {
        this.connection = connection;
    }


    public ConnectionListener getConnectionListener() {
        return connectionListener;
    }


    public void startReconnectionThread() {
        synchronized (reconnection) {
            if (!reconnection.isAlive()) {
                reconnection.setName("Xmpp Reconnection Thread");
                reconnection.start();
            }
        }
    }

    public Handler getHandler() {
        return handler;
    }

    public void reregisterAccount(String name, String passwd) {
        removeAccount();
//        submitLoginTask();
        addTask(new RegisterTask(name, passwd));
        runTask();
    }

    public void submitTask(Runnable runnable) {
        addTask(runnable);
        runTask();
    }

    public List<Runnable> getTaskList() {
        return taskList;
    }

    public Future<?> getFutureTask() {
        return futureTask;
    }

    public void runTask() {
        Log.d(LOGTAG, "runTask()...");
        synchronized (taskList) {
            running = false;
            futureTask = null;
            if (!taskList.isEmpty()) {
                Runnable runnable = (Runnable) taskList.get(0);
                taskList.remove(0);
                running = true;
                futureTask = taskSubmitter.submit(runnable);
                if (futureTask == null) {
                    taskTracker.decrease();
                }
            }
        }
        taskTracker.decrease();
        Log.d(LOGTAG, "runTask()...done");
    }

    public void sendPacketAsync(Packet packet) {
        addTask(new SendPacketTask(packet));
        runTask();
    }

    private String newRandomUUID() {
        String uuidRaw = UUID.randomUUID().toString();
        return uuidRaw.replaceAll("-", "");
    }

    private boolean isConnected() {
        return connection != null && connection.isConnected();
    }

    private boolean isAuthenticated() {
        return connection != null && connection.isConnected()
                && connection.isAuthenticated();
    }

    private boolean isRegistered() {
        return sharedPrefs.contains(Constants.XMPP_USERNAME)
                && sharedPrefs.contains(Constants.XMPP_PASSWORD);
    }

    private void submitRegisterTask(String username, String passwd) {
        Log.d(LOGTAG, "submitRegisterTask()...");
        addTask(new ConnectTask());
        addTask(new RegisterTask(username, passwd));
        runTask();
    }

    private void submitLoginTask() {
        Log.d(LOGTAG, "submitLoginTask()...");
//        submitRegisterTask();
        addTask(new ConnectTask());
        addTask(new LoginTask());
        runTask();
    }

    private void addTask(Runnable runnable) {
        Log.d(LOGTAG, "addTask(runnable)...");
        taskTracker.increase();
        synchronized (taskList) {
            if (taskList.isEmpty() && !running) {
                running = true;
                futureTask = taskSubmitter.submit(runnable);
                if (futureTask == null) {
                    taskTracker.decrease();
                }
            } else {
                taskList.add(runnable);
            }
        }
        Log.d(LOGTAG, "addTask(runnable)... done");
    }

    private void registerPacketListener() {
        if (getConnection().isConnected()) {
//            PacketFilter packetFilter = new PacketTypeFilter(Message.class);
//            PacketFilter packetFilter1 = new PacketTypeFilter(NotificationIQ.class);
//            connection.addPacketListener(new MessagePacketListener(), packetFilter);
//            connection.addPacketListener(new NotificationPacketListener(appManager), packetFilter1);

            // packet provider
            ProviderManager.getInstance().addIQProvider("notification",
                    "androidpn:iq:notification",
                    new NotificationIQProvider());
            PacketFilter notificationFilter = new PacketTypeFilter(
                    NotificationIQ.class);
            notificationPacketListener = NotificationPacketListener.getInstance(this);
            getConnection().addPacketListener(notificationPacketListener, notificationFilter);
            getConnection().addPacketListener(new MessagePacketListener(), new PacketTypeFilter(Message.class));
            getConnection().addPacketListener(new PresencePacketListener(), new PacketTypeFilter(Presence.class));
            getConnection().addPacketListener(new IqPacketListener(), new PacketTypeFilter(IQ.class));
        }
    }

    private void unregisterPacketListener() {
        if (notificationPacketListener != null)
            getConnection().removePacketListener(notificationPacketListener);
        if (messagePacketListener != null)
            getConnection().removePacketListener(messagePacketListener);
        if (presenceListener != null)
            getConnection().removePacketListener(presenceListener);
    }

    private void removeAccount() {
        Editor editor = sharedPrefs.edit();
        editor.remove(Constants.XMPP_USERNAME);
        editor.remove(Constants.XMPP_PASSWORD);
        editor.commit();
    }

    /**
     * A runnable task to connect the server.
     */
    private class ConnectTask implements Runnable {

        final AppManager appManager;

        private ConnectTask() {
            this.appManager = AppManager.this;
        }

        public void run() {
            Log.i(LOGTAG, "ConnectTask.run()...");

            if (!appManager.isConnected()) {
                // Create the configuration for this new connection
                ConnectionConfiguration connConfig = new ConnectionConfiguration(
                        xmppHost, xmppPort);
                connConfig.setSecurityMode(SecurityMode.disabled);
//                connConfig.setSecurityMode(SecurityMode.required);
                connConfig.setSASLAuthenticationEnabled(false);
                connConfig.setCompressionEnabled(false);
//                String name = sharedPrefs.getString(Constants.XMPP_USERNAME,"");
//                String psswd = sharedPrefs.getString(Constants.XMPP_PASSWORD,"");
                XMPPConnection connection = new XMPPConnection(connConfig);
                appManager.setConnection(connection);
                try {
                    // Connect to the server
                    connection.connect();
                    registerPacketListener();
//                    broadcastXmppStatus(XmppStatusCode.ConectedSuccess);
                } catch (Exception e) {
                    Log.e(LOGTAG, "XMPP connection failed", e);
                    AppErrorMessage errorMessage = new AppErrorMessage();
                    errorMessage.errorType = AppErrorMessage.ErrorType.ConnectError;
                    errorMessage.message = e.getMessage();
                    errorMessage.cause = e.getCause().getMessage();
                    CustomApplication.getInstance().eventBus.post(new AppErrorEvent(errorMessage));
                }

                appManager.runTask();

            } else {
                Log.i(LOGTAG, "XMPP connected already");
//                broadcastXmppStatus(XmppStatusCode.ConnectedAlready);
                appManager.runTask();
            }
        }
    }

    /**
     * A runnable task to register a new user onto the server.
     */
    private class RegisterTask implements Runnable {
        private String newPasswd;
        private String newUsername;
        final AppManager appManager;

        private RegisterTask(String username, String passwd) {
            appManager = AppManager.this;
            newUsername = username;
            newPasswd = passwd;
        }

        public void run() {
            if (newUsername == null) {
//                broadcastXmppStatus(XmppStatusCode.NoUserNumber);
            } else if (newPasswd == null) {
//                broadcastXmppStatus(XmppStatusCode.NoPassword);
            } else {
                try {
                    Integer nu = Integer.getInteger(newUsername);
                    if (nu > 0) {
                        Log.i(LOGTAG, "RegisterTask.run()...");
                        register();
                    } else {
//                        broadcastXmppStatus(XmppStatusCode.ErrorUserNumber, "你输入的用户名不能小于一");
                    }
                } catch (Exception e) {
//                    broadcastXmppStatus(XmppStatusCode.ErrorUserNumber, "用户名必须为数字");
                }
            }
        }

        private void register() {
            if (!appManager.isRegistered()) {
                Registration registration = new Registration();
                PacketFilter packetFilter = new AndFilter(new PacketIDFilter(
                        registration.getPacketID()), new PacketTypeFilter(
                        IQ.class));

                PacketListener packetListener = new PacketListener() {

                    public void processPacket(Packet packet) {
                        L.d("RegisterTask",
                                "processPacket().....");
                        L.d("RegisterTask.PacketListener", "packet="
                                + packet.toXML());

                        if (packet instanceof IQ) {
                            IQ response = (IQ) packet;
                            if (response.getType() == IQ.Type.ERROR) {
                                if (!response.getError().toString().contains(
                                        "409")) {
                                    Log.e(LOGTAG,
                                            "Unknown error while registering XMPP account! "
                                                    + response.getError()
                                                    .getCondition());
//                                    broadcastXmppStatus(XmppStatusCode.RegisterFailed, response.getError().getCondition());

                                }
                            } else if (response.getType() == IQ.Type.RESULT) {
//                                appManager.setUsername(newUsername);
//                                appManager.setPassword(newPassword);
                                Log.d(LOGTAG, "username=" + newUsername);
                                Log.d(LOGTAG, "password=" + newPasswd);

                                Editor editor = sharedPrefs.edit();
                                editor.putString(Constants.XMPP_USERNAME,
                                        newUsername);
                                editor.putString(Constants.XMPP_PASSWORD,
                                        newPasswd);
                                editor.commit();
//                                broadcastXmppStatus(XmppStatusCode.RegisterSuccess);
                                Log
                                        .i(LOGTAG,
                                                "Account registered successfully");
                                appManager.runTask();
                            }
                        }
                    }
                };
                connection.addPacketListener(packetListener, packetFilter);
                registration.setType(IQ.Type.SET);
                // registration.setTo(xmppHost);
                // Map<String, String> attributes = new HashMap<String, String>();
                // attributes.put("username", rUsername);
                // attributes.put("password", rPassword);
                // registration.setAttributes(attributes);
                registration.addAttribute("username", newUsername);
                registration.addAttribute("password", newPasswd);
                connection.sendPacket(registration);
            } else {
                Log.i(LOGTAG, "Account registered already");
//                broadcastXmppStatus(XmppStatusCode.AccountAlreadyRegisted);
                appManager.runTask();
            }
        }
    }

    /**
     * A runnable task to log into the server.
     */
    private class LoginTask implements Runnable {

        final AppManager appManager;

        private LoginTask() {
            this.appManager = AppManager.this;
        }

        public void run() {
            Log.i(LOGTAG, "LoginTask.run()...");

            login(username, password);
//            try {
//                if (username == "" ||null==username) {
//                    broadcastXmppStatus(XmppStatusCode.NoUserNumber);
//                } else if (password == "" || null==password) {
//                    broadcastXmppStatus(XmppStatusCode.NoPassword);
//                } else {
//                    Integer un = Integer.parseInt(
//                            username);
//                    if (un < 1) {
//                        broadcastXmppStatus(XmppStatusCode.ErrorUserNumber);
//                    } else {
//                        login(username, password);
//                    }
//                }
//
//
//            } catch (Exception e) {
//                broadcastXmppStatus(XmppStatusCode.ErrorUserNumber);
//            }
        }

        private void login(String username, String password) {
            Log.d(LOGTAG, "start login()->user:" + username + "   passwd:" + password);
            if (!appManager.isAuthenticated()) {
                Log.d(LOGTAG, "username=" + username);
                Log.d(LOGTAG, "password=" + password);
                try {
                    // packet listener
//                    registerPacketListener();
                    appManager.getConnection().login(
                            String.valueOf(username),
                            password, XmppResourceName);
                    Log.d(LOGTAG, "Loggedn in successfully");

                    CustomApplication.getInstance().eventBus.post(new LoginEvent(true));
                    if (appManager.getConnectionListener() != null) {
                        appManager.getConnection().addConnectionListener(
                                appManager.getConnectionListener());
                    }
                    appManager.runTask();

                } catch (XMPPException e) {
                    CustomApplication.getInstance().eventBus.post(new LoginEvent(false, e.getMessage()));
                    Log.e(LOGTAG, "LoginTask.run()... xmpp error");
                    Log.e(LOGTAG, "Failed to login to xmpp server. Caused by: "
                            + e.getMessage());
                    String INVALID_CREDENTIALS_ERROR_CODE = "401";
                    String errorMessage = e.getMessage();
                    if (errorMessage != null
                            && errorMessage
                            .contains(INVALID_CREDENTIALS_ERROR_CODE)) {
//                        appManager.reregisterAccount();
                        CustomApplication.getInstance().eventBus.post(new LoginEvent(false, "登录失败 cause by:" + e.getMessage()));

                        return;
                    }
                    appManager.startReconnectionThread();

                } catch (Exception e) {
                    Log.e(LOGTAG, "LoginTask.run()... other error");
                    Log.e(LOGTAG, "Failed to login to xmpp server. Caused by: "
                            + e.getMessage());
                    CustomApplication.getInstance().eventBus.post(new LoginEvent(false, "登录失败 cause by:" + e.getMessage()));
                    appManager.startReconnectionThread();
                }

            } else {
                Log.i(LOGTAG, "Logged in already");
                CustomApplication.getInstance().eventBus.post(new LoginEvent(false, "已经登录"));
                appManager.runTask();
            }
        }

    }

    private class SendPacketTask implements Runnable {
        final AppManager appManager;
        final Packet packet;

        private SendPacketTask(Packet packet) {
            this.appManager = AppManager.this;
            this.packet = packet;
        }

        @Override
        public void run() {
            try {
                appManager.getConnection().sendPacket(packet);
            } catch (Exception e) {
//                broadcastXmppStatus(XmppStatusCode.SendPacketError);
            }
        }
    }

    private class DownloadTask implements Runnable{

        @Override
        public void run() {

        }
    }

}
