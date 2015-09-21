package com.jnsw.android.xunjian.db.data;

import com.j256.ormlite.field.DataType;
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
@RemoteTable(tableName = "xx_专题_巡检_巡检工作点表")
@DatabaseTable(tableName = "巡检点工作表")
public class XunjiandianGongzuo {
    @DatabaseField(generatedId = true)
    @LocalField
    private int LID;
    @DatabaseField
    private int ID;

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

    public Date get巡检时间() {
        return 巡检时间;
    }

    public void set巡检时间(Date 巡检时间) {
        this.巡检时间 = 巡检时间;
    }

    @DatabaseField
    private int 节点序号;
    @DatabaseField
    private int 任务ID;
    @DatabaseField
    private BigDecimal 坐标X;
    @DatabaseField
    private BigDecimal 坐标Y;
    @DatabaseField
    private String 节点类型;
    @DatabaseField
    private String 节点描述;
    @DatabaseField(dataType = DataType.DATE_STRING,format = "yyyy-MM-dd HH:mm:ss")
    private Date 巡检时间;

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
