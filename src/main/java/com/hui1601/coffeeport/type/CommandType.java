package com.hui1601.coffeeport.type;

import com.google.gson.annotations.SerializedName;

public enum CommandType {
    @SerializedName("getVersion")
    VERSION("getVersion"),
    @SerializedName("getAxInfo")
    AX_INFO("getAxInfo");
    private final String type;
    CommandType(String type){
        this.type = type;
    }
    @Override
    public String toString(){
        return type;
    }
}
