package com.barrybarry.coffeeport.type;

public enum ContextType {
    TEXT("html"), BASE64("base64");
    private String type = "";
    ContextType(String type){
        this.type = type;
    }
    @Override
    public String toString(){
        return type;
    }
}
