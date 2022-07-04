package com.hui1601.coffeeport.handler;

import com.hui1601.coffeeport.dto.ResponseDTO;
import com.hui1601.coffeeport.dto.TaskDTO;
import com.hui1601.coffeeport.dto.TaskData;
import com.hui1601.coffeeport.sigleton.TaskListManager;

import java.util.logging.Logger;

public class TaskHandler extends Thread{
    @Override
    public void run() {
        Logger.getLogger(this.getName()).info("TaskHandler started!");
        //noinspection InfiniteLoopStatement
        while (true) {
            String[] tasks = TaskListManager.getInstance().getTaskList();
            for (String sid : tasks) {
                TaskDTO taskDTO = TaskListManager.getInstance().getTask(sid);
                if (taskDTO.getCallbackData() != null) {
                    continue;
                }
                Logger.getLogger(this.getName()).info("Handling sid: " + sid);
                if(taskDTO.getData().getCmd() != null)
                    Logger.getLogger(this.getName()).info("task cmd: " + taskDTO.getData().getCmd().toString());
                ResponseDTO res = new ResponseDTO();
                CommandHandler.handleCommand(res, taskDTO);
                taskDTO.setCallbackData(res);
                TaskListManager.getInstance().setTask(sid, taskDTO);
                Logger.getLogger(this.getName()).info((String) ((ResponseDTO) TaskListManager.getInstance().getTask(sid).getCallbackData()).getData());
            }
        }
    }
}
