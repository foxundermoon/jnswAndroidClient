package com.jnsw.core.appmanager.task;

import com.google.gson.JsonObject;
import com.jnsw.core.Constants;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.config.ClientConfig;
import com.jnsw.core.data.FileMessage;
import com.jnsw.core.data.MutiFileMessage;
import com.jnsw.core.event.MutiUploadedEvent;
import com.jnsw.core.event.UploadedEvent;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by foxundermoon on 2015/6/9.
 */
public class MutiUploadTask implements Runnable {
    private MutiFileMessage filesMessage;

    @Override
    public void run() {
        String fileSysUri = ClientConfig.getStringConfig(Constants.FILE_SYS_URI, "http://10.80.5.222:8080/");
        CustomApplication app = CustomApplication.getInstance();
        for (FileMessage fileMessage : filesMessage.getFiles()) {

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
                String jsonrst = app.httpClient.uploadByPut(fileSysUri, fileMessage.getData(), fileMessage.getFileName());
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
                filesMessage.addError(e.getMessage());
            } catch (IOException e) {
                fileMessage.setErrorMessage(e.getMessage());
                filesMessage.addError(e.getMessage());
            } catch (Exception e) {
                fileMessage.setErrorMessage(e.getMessage());
                filesMessage.addError(e.getMessage());
            } finally {
            }
        }
        if(filesMessage.getErrorMessage()==null)
            filesMessage.setUploadSuccess(true);
        else {
            filesMessage.setUploadSuccess(false);
        }
        CustomApplication.getInstance().eventBus.post(new MutiUploadedEvent(filesMessage));
    }

    public MutiUploadTask(MutiFileMessage eventData) {
        filesMessage = eventData;
    }
}
