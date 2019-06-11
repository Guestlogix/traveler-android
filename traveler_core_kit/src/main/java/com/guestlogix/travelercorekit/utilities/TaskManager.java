package com.guestlogix.travelercorekit.utilities;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskManager implements TaskObserver {
    public enum Mode {
        SERIAL, CONCURRENT
    }

    private ArrayList<Task> tasks;
    private ExecutorService executor;
    private Mode mode;

    private static TaskManager mainTaskManager = new MainTaskManager();
    public static TaskManager getMainTaskManager() {
        return mainTaskManager;
    }

    public TaskManager() {
        this(Mode.CONCURRENT);
    }

    public TaskManager(Mode mode) {
        tasks = new ArrayList<>();

        switch (mode) {
            case SERIAL:
                executor = Executors.newSingleThreadExecutor();
            case CONCURRENT:
                executor = Executors.newCachedThreadPool();
        }
    }

    public void addTask(Task task) {
        task.addObserver(this);
        tasks.add(task);
        executor.execute(new TaskWrapper(task));
    }

    public void cancelAllTasks() {
        for (Task task : tasks) {
            task.cancel();
        }
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
