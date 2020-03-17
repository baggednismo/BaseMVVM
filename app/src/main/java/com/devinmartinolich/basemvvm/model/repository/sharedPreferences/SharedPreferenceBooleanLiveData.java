package com.devinmartinolich.basemvvm.model.repository.sharedPreferences;

import android.content.SharedPreferences;

import com.devinmartinolich.basemvvm.utils.AppLog;

public class SharedPreferenceBooleanLiveData extends SharedPreferenceLiveData<Boolean> {
    private static final String TAG = "SharedPreferenceBooleanLiveData";

    public SharedPreferenceBooleanLiveData(SharedPreferences prefs, String key, Boolean defValue) {
        super(prefs, key, defValue);
        AppLog.d(TAG, "SharedPreferenceBooleanLiveData()");
    }

    @Override
    Boolean getValueFromPreferences(String key, Boolean defValue) {
        AppLog.d(TAG, "getValueFromPreferences()");
        return sharedPrefs.getBoolean(key, defValue);
    }
}
