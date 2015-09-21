package com.jnsw.android.xunjian.db.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.jnsw.android.xunjian.db.DataStatus;
import com.jnsw.core.anotation.LocalField;
import com.jnsw.core.anotation.RemoteTable;

import java.math.BigDecimal;

/**
 * Created by foxundermoon on 2015/7/17.
 */
@RemoteTable(tableName = "xx_专题_巡检_线路节点表")
@DatabaseTable(tableName = "线路节点表")
public class XianluJiedian {
    @DatabaseField(generatedId = true)
    @LocalField

    private int LID;
    @DatabaseField
    private int ID;
    @DatabaseField
    private int 节点序号;
    @DatabaseField
    private int 线路ID;
    @DatabaseField
    private BigDecimal 坐标X;

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

    public int get节点序号() {
        return 节点序号;
    }

    public void set节点序号(int 节点序号) {
        this.节点序号 = 节点序号;
    }

    public int get线路ID() {
        return 线路ID;
    }

    public void set线路ID(int 线路ID) {
        this.线路ID = 线路ID;
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

    public String get节点类型() {
        return 节点类型;
    }

    public void set节点类型(String 节点类型) {
        this.节点类型 = 节点类型;
    }

    public String get节点描述() {
        return 节点描述;
    }

    public void set节点描述(String 节点描述) {
        this.节点描述 = 节点描述;
    }

    @DatabaseField
    private BigDecimal 坐标Y;
    @DatabaseField
    private String 节点类型;
    @DatabaseField
    private String 节点描述;
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
