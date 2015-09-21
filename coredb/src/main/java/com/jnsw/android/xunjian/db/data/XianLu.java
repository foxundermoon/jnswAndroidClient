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
@RemoteTable(tableName = "xx_专题_巡检_线路表")
@DatabaseTable(tableName = "线路表")
public class XianLu {
    @DatabaseField(generatedId = true)
    @LocalField
    private int LID;
    @DatabaseField
    private int ID;
    @DatabaseField
    private String 线路名称;
    @DatabaseField
    private String 线路类型;
    @DatabaseField(dataType = DataType.STRING, width = 1000)
    private String 线路描述;
    @DatabaseField(width = 200)
    private String 巡检意见;
    @DatabaseField
    private int 巡检周期;
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

    public String get线路名称() {
        return 线路名称;
    }

    public void set线路名称(String 线路名称) {
        this.线路名称 = 线路名称;
    }

    public String get线路类型() {
        return 线路类型;
    }

    public void set线路类型(String 线路类型) {
        this.线路类型 = 线路类型;
    }

    public String get线路描述() {
        return 线路描述;
    }

    public void set线路描述(String 线路描述) {
        this.线路描述 = 线路描述;
    }

    public String get巡检意见() {
        return 巡检意见;
    }

    public void set巡检意见(String 巡检意见) {
        this.巡检意见 = 巡检意见;
    }

    public int get巡检周期() {
        return 巡检周期;
    }

    public void set巡检周期(int 巡检周期) {
        this.巡检周期 = 巡检周期;
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

    public Date get创建时间() {
        return 创建时间;
    }

    public void set创建时间(Date 创建时间) {
        this.创建时间 = 创建时间;
    }

    @DatabaseField
    private int 版本;
    @DatabaseField(dataType = DataType.DATE_STRING,format = "yyyy-MM-dd HH:mm:ss")
    private Date 创建时间;
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
