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
@RemoteTable(tableName = "xx_专题_巡检_排班")
@DatabaseTable(tableName = "排班表")
public class PaiBan {
    @DatabaseField(generatedId = true)
    @LocalField

    private int LID;
    @DatabaseField
    private int ID;
    @DatabaseField
    private int 线路ID;
    @LocalField
    @DatabaseField(foreign = true,foreignAutoRefresh = true,foreignColumnName = "ID")
    private XianLu 线路;
    @DatabaseField
    private  int 用户ID;
    @DatabaseField(dataType = DataType.DATE_STRING,format = "yyyy-MM-dd HH:mm:ss")
    private Date 计划日期;
    @DatabaseField
    private String 执行状态;
    @DatabaseField
    private int 应巡点数;
    @DatabaseField
    private int 已巡点数;
    @DatabaseField
    private String 是否历史;
    @DatabaseField
    private int 版本;

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

    public Date get计划日期() {
        return 计划日期;
    }

    public void set计划日期(Date 计划日期) {
        this.计划日期 = 计划日期;
    }

    public String get执行状态() {
        return 执行状态;
    }

    public void set执行状态(String 执行状态) {
        this.执行状态 = 执行状态;
    }

    public int get应巡点数() {
        return 应巡点数;
    }

    public void set应巡点数(int 应巡点数) {
        this.应巡点数 = 应巡点数;
    }

    public int get已巡点数() {
        return 已巡点数;
    }

    public void set已巡点数(int 已巡点数) {
        this.已巡点数 = 已巡点数;
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
    @LocalField
    private DataStatus 数据状态;
    public DataStatus get数据状态() {
        return 数据状态;
    }

    public void set数据状态(DataStatus 数据状态) {
        this.数据状态 = 数据状态;
    }

}
