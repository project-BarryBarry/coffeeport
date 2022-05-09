package com.hui1601.coffeeport.dto;

public class ResponseDTO {
    private int res;
    private Object data;

    public int getRes() {
        return res;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setRes(int res) {
        this.res = res;
    }

    @Override
    public String toString() {
        return "ResponseDTO{" +
                "res=" + res +
                ", data=" + data +
                '}';
    }
}
