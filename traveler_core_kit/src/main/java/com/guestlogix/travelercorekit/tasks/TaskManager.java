package com.guestlogix.travelercorekit.tasks;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskManager implements TaskObserver {
    private ArrayList<Task> tasks;
    private ExecutorService executor;

    private static TaskManager mainTaskManager = new MainTaskManager();
    public static TaskManager getMainTaskManager() {
        return  mainTaskManager;
    }

    public TaskManager() {
        tasks = new ArrayList<Task>();
        executor = Executors.newCachedThreadPool();
    }

    public void addTask(Task task) {
        task.addObserver(this);
        tasks.add(task);
        executor.execute(new TaskWrapper(task));
    }

    // TASK OBSERVER

    @Override
    public void onStateChanged(Task task) {
        switch (task.getState()) {
            case READY:
                break;
            case RUNNING:
                break;
            case FINISHED:
                task.removeObserver(this);
                tasks.remove(task);
        }
    }
}
