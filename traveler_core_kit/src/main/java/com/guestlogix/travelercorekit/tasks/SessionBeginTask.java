package com.guestlogix.travelercorekit.tasks;

import com.guestlogix.travelercorekit.models.Session;
import com.guestlogix.travelercorekit.models.Token;
import com.guestlogix.travelercorekit.utilities.Task;
import com.guestlogix.travelercorekit.utilities.TaskManager;

public class SessionBeginTask extends Task {

    private TaskManager taskManager = new TaskManager();
    private Session session;

    public SessionBeginTask(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    @Override
    public void execute() {
        //read encrypted data from shared prefs
        SharedPrefsReadTask sharedPrefsReadTask = new SharedPrefsReadTask(session.getContext(), session.getApiKey());

        BlockTask sharedPrefsReadBlockTask = new BlockTask() {
            @Override
            protected void main() {
                if (null != sharedPrefsReadTask.getError()) {
                    sharedPrefsReadTask.getError().printStackTrace();
                    return;
                }
                session.setToken(new Token(sharedPrefsReadTask.getResult()));
                //session.getAuthToken().setValue(sharedPrefsReadTask.getResult());
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
