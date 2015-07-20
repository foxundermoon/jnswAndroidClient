
package com.jnsw.core.appmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.util.Log;

import com.jnsw.core.Constants;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.config.ClientConfig;
import com.jnsw.core.data.AppErrorMessage;
import com.jnsw.core.data.LoginStatusMessage;
import com.jnsw.core.data.StatusCode;
import com.jnsw.core.event.AppErrorEvent;
import com.jnsw.core.event.LoginedEvent;
import com.jnsw.core.event.LoginStatusEvent;
import com.jnsw.core.service.AppService;
import com.jnsw.core.util.L;
import com.jnsw.core.xmpp.LogUtil;
import com.jnsw.core.xmpp.listener.*;

import net.qiujuer.genius.nettool.NetModel;
import net.qiujuer.genius.nettool.Ping;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class AppManager {

    private static final String LOGTAG = LogUtil.makeLogTag(AppManager.class);

    private String XmppResourceName;
    private final String password;
    private final String username;

    private Context context;

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
//        handler = new Handler();
        taskList = new ArrayList<Runnable>();
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
            final AppManager appManager = AppManager.this;

            public void run() {
                if (appManager.isConnected()) {
                    Log.d(LOGTAG, "terminatePersistentConnection()... run()");
//                    xmppManager.unregisterPacketListener();
//                    appManager.getConnection().removePacketListener(
//                            appManager.ionPacketListener());
                    appManager.getConnection().disconnect();
                }
                appManager.runTask();
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

//    public void reregisterAccount(String name, String passwd) {
//        removeAccount();
////        submitLoginTask();
//        addTask(new RegisterTask(name, passwd));
//        runTask();
//    }

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
            getConnection().addPacketListener(new MessagePacketListener(), new PacketTypeFilter(Message.class));
        }
    }

//    private void unregisterPacketListener() {
//        if (messagePacketListener != null)
//            getConnection().removePacketListener(messagePacketListener);
//    }

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
                ConnectionConfiguration connConfig = new ConnectionConfiguration(
                        xmppHost, xmppPort);
                connConfig.setSecurityMode(SecurityMode.disabled);
                connConfig.setSASLAuthenticationEnabled(false);
                connConfig.setCompressionEnabled(false);
                XMPPConnection connection = new XMPPConnection(connConfig);
                LoginStatusMessage loginStatusMessage = new LoginStatusMessage(StatusCode.LOGIN_FAILED);
                appManager.setConnection(connection);
                try {
                    connection.connect();
                    registerPacketListener();
                } catch (Exception e) {
                    Log.e(LOGTAG, "XMPP connection failed", e);
                    AppErrorMessage errorMessage = new AppErrorMessage();
                    errorMessage.errorType = AppErrorMessage.ErrorType.ConnectError;
                    errorMessage.message = e.getMessage();
//                    errorMessage.cause = e.getCause().getMessage();
                    CustomApplication.getInstance().eventBus.post(new LoginStatusEvent(loginStatusMessage));
                }
                appManager.runTask();
            } else {
                Log.i(LOGTAG, "XMPP connected already");
//                broadcastXmppStatus(XmppStatusCode.ConnectedAlready);
                appManager.runTask();
            }
        }
    }


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
            LoginStatusMessage loginStatusMessage = new LoginStatusMessage(StatusCode.NOT_LOGIN);
            if (!appManager.isAuthenticated()) {
                Log.d(LOGTAG, "username=" + username);
                Log.d(LOGTAG, "password=" + password);
                try {
                    appManager.getConnection().login(
                            String.valueOf(username),
                            password, XmppResourceName);
                    Log.d(LOGTAG, "Loggedn in successfully");
                    loginStatusMessage.setLoginStatus(StatusCode.LOGIN_SUCCESS);
                    if (appManager.getConnectionListener() != null) {
                        appManager.getConnection().addConnectionListener(
                                appManager.getConnectionListener());
                    }
                    appManager.runTask();

                } catch (XMPPException e) {
                    Log.e(LOGTAG, "LoginTask.run()... xmpp error");
                    Log.e(LOGTAG, "Failed to login to xmpp server. Caused by: "
                            + e.getMessage());
                } catch (Exception e) {
                    Log.e(LOGTAG, "LoginTask.run()... other error");
                    Log.e(LOGTAG, "Failed to login to xmpp server. Caused by: "
                            + e.getMessage());
                    if (e.getMessage().contains("Already logged in to server."))
                        loginStatusMessage.setLoginStatus(StatusCode.LOGIN_ALREADY);
                    else
                        loginStatusMessage.setLoginStatus(StatusCode.LOGIN_FAILED);
//                    loginStatusMessage.setNetStatus(StatusCode.NETWORK_NOT_CONNECT_SERVER);
//                    loginStatusMessage.setErrorMessage("无法连接消息服务器");
//                    appManager.startReconnectionThread();
                } finally {
                    CustomApplication.getInstance().eventBus.post(new LoginStatusEvent(loginStatusMessage));
                }

            } else {
                Log.i(LOGTAG, "Logged in already");
                loginStatusMessage.setErrorMessage("你已经登录，无须登录");
                loginStatusMessage.setLoginStatus(StatusCode.LOGIN_ALREADY);
                CustomApplication.getInstance().eventBus.post(new LoginStatusEvent(loginStatusMessage));
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

    private class DownloadTask implements Runnable {

        @Override
        public void run() {

        }
    }

}
