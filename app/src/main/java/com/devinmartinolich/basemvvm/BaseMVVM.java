package com.devinmartinolich.basemvvm;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.crashlytics.android.Crashlytics;
import com.devinmartinolich.basemvvm.utils.AppLog;
import com.devinmartinolich.basemvvm.utils.GsonFactory;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

public class BaseMVVM extends Application {
    private static final String TAG = "BaseMVVM";

    private static BaseMVVM mAppInstance;
    private static FirebaseAnalytics mFirebaseAnalytics;
    private Crashlytics crashlytics;
    private static Gson mGson;
    private static String username;
    private static String password;

    @Override
    public void onCreate()
    {
        super.onCreate();
        AppLog.d(TAG, "onCreate() called");

        /**
         * To resolve drawable not found issue
         * https://stackoverflow.com/questions/37615470/support-library-vectordrawable-resourcesnotfoundexception
         */
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        mAppInstance = this;
    }

    /**
     * Name: BaseMVVM getAppInstance
     * Created by Devin Martinolich on 1/24/18.
     * Modified by
     * Purpose: To get app instance for presenter.
     *
     * @return : app instance
     */
    public static BaseMVVM getAppInstance() {
        return mAppInstance;
    }

    /**
     * Name: BaseMVVM getCrashlytics
     * Created by Devin Martinolich on 1/24/18.
     * Modified by
     * Purpose: To get crashlytics instance for presenter.
     *
     * @return : crashlytics instance
     */
    public Crashlytics getCrashlytics()
    {
        return crashlytics;
    }

    /**
     * Name: BaseMVVM getFirebaseAnalytics
     * Created by Devin Martinolich on 1/20/2020
     * Modified by
     * Purpose: To get Firebase Analytics for presenter.
     *
     * @return : firebase analytics instance
     */
    public static FirebaseAnalytics getFirebaseAnalytics() {
        return mFirebaseAnalytics;
    }

    /**
     * Name: BaseMVVM setFirebaseAnalytics
     * Created by Devin Martinolich on 1/20/2020
     * Modified by
     * Purpose: To set Firebase Analytics instance
     */
    public static void setFirebaseAnalytics(FirebaseAnalytics mFirebaseAnalytics) {
        BaseMVVM.mFirebaseAnalytics = mFirebaseAnalytics;
    }

    /**
     * Name: getGson
     * Created by Devin Martinolich on 12/3/2019
     * Modified by:
     * Purpose: To get singleton instance of {@link Gson} with exclude field without expose.
     *          It will generate {@link Gson} instance only if existing instance is null
     *
     * @return OkHttpClient instance
     */
    public static Gson getGson() {
        if (mGson == null) {
            GsonFactory.Builder builder = new GsonFactory.Builder();
            mGson = builder.buildByExcludeFieldsWithoutExpose();
        }
        return mGson;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        BaseMVVM.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        BaseMVVM.password = password;
    }
}
