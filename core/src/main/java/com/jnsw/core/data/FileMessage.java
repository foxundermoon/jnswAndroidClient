package com.jnsw.core.data;

import java.io.File;
import java.util.UUID;

/**
 * Created by foxundermoon on 2015/5/9.
 */
public class FileMessage {
    private boolean uploaded;
    private String fileName;
    private String md5;
    private byte[] data;
    private String errorMessage;
    private  int id;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    private File file;
    private boolean downloaded;
    private int lid;

    public int getLid() {
        return lid;
    }

    public String getUuid() {
        return uuid;
    }

    private String uuid;

    public FileMessage() {
        uuid = UUID.randomUUID().toString().replace("-","");
    }


    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
