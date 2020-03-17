package com.devinmartinolich.basemvvm.viewmodel;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.devinmartinolich.basemvvm.BaseMVVM;
import com.devinmartinolich.basemvvm.R;
import com.devinmartinolich.basemvvm.model.AlertModel;
import com.devinmartinolich.basemvvm.model.NavigationModel;
import com.devinmartinolich.basemvvm.model.NotificationModel;
import com.devinmartinolich.basemvvm.utils.AppLog;
import com.devinmartinolich.basemvvm.utils.Constants;
import com.devinmartinolich.basemvvm.utils.FabricUtils;
import com.devinmartinolich.basemvvm.utils.SharedPrefUtils;
import com.devinmartinolich.basemvvm.utils.SingleLiveEvent;
import com.devinmartinolich.basemvvm.utils.crypto.DeCryptor;
import com.devinmartinolich.basemvvm.utils.crypto.EnCryptor;
import com.devinmartinolich.basemvvm.view.interfaces.FirebaseAnalyticsListener;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class BaseViewModel extends ViewModel {

    private static final String TAG = "BaseViewModel";

    public SingleLiveEvent<NotificationModel> notificationModelMutableLiveData = new SingleLiveEvent<>();
    public SingleLiveEvent<AlertModel> alertModelSingleLiveEvent = new SingleLiveEvent<>();
    protected SingleLiveEvent<Integer> navLocation = new SingleLiveEvent<>();
    protected FirebaseAnalyticsListener mFirebaseAnalyticsListener;

    /**
     * Name: getNotificationModel
     * Created by Devin Martinolich 12/4/2019
     * Modified by
     * Purpose: Getter used by an Activity or Fragment to observe LiveData
     *
     * @return  NotificationModel LiveData
     */
    public SingleLiveEvent<NotificationModel> getNotificationModel() {
        AppLog.d(TAG, "-> getNotificationModel()");
        return notificationModelMutableLiveData;
    }

    /**
     * Name: setNotificationModel
     * Created by Devin Martinolich 12/30/2019
     * Modified by
     * Purpose: Insert the model into the LiveData object
     */
    public void setNotificationModel(NotificationModel notificationModel) {
        AppLog.d(TAG, "-> setNotificationModel()");
        this.notificationModelMutableLiveData.setValue(notificationModel);
    }

    /**
     * Name: getAlertModel
     * Created by Devin Martinolich 12/30/2019
     * Modified by
     * Purpose: Getter used by an Activity or Fragment to observe LiveData
     *
     * @return  AlertModel LiveData
     */
    public SingleLiveEvent<AlertModel> getAlertModel() {
        AppLog.d(TAG, "-> getAlertModel()");
        return alertModelSingleLiveEvent;
    }

    /**
     * Name: setAlertModel
     * Created by Devin Martinolich 12/30/2019
     * Modified by
     * Purpose: Insert the model into the LiveData object
     */
    public void setAlertModel(AlertModel alertModel) {
        AppLog.d(TAG, "-> setAlertModel()");
        this.alertModelSingleLiveEvent.setValue(alertModel);
    }

    /**
     * Name: getNavLocation
     * Created by Devin Martinolich 12/13/2019
     * Modified by
     * Purpose: Getter used by an Activity or Fragment to observe LiveData
     *
     * @return Integer LiveData
     */
    public SingleLiveEvent<Integer> getNavLocation() {
        return navLocation;
    }

    /**
     * Name: navClicked
     * Created by Devin Martinolich 12/10/2019
     * Modified by
     * Purpose: Handle most clicks within the view.
     * @param view
     */
    public void navClicked(@NonNull View view) {
        switch (view.getId()) {
            case R.id.btnClose:
                AppLog.d(TAG, "btnClose clicked");
                navLocation.setValue(R.id.btnClose);
                break;
            default:
                AppLog.d(TAG, "Unknown ID: " + view.getId());
                break;
        }
    }

    /**
     * Name: decryptStoredPass
     * Created by abennett
     * Modified by Devin Martinolich 12/4/2019
     * Purpose: Decrypt the password stored in shared preferences
     */
    public String decryptStoredPass() {
        try {
            DeCryptor decryptor = new DeCryptor();

            String pass = SharedPrefUtils.getString(BaseMVVM.getAppInstance().getApplicationContext(),
                    Constants.SharedPrefName.APP_PREFS,
                    Constants.SharedPrefKeys.PASSWORD,
                    "");

            String ivPass = SharedPrefUtils.getString(BaseMVVM.getAppInstance().getApplicationContext(),
                    Constants.SharedPrefName.APP_PREFS,
                    Constants.SharedPrefKeys.IVPASS,
                    "");

            if (!pass.equals("") && !ivPass.equals("")) {

                String password = decryptor.decryptData("password",
                        android.util.Base64.decode(pass, android.util.Base64.DEFAULT),
                        android.util.Base64.decode(ivPass, android.util.Base64.DEFAULT));

                return password;
            }

        } catch (UnrecoverableEntryException | NoSuchAlgorithmException |
                KeyStoreException | NoSuchPaddingException | CertificateException | IOException |
                InvalidKeyException | IllegalBlockSizeException | BadPaddingException |
                InvalidAlgorithmParameterException e) {
            AppLog.e(TAG, e.getMessage());
            FabricUtils.fabricException(e);
        } catch (NullPointerException e) {
            AppLog.e(TAG, e.getMessage());
        }

        return null;
    }

    /**
     * Name: encryptPass
     * Created by abennett
     * Modified by Devin Martinolich 1/8/2020
     * Purpose: Encrypt the password and store it in shared preferences.
     *
     * @param password
     */
    public void encryptPass(String password) {
        try {
            EnCryptor encryptor = new EnCryptor();

            final byte[] encryptedPassword;
            encryptedPassword = encryptor.encryptText("password", password);

            SharedPrefUtils.setString(BaseMVVM.getAppInstance().getApplicationContext(),
                    Constants.SharedPrefName.APP_PREFS,
                    Constants.SharedPrefKeys.IVPASS,
                    android.util.Base64.encodeToString(encryptor.getIv(), android.util.Base64.DEFAULT));

            SharedPrefUtils.setString(BaseMVVM.getAppInstance().getApplicationContext(),
                    Constants.SharedPrefName.APP_PREFS,
                    Constants.SharedPrefKeys.PASSWORD,
                    android.util.Base64.encodeToString(encryptedPassword, android.util.Base64.DEFAULT));

        } catch (UnrecoverableEntryException | NoSuchAlgorithmException | NoSuchProviderException | KeyStoreException | IOException | NoSuchPaddingException | InvalidKeyException e) {
            AppLog.e(TAG, "onClick() called with: " + e.getMessage());
            FabricUtils.fabricException(e);
        } catch (InvalidAlgorithmParameterException | SignatureException | IllegalBlockSizeException | BadPaddingException e) {
            FabricUtils.fabricException(e);
            e.printStackTrace();
        }
    }

    public void logAnalytics(String type, String item, String event) {
        AppLog.d(TAG, "-> logAnalytics(" + type + ", " + item + ")");

        if (mFirebaseAnalyticsListener != null) {
            mFirebaseAnalyticsListener.logAnalytics(type, item, event);
        }
        else {
            AppLog.d(TAG, "FirebaseAnalyticsListener is null...");
        }
    }
}