package com.jnsw.android.xunjian.db.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.jnsw.android.xunjian.db.DataStatus;
import com.jnsw.core.anotation.LocalField;
import com.jnsw.core.anotation.RemoteTable;

/**
 * Created by Administrator on 2015/7/28.
 */
@RemoteTable(tableName = "xx_常量配置表")
@DatabaseTable(tableName = "常量配置表")
public class ChangLiang {
    @DatabaseField(generatedId = true)
    @LocalField
    private int LID;
    @DatabaseField
    private int ID;
    @DatabaseField
    private String 常量名称;
    @DatabaseField
    private String 常量值;
    @DatabaseField
    private String 常量单位;
    @DatabaseField
    private int 版本;

    @DatabaseField
    @LocalField
    private DataStatus 数据状态;
    public DataStatus get数据状态() {
        return 数据状态;
    }

    public void set数据状态(DataStatus 数据状态) {
        this.数据状态 = 数据状态;
    }



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

    public String get常量名称() {
        return 常量名称;
    }

    public void set常量名称(String 常量名称) {
        this.常量名称 = 常量名称;
    }

    public String get常量值() {
        return 常量值;
    }

    public void set常量值(String 常量值) {
        this.常量值 = 常量值;
    }

    public String get常量单位() {
        return 常量单位;
    }

    public void set常量单位(String 常量单位) {
        this.常量单位 = 常量单位;
    }

    public int get版本() {
        return 版本;
    }

    public void set版本(int 版本) {
        this.版本 = 版本;
    }
}
