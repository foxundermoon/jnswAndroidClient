package com.jnsw.android.xunjian.db.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.jnsw.android.xunjian.db.DataStatus;
import com.jnsw.core.anotation.LocalField;
import com.jnsw.core.anotation.RemoteTable;

import java.util.Date;

/**
 * Created by foxundermoon on 2015/7/17.
 */
@RemoteTable(tableName = "xx_专题_巡检_故障点表")
@DatabaseTable(tableName = "故障点表")
public class GuzhangDian {
    @DatabaseField(generatedId = true)
    @LocalField

    private int LID;
    @DatabaseField
    private int ID;
    @DatabaseField
    private int 任务ID;

    @DatabaseField
    private String 坐标X;
    @DatabaseField
    private String 坐标Y;

    @DatabaseField
    private String 隐患大类;
    @DatabaseField
    private String 隐患小类;
    @DatabaseField
    private String 隐患名称;
    @DatabaseField
    private String 施工单位;
    @DatabaseField
    private String 占压单位;
    @DatabaseField
    private String 隐患地址;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int get任务ID() {
        return 任务ID;
    }

    public void set任务ID(int 任务ID) {
        this.任务ID = 任务ID;
    }

    public int getLID() {
        return LID;
    }

    public void setLID(int LID) {
        this.LID = LID;
    }

    public String get坐标X() {
        return 坐标X;
    }

    public void set坐标X(String 坐标X) {
        this.坐标X = 坐标X;
    }

    public String get坐标Y() {
        return 坐标Y;
    }

    public void set坐标Y(String 坐标Y) {
        this.坐标Y = 坐标Y;
    }

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

    public String get隐患名称() {
        return 隐患名称;
    }

    public void set隐患名称(String 隐患名称) {
        this.隐患名称 = 隐患名称;
    }

    public String get施工单位() {
        return 施工单位;
    }

    public void set施工单位(String 施工单位) {
        this.施工单位 = 施工单位;
    }

    public String get占压单位() {
        return 占压单位;
    }

    public void set占压单位(String 占压单位) {
        this.占压单位 = 占压单位;
    }

    public String get隐患地址() {
        return 隐患地址;
    }

    public void set隐患地址(String 隐患地址) {
        this.隐患地址 = 隐患地址;
    }

    public String get隐患描述() {
        return 隐患描述;
    }

    public void set隐患描述(String 隐患描述) {
        this.隐患描述 = 隐患描述;
    }

    public Date get巡检时间() {
        return 巡检时间;
    }

    public void set巡检时间(Date 巡检时间) {
        this.巡检时间 = 巡检时间;
    }

    public String get处理状态() {
        return 处理状态;
    }

    public void set处理状态(String 处理状态) {
        this.处理状态 = 处理状态;
    }

    public String get处理意见() {
        return 处理意见;
    }

    public void set处理意见(String 处理意见) {
        this.处理意见 = 处理意见;
    }


    @DatabaseField
    private String 隐患描述;
    @DatabaseField
    private Date 巡检时间;
    @DatabaseField
    private String 处理状态;
    @DatabaseField
    private String 处理意见;

    public int get用户ID() {
        return 用户ID;
    }

    public void set用户ID(int 用户ID) {
        this.用户ID = 用户ID;
    }

    @DatabaseField
    private int 用户ID;
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
