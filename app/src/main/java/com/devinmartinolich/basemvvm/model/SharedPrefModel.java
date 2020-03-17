package com.devinmartinolich.basemvvm.model;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.devinmartinolich.basemvvm.utils.AppLog;
import com.devinmartinolich.basemvvm.utils.Constants;

import java.util.Map;

public class SharedPrefModel extends LiveData {

    private static final String TAG = "SharedPrefModel";
    private SharedPreferences sharedPreferences;

    private Boolean loggedIn = null;
    private String authToken = null;
    private Boolean fingerprint = null;
    private Boolean rememberUsername = null;
    private String username = null;
    private String password = null;
    private String ivpass = null;

    /**
     * Name: SharedPrefModel
     * Created By: Devin Martinolich on 11/19/19
     * Purpose: This constructor builds the class variables and sets a Shared Preferences listener
     *          on all the values to automatically update this class if they change elsewhere.
     * @param sharedPreferences : The Shared Preference instance for the application
     */
    public SharedPrefModel(@NonNull SharedPreferences sharedPreferences) {
        AppLog.d(TAG, "SharedPrefModel()");
        this.sharedPreferences = sharedPreferences;

        populateFromFile();
    }

    @Override
    protected void onActive() {
        super.onActive();
        AppLog.d(TAG, "onActive()");
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(mSharedPreferenceListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        AppLog.d(TAG, "onInactive()");
        this.sharedPreferences.unregisterOnSharedPreferenceChangeListener(mSharedPreferenceListener);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener mSharedPreferenceListener = (sharedPreferences, key) -> {
        AppLog.d(TAG, "onSharedPreferenceChanged()");
        if (sharedPreferences != null) {
            switch (key) {
                case Constants.SharedPrefKeys.LOGGED_IN:
                    loggedIn = sharedPreferences.getBoolean(key, false);
                    break;
                case Constants.SharedPrefKeys.AUTH_TOKEN:
                    authToken = sharedPreferences.getString(key, "");
                    break;
            }
        }
    };

    /**
     * Name: populateFromFile
     * Created by: Devin Martinolich on 11/19/19
     * Modified by:
     * Purpose: This method is used to pre-populate the variables in this class based on the values
     *          stored in shared preferences.
     */
    private void populateFromFile() {
        AppLog.d(TAG, "populateFromFile()");

        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            switch (entry.getKey()) {
                case Constants.SharedPrefKeys.LOGGED_IN:
                    loggedIn = (Boolean) entry.getValue();
                    break;
                case Constants.SharedPrefKeys.AUTH_TOKEN:
                    authToken = (String) entry.getKey();
                    break;
            }
        }
    }

    public Boolean getLoggedIn() {
        return loggedIn;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Boolean getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(Boolean fingerprint) {
        this.fingerprint = fingerprint;
    }

    public Boolean getRememberUsername() {
        return rememberUsername;
    }

    public void setRememberUsername(Boolean rememberUsername) {
        this.rememberUsername = rememberUsername;
    }

    public String getUsername() {
        return username;
    }

    public String getIvpass() {
        return ivpass;
    }

    public String getPassword() {
        return password;
    }
}
