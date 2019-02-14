package com.guestlogix.travelercorekit.tasks;

import com.guestlogix.travelercorekit.utilities.TravelerLog;

import java.util.concurrent.Semaphore;

class TaskWrapper implements Runnable, TaskObserver {
    private Task mTask;
    private Semaphore mSemephore;

    TaskWrapper(Task task) {
        mTask = task;
        mSemephore = new Semaphore(0);
    }

    @Override
    public void run() {
        mTask.addObserver(this);

        mTask.start();

        try {
            mSemephore.acquire();
        } catch (InterruptedException e) {
            TravelerLog.e("Could not block thread");
        }

        mTask.removeObserver(this);
    }

    @Override
    public void onStateChanged(Task task) {
        switch (task.getState()) {
            case RUNNING:
                break;
            case FINISHED:
                mSemephore.release();
            case READY:
                break;
        }
    }
}