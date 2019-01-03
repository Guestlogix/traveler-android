package com.guestlogix.task;

import android.util.Log;
import com.guestlogix.travelercorekit.models.Session;

public class SessionBeginTask extends Task {

    private TaskManager mTaskManager = new TaskManager();
    private Session mSession;

    public SessionBeginTask(Session session) {
        this.mSession = session;
    }

    public Session getSession() {
        return mSession;
    }

    @Override
    public void execute() {
        Log.v("Traveler", "SessionBeginTask execute()");

        //read encrypted data from shared prefs
        SharedPrefsReadTask sharedPrefsReadTask = new SharedPrefsReadTask(mSession.getContext(), mSession.getApiKey());

        BlockTask sharedPrefsReadBlockTask = new BlockTask() {
            @Override
            protected void main() {
                mSession.getAuthToken().setValue(sharedPrefsReadTask.getResult());
            }
        };

        BlockTask finishTask = new BlockTask() {
            @Override
            protected void main() {
                SessionBeginTask.this.finish();
            }
        };

        // if session token is empty
        // use read task and update session
        // else
        // finish

        sharedPrefsReadBlockTask.addDependency(sharedPrefsReadTask);
        finishTask.addDependency(sharedPrefsReadBlockTask);

        mTaskManager.addTask(sharedPrefsReadTask);
        mTaskManager.addTask(sharedPrefsReadBlockTask);
        mTaskManager.addTask(finishTask);

    }
}
