package com.jnsw.core.event;

import com.jnsw.core.data.Task;

/**
 * Created by foxundermoon on 2015/6/24.
 */
public class TaskEvent extends AppEvent<Task> {
    public TaskEvent(Task task){
        setEventData(task);
    }
}
