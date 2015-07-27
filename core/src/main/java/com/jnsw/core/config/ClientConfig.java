package com.jnsw.core.config;

import android.content.Context;
import android.content.SharedPreferences;
import com.jnsw.core.Constants;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.xmpp.ServiceManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by foxundermoon on 2014/11/28.
 */
public class ClientConfig {
    public static String getLocalJid() {
        String host = getStringConfig(Constants.XMPP_HOST, "");
        String user = getStringConfig(Constants.XMPP_USERNAME, "-1");
        String resource = getStringConfig(Constants.XMPP_RESOURCE, "");
        return user + "@" + host + (resource == "" ? "" : "/" +getXmppResource());
    }

    public static String getJidByUserName(String userName) {
        String host = getStringConfig(Constants.XMPP_HOST, "");
        return userName + "@" + host +"/"+getXmppResource();
    }
    public static String getServerJid() {
        String host = getStringConfig(Constants.XMPP_HOST, "");
        return "0@" + host +"/server";
    }

    public static boolean putString(String key, String value) {
      return   Builder.getInstance().getSharedPreferences().edit().putString(key,value).commit();
    }

    public static boolean putInt(String key, int value) {
        return Builder.getInstance().getSharedPreferences().edit().putInt(key, value).commit();
    }

    public static boolean putBoolean(String key, boolean value) {
        return Builder.getInstance().getSharedPreferences().edit().putBoolean(key, value).commit();
    }

    public static SharedPreferences getSharedPerferences() {
        return Builder.getInstance().getSharedPreferences();
    }
    public static String getServerDefaultDatabase(){
        return getStringConfig(Constants.SERVER_DEFAULT_DATABASE,"");
    }

    public static int getIntConfig(Context context, String key, int defaultValue) {
        return Builder.getInstance().getSharedPreferences(context).getInt(key, defaultValue);
    }
    public static  int getIntConfig(String key,int defaultValue) {
        return getIntConfig(CustomApplication.getInstance(), key, defaultValue);
    }

    public static String getStringConfig(Context context, String key, String defValue) {
        return Builder.getInstance().getSharedPreferences(context).getString(key, defValue);
    }

    public static String getStringConfig(String key, String defValue) {
        return getStringConfig(CustomApplication.getInstance(), key, defValue);
    }

    public static String getXmppHost() {
        return getStringConfig(Constants.XMPP_HOST, "");
    }

    public static int getXmppPort() {
        return getIntConfig(Constants.XMPP_PORT,  -1);
    }
    public static   String getXmppPassword() {
        return getStringConfig(Constants.XMPP_PASSWORD, "");
    }

    public static String getUsrName() {
        return getStringConfig(Constants.XMPP_USERNAME, "");
    }

    public static  String getXmppResource(){
        return  getStringConfig(Constants.XMPP_RESOURCE,"");
    }
    public static class Builder {
        Context context;
        private Map<String, Object> configs;
        private static Builder instance;

        private Builder() {
            configs = new HashMap<String, Object>();
        }

        public static Builder getInstance() {
            synchronized (Builder.class) {
                if (null == instance) {
                    instance = new Builder();
                }
                return instance;
            }
        }

        public Builder set(String key, Object value) {
            configs.put(key, value);
            return this;
        }

        public Builder setFileServerUrl(String fileServerUrl){
            return set(Constants.FILE_SYS_URI,fileServerUrl);
        }
        public Builder setXmppServerHost(String host) {
            return set(Constants.XMPP_HOST, host);
        }

        public Builder setXmppServerPort(int port) {
            return set(Constants.XMPP_PORT, port);
        }

        public Builder setXmppUser(String userName) {
            return set(Constants.XMPP_USERNAME, userName);
        }

        public Builder setXmppPasword(String passwd) {
            return set(Constants.XMPP_PASSWORD, passwd);
        }
        public Builder setServerDefaultDatabase(String database){
            return set(Constants.SERVER_DEFAULT_DATABASE,database);
        }
        public Builder commit(Context context) {
            this.context = context;
            SharedPreferences.Editor editor = getBuildEditor(context);
            if (editor.commit())
                return this;
            else
                return null;
        }
        public Builder commit() {
            return commit(CustomApplication.getInstance());
        }

        public void startXmppService() {
            ServiceManager.getInstance(context).startService();
        }

        public Builder setXmppResource(String resource) {
            return set(Constants.XMPP_RESOURCE, resource);
        }

        public void apply(Context context) {
            getBuildEditor(context).apply();
        }

        private SharedPreferences.Editor getBuildEditor(Context context) {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            for (Map.Entry entry : configs.entrySet()) {
                if (entry.getValue() instanceof Integer) {
                    editor.putInt((String) entry.getKey(), (Integer) entry.getValue());
                } else if (entry.getValue() instanceof Boolean) {
                    editor.putBoolean((String) entry.getKey(), (Boolean) entry.getValue());
                } else {  //if (entry.getValue() instanceof String) {
                    editor.putString((String) entry.getKey(), (String) entry.getValue());
                }
            }
            return editor;
        }

        public SharedPreferences getSharedPreferences(Context context) {
            return context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        public  SharedPreferences getSharedPreferences() {
            return getSharedPreferences(CustomApplication.getInstance());
        }

    }
}
