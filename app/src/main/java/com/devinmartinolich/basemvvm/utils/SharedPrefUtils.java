package com.devinmartinolich.basemvvm.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.devinmartinolich.basemvvm.BaseMVVM;

import java.util.Map;

/**
 * Name: SharedPrefUtils
 * Created by Devin Martinolich on 1/24/18.
 * Modified by Devin Martinolich on 11/19/2019
 * Purpose: This class will contain all the methods related to shared preferences.
 */
public class SharedPrefUtils
{
    private static SharedPreferences mSharedPreferences;
    private static String mPrefName;

    public static SharedPreferences getSharedPreferences(Context aContext, String aSharedPrefName) {
        if (mSharedPreferences == null || !mPrefName.equals(aSharedPrefName)) {
            mPrefName = aSharedPrefName;
            mSharedPreferences = aContext.getSharedPreferences(aSharedPrefName, Context.MODE_PRIVATE);
        }
        return mSharedPreferences;
    }

    public static boolean getBool(Context aContext, String aSharedPrefName, String aKey, boolean aDefaultValue) {
        return getSharedPreferences(aContext, aSharedPrefName).getBoolean(aKey, aDefaultValue);
    }

    public static void setBool(Context aContext, String aSharedPrefName, String aKey, boolean aValue) {
        getSharedPreferences(aContext, aSharedPrefName).edit().putBoolean(aKey, aValue).apply();
    }

    public static String getString(Context aContext, String aSharedPrefName, String aKey, String aDefaultValue) {
        return getSharedPreferences(aContext, aSharedPrefName).getString(aKey, aDefaultValue);
    }

    public static void setString(Context aContext, String aSharedPrefName, String aKey, String aValue) {
        getSharedPreferences(aContext, aSharedPrefName).edit().putString(aKey, aValue).apply();
    }

    public static long getLong(Context aContext, String aSharedPrefName, String aKey, long aDefaultValue) {
        return getSharedPreferences(aContext, aSharedPrefName).getLong(aKey, aDefaultValue);
    }

    public static void setLong(Context aContext, String aSharedPrefName, String aKey, long aValue) {
        getSharedPreferences(aContext, aSharedPrefName).edit().putLong(aKey, aValue).apply();
    }

    public static int getInt(Context aContext, String aSharedPrefName, String aKey, int aDefaultValue) {
        return getSharedPreferences(aContext, aSharedPrefName).getInt(aKey, aDefaultValue);
    }

    public static void setInt(Context aContext, String aSharedPrefName, String aKey, int aValue) {
        getSharedPreferences(aContext, aSharedPrefName).edit().putInt(aKey, aValue).apply();
    }

    public static Boolean contains(Context aContext, String aSharedPrefName, String aKey) {
        return getSharedPreferences(aContext, aSharedPrefName).contains(aKey);
    }

    public static void remove(Context aContext, String aSharedPrefName, String aKey) {
        getSharedPreferences(aContext, aSharedPrefName).edit().remove(aKey).apply();
    }

    public static Map<String, ?> getAll(Context aContext, String aSharedPrefName) {
        return getSharedPreferences(aContext, aSharedPrefName).getAll();
    }

    /**
     *  Name: migrateSharedPreferences
     *  Create by Devin Martinolich 3/17/2020
     *  Modified by
     *  Purpose: Move SharedPreferences key/value pair to another SharedPreferences file.
     *
     * @param context - Application context
     * @param destSharedPrefName - SharedPreference name of new file
     * @param sourceSharedPrefName - SharedPreference name of old file
     * @param removeSource - Boolean of what to do with source records
     */
    public static Boolean migrateSharedPreferences(Context context, String destSharedPrefName, String sourceSharedPrefName, Boolean removeSource) {
        Map<String, ?> allEntries = getAll(context, sourceSharedPrefName);
        if (allEntries != null) {
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                setString(context, destSharedPrefName, entry.getKey(), entry.getValue().toString());
                if (removeSource) {
                    remove(context, sourceSharedPrefName, entry.getKey());
                }
            }
            return true;
        }
        return false;
    }
}