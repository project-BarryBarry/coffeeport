package com.barrybarry.coffeeport.utils;

import com.barrybarry.coffeeport.constant.Config;
import com.barrybarry.coffeeport.data.xml.PluginInstallInfoData;
import com.barrybarry.coffeeport.data.task.TaskData;
import com.barrybarry.coffeeport.data.xml.UiUpdateData;
import com.barrybarry.coffeeport.sigleton.TaskListManager;
import com.barrybarry.coffeeport.type.OsType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("unused")
public class SystemUtil {
    private static final Logger logger = LoggerFactory.getLogger(SystemUtil.class.getName());

    public static OsType getOs() {
        return switch (getOsName()) {
            case "Windows NT" -> OsType.Windows;
            case "Mac OS X" -> OsType.MacOS;
            case "Linux" -> OsType.Linux;
            case "Unix" -> OsType.Unix;
            default -> OsType.Unknown;
        };
    }

    public static String getOsName() {
        return SystemUtils.OS_NAME;
    }

    public static String getLinuxDistroName() {
        try {
            HashMap<String, String> distroInfo = new HashMap<>();
            String distroName, distroVersion;
            for (String line : Objects.requireNonNull(FileUtil.readContent("/etc/os-release")).split("\n")) {
                String[] tokenizedLine = line.split("=");
                String key = tokenizedLine[0], value = tokenizedLine[1];
                if (value.startsWith("\"")) {
                    value = StringUtil.unescapeString(value.substring(1, value.length() - 1));
                }
                distroInfo.put(key, value);
            }
            distroName = distroInfo.get("NAME");
            distroVersion = distroInfo.get("VERSION_ID");
            return distroName + "/" + distroVersion;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return System.getProperty("os.name");
    }

    public static String getAppDataPath() {
        String path = switch (getOs()) {
            case Windows -> System.getenv("AppData");
            case MacOS -> System.getProperty("user.home") + "/Library/Application Support";
            case Unix, Linux, Unknown -> System.getProperty("user.home") + "/.config";
        } + "/coffeeport/";
        if (!FileUtil.exists(path)) {
            FileUtil.createDir(path);
        }
        return path;
    }

    public static String getKeystorePath() {
        return getAppDataPath() + "keystore.jks";
    }

    public static String getUUid() {
        String uuid;
        if (FileUtil.exists(getAppDataPath() + "uuid")) {
            uuid = FileUtil.readContent(getAppDataPath() + "uuid");
        } else {
            uuid = UUID.randomUUID().toString();
            FileUtil.writeContent(getAppDataPath() + "uuid", uuid);
        }
        return uuid;
    }

    public static String getHashedUUID() {
        return SecurityUtil.encryptSha3(getUUid());
    }

    public static boolean isInstalled(String objectMime){
        return FileUtil.exists(FileUtil.parseFileUrl(objectMime));
    }

    private static PluginInstallInfoData getPluginInstallInfo(String pluginInstallInfo, String origin) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            // write xml to tmp dir for debug
            FileUtil.writeContent("./tmp/activexInfo.xml", pluginInstallInfo);
            PluginInstallInfoData info = xmlMapper.readValue(pluginInstallInfo, PluginInstallInfoData.class);
            logger.debug("Successfully parsed plugin install info");
            if(!info.getAllowContexts().equals("wizvera")){
                logger.warn("cannot find correct method to handle plugin info with {} context, but I'll try to handle it.", info.getAllowContexts());
            }
            // check allowed domains
            if(!SecurityUtil.checkAllowedDomain(origin, info.getAllowDomains().split(";"))){
                logger.error("origin {} is not allowed to install plugin. Allowed origin: {}", origin, info.getAllowDomains());
                return null;
            }
            return info;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public static PluginInstallInfoData getInstallationInfo(TaskData task) {
        String activexInfoBase64 = task.getData().getData().getConfigure().getActivexInfo();
        byte[] activexInfo = StringUtil.decodeBase64(activexInfoBase64);
        String axinfoContent = SecurityUtil.verifyCmsSign(activexInfo, Config.getVeraCert());
        // when activexInfo signature verification failed, we don't trust this activex
        if (axinfoContent == null) {
            logger.error("Failed to verify CMS signature of activexInfo");
            return null;
        }
        // parse xml
        return getPluginInstallInfo(axinfoContent, task.getOrigin());
    }

    public static void installActiveX(PluginInstallInfoData.PluginInfo info, @SuppressWarnings("unused") TaskData task) {

        // download plugin
//        String pluginUrl = (info.getDownloadUrl().startsWith("https://")||info.getDownloadUrl().startsWith("http://")) ? info.getDownloadUrl() : task.getOrigin() + info.getDownloadUrl();
//        String tmpPath = "/tmp/" + FileUtil.getFileName(info.getDownloadUrl());
//        InputStream in = HttpUtil.download(pluginUrl);
//        FileUtil.writeContent(tmpPath, in);
        Gson gson = new Gson();

        // install plugin using apt
        logger.debug("installing plugin using apt: {}", info.getDisplayName());
        try {
            UiUpdateData data = new UiUpdateData();
            // send dummy data
            for (int i = 0; i <= 10; i++) {
                ArrayList<String> uiUpdates = new ArrayList<>();
                int finalI = i;
                data.setFunc("VP_setInstallProgress");
                data.setArgs(new ArrayList<>() {{
                    add(finalI*10);
                }});
                uiUpdates.add(gson.toJson(data));

                data.setFunc("VP_setInstallMessage");
                if(i % 3 == 0) {
                    data.setArgs(new ArrayList<>() {{
                        add("Coffeeport 디버그 모드");
                    }});
                }
                else if(i % 3 == 1) {
                    data.setArgs(new ArrayList<>() {{
                        add("실제로 프로그램의 설치를 하지않습니다.");
                    }});
                }
                else {
                    data.setArgs(new ArrayList<>() {{
                        add(info.getDisplayName() + "을 설치하고 있습니다.");
                    }});
                }
                uiUpdates.add(gson.toJson(data));
                TaskListManager.getInstance().setUiUpdate(uiUpdates);
                logger.debug("plugin installation finished");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
}
