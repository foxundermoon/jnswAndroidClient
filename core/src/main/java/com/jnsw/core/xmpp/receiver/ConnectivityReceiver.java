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
package com.jnsw.core.xmpp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.jnsw.core.xmpp.LogUtil;
import com.jnsw.core.service.AppService;

/** 
 * A broadcast receiver to handle the changes in network connectiion states.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
@Deprecated
public class ConnectivityReceiver extends BroadcastReceiver {

    private static final String LOGTAG = LogUtil
            .makeLogTag(ConnectivityReceiver.class);

    private AppService appService;

    public ConnectivityReceiver(AppService appService) {
        this.appService = appService;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOGTAG, "ConnectivityReceiver.onPacket()...");
        String action = intent.getAction();
        Log.d(LOGTAG, "action=" + action);

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            Log.d(LOGTAG, "Network Type  = " + networkInfo.getTypeName());
            Log.d(LOGTAG, "Network State = " + networkInfo.getState());
            if (networkInfo.isConnected()) {
                Log.i(LOGTAG, "Network connected");
                appService.connect();
            }
        } else {
            Log.e(LOGTAG, "Network unavailable");
            appService.disconnect();
        }
    }

}
