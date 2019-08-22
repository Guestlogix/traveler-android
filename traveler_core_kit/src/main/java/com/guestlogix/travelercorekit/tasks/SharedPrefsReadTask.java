package com.guestlogix.travelercorekit.tasks;

import android.content.Context;
import android.text.TextUtils;

public class SharedPrefsReadTask extends SharedPrefsTask {

    private Error error = null;
    private String result = null;

    private class SharedPrefsReadError extends Error {
    }

    SharedPrefsReadTask(Context context, String key) {
        super(context,key);
    }

    Error getError() {
        return error;
    }

    String getResult() {
        return result;
    }

    @Override
    public void execute() {
        if (TextUtils.isEmpty(key)) {
            error = new SharedPrefsReadError();
            finish();
            return;
        }

        String value = sharedPreferences.getString(key, "");

        if (TextUtils.isEmpty(value)) {
            error = new SharedPrefsReadError(); // Add somethign here
            finish();
            return;
        }

        result = value;

        finish();
    }
}
