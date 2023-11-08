package com.barrybarry.coffeeport.handler;

import com.barrybarry.coffeeport.data.ResponseData;
import com.barrybarry.coffeeport.data.task.TaskData;
import com.barrybarry.coffeeport.sigleton.TaskListManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TaskHandler extends Thread{
    private static final Logger logger = LoggerFactory.getLogger(TaskHandler.class);
    private boolean isRunning = true;
    @Override
    public void run() {
        logger.debug("TaskHandler started!");
        while (isRunning) {
            String[] tasks = TaskListManager.getInstance().getTaskList();
            for (String sid : tasks) {
                TaskData taskData = TaskListManager.getInstance().getTask(sid);
                if (taskData == null || taskData.getCallbackData() != null) {
                    continue;
                }
                logger.debug("Handling sid: " + sid);
                if(taskData.getData().getCmd() != null) {
                    logger.debug("task cmd: {}", taskData.getData().getCmd());
                }
                ResponseData res = new ResponseData();
                CommandHandler.handleCommand(res, taskData);
                taskData.setCallbackData(res);
                TaskListManager.getInstance().setTask(sid, taskData);
            }
        }
    }

    @Override
    public void interrupt() {
        logger.info("TaskHandler interrupted!");
        isRunning = false;
        super.interrupt();
    }
}
