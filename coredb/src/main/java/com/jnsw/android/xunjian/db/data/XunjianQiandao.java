package com.jnsw.android.xunjian.db.data;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.jnsw.android.xunjian.db.DataStatus;
import com.jnsw.core.anotation.LocalField;
import com.jnsw.core.anotation.RemoteTable;

import java.util.Date;

/**
 * Created by foxundermoon on 2015/7/17.
 */
@RemoteTable(tableName = "xx_专题_巡检_员工签到表")
@DatabaseTable(tableName = "巡检签到表")
public class XunjianQiandao {
    @LocalField
    @DatabaseField(generatedId = true)
    private int LID;
    @DatabaseField
    private int ID;

    public int get用户ID() {
        return 用户ID;
    }


    public void set用户ID(int 用户ID) {
        this.用户ID = 用户ID;
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

    @DatabaseField
    private int 用户ID;
    @DatabaseField
    private String 用户名称;
    @DatabaseField(dataType = DataType.DATE_STRING,format = "yyyy-MM-dd HH:mm:ss")
    private Date 上班时间;
    @DatabaseField(dataType = DataType.DATE_STRING,format = "yyyy-MM-dd HH:mm:ss")
    private Date 下班时间;

    public int get版本() {
        return 版本;
    }

    public void set版本(int 版本) {
        this.版本 = 版本;
    }

    @LocalField
    @DatabaseField
    private int 版本;
    public String get用户名称() {
        return 用户名称;
    }

    public void set用户名称(String 用户名称) {
        this.用户名称 = 用户名称;
    }

    public Date get上班时间() {
        return 上班时间;
    }

    public void set上班时间(Date 上班时间) {
        this.上班时间 = 上班时间;
    }

    public Date get下班时间() {
        return 下班时间;
    }

    public void set下班时间(Date 下班时间) {
        this.下班时间 = 下班时间;
    }
    @DatabaseField
    @LocalField
    private DataStatus 数据状态;
    public DataStatus get数据状态() {
        return 数据状态;
    }

    public void set数据状态(DataStatus 数据状态) {
        this.数据状态 = 数据状态;
    }

}
