package com.guestlogix.travelercorekit.tasks;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class SharedPrefsWriteTask extends SharedPrefsTask {

    private String mData = null;

    public SharedPrefsWriteTask(Context mContext, String key) {
        super(mContext, key);
    }

    public void setData(String data) {
        this.mData = data;
    }

    @Override
    public void execute() {
        if (TextUtils.isEmpty(mKey)) {
            //throw empty key exception
            finish();
            return;
        }

        if (TextUtils.isEmpty(mData)) {
            //throw empty value exception
            finish();
            return;
        }

        mSharedPreferences.edit().putString(mKey, mData).commit();

        finish();
    }
}
