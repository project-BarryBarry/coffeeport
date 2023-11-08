package com.barrybarry.coffeeport.type;

import com.google.gson.annotations.SerializedName;

public enum CommandType {
    @SerializedName("getVersion")
    VERSION("getVersion"),
    @SerializedName("getOsInfo")
    OS_INFO("getOsInfo"),
    @SerializedName("getResult")
    RESULT("getResult"),
    @SerializedName("getAxInfo")
    AX_INFO("getAxInfo"),
    @SerializedName("show")
    SHOW("show"),
    @SerializedName("isRunning")
    IS_RUNNING("isRunning"),
    @SerializedName("updateUI")
    UPDATE_UI("updateUI"),;
    private final String type;
    CommandType(String type){
        this.type = type;
    }
    @Override
    public String toString(){
        return type;
    }
}
