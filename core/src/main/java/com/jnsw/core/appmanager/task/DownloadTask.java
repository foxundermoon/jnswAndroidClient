package com.jnsw.core.appmanager.task;

import com.google.common.base.Strings;
import com.jnsw.core.Constants;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.config.ClientConfig;
import com.jnsw.core.data.FileMessage;
import com.jnsw.core.data.swRow;
import com.jnsw.core.event.DownloadEvent;
import com.jnsw.core.event.DownloadedEvent;
import com.jnsw.core.http.ErrorResponseException;

import java.io.IOException;

/**
 * Created by foxundermoon on 2015/5/7.
 */
public class DownloadTask implements Runnable {
    FileMessage fileMessage;

    public DownloadTask(FileMessage fileMessage) {
        this.fileMessage = fileMessage;
    }

    @Override
    public void run() {
        CustomApplication app = CustomApplication.getInstance();
        String fileSysUri = ClientConfig.getStringConfig(Constants.FILE_SYS_URI, "http://10.80.5.222:8080/");
        boolean can = false;
        if (fileMessage.getId() > 0) {
            fileSysUri += "down?id=";
            fileSysUri += fileMessage.getId();
            can = true;
        } else if (!Strings.isNullOrEmpty(fileMessage.getMd5())) {
            fileSysUri += "down?md5=";
            fileSysUri += fileMessage.getMd5();
            can = true;
        }
        if (can) {
            fileMessage = app.httpClient.downloadFile(fileSysUri);
        } else {
            fileMessage.setErrorMessage("没有id或者md5");
        }
        app.eventBus.post(new DownloadedEvent(fileMessage));
    }
}
