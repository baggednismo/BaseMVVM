package com.devinmartinolich.basemvvm.view.interfaces;

/**
 * Name: FirebaseAnalyticsListener
 * Created by Devin Martinolich 1/30/2020
 * Modified by
 * Purpose: Provide a contract for callback
 */
public interface FirebaseAnalyticsListener {
    public void logAnalytics(String type, String item, String event);
}
