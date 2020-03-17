package com.devinmartinolich.basemvvm.utils;

import com.devinmartinolich.basemvvm.BaseMVVM;
import com.devinmartinolich.basemvvm.model.rest.RestApis;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientFactory {

    private static final String TAG = "RetrofitClientFactory";
    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;

    /**
     * Name: RetrofitClientFactory get
     * Created by: Devin Martinolich on 12/2/2019
     * Modified by:
     * Purpose: generate instance of restAPI
     *
     * @return retrofit instance
     */
    public static RestApis getClient(String mBaseUrl)
    {
        AppLog.d(TAG, "-> getClient()");
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(Constants.DefaultValues.DEFAULT_API_CONNECTION_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .writeTimeout(Constants.DefaultValues.DEFAULT_API_WRITE_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .readTimeout(Constants.DefaultValues.DEFAULT_API_READ_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .build();

//        Strategy strategy = new AnnotationStrategy();
//        Serializer serializer = new Persister(strategy);
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl(mBaseUrl)
//                .addConverterFactory(SimpleXmlConverterFactory.create(serializer))
//                .client(okHttpClient)
//                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create(BaseMVVM.getGson()))
                .client(okHttpClient)
                .build();

        return retrofit.create(RestApis.class);
    }

    public static Retrofit getRetrofit(){
        return retrofit;
    }
}
