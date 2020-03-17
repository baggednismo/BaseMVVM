package com.devinmartinolich.basemvvm.model.repository.sharedPreferences;

import android.content.SharedPreferences;

import com.devinmartinolich.basemvvm.utils.AppLog;

public class SharedPreferenceStringLiveData extends SharedPreferenceLiveData<String> {
    private static final String TAG = "SharedPreferenceStringLiveData";

    public SharedPreferenceStringLiveData(SharedPreferences prefs, String key, String defValue) {
        super(prefs, key, defValue);
        AppLog.d(TAG, "SharedPreferenceStringLiveData()");
    }

    @Override
    String getValueFromPreferences(String key, String defValue) {
        AppLog.d(TAG, "getValueFromPreferences()");
        return sharedPrefs.getString(key, defValue);
    }
}
