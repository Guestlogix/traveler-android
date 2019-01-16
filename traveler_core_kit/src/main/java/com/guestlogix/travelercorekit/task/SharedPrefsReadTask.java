package com.guestlogix.travelercorekit.task;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class SharedPrefsReadTask extends SharedPrefsTask {

    private Error mError = null;
    private String mResult = null;

    public class SharedPrefsReadError extends Error {

    }

    public SharedPrefsReadTask(Context context, String key) {
        super(context,key);
    }

    public String getResult() {
        return mResult;
    }

    @Override
    public void execute() {
        Log.v("Traveler", "SharedPrefsReadTask  execute()");

        if (TextUtils.isEmpty(mKey)) {
            mError = new SharedPrefsReadError();
            finish();
            return;
        }

        String value = mSharedPreferences.getString(mKey, "");

        if (TextUtils.isEmpty(value)) {
            mError = new SharedPrefsReadError(); // Add somethign here
            finish();
            return;
        }

        mResult = value;

        finish();
    }
}
