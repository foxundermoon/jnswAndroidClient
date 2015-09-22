package com.jnsw.core.filetransfer;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.jnsw.core.Constants;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.config.ClientConfig;
import com.jnsw.core.data.FileMessage;
import com.jnsw.core.event.DownloadedEvent;
import com.jnsw.core.event.UploadedEvent;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by fox on 2015/9/22.
 */
public class FileTransfer {
    private static FileTransfer instance;

    public synchronized static FileTransfer getInstance() {
        if (instance == null) {
            instance = new FileTransfer();
        }
        return instance;
    }

    public FileMessage uploadFile(FileMessage fileMessage) {
        String fileSysUri = ClientConfig.getStringConfig(Constants.FILE_SYS_URI, "http://10.80.5.222:8080/");
        CustomApplication app = CustomApplication.getInstance();
        try {
/***********************************
 *  result format
 *     {
 *          "result"    :{
 *              "md5" : "the file md5",
 *              "id"  : "the file id"
 *              ...
 *              "length": "the size of file"
 *           }
 *           "err"      : "the error reason"
 *        }
 ****************************************/
            if (fileMessage.getFileName() == null || fileMessage.getFileName().length() < 1) {
                fileMessage.setFileName("null");
            }
            String jsonrst;
            if (fileMessage.getFile() != null) {
                jsonrst = app.httpClient.upload(fileSysUri, fileMessage.getFile());
            } else {
                jsonrst = app.httpClient.uploadByPut(fileSysUri, fileMessage.getData(), fileMessage.getFileName());
            }
            JsonObject result = app.gson.fromJson(jsonrst, JsonObject.class);
            if (result == null) {
                fileMessage.setErrorMessage("上传失败");
            } else {
                JsonObject re = result.getAsJsonObject("result");
                fileMessage.setId(re.getAsJsonPrimitive("id").getAsInt());
                fileMessage.setMd5(re.getAsJsonPrimitive("md5").getAsString());
                fileMessage.setFileName(re.getAsJsonPrimitive("name").getAsString());
                fileMessage.setUploaded(true);
            }
        } catch (URISyntaxException e) {
            fileMessage.setErrorMessage(e.getMessage());
        } catch (IOException e) {
            fileMessage.setErrorMessage(e.getMessage());
        } catch (Exception e) {
            fileMessage.setErrorMessage(e.getMessage());
        } finally {
//            app.eventBus.post(new UploadedEvent(fileMessage));
            return fileMessage;
        }
    }

    public  FileMessage downloadFile(FileMessage fileMessage) {
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
        return fileMessage;
//        app.eventBus.post(new DownloadedEvent(fileMessage));
    }

}
