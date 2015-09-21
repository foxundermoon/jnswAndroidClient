package com.jnsw.android.xunjian.db.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.jnsw.android.xunjian.db.TaskStatus;
import com.jnsw.core.anotation.LocalField;

/**
 * Created by fox on 2015/8/14.
 */
@DatabaseTable(tableName = "任务记录表")
public class TaskRecord {
    public int getLID() {
        return LID;
    }

    public void setLID(int LID) {
        this.LID = LID;
    }

    public int get排班ID() {
        return 排班ID;
    }

    public void set排班ID(int 排班ID) {
        this.排班ID = 排班ID;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @DatabaseField(generatedId = true)
    @LocalField
    private int LID;
    @DatabaseField
    private int 排班ID;
    @DatabaseField
    private TaskStatus taskStatus;

    public int get版本() {
        return 版本;
    }

    public void set版本(int 版本) {
        this.版本 = 版本;
    }

    @DatabaseField
    private int 版本;

}
