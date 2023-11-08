package com.barrybarry.coffeeport.utils;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;

public class StringUtil {
    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);
    public static String unescapeString(String str) {
        return StringEscapeUtils.unescapeJson(str);
    }

    //decode base64
    public static byte[] decodeBase64(String str) {
        // remove line breaks(\r, \n)
        str = str.replace("\r", "").replace("\n", "");
        
        return Base64.getDecoder().decode(str);
    }

    //decode url encoded string
    public static String decodeUri(String str) {
        return URLDecoder.decode(str, StandardCharsets.UTF_8);
    }

    //parse uri data
    public static HashMap<String,String> parseUriData(String uri) {
        logger.info("Parsing URI data: " + uri);
        HashMap<String,String> data = new HashMap<>();
        String[] tokenizedData = uri.split("&");
        Arrays.stream(tokenizedData).forEach((String str) -> {
            String[] tokenizedStr = str.split("=");
            data.put(tokenizedStr[0], decodeUri(tokenizedStr[1]));
        });
        return data;
    }
}
