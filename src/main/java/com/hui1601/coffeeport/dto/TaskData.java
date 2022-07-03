package com.hui1601.coffeeport.dto;

import com.google.gson.Gson;
import com.hui1601.coffeeport.type.CommandType;

public class TaskData {
    private CommandType cmd;
    private Object data;

    private String sid;

    public CommandType getCmd() {
        return cmd;
    }

    public void setCmd(CommandType cmd) {
        this.cmd = cmd;
    }

    public Data getData() {
        return switch (data){
            case String string -> (Data) (data = new Gson().fromJson(string, Data.class));
            case Data data -> data;
            default -> null;
        };
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @Override
    public String toString() {
        return "TaskData{" +
                "cmd=" + cmd +
                ", data=" + data +
                '}';
    }
}
