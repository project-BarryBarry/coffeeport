package com.barrybarry.coffeeport.sigleton;

import com.barrybarry.coffeeport.data.task.TaskData;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class TaskListManager {
    private TaskListManager(){
    }
    private final ConcurrentHashMap<String, TaskData> tasks = new ConcurrentHashMap<>();

    private volatile boolean isInstallationRunning = false;

    private List<String> uiUpdate = Collections.synchronizedList(new ArrayList<>());
    public final TaskData getTask(String sid){
        return tasks.get(sid);
    }

    public final void setTask(String sid, TaskData task){
        tasks.put(sid, task);
    }

    public final void removeTask(String sid){
        tasks.remove(sid);
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
