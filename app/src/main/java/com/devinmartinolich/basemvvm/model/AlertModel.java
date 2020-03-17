package com.devinmartinolich.basemvvm.model;

import androidx.annotation.Nullable;

import com.devinmartinolich.basemvvm.utils.AppLog;

public class AlertModel {
    private static final String TAG = "AlertModel";

    private String body;
    private String title;
    private String negativeBtn;

    public AlertModel(@Nullable String title, String body, @Nullable String negativeBtn) {
        AppLog.d(TAG, "AlertModel()");
        this.title = title;
        this.body = body;
        this.negativeBtn = negativeBtn;
    }

    public String getBody(){
        return body == null ? "" : body;
    }

    public void setBody(String body){
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNegativeBtn() {
        return negativeBtn;
    }

    public void setNegativeBtn(String negativeBtn) {
        this.negativeBtn = negativeBtn;
    }
}
