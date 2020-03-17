package com.devinmartinolich.basemvvm.model.rest;

import com.google.gson.annotations.SerializedName;

public class RestError {
    @SerializedName("code")
    public int code;
    @SerializedName("error")
    public String errorDetails;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }
}
