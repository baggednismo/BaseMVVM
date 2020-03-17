package com.devinmartinolich.basemvvm.model.rest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Name: RestApis
 * Created by: Devin Martinolich on 12/2/2019
 * Modified by:
 * Purpose: This interface is to refine out type safe API endpoints
 */
public interface RestApis {

    @FormUrlEncoded
    @POST("VerifyCredentialsWebservice/")
    Call<String> doAuthSynchronously(@Field("user") String user,
                                     @Field("pass") String pass);
}
