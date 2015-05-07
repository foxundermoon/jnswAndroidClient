package com.jnsw.core;

import android.app.Application;
import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jnsw.core.data.*;
import com.jnsw.core.http.MyHttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by foxundermoon on 2014/10/30.
 */
public class CustomApplication extends Application {
    public Map<String, Object> shareMap;
    public EventBus eventBus;
    public static CustomApplication instance;
    public MyHttpClient httpClient;
    public Gson gson;
    public String creatUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static CustomApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        shareMap = new HashMap<String, Object>();
        Map<String, Objects> xmppStatus = new HashMap<String, Objects>();
        shareMap.put(Constants.XMPP_STATUS, xmppStatus);
        eventBus = new EventBus(Constants.CUSTOM_EVENT_BUS);
        instance = this;
        httpClient = MyHttpClient.getInstance();
        gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(Column.class, new ColumnSerilizer())
                .registerTypeAdapter(Table.class, new TableSerializer())
                .registerTypeAdapter(Command.class, new CommandSerilizer())
                .registerTypeAdapter(Message.class, new MessageSerializer())
                .registerTypeAdapter(Row.class, new RowSerializer()).create();
    }

    @Override
    public void onTerminate() {
        eventBus=null;
        try {
            httpClient.getHttpClient().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onTerminate();
    }

    public CustomApplication() {
        super();
    }
}
