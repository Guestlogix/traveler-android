package com.guestlogix.travelercorekit.tasks;

import android.content.Context;

public class SharedPrefsReadTask extends SharedPrefsTask {

    private Error error = null;
    private String result = null;

    private class SharedPrefsReadError extends Error {
    }

    SharedPrefsReadTask(Context context, String key) {
        super(context,key);
    }

    String getResult() {
        return result;
    }

    @Override
    public void execute() {
        if (isEmpty(key)) {
            error = new SharedPrefsReadError();
            finish();
            return;
        }

        String value = sharedPreferences.getString(key, "");

        if (isEmpty(value)) {
            error = new SharedPrefsReadError(); // Add somethign here
            finish();
            return;
        }

        result = value;

        finish();
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }
}
