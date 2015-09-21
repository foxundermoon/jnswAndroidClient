package com.jnsw.android.xunjian.db.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.jnsw.core.anotation.LocalField;

/**
 * Created by Administrator on 2015/8/1.
 */

@DatabaseTable(tableName = "Note")
public class Note {
    @LocalField
    @DatabaseField
    private int LID;
    @DatabaseField
    private int ID;
    @DatabaseField
    private String 类型;
    @DatabaseField
    private String 坐标串;
    @DatabaseField
    private String 文本;

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

    public String get类型() {
        return 类型;
    }

    public void set类型(String 类型) {
        this.类型 = 类型;
    }

    public String get坐标串() {
        return 坐标串;
    }

    public void set坐标串(String 坐标串) {
        this.坐标串 = 坐标串;
    }

    public String get文本() {
        return 文本;
    }

    public void set文本(String 文本) {
        this.文本 = 文本;
    }
}
