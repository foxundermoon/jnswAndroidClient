package com.jnsw.core.data.task;

import android.os.AsyncTask;
import com.jnsw.core.data.Message;
import com.jnsw.core.event.ReceivedMessageEvent;
import com.jnsw.core.util.EncryptUtil;

/**
 * Created by foxundermoon on 2015/5/5.
 */
public class DeserilizeAndBroadcastTask extends AsyncTask<Void, Void, Message> {
    String data;
    boolean needBase64;

    @Override
    protected void onPostExecute(Message message) {
        ReceivedMessageEvent event = new ReceivedMessageEvent(message);
        event.post();
    }

    @Override
    protected Message doInBackground(Void... params) {
        String jsonMsg = null;
        if (needBase64) {
            jsonMsg = EncryptUtil.decryptBASE64ByGzip(data);
        } else {
            jsonMsg = data;
        }
        Message message = null;
        try {
            message = Message.fromJson(jsonMsg);
        } catch (Exception e) {
            com.jnsw.core.data.Message msg = new com.jnsw.core.data.Message();
            msg.setError(e.getMessage());
        }
        return message;
    }

    public DeserilizeAndBroadcastTask(String data, boolean needBase64) {
        this.data = data;
        this.needBase64 = needBase64;
    }
}
