package com.devinmartinolich.basemvvm.model.rest;

import com.devinmartinolich.basemvvm.BaseMVVM;
import com.devinmartinolich.basemvvm.model.NotificationModel;
import com.devinmartinolich.basemvvm.utils.AppLog;
import com.devinmartinolich.basemvvm.utils.Constants;
import com.devinmartinolich.basemvvm.utils.FabricUtils;
import com.devinmartinolich.basemvvm.utils.RetrofitClientFactory;
import com.devinmartinolich.basemvvm.utils.SharedPrefUtils;
import com.devinmartinolich.basemvvm.utils.SingleLiveEvent;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;

public class RestApiImpl {
    private String TAG = "RestApiImpl";
    private String mBaseUrl;
    private RestApis mRestApis;
    private SingleLiveEvent<NotificationModel> notificationModelMutableLiveData;

    public RestApiImpl(SingleLiveEvent<NotificationModel> notificationModelMutableLiveData, String mBaseUrl) {
        AppLog.d(TAG, "-> RestApiImpl()");
        this.mBaseUrl = mBaseUrl;
        mRestApis = RetrofitClientFactory.getClient(mBaseUrl);
        this.notificationModelMutableLiveData = notificationModelMutableLiveData;
    }

    /**
     * Name: RestApiImpl doAuthSync
     * Created by: Devin Martinolich on 12/2/2019
     * Modified by:
     * Purpose: To call authentication api. Thread is defined on implementation.
     *          This method will use {@link Call#execute()} to call api
     * @param user username
     * @param pass password
     */
    public String doAuthSync(@Field("user") String user,
                             @Field("pass") String pass) {
        AppLog.d(TAG, "-> doAuthSync()");
        Call<String> authRequest = mRestApis.doAuthSynchronously(user, pass);

        String authResponse = null;

        try {
            Response<String> response = authRequest.execute();
            authResponse = response.body();

            if (response.isSuccessful() && response.body() != null) {
                if (authResponse.equals("true")) {
                    setLoginPref(true);
                } else {
                    setLoginPref(false);
                }
            }
            else if (!response.isSuccessful()) {
                try {
                    authResponse = response.errorBody().string();
                } catch (IOException e) {
                    AppLog.e(TAG, e.getMessage());
                    FabricUtils.fabricExceptionWithMessage(e, e.getMessage());
                }
                setLoginPref(false);
            }
        } catch (Throwable aE) {
            aE.printStackTrace();
            AppLog.e(TAG, "Authentication Failed: " + aE.getMessage());
            FabricUtils.fabricExceptionWithMessage(aE, "Authentication Failed");
            setLoginPref(false);
        }

        return authResponse;
    }

    private void setLoginPref(Boolean bool) {
        SharedPrefUtils.setBool(BaseMVVM.getAppInstance().getApplicationContext(),
                Constants.SharedPrefName.APP_PREFS,
                Constants.SharedPrefKeys.LOGGED_IN,
                bool);
    }
}
