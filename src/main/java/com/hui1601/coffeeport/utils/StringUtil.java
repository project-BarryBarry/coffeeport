package com.hui1601.coffeeport.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtil {
    public static String unescapeString(String str){
        return StringEscapeUtils.unescapeJson(str);
    }

}
