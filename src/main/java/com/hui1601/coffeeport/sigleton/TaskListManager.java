package com.hui1601.coffeeport.sigleton;

import com.hui1601.coffeeport.dto.TaskDTO;

import java.util.HashMap;
import java.util.Queue;

public class TaskListManager {
    private TaskListManager(){
    }
    private final HashMap<String, TaskDTO> tasks = new HashMap<>();
    private static class Lazy{
        private static final TaskListManager instance = new TaskListManager();
    }
    public static TaskListManager getInstance() {
        return Lazy.instance;
    }
}
