package com.hui1601.coffeeport.utils;

import com.hui1601.coffeeport.type.OsType;
import org.apache.commons.lang3.SystemUtils;

public class SystemUtil {
    public static OsType getOs(){
        if(SystemUtils.IS_OS_WINDOWS){
            return OsType.Windows;
        }
        if(SystemUtils.IS_OS_MAC){
            return OsType.MacOS;
        }
        if(SystemUtils.IS_OS_LINUX){
            return OsType.Linux;
        }
        if(SystemUtils.IS_OS_UNIX){
            return OsType.Unix;
        }
        return OsType.Unknown;
    }
}
