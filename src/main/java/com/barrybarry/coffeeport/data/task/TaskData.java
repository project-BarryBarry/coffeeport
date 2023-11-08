package com.barrybarry.coffeeport.data.task;

import com.barrybarry.coffeeport.data.xml.VeraTaskData;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
@Setter
@Getter
public class TaskData {
    private String callback;

    private String sid;
    private VeraTaskData data;

    private Object callbackData;

    private String origin;

    public void setData(String json) {
        this.data = new Gson().fromJson(json, VeraTaskData.class);
    }

    @SuppressWarnings({"LombokSetterMayBeUsed", "RedundantSuppression"})
    public void setData(VeraTaskData data) {
        this.data = data;
    }
}

