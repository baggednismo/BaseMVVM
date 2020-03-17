package com.devinmartinolich.basemvvm.model;

/**
 * Name: DialogModel
 * Created by Devin Martinolich 1/2/2020
 * Modified by
 * Purpose: POJO model for dialogs
 */
public class DialogModel {
    private int id;
    private String tag;
    private Boolean cancelable;

    public DialogModel(int id, String tag, Boolean cancelable) {
        this.id = id;
        this.tag = tag;
        this.cancelable = cancelable;
    }

    public int getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public Boolean getCancelable() {
        return cancelable;
    }
}
