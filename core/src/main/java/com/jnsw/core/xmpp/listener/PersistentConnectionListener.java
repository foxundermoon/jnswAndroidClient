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
package com.jnsw.core.xmpp.listener;

import android.util.Log;
import com.jnsw.core.appmanager.AppManager;
import com.jnsw.core.xmpp.LogUtil;
import org.jivesoftware.smack.ConnectionListener;

/** 
 * A listener class for monitoring connection closing and reconnection events.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 * PersistentConnectionListener
 */
public class PersistentConnectionListener implements ConnectionListener {

    private static final String LOGTAG = LogUtil
            .makeLogTag(PersistentConnectionListener.class);

    private final AppManager appManager;

    public PersistentConnectionListener(AppManager appManager) {
        this.appManager = appManager;
    }

    @Override
    public void connectionClosed() {
        Log.d(LOGTAG, "connectionClosed()...");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.d(LOGTAG, "connectionClosedOnError()...");
        if (appManager.getConnection() != null
                && appManager.getConnection().isConnected()) {
            appManager.getConnection().disconnect();
        }
        appManager.startReconnectionThread();
    }

    @Override
    public void reconnectingIn(int seconds) {
        Log.d(LOGTAG, "reconnectingIn()...");
    }

    @Override
    public void reconnectionFailed(Exception e) {
        Log.d(LOGTAG, "reconnectionFailed()...");
    }

    @Override
    public void reconnectionSuccessful() {
        Log.d(LOGTAG, "reconnectionSuccessful()...");
}

}
