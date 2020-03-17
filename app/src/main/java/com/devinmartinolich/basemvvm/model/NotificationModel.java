package com.devinmartinolich.basemvvm.model;

import androidx.annotation.Nullable;

import com.devinmartinolich.basemvvm.utils.AppLog;

public class NotificationModel {
    private static final String TAG = "NotificationModel";

    private String notification;
    private Boolean loginSuccess;
    private String docId;

    public NotificationModel(String notification, Boolean loginSuccess,@Nullable String docId) {
        AppLog.d(TAG, "NotificationModel()");
        this.notification = notification;
        this.loginSuccess = loginSuccess;
        this.docId = docId;
    }

    public String getNotification(){
        return notification == null ? "" : notification;
    }

    public void setNotification(String notification) {
        AppLog.d(TAG, "Set notification to: \"" + notification + "\"");
        this.notification = notification;
    }

    public Boolean getLoginSuccess() {
        return loginSuccess == null ? false : loginSuccess;
    }

    public void setLoginSuccess(Boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
