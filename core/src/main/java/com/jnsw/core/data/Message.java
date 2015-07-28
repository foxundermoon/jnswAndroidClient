package com.jnsw.core.data;

import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonObject;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.config.ClientConfig;
import com.jnsw.core.event.ReceivedMessageEvent;
import com.jnsw.core.event.SendMessageEvent;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by foxundermoon on 2015/4/23.
 */
public final class Message {
    private Table dataTable;
    public boolean hasError = false;
    public boolean hasFile = false;
    public String errorType;
    public String errorMessage;
    private Callback<Message> callback;

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    private Map<String, Object> properties;

    public Object getProperty(String key) {
        if (properties == null)
            return null;
        return properties.get(key);
    }

    public Message() {
        properties = new HashMap<String, Object>();
    }

    private MessageCallback _callback;

    public Command getCommand() {
        return command;
    }

    public Message switchDirection() {
        String toUser = getToUser();
        String fromUser = getFromUser();
        setFromUser(toUser);
        setToUser(fromUser);
        return this;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    private Command command;

    public int propertiesCount() {
        return properties.entrySet().size();
    }

    public boolean hasProperty(String key) {
        if (properties == null)
            return false;
        return properties.containsKey(key);
    }

    public String getFromUser() {
        if (hasProperty(MapKeys.fromUser)) {
            return getProperty(MapKeys.fromUser).toString();
        }
        return ClientConfig.getUsrName();
    }

    public Message addProperty(String key, Object value) {
        if (properties == null) {
            properties = new HashMap<String, Object>();
        }
        properties.put(key, value);
        return this;
    }

    public Message setFromUser(String fromUser) {
        addProperty(MapKeys.fromUser, fromUser);
        return this;
    }

    public String getToResource(){
        return (String) getProperty(MapKeys.toResource);
    }
    public String getToUser() {
        if (hasProperty(MapKeys.toUser)) {
            return getProperty(MapKeys.toUser).toString();
        }
        return "0";
    }

    public Message setToUser(String toUser) {
        addProperty(MapKeys.toUser, toUser);
        return this;
    }
    public Message setToResource(String toResource){
        addProperty(MapKeys.toResource,toResource);
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
            String id = UUID.randomUUID().toString().replace("-", "").substring(0, 5);
            setId(id);
            return id;
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
        application.eventBus.register(this);
        application.eventBus.post(new SendMessageEvent(this));
    }

    @Subscribe
    public void onCallback(ReceivedMessageEvent receivedMessageEvent) {
        Message message = receivedMessageEvent.getEventData();
//        if (message != null) {
        if ((message !=null )&&  ( getId().equals(message.getId()))) {
            String sid = getId();
            String rid = message.getId();
            if ((getId().equals(message.getId()))) {
                if (_callback != null)
                    _callback.onCallback(message);
                if (callback != null)
                    callback.onCallback(message);
                CustomApplication.getInstance().eventBus.unregister(this);
            }
        }
    }

    public JsonObject toJsonObject() {
        return CustomApplication.getInstance().gson.toJsonTree(this).getAsJsonObject();
    }

    public static Message fromJson(String json) {
        return CustomApplication.getInstance().gson.fromJson(json, Message.class);
    }

    @Deprecated
    public Message setCallback(MessageCallback _callback) {
        this._callback = _callback;
        return this;
    }

    public Message setCallback(Callback<Message> callback) {
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
        return CustomApplication.getInstance().gson.toJson(command);
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
