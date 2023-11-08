package com.barrybarry.coffeeport.converter;

import com.barrybarry.coffeeport.data.task.VeraData;
import com.google.gson.Gson;
import com.linecorp.armeria.common.HttpData;
import com.linecorp.armeria.common.HttpHeaders;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.ResponseHeaders;
import com.linecorp.armeria.common.annotation.Nullable;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.annotation.ResponseConverterFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.regex.Pattern;

public class VeraDataConverter implements ResponseConverterFunction {
    // 正直、この方法は良くないと思う
    private static final String callbackFormat = "try {%s(%s);} catch(e){}";
    private final String[] allowedCallbackPrefix = {"vp20handler_callback", "jQuery"};

    @Override
    public @NotNull HttpResponse convertResponse(@NotNull ServiceRequestContext ctx, @NotNull ResponseHeaders headers, @Nullable Object res, @NotNull HttpHeaders trailers) {
        HttpResponse response;
        VeraData result = (VeraData) res;
        if (result == null) {
            response = HttpResponse.of(headers, HttpData.ofUtf8(String.format(callbackFormat, "alert", "\"error!\"")), trailers);
        } else {
            // Migration: callback xss vulnerability
            Pattern pattern = Pattern.compile("[A-Za-z0-9_]+");
            if (!pattern.matcher(result.getCallback()).matches() || Arrays.stream(allowedCallbackPrefix).noneMatch(result.getCallback()::startsWith)) {
                // do nothing
                response = HttpResponse.of(headers, HttpData.ofUtf8(String.format(callbackFormat, "void", "0")));
            } else {
                String json = new Gson().toJson(result.getResponseData());
                response = HttpResponse.of(headers, HttpData.ofUtf8(String.format(callbackFormat, result.getCallback(), json)), trailers);
            }
        }
        return response;
    }
}
