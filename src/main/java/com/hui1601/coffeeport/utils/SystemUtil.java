package com.hui1601.coffeeport.utils;

import com.hui1601.coffeeport.type.OsType;
import org.apache.commons.lang3.SystemUtils;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

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
    public static String getOsName(){
        return SystemUtils.OS_NAME;
    }
    public static String getLinuxDistroName(){
        try {
            HashMap<String, String> distroInfo = new HashMap<>();
            String distroName, distroVersion;
            for (String line : Objects.requireNonNull(FileUtil.readContent("/etc/os-release")).split("\n")){
                String[] tokenizedLine = line.split("=");
                String key = tokenizedLine[0], value = tokenizedLine[1];
                if(value.startsWith("\"")){
                    value = StringUtil.unescapeString(value.substring(1, value.length() - 1));
                }
                distroInfo.put(key, value);
            }
            distroName = distroInfo.get("NAME");
            distroVersion = distroInfo.get("VERSION_ID");
            return distroName + "/" + distroVersion;
        } catch (Exception e){
            Logger.getGlobal().severe(e.toString());
        }
        return System.getProperty("os.name");
    }
    public static String getAppDataPath(){
        String path = switch (getOs()){
            case Windows -> System.getenv("AppData");
            case MacOS -> System.getProperty("user.home") + "/Library/Application Support";
            case Unix,Linux,Unknown -> System.getProperty("user.home") + "/.config";
        } + "/coffeeport/";
        if(!FileUtil.exists(path)){
            FileUtil.createDir(path);
        }
        return path;
    }

    public static String getKeystorePath(){
        return getAppDataPath() + "keystore.jks";
    }

    public static String getUUid(){
        if(FileUtil.exists(getAppDataPath() + "uuid")){
            return FileUtil.readContent(getAppDataPath() + "uuid");
        }
        String uuid = UUID.randomUUID().toString();
        FileUtil.writeContent(getAppDataPath() + "uuid", uuid);
        return uuid;
    }

    public static String getHashedUUID(){
        return SecurityUtil.encryptSha3(getUUid());
    }
}
