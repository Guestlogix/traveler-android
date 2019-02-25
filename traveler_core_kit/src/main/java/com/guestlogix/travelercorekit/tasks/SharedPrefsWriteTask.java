package com.guestlogix.travelercorekit.tasks;

import android.content.Context;
import android.text.TextUtils;

public class SharedPrefsWriteTask extends SharedPrefsTask {

    private String data = null;

    public SharedPrefsWriteTask(Context mContext, String key) {
        super(mContext, key);
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public void execute() {
        if (TextUtils.isEmpty(key)) {
            //throw empty key exception
            finish();
            return;
        }

        if (TextUtils.isEmpty(data)) {
            //throw empty value exception
            finish();
            return;
        }

        sharedPreferences.edit().putString(key, data).commit();

        finish();
    }
}
