package com.hui1601.coffeeport.dto;

import com.hui1601.coffeeport.type.CommandType;

public class Data {
    private CommandType cmd;

    public CommandType getCmd() {
        return cmd;
    }

    public void setCmd(CommandType cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return "Data{" +
                "cmd=" + cmd +
                '}';
    }
}
