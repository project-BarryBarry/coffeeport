package com.hui1601.coffeeport.utils;

import com.google.gson.Gson;
import fi.iki.elonen.NanoHTTPD;

public class ResponseUtil {
    private static final String callbackFormat = "try {%s(%s);} catch(e){}";
    public static NanoHTTPD.Response getCallback(String callback, Object obj){
        String json = new Gson().toJson(obj);
        return NanoHTTPD.newFixedLengthResponse(String.format(callbackFormat, callback, json));
    }
}
