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
@RemoteTable(tableName = "xx_专题_巡检_调压箱记录表")
@DatabaseTable(tableName = "调压箱记录表")
public class TiaoxiaxiangJilu {
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

    public int get用户ID() {
        return 用户ID;
    }

    public void set用户ID(int 用户ID) {
        this.用户ID = 用户ID;
    }

    public int get任务ID() {
        return 任务ID;
    }

    public void set任务ID(int 任务ID) {
        this.任务ID = 任务ID;
    }

    public BigDecimal get坐标X() {
        return 坐标X;
    }

    public void set坐标X(BigDecimal 坐标X) {
        this.坐标X = 坐标X;
    }

    public BigDecimal get坐标Y() {
        return 坐标Y;
    }

    public void set坐标Y(BigDecimal 坐标Y) {
        this.坐标Y = 坐标Y;
    }

    public String get调压箱名() {
        return 调压箱名;
    }

    public void set调压箱名(String 调压箱名) {
        this.调压箱名 = 调压箱名;
    }

    public BigDecimal get进口压力1() {
        return 进口压力1;
    }

    public void set进口压力1(BigDecimal 进口压力1) {
        this.进口压力1 = 进口压力1;
    }

    public BigDecimal get进口压力2() {
        return 进口压力2;
    }

    public void set进口压力2(BigDecimal 进口压力2) {
        this.进口压力2 = 进口压力2;
    }

    public BigDecimal get出口压力1() {
        return 出口压力1;
    }

    public void set出口压力1(BigDecimal 出口压力1) {
        this.出口压力1 = 出口压力1;
    }

    public BigDecimal get出口压力2() {
        return 出口压力2;
    }

    public void set出口压力2(BigDecimal 出口压力2) {
        this.出口压力2 = 出口压力2;
    }

    public String get运行隐患() {
        return 运行隐患;
    }

    public void set运行隐患(String 运行隐患) {
        this.运行隐患 = 运行隐患;
    }

    public String get隐患描述() {
        return 隐患描述;
    }

    public void set隐患描述(String 隐患描述) {
        this.隐患描述 = 隐患描述;
    }

    public String get隐患地址() {
        return 隐患地址;
    }

    public void set隐患地址(String 隐患地址) {
        this.隐患地址 = 隐患地址;
    }

    public Date get巡检时间() {
        return 巡检时间;
    }

    public void set巡检时间(Date 巡检时间) {
        this.巡检时间 = 巡检时间;
    }

    @DatabaseField(generatedId = true)
    @LocalField

    private int LID;
    @DatabaseField
    private int ID;
    @DatabaseField
    private int 用户ID;
    @DatabaseField
    private int 任务ID;
    @DatabaseField
    private BigDecimal 坐标X;
    @DatabaseField
    private BigDecimal 坐标Y;
    @DatabaseField
    private String 调压箱名;
    @DatabaseField
    private BigDecimal 进口压力1;
    @DatabaseField
    private BigDecimal 进口压力2;
    @DatabaseField
    private BigDecimal 出口压力1;
    @DatabaseField
    private BigDecimal 出口压力2;
    @DatabaseField
    private String 运行隐患;
    @DatabaseField
    private String 隐患描述;
    @DatabaseField
    private String 隐患地址;
    @DatabaseField
    private Date 巡检时间;
    @DatabaseField
    private int 图片ID;

    public int get版本() {
        return 版本;
    }

    public void set版本(int 版本) {
        this.版本 = 版本;
    }

    @LocalField
    @DatabaseField
    private int 版本;

    public int get图片ID() {
        return 图片ID;
    }

    public void set图片ID(int 图片ID) {
        this.图片ID = 图片ID;
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
