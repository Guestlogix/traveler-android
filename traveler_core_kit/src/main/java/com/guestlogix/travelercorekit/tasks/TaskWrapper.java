package com.guestlogix.travelercorekit.tasks;

import com.guestlogix.travelercorekit.utilities.TravelerLog;

import java.util.concurrent.Semaphore;

class TaskWrapper implements Runnable, TaskObserver {
    private Task task;
    private Semaphore semephore;

    TaskWrapper(Task task) {
        this.task = task;
        semephore = new Semaphore(0);
    }

    @Override
    public void run() {
        task.addObserver(this);

        task.start();

        try {
            semephore.acquire();
        } catch (InterruptedException e) {
            TravelerLog.e("Could not block thread");
        }

        task.removeObserver(this);
    }

    @Override
    public void onStateChanged(Task task) {
        switch (task.getState()) {
            case RUNNING:
                break;
            case FINISHED:
                semephore.release();
            case READY:
                break;
        }
    }
}
