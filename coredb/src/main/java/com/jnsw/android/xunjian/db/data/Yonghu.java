package com.jnsw.android.xunjian.db.data;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.jnsw.android.xunjian.db.DataStatus;
import com.jnsw.core.anotation.LocalField;
import com.jnsw.core.anotation.RemoteTable;

import java.util.Date;

/**
 * Created by Administrator on 2015/7/22.
 */
@RemoteTable(tableName = "xx_用户表")
@DatabaseTable(tableName = "用户表")
public class Yonghu {
    @LocalField
    @DatabaseField(generatedId = true)
    private int LID;
    @DatabaseField
    private int ID;
    @DatabaseField  //唯一键 不重复
    private String 登录名;
    @DatabaseField
    private String 名称;
    @DatabaseField
    private String 密码;
    @DatabaseField
    private String 状态;
    @LocalField
    @DatabaseField(dataType = DataType.DATE_STRING,format = "yyyy-MM-dd HH:mm:ss")
    private Date 登录日期;
    @LocalField
    @DatabaseField
    private String 保存密码;
    @DatabaseField
    private int 版本;

    public int get版本() {
        return 版本;
    }

    public void set版本(int 版本) {
        this.版本 = 版本;
    }

    public String get保存密码() {
        return 保存密码;
    }

    public void set保存密码(String 保存密码) {
        this.保存密码 = 保存密码;
    }

    public Date get登录日期() {
        return 登录日期;
    }

    public void set登录日期(Date 登录日期) {
        this.登录日期 = 登录日期;
    }

    public String get名称() {
        return 名称;
    }

    public void set名称(String 名称) {
        this.名称 = 名称;
    }

    public String get状态() {
        return 状态;
    }

    public void set状态(String 状态) {
        this.状态 = 状态;
    }

    public String get登录名() {
        return 登录名;
    }

    public void set登录名(String 登录名) {
        this.登录名 = 登录名;
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


    public String get密码() {
        return 密码;
    }

    public void set密码(String 密码) {
        this.密码 = 密码;
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
