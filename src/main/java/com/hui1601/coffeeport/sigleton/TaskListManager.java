package com.hui1601.coffeeport.sigleton;

import com.hui1601.coffeeport.dto.TaskDTO;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class TaskListManager {
    private TaskListManager(){
    }
    private final ConcurrentHashMap<String, TaskDTO> tasks = new ConcurrentHashMap<>();
    public final TaskDTO getTask(String sid){
        return tasks.get(sid);
    }

    public final void setTask(String sid, TaskDTO task){
        tasks.put(sid, task);
    }

    public final String[] getTaskList(){
        return tasks.keySet().toArray(new String[0]);
    }
    private static class Lazy{
        private static final TaskListManager instance = new TaskListManager();
    }
    public static TaskListManager getInstance() {
        return Lazy.instance;
    }
}
