package com.hui1601.coffeeport.type;

public enum CommandType {
    VERSION("getVersion"), AX_INFO("getAxInfo");
    private String type = "";
    CommandType(String type){
        this.type = type;
    }
    @Override
    public String toString(){
        return type;
    }
}
