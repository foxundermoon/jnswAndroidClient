package com.jnsw.android.xunjian.db.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.jnsw.android.xunjian.db.DataStatus;
import com.jnsw.core.anotation.LocalField;

/**
 * Created by Administrator on 2015/7/23.
 */
@DatabaseTable(tableName = "网络连接")
public class WangLuoLianJie {
    @DatabaseField(generatedId = true)
    @LocalField
    private int LID;
    @DatabaseField
    private int ID;
    @DatabaseField
    private String IP;
    @DatabaseField
    private String  端口号;
    @DatabaseField
    private String 程序状态;
    @DatabaseField
    private String 启用状态;
    @DatabaseField
    private String 类型;

    public int get版本() {
        return 版本;
    }

    public void set版本(int 版本) {
        this.版本 = 版本;
    }

    @DatabaseField
    private int 版本;

    public String get类型() {
        return 类型;
    }

    public void set类型(String 类型) {
        this.类型 = 类型;
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

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String get端口号() {
        return 端口号;
    }

    public void set端口号(String 端口号) {
        this.端口号 = 端口号;
    }

    public String get程序状态() {
        return 程序状态;
    }

    public void set程序状态(String 程序状态) {
        this.程序状态 = 程序状态;
    }

    public String get启用状态() {
        return 启用状态;
    }

    public void set启用状态(String 启用状态) {
        this.启用状态 = 启用状态;
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
