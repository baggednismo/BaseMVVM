package com.devinmartinolich.basemvvm.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Name: GsonFactory
 * Created by Devin Martinolich on 12/3/2019
 * Modified by:
 * Purpose: To get Gson instance for application. It is a basic Gson factory class which
 *          can provide Gson with ignore or with expose field policy.
 *          To get Gson object use Builder class of GsonFactory.
 */
public class GsonFactory {

    public static class Builder {
        GsonBuilder mGsonBuilder;

        public Builder()
        {
            mGsonBuilder = new GsonBuilder();
        }

        public Gson buildByExcludeFieldsWithoutExpose() {
            mGsonBuilder.excludeFieldsWithoutExposeAnnotation();

            // Adding setLenient() to allow non-json/xml responses (like auth response string)
            return mGsonBuilder.setLenient().create();
        }

        public Gson build()
        {
            return mGsonBuilder.create();
        }
    }
}
