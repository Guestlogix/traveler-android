package com.guestlogix.travelercorekit.utilities;

import android.os.Handler;
import android.os.Looper;
import com.guestlogix.travelercorekit.utilities.Task;
import com.guestlogix.travelercorekit.utilities.TaskManager;

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
        mainExecutor.execute(task::execute);
    }

    static private class MainThreadExecutor implements Executor {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    }
}
