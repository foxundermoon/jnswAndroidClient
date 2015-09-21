package com.jnsw.android.xunjian.db.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.jnsw.core.anotation.LocalField;
import com.jnsw.core.anotation.RemoteTable;

/**
 * Created by fox on 8/6/2015.
 */
@RemoteTable(tableName = "xx_专题_巡检_调压箱图片表")
@DatabaseTable(tableName = "调压箱图片")
public class TiaoyaxiangTupian {
    @DatabaseField(generatedId = true)
    @LocalField
    private int LID;
    @DatabaseField(unique = true)
    private int ID;
    @DatabaseField
    private int 调压箱记录ID;
    @DatabaseField
    private String 文件名;
    @DatabaseField
    private int 文件ID;
    public int get文件ID() {
        return 文件ID;
    }

    public void set文件ID(int 文件ID) {
        this.文件ID = 文件ID;
    }

    public String get文件名() {
        return 文件名;
    }

    public void set文件名(String 文件名) {
        this.文件名 = 文件名;
    }

    public int get调压箱记录ID() {
        return 调压箱记录ID;
    }

    public void set调压箱记录ID(int 调压箱记录ID) {
        this.调压箱记录ID = 调压箱记录ID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getLID() {
        return LID;
    }

    public void setLID(int LID) {
        this.LID = LID;
    }

}
