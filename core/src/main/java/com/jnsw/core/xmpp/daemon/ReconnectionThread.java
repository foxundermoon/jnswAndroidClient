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
package com.jnsw.core.xmpp.daemon;

import android.util.Log;
import com.jnsw.core.xmpp.LogUtil;
import com.jnsw.core.appmanager.AppManager;

/** 
 * A thread class for recennecting the server.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
@Deprecated
public class ReconnectionThread extends Thread {

    private static final String LOGTAG = LogUtil
            .makeLogTag(ReconnectionThread.class);

    private final AppManager appManager;

    private int waiting;

    public ReconnectionThread(AppManager appManager) {
        this.appManager = appManager;
        this.waiting = 0;
    }

    public void run() {
        try {
            while (!isInterrupted()) {
                Log.d(LOGTAG, "Trying to reconnect in " + waiting()
                        + " seconds");
                Thread.sleep((long) waiting() * 1000L);
                appManager.connect();
                waiting++;
            }
        } catch (final InterruptedException e) {
            appManager.getHandler().post(new Runnable() {
                public void run() {
                    appManager.getConnectionListener().reconnectionFailed(e);
                }
            });
        }
    }

    private int waiting() {
        if (waiting > 20) {
            return 600;
        }
        if (waiting > 13) {
            return 300;
        }
        return waiting <= 7 ? 10 : 60;
    }
}
