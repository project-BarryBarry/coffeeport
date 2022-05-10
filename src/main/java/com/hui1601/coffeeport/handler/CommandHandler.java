package com.hui1601.coffeeport.handler;

import com.hui1601.coffeeport.constant.Config;
import com.hui1601.coffeeport.dto.Data;
import com.hui1601.coffeeport.dto.ResponseDTO;
import com.hui1601.coffeeport.dto.TaskDTO;

public class CommandHandler {
    public static void handleCommand(ResponseDTO res, TaskDTO task){
        res.setRes(0);
        res.setData(switch (task.getData().getCmd()){
            case VERSION -> getVersion();
            case AX_INFO -> getInstallationInfo(task.getData().getData());
        });
    }
    private static Object getVersion(){
        return Config.veraVersion;
    }
    private static Object getInstallationInfo(Data data){
        return Config.veraVersion;
    }
}
