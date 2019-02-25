package com.guestlogix.travelercorekit.tasks;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

class MainTaskManager extends TaskManager implements Task.Performer {

    private Executor mainExecutor;

    MainTaskManager() {
        super();
        mainExecutor = new MainThreadExecutor();
    }

    @Override
    public void addTask(Task task) {
        task.setPerformer(this);
        super.addTask(task);
    }

    @Override
    public void onPerform(final Task task) {
        mainExecutor.execute(new Runnable() {
            @Override
            public void run() {
                task.execute();
            }
        });
    }

    static private class MainThreadExecutor implements Executor {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    }
}
