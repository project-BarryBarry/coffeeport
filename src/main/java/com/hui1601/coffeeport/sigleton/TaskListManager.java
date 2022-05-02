package com.hui1601.coffeeport.sigleton;

public class TaskListManager {
    private TaskListManager(){
    }
    private static class Lazy{
        private static final TaskListManager instance = new TaskListManager();
    }
    public static TaskListManager getInstance() {
        return Lazy.instance;
    }
}
