package com.barrybarry.coffeeport.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.InputStream;
import java.util.Objects;

public class HttpUtil {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";
    private static final OkHttpClient client = new OkHttpClient.Builder().build();
    public static InputStream download(String url) {
        try {
            return Objects.requireNonNull(client.newCall(new Request.Builder().addHeader("user-agent", USER_AGENT).url(url).build()).execute().body()).byteStream();
        } catch (Exception ignored) {
        }
        return null;
    }
}
