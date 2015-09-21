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
@RemoteTable(tableName = "xx_专题_巡检_线路分配表")
@DatabaseTable(tableName = "线路分配表")
public class XianluFenpei {
    @DatabaseField(generatedId = true)
    @LocalField

    private int LID;
    @DatabaseField
    private int ID;
    @DatabaseField
    private int 线路ID;
    @DatabaseField
    private int 用户ID;
    @DatabaseField
    private String 用户名称;
    @DatabaseField(dataType = DataType.DATE_STRING,format = "yyyy-MM-dd HH:mm:ss")
    private Date 下发日期;
    @DatabaseField
    private String 是否历史;

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

    public int get线路ID() {
        return 线路ID;
    }

    public void set线路ID(int 线路ID) {
        this.线路ID = 线路ID;
    }

    public int get用户ID() {
        return 用户ID;
    }

    public void set用户ID(int 用户ID) {
        this.用户ID = 用户ID;
    }

    public String get用户名称() {
        return 用户名称;
    }

    public void set用户名称(String 用户名称) {
        this.用户名称 = 用户名称;
    }

    public Date get下发日期() {
        return 下发日期;
    }

    public void set下发日期(Date 下发日期) {
        this.下发日期 = 下发日期;
    }

    public String get是否历史() {
        return 是否历史;
    }

    public void set是否历史(String 是否历史) {
        this.是否历史 = 是否历史;
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
    @LocalField
    private DataStatus 数据状态;
    public DataStatus get数据状态() {
        return 数据状态;
    }

    public void set数据状态(DataStatus 数据状态) {
        this.数据状态 = 数据状态;
    }


}
