package com.hui1601.coffeeport.dto;

import com.google.gson.Gson;

public class TaskDTO {
    private String callback;
    private TaskData data;

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

    public void setData(String json){
        this.data = new Gson().fromJson(json, TaskData.class);
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
                "callback='" + callback + '\'' +
                ", data=" + data +
                '}';
    }
}

