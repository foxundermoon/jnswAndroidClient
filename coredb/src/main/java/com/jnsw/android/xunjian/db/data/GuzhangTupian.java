package com.jnsw.android.xunjian.db.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.jnsw.android.xunjian.db.DataStatus;
import com.jnsw.core.anotation.LocalField;
import com.jnsw.core.anotation.RemoteTable;

/**
 * Created by foxundermoon on 2015/7/17.
 */
@RemoteTable(tableName = "xx_专题_巡检_故障图片表")
@DatabaseTable(tableName = "故障图片表")
public class GuzhangTupian {
    @DatabaseField(generatedId = true)
    @LocalField
    private int LID;
    @DatabaseField
    private int ID;
    @DatabaseField
    private int 故障ID;
    @DatabaseField
    private String 文件名;
    @DatabaseField
    private int 文件ID;
    @DatabaseField
    private  String 故障类别;

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

    public int get故障ID() {
        return 故障ID;
    }

    public void set故障ID(int 故障ID) {
        this.故障ID = 故障ID;
    }

    public String get文件名() {
        return 文件名;
    }

    public void set文件名(String 文件名) {
        this.文件名 = 文件名;
    }

    public int get文件ID() {
        return 文件ID;
    }

    public void set文件ID(int 文件ID) {
        this.文件ID = 文件ID;
    }

    public String get故障类别() {
        return 故障类别;
    }

    public void set故障类别(String 故障类型) {
        this.故障类别 = 故障类型;
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
