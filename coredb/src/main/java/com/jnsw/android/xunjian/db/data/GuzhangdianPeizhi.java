package com.jnsw.android.xunjian.db.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.jnsw.android.xunjian.db.DataStatus;
import com.jnsw.core.anotation.LocalField;
import com.jnsw.core.anotation.RemoteTable;

/**
 * Created by foxundermoon on 2015/7/17.
 */
@RemoteTable(tableName = "xx_专题_巡检_故障点配置表")
@DatabaseTable(tableName = "故障点配置表")
public class GuzhangdianPeizhi {
    @LocalField

    @DatabaseField(generatedId = true)
    private int LID;
    @DatabaseField(unique = true)
    private int ID;
    @DatabaseField
    private String 编码;
    @DatabaseField
    private String 隐患名称;

    public int getLID() {
        return LID;
    }

    public void setLID(int LID) {
        this.LID = LID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String get编码() {
        return 编码;
    }

    public void set编码(String 编码) {
        this.编码 = 编码;
    }

    public String get名称() {
        return 隐患名称;
    }

    public void set名称(String 名称) {
        this.隐患名称 = 名称;
    }

    public int get版本() {
        return 版本;
    }

    public void set版本(int 版本) {
        this.版本 = 版本;
    }

    @DatabaseField
    private int 版本;
    @DatabaseField
    private String 隐患大类;
    @DatabaseField
    private String 隐患小类;

    public String get隐患大类() {
        return 隐患大类;
    }

    public void set隐患大类(String 隐患大类) {
        this.隐患大类 = 隐患大类;
    }

    public String get隐患小类() {
        return 隐患小类;
    }

    public void set隐患小类(String 隐患小类) {
        this.隐患小类 = 隐患小类;
    }

    public String get隐患描述() {
        return 隐患描述;
    }

    public void set隐患描述(String 隐患描述) {
        this.隐患描述 = 隐患描述;
    }

    @DatabaseField
    private String 隐患描述;
    @DatabaseField
    @LocalField
    private DataStatus 数据状态;
    public DataStatus get数据状态() {
        return 数据状态;
    }

    public void set数据状态(DataStatus 数据状态) {
        this.数据状态 = 数据状态;
    }


    public String get隐患名称() {
        return 隐患名称;
    }

    public void set隐患名称(String 隐患名称) {
        this.隐患名称 = 隐患名称;
    }
}
