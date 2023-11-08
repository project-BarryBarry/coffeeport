package com.barrybarry.coffeeport.handler;

import com.barrybarry.coffeeport.constant.Config;
import com.barrybarry.coffeeport.data.*;
import com.barrybarry.coffeeport.data.task.TaskData;
import com.barrybarry.coffeeport.data.xml.InstallationData;
import com.barrybarry.coffeeport.data.xml.PluginInstallInfoData;
import com.barrybarry.coffeeport.data.xml.UiUpdateData;
import com.barrybarry.coffeeport.sigleton.TaskListManager;
import com.barrybarry.coffeeport.type.OsType;
import com.barrybarry.coffeeport.utils.SystemUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);
    public static void handleCommand(ResponseData res, TaskData task){
        res.setRes(0);
        if(task.getData().getCmd() != null) {
            res.setData(switch (task.getData().getCmd()) {
                case VERSION -> getVersion();
                case OS_INFO -> getOsInfo();
                case RESULT -> getResult(task.getSid());
                case AX_INFO -> getInstallationInfo(task);
                case SHOW -> startInstallation(task);
                case IS_RUNNING -> isRunning();
                case UPDATE_UI -> updateUI();
            });
        }
        if(res.getData() == null) res.setRes(1);
    }

    private static Object updateUI() {
        logger.debug("updateUI");
        List<String> Ui = new ArrayList<>(TaskListManager.getInstance().getUiUpdate());
        TaskListManager.getInstance().getUiUpdate().clear();
        return Ui;
    }

    private static Object isRunning() {
        return TaskListManager.getInstance().isInstallationRunning()?1:0;
    }

    private static Object getVersion(){
        return Config.veraVersion;
    }
    private static Object getInstallationInfo(TaskData task){
        PluginInstallInfoData info = SystemUtil.getInstallationInfo(task);
        if(info == null) return null;
        String origin = task.getOrigin();
        ArrayList<InstallationData> installationDataList = new ArrayList<>();
        for (PluginInstallInfoData.PluginInfo pluginInfo : info.getPlugins()) {
            InstallationData installationData = new InstallationData();
            installationData.setObjectName(pluginInfo.getObjectName());
            installationData.setObjectVersion(pluginInfo.getObjectVersion());
            if(!pluginInfo.getBackupUrl().startsWith("https://")) {
                installationData.setBackupUrl(origin + "/" + pluginInfo.getBackupUrl());
            }
            else{
                installationData.setBackupUrl(pluginInfo.getBackupUrl());
            }
            if(!pluginInfo.getDownloadUrl().startsWith("https://")) {
                installationData.setDownloadUrl(origin + "/" + pluginInfo.getDownloadUrl());
            }
            else{
                installationData.setDownloadUrl(pluginInfo.getDownloadUrl());
            }
            installationData.setDisplayName(pluginInfo.getDisplayName());
            installationData.setObjectVersion(pluginInfo.getObjectVersion());
            installationData.setUpdateState(false);
//            installationData.setInstallState(true);
                installationData.setInstallState(SystemUtil.isInstalled(pluginInfo.getObjectMIMEType()));
            installationDataList.add(installationData);
        }
        return installationDataList;
    }
    private static Object getOsInfo(){
        if(SystemUtil.getOs() == OsType.Linux)  return SystemUtil.getLinuxDistroName();
        return SystemUtil.getOsName(); // It seems checks when os is linux, so we don't need to consider windows, macos, etc.
    }

    private static Object startInstallation(TaskData task){
        return installActiveX(task);
    }

    private static Object installActiveX(TaskData task) {
        String[] installationList = task.getData().getData().getConfigure().getSelectObject().split(",");
        PluginInstallInfoData activexInfo = SystemUtil.getInstallationInfo(task);
        assert activexInfo != null;
        TaskListManager.getInstance().setInstallationRunning(true);
        for (String installation:installationList) {
            for (PluginInstallInfoData.PluginInfo info:activexInfo.getPlugins()) {
                if(info.getObjectName().equals(installation)){
                    logger.debug("installActiveX: {}", info.getObjectName());
                    SystemUtil.installActiveX(info,task);
                }
            }
        }

        // install done, send complete message
        UiUpdateData uiUpdateData = new UiUpdateData();
        uiUpdateData.setFunc("VP_complete");
        TaskListManager.getInstance().setUiUpdate(new ArrayList<>() {{add(new Gson().toJson(uiUpdateData));}});
        TaskListManager.getInstance().setInstallationRunning(false);
        return new Object();
    }

    private static Object getResult(String sid){
        TaskData task = TaskListManager.getInstance().getTask(sid);
        TaskListManager.getInstance().removeTask(sid);
        if(task == null || task.getCallbackData() == null){
            return "";
        }
        if(task.getCallbackData() instanceof ResponseData result) {
            return result.getData();
        }
        if (task.getCallbackData() instanceof String str) {
            return str;
        }
        throw new IllegalStateException("Unexpected value: " + task.getCallbackData());
    }
}
