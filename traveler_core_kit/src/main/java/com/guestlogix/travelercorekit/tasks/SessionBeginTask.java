package com.guestlogix.travelercorekit.tasks;

import android.content.Context;
import com.guestlogix.travelercorekit.models.Session;
import com.guestlogix.travelercorekit.models.Token;
import com.guestlogix.travelercorekit.utilities.Task;
import com.guestlogix.travelercorekit.utilities.TaskManager;

public class SessionBeginTask extends Task {

    private TaskManager taskManager = new TaskManager();
    private Session session;
    private Context context;
    private SharedPrefsReadTask sharedPrefsReadTask;

    public SessionBeginTask(Session session, Context context) {
        this.session = session;
        this.context = context;
    }

    public Session getSession() {
        return session;
    }

    @Override
    public void execute() {

        // Read encrypted data from shared prefs
        sharedPrefsReadTask = new SharedPrefsReadTask(context, session.getApiKey());

        BlockTask sharedPrefsReadBlockTask = new BlockTask() {
            @Override
            protected void main() {
                session.setToken(new Token(sharedPrefsReadTask.getResult()));
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

        taskManager.addTask(sharedPrefsReadTask);
        taskManager.addTask(sharedPrefsReadBlockTask);
        taskManager.addTask(finishTask);
    }
}
