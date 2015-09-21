package com.jnsw.android.xunjian.db.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.jnsw.android.xunjian.db.DataStatus;
import com.jnsw.core.anotation.LocalField;
import com.jnsw.core.anotation.RemoteTable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by foxundermoon on 2015/7/17.
 */
@RemoteTable(tableName = "xx_专题_巡检_巡检点表")
@DatabaseTable(tableName = "巡检点表")
public class XunjianDian {
    @LocalField
    @DatabaseField(generatedId = true)
    private int LID;
    @DatabaseField
    private int ID;
    @DatabaseField
    private int 任务ID;
    @DatabaseField
    private int 用户ID;
    @DatabaseField
    private BigDecimal X;
    @DatabaseField
    private BigDecimal Y;
    @DatabaseField
    private Date 时间;
    @DatabaseField
    private  BigDecimal 速度;

    public int get版本() {
        return 版本;
    }

    public void set版本(int 版本) {
        this.版本 = 版本;
    }

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

    public int get任务ID() {
        return 任务ID;
    }

    public void set任务ID(int 任务ID) {
        this.任务ID = 任务ID;
    }

    public int get用户ID() {
        return 用户ID;
    }

    public void set用户ID(int 用户ID) {
        this.用户ID = 用户ID;
    }

    public BigDecimal getX() {
        return X;
    }

    public void setX(BigDecimal x) {
        X = x;
    }

    public BigDecimal getY() {
        return Y;
    }

    public void setY(BigDecimal y) {
        Y = y;
    }

    public Date get时间() {
        return 时间;
    }

    public void set时间(Date 时间) {
        this.时间 = 时间;
    }

    public BigDecimal get速度() {
        return 速度;
    }

    public void set速度(BigDecimal 速度) {
        this.速度 = 速度;
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
