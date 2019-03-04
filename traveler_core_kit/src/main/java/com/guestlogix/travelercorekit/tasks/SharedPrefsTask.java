package com.guestlogix.travelercorekit.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import com.guestlogix.travelercorekit.utilities.Task;

import static android.content.Context.MODE_PRIVATE;

public abstract class SharedPrefsTask extends Task {

    private String MY_PREFS_NAME = "TRAVELER";
    private Context context;
    protected String key;
    SharedPreferences sharedPreferences;

    SharedPrefsTask(Context context, String key) {
        this.context = context;
        this.key = key;
        this.sharedPreferences = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
