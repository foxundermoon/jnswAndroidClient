package com.jnsw.core.data;

import com.google.common.base.Strings;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.event.TaskEvent;

import java.util.UUID;

/**
 * Created by foxundermoon on 2015/6/24.
 */
public class Task implements Runnable {
    private String id;
    private Runnable taskBody;
    private Runnable onSuccess;
    private Runnable onFailed;
    private String errorMsg;
    private boolean hasError;

    public void post() {
        CustomApplication.getInstance().eventBus.post(new TaskEvent(this));
    }

    public boolean isHasError() {
        return hasError;
    }

    public Task setHasError(boolean hasError) {
        this.hasError = hasError;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public Task setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public Runnable getOnSuccess() {
        return onSuccess;
    }

    public Task setOnSuccess(Runnable onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    public Runnable getOnFailed() {
        return onFailed;
    }

    public Task setOnFailed(Runnable onFailed) {
        this.onFailed = onFailed;
        return this;
    }

    public Runnable getTaskBody() {
        return taskBody;
    }

    public Task setTaskBody(Runnable taskBody) {
        this.taskBody = taskBody;
        return this;
    }

    public synchronized String getId() {
        if (id == null || "" == id) {
            id = UUID.randomUUID().toString();
        }
        return id;
    }

    public Task setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public void run() {
        if (taskBody != null) {
            try {
                taskBody.run();
                if (onSuccess != null) {
                    onSuccess.run();
                }
            } catch (Exception e) {
                hasError = true;
                errorMsg = e.getMessage();
                if (onFailed != null) {
                    onFailed.run();
                }
            }
        }

    }
}
