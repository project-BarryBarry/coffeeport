package com.barrybarry.coffeeport.type;

public enum LanguageType {
    KOREAN("kor");
    private String type = "";
    LanguageType(String type){
        this.type = type;
    }
    @Override
    public String toString(){
        return type;
    }
}
