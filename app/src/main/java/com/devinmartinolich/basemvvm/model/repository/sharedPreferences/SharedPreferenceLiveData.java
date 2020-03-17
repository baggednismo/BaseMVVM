package com.devinmartinolich.basemvvm.model.repository.sharedPreferences;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

import com.devinmartinolich.basemvvm.utils.AppLog;

public abstract class SharedPreferenceLiveData<T> extends LiveData<T> {
    private static final String TAG = "SharedPreferenceLiveData";
    SharedPreferences sharedPrefs;
    String key;
    private T defValue;

    SharedPreferenceLiveData(SharedPreferences prefs, String key, T defValue) {
        AppLog.d(TAG, "SharedPreferenceLiveData()");
        this.sharedPrefs = prefs;
        this.key = key;
        this.defValue = defValue;
    }

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            AppLog.d(TAG, "onSharedPreferenceChanged()");
            if (SharedPreferenceLiveData.this.key.equals(key)) {
                setValue(getValueFromPreferences(key, defValue));
            }
        }
    };
    abstract T getValueFromPreferences(String key, T defValue);

    @Override
    protected void onActive() {
        AppLog.d(TAG, "onSharedPreferenceChanged()");
        super.onActive();
        setValue(getValueFromPreferences(key, defValue));
        sharedPrefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    protected void onInactive() {
        AppLog.d(TAG, "onSharedPreferenceChanged()");
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
        super.onInactive();
    }

    public SharedPreferenceLiveData<Boolean> getBooleanLiveData(String key, Boolean defaultValue) {
        return new SharedPreferenceBooleanLiveData(sharedPrefs, key, defaultValue);
    }

    public SharedPreferenceLiveData<String> getStringLiveData(String key, String defaultValue) {
        return new SharedPreferenceStringLiveData(sharedPrefs, key, defaultValue);
    }
}

