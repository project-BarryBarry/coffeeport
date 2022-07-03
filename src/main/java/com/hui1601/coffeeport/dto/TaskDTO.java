package com.hui1601.coffeeport.dto;

import com.google.gson.Gson;

public class TaskDTO {
    private String callback;

    private String sid;
    private TaskData data;

    private Object callbackData;

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public TaskData getData() {
        return data;
    }

    public void setData(TaskData data) {
        this.data = data;
    }

    public Object getCallbackData() {
        return callbackData;
    }

    public void setCallbackData(Object callbackData) {
        this.callbackData = callbackData;
    }

    public void setData(String json){
        this.data = new Gson().fromJson(json, TaskData.class);
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
                "callback='" + callback + '\'' +
                ", data=" + data +
                '}';
    }
}

