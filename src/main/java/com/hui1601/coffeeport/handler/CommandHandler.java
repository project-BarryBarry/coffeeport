package com.hui1601.coffeeport.handler;

import com.hui1601.coffeeport.constant.Config;
import com.hui1601.coffeeport.dto.Data;
import com.hui1601.coffeeport.dto.ResponseDTO;
import com.hui1601.coffeeport.dto.TaskDTO;
import com.hui1601.coffeeport.sigleton.TaskListManager;
import com.hui1601.coffeeport.type.OsType;
import com.hui1601.coffeeport.utils.SystemUtil;

public class CommandHandler {
    public static void handleCommand(ResponseDTO res, TaskDTO task){
        res.setRes(0);
        if(task.getData().getCmd() != null) {
            res.setData(switch (task.getData().getCmd()) {
                case VERSION -> getVersion();
                case OS_INFO -> getOsInfo();
                case RESULT -> getResult(task.getSid());
                case AX_INFO -> getInstallationInfo();
            });
        }
        if(res.getData() == null) res.setRes(1);
    }
    private static Object getVersion(){
        return Config.veraVersion;
    }
    private static Object getInstallationInfo(){
        return Config.veraVersion;
    }
    private static Object getOsInfo(){
        if(SystemUtil.getOs() == OsType.Linux)  return SystemUtil.getLinuxDistroName();
        return SystemUtil.getOsName(); // Linux인 경우에만 호출하는 것으로 추정되기에 대충해도 될 듯?
    }

    private static Object getResult(String sid){
        TaskDTO task = TaskListManager.getInstance().getTask(sid);
        if(task == null || task.getCallbackData() == null){
            return "";
        }
        return switch (task.getCallbackData()){
            case ResponseDTO result -> result.getData();
            case String str -> str;
            default -> throw new IllegalStateException("Unexpected value: " + task.getCallbackData());
        };
    }
}
