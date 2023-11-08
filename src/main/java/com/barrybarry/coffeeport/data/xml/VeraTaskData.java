package com.barrybarry.coffeeport.data.xml;

import com.barrybarry.coffeeport.type.CommandType;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
@Getter
@Setter
public class VeraTaskData {
    private CommandType cmd;
    private Object data;

    private String sid;

    public Data getData() {
        if (data == null) return null;
        if(data instanceof Data dat){
            return dat;
        }
        if (data instanceof String str) {
            return new Gson().fromJson(str, Data.class);
        }
        return null;
    }
}
