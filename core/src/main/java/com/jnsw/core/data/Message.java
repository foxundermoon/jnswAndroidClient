package com.jnsw.core.data;

import android.app.ActionBar;
import android.app.Application;
import android.content.Context;
import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jnsw.core.Constants;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.config.ClientConfig;
import com.jnsw.core.event.ReceivedMessageEvent;
import com.jnsw.core.event.SendMessageEvent;
import com.jnsw.core.util.EncryptUtil;
import com.jnsw.core.util.JSONHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by foxundermoon on 2015/4/23.
 */
public final class Message {
    static Gson gson = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(Column.class, new ColumnSerilizer())
            .registerTypeAdapter(Table.class, new TableSerializer())
            .registerTypeAdapter(Command.class, new CommandSerilizer())
            .registerTypeAdapter(Message.class, new MessageSerializer())
            .registerTypeAdapter(Row.class, new RowSerializer()).create();
    private Table dataTable;
    public boolean hasError = false;

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    private Map<String, Object> properties;

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public Message() {
        properties = new HashMap<String, Object>();
    }

    private MessageCallback callback;
    private Command command;

    public int propertiesCount() {
        return properties.entrySet().size();
    }

    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }

    public String getFromUser() {
        if (hasProperty(MapKeys.fromUser)) {
            return getProperty(MapKeys.fromUser).toString();
        }
        return null;
    }

    public Message addProperty(String key, Object value) {
        properties.put(key, value);
        return this;
    }

    public Message setFromUser(String fromUser) {
        addProperty(MapKeys.fromUser, fromUser);
        return this;
    }

    public String getToUser() {
        if (hasProperty(MapKeys.toUser)) {
            return getProperty(MapKeys.toUser).toString();
        }
        return null;
    }

    public Message setToUser(String toUser) {
        addProperty(MapKeys.toUser, toUser);
        return this;
    }

    public Table getDataTable() {
        return dataTable;
    }

    public Message setDataTable(Table dataTable) {
        this.dataTable = dataTable;
        return this;
    }


    public String getId() {
        if (hasProperty(MapKeys.id)) {
            return (String) getProperty(MapKeys.id);
        } else {
            return null;
        }
    }

    public Message setId(String id) {
        addProperty(MapKeys.id, id);
        return this;
    }


    public Table createTable(String... columns) {
        dataTable = new Table(columns);
        return dataTable;
    }

    public Table createTable(Column... columns) {
        dataTable = new Table(columns);
        return dataTable;
    }

    public Table createTable(List<Column> colmns) {
        dataTable = new Table(colmns);
        return dataTable;
    }

    public Command creatCommand() {
        Command command = new Command();
        this.command = command;
        return command;
    }


    public void send() throws JSONException {
        CustomApplication application = CustomApplication.getInstance();
        if (Strings.isNullOrEmpty(getId())) {
            setId(UUID.randomUUID().toString().replace("-",""));
        }
        application.eventBus.register(this);
        application.eventBus.post(new SendMessageEvent(this));
    }

    @Subscribe
    public void onCallback(final ReceivedMessageEvent receivedMessageEvent) {
        Message message = receivedMessageEvent.getEventData();
        if (message != null) {
//        if ((message !=null )&&  ( getId().equals(message.getId())) && (callback != null)) {
//            String sid = getId();
//            String rid = message.getId();
            if ((getId().equals(message.getId()))) {
                callback.onCallback(message);
                CustomApplication.getInstance().eventBus.unregister(this);
            }
        }
    }

    public JsonObject toJsonObject() {
        return gson.toJsonTree(this).getAsJsonObject();
    }

    public static Message fromJson(String json) {
        return gson.fromJson(json, Message.class);
    }

    public Message setCallback(MessageCallback callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public String toString() {
        return toJsonObject().toString();
    }

    public String toJson() {
        return toString();
    }

    public String getJsonCommand() {
        return gson.toJson(command);
    }

    public void setError(String message) {
        hasError = true;
        addProperty(MapKeys.error, message);
    }

    public String getError() {
        if (hasError) {
            return (String) getProperty(MapKeys.error);
        }
        return null;
    }
}
