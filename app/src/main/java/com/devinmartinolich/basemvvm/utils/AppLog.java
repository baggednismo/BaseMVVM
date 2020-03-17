package com.devinmartinolich.basemvvm.utils;

import android.util.Log;

import com.devinmartinolich.basemvvm.BuildConfig;

/**
 * Name: AppLog
 * Created by Devin Martinolich on 1/24/18.
 *  * Modified by
 * Purpose: This class prints logs in Logcat it will ONLY print when build flavor is Debug
 *           This is useful when we want to see logger in its seperated colors while dev/testing
 *           but do NOT want anyone seeing details of the app when released OR consuming lots of
 *           space on client devices.
 */
public class AppLog
{
    /**
     * Flag represents whether to print debug log or not.
     */
    public static boolean sFlagDebug = BuildConfig.DEBUG;

    /**
     * Flag represents whether to print info log or not.
     */
    public static boolean sFlagInfo = BuildConfig.DEBUG;

    /**
     * Flag represents whether to print error log or not.
     */
    public static boolean sFlagErr = BuildConfig.DEBUG;

    /**
     * This method prints debug log.
     *
     * @param tag
     * @param message
     */
    public static void d(String tag, String message) {
        if (sFlagDebug)
            Log.d(tag, message);
    }

    /**
     * This method prints info log.
     *
     * @param tag
     * @param message
     */
    public static void i(String tag, String message) {
        if (sFlagInfo)
            Log.i(tag, message);
    }

    /**
     * This method prints error log.
     *
     * @param tag
     * @param message
     */
    public static void e(String tag, String message) {
        if (sFlagErr)
            Log.e(tag, message);
    }
}
