package com.guestlogix.task;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class TaskManager implements TaskObserver {
    private ArrayList<Task> mTasks;
    private ExecutorService mExecutor;

    public TaskManager() {
        mTasks = new ArrayList<>();
        mExecutor = Executors.newCachedThreadPool();
    }

    public void addTask(Task task) {
        task.addObserver(this);
        mTasks.add(task);
        mExecutor.execute(new TaskWrapper(task));
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
                mTasks.remove(task);
        }
    }
}
