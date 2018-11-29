package com.guestlogix.task;

import android.util.Log;

import java.util.concurrent.Semaphore;

class TaskWrapper implements Runnable, TaskObserver {
    private Task mTask;
    private Semaphore mSemaphore;

    TaskWrapper(Task task) {
        mTask = task;
        mSemaphore = new Semaphore(0);
    }

    @Override
    public void run() {
        mTask.addObserver(this);

        mTask.start();

        try {
            mSemaphore.acquire();
        } catch (InterruptedException e) {
            Log.e("TaskWrapper", "Could not block thread");
        }

        mTask.removeObserver(this);
    }

    @Override
    public void onStateChanged(Task task) {
        switch (task.getState()) {
            case RUNNING:
                break;
            case FINISHED:
                mSemaphore.release();
            case READY:
                break;
        }
    }
}
