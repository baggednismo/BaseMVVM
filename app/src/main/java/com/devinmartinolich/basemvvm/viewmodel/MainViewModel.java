package com.devinmartinolich.basemvvm.viewmodel;

import android.content.SharedPreferences;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;

import com.devinmartinolich.basemvvm.BaseMVVM;
import com.devinmartinolich.basemvvm.BuildConfig;
import com.devinmartinolich.basemvvm.R;
import com.devinmartinolich.basemvvm.model.AlertModel;
import com.devinmartinolich.basemvvm.model.DialogModel;
import com.devinmartinolich.basemvvm.model.NotificationModel;
import com.devinmartinolich.basemvvm.model.SharedPrefModel;
import com.devinmartinolich.basemvvm.model.rest.RestApiImpl;
import com.devinmartinolich.basemvvm.utils.AppLog;
import com.devinmartinolich.basemvvm.utils.Constants;
import com.devinmartinolich.basemvvm.utils.FabricUtils;
import com.devinmartinolich.basemvvm.utils.SharedPrefUtils;
import com.devinmartinolich.basemvvm.utils.SingleLiveEvent;
import com.devinmartinolich.basemvvm.utils.biometrics.BiometricCallback;
import com.devinmartinolich.basemvvm.utils.biometrics.BiometricUtils;
import com.devinmartinolich.basemvvm.view.interfaces.FirebaseAnalyticsListener;
import com.google.firebase.analytics.FirebaseAnalytics;

import static com.devinmartinolich.basemvvm.utils.NetworkUtils.isNetworkAvailable;
import static com.devinmartinolich.basemvvm.utils.StringUtils.isTrimmedEmpty;

public class MainViewModel extends BaseViewModel implements LifecycleObserver, BiometricCallback, TextView.OnEditorActionListener {
    private static final String TAG = "MainViewModel";
    private SharedPreferences mSharedPreferences;
    private RestApiImpl mRestApi;
    private String authResponse;

    public MutableLiveData<String> username = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<Boolean> fingerprintSwitch = new MutableLiveData<>();
    public MutableLiveData<Boolean> rememberUsername = new MutableLiveData<>();
    private SingleLiveEvent<Boolean> showLoading = new SingleLiveEvent<>();
    private MutableLiveData<Boolean> showFingerprintSwitch = new MutableLiveData<>();
    private MutableLiveData<Boolean> showFingerprintNav = new MutableLiveData<>();
    private MutableLiveData<Boolean> showLoginBox = new MutableLiveData<>();
    public SingleLiveEvent<DialogModel> dialogModel = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> useFingerprint = new SingleLiveEvent<>();

    private MutableLiveData<SharedPrefModel> sharedPrefModelMutableLiveData;

    public MainViewModel(FirebaseAnalyticsListener mFirebaseAnalyticsListener) {
        AppLog.d(TAG, "MainViewModel()");

        if (BuildConfig.DEBUG) {
            username.setValue("testing");
            password.setValue("password");
        }

        this.mFirebaseAnalyticsListener = mFirebaseAnalyticsListener;

        mSharedPreferences = SharedPrefUtils.getSharedPreferences(BaseMVVM.getAppInstance(),
                Constants.SharedPrefName.APP_PREFS);

        populateSettings();
        showFingerprint();
    }

    /**
     * Name: populateSettings
     * Created by Devin Martinolich 12/4/2019
     * Modified by
     * Purpose: Set the values for view elements when the view loads.
     */
    private void populateSettings(){
        AppLog.d(TAG, "populateSettings()");

        SharedPrefModel sharedPrefModel = new SharedPrefModel(mSharedPreferences);
        fingerprintSwitch.setValue(sharedPrefModel.getFingerprint());
        rememberUsername.setValue(sharedPrefModel.getRememberUsername());

        // Could be the first time the app runs and LoggedIn flag doesnt exist yet
        if ((sharedPrefModel.getLoggedIn() != null &&
                !sharedPrefModel.getLoggedIn()) || BaseMVVM.getUsername() == null) {
            showLoginBox.setValue(true);
        }
        else {
            showLoginBox.setValue(false);
        }
    }

    /**
     * Name: showFingerprint
     * Created by Devin Martinolich 12/4/2019
     * Modified by
     * Purpose: Update the LiveData variable to trigger the show or hide of the fingerprintSwitch switch.
     */
    private void showFingerprint() {
        showFingerprintSwitch.setValue((BiometricUtils.isHardwareSupported(BaseMVVM.getAppInstance()) &&
                BiometricUtils.isFingerprintAvailable(BaseMVVM.getAppInstance())));

        if (fingerprintSwitch.getValue() != null && fingerprintSwitch.getValue()) {
            showFingerprintNav.postValue(true);
        }
        else {
            showFingerprintNav.postValue(false);
        }
    }

    /**
     * Name: onCheckedChangeRemember
     * Created by Devin Martinolich 12/4/2019
     * Modified by
     * Purpose: Update SharedPreferences with the users request based on
     *          "Remember Username" Switch status.
     */
    public void onCheckedChangeRemember(Boolean bool) {
        AppLog.d(TAG, "onCheckedChangeRemember(" + bool.toString() + ")");
        SharedPrefUtils.setBool(BaseMVVM.getAppInstance(),
                Constants.SharedPrefName.APP_PREFS,
                Constants.SharedPrefKeys.REMEMBER_USERNAME,
                bool);
        rememberUsername.setValue(bool);

        /*
         * Set the username to empty string in shared preferences so its not saved
         * we set the field when attempting to log in if the user has the option turned on.
         * We shouldnt remove it if the user has fingerprint enabled.
         */
        if (!shouldRememberUsername()) {
            SharedPrefUtils.setString(BaseMVVM.getAppInstance(),
                    Constants.SharedPrefName.APP_PREFS,
                    Constants.SharedPrefKeys.USERNAME,
                    "");
        }
    }

    /**
     * Name: onCheckedChangeFingerprint
     * Created by Devin Martinolich 12/4/2019
     * Modified by
     * Purpose: Update SharedPreferences with the users request based on
     *          "Use Fingerprint" Switch status.
     */
    public void onCheckedChangeFingerprint(Boolean bool) {
        AppLog.d(TAG, "onCheckedChangeFingerprint(" + bool.toString() + ")");
        SharedPrefUtils.setBool(BaseMVVM.getAppInstance(),
                Constants.SharedPrefName.APP_PREFS,
                Constants.SharedPrefKeys.FINGERPRINT,
                bool);

        // This updates the shared preferences of the users choice.
        fingerprintSwitch.setValue(bool);

        /*
         * If the user disabled fingerprint authentication we need to get rid
         * of the password stored in shared preferences
         */
        if (!bool) {
            SharedPrefUtils.setString(BaseMVVM.getAppInstance().getApplicationContext(),
                    Constants.SharedPrefName.APP_PREFS,
                    Constants.SharedPrefKeys.PASSWORD,
                    "");
        }

        /*
         * We might have the username stored because the user wanted to authenticate
         * with fingerprint. We should remove it assuming they don't want to be remembered
         * and they don't want to use fingerprint anymore.
         */
        if (!shouldRememberUsername()) {
            SharedPrefUtils.setString(BaseMVVM.getAppInstance().getApplicationContext(),
                    Constants.SharedPrefName.APP_PREFS,
                    Constants.SharedPrefKeys.USERNAME,
                    "");
        }

        // This will show/hide the fingerprintSwitch button on the screen.
        showFingerprintNav.postValue(bool);
    }

    /**
     * Name: getSharedPrefModel
     * Created by Devin Martinolich 12/4/2019
     * Modified by
     * Purpose: Getter used by an Activity or Fragment to observe LiveData
     *
     * @return  SharedPrefModel LiveData
     */
    public LiveData<SharedPrefModel> getSharedPrefModel() {
        AppLog.d(TAG, "getSharedPrefModel()");

        if (sharedPrefModelMutableLiveData == null)
            sharedPrefModelMutableLiveData = new MutableLiveData<>();

//        if (transLiveData == null) {
//            transLiveData = Transformations.switchMap(sharedPrefModelMutableLiveData, sharedPrefs -> {
//
//            });
//        }
        return sharedPrefModelMutableLiveData;
    }

    /**
     * Name: getShowLoading
     * Created by Devin Martinolich 12/4/2019
     * Modified by
     * Purpose: Getter used by an Activity or Fragment to observe LiveData
     *
     * @return  Boolean LiveData
     */
    public SingleLiveEvent<Boolean> getShowLoading() {
        return showLoading;
    }

    /**
     * Name: setShowLoading
     * Created by Devin Martinolich 1/22/2020
     * Modified by
     * Purpose: Setter used by an Activity or Fragment to update LiveData
     *
     * @param showLoading Boolean
     */
    public void setShowLoading(Boolean showLoading) {
        this.showLoading.setValue(showLoading);
    }

    /**
     * Name: getShowFingerprintSwitch
     * Created by Devin Martinolich 12/4/2019
     * Modified by
     * Purpose: Getter used by an Activity or Fragment to observe LiveData
     *
     * @return Boolean LiveData
     */
    public MutableLiveData<Boolean> getShowFingerprintSwitch() {
        return showFingerprintSwitch;
    }

    /**
     * Name: getShowFingerprintNav
     * Created by Devin Martinolich 12/4/2019
     * Modified by
     * Purpose: Getter used by an Activity or Fragment to observe LiveData
     *
     * @return Boolean LiveData
     */
    public MutableLiveData<Boolean> getShowFingerprintNav() {
        return showFingerprintNav;
    }

    /**
     * Name: getShowLoginBox
     * Created by Devin Martinolich 12/16/2019
     * Modified by
     * Purpose: Getter used by an Activity or Fragment to observe LiveData
     *
     * @return Boolean LiveData
     */
    public MutableLiveData<Boolean> getShowLoginBox() {
        return showLoginBox;
    }

    /**
     * Name: getDialogModel
     * Created by Devin Martinolich 1/3/2020
     * Modified by
     * Purpose: Getter used by an Activity or Fragment to observe LiveData
     *
     * @return Boolean LiveData
     */
    public SingleLiveEvent<DialogModel> getDialogModel() {
        return dialogModel;
    }

    /**
     * Name: getUseFingerprint
     * Created by Devin Martinolich 1/7/2020
     * Modified by
     * Purpose: Getter used by an Activity or Fragment to observe LiveData
     *
     * @return Boolean LiveData
     */
    public SingleLiveEvent<Boolean> getUseFingerprint() {
        return useFingerprint;
    }

    /**
     * Name: shouldRememberUsername
     * Created by Devin Martinolich 1/24/2020
     * Modified by
     * Purpose: Helper method to determine based on settings if we should store the username
     *
     * @return boolean
     */
    private boolean shouldRememberUsername() {
        return (rememberUsername.getValue() != null && rememberUsername.getValue()) ||
                fingerprintSwitch.getValue() != null && fingerprintSwitch.getValue();
    }

    /**
     * Name: loginClicked
     * Created by Devin Martinolich 12/4/2019
     * Modified by
     * Purpose: Button OnClick() Event mapped by XML
     */
    public void loginClicked() {
        logAnalytics(FirebaseAnalytics.Event.LOGIN,
                Constants.AnalyticsValues.LOGIN_MANUAL,
                FirebaseAnalytics.Event.SELECT_CONTENT);
        AppLog.d(TAG, "loginClicked()");

        showLoading.setValue(true);

        if (isTrimmedEmpty(username.getValue()) || isTrimmedEmpty(password.getValue())) {
            setNotificationModel(
                    new NotificationModel("Please enter your username and password.",
                            false, null));
        } else {
            if (shouldRememberUsername() && username.getValue() != null) {
                SharedPrefUtils.setString(BaseMVVM.getAppInstance(),
                        Constants.SharedPrefName.APP_PREFS,
                        Constants.SharedPrefKeys.USERNAME,
                        username.getValue());
            }
            login(username.getValue(), password.getValue());
        }
    }

    /**
     * Name: loginFingerprint
     * Created by Devin Martinolich 12/4/2019
     * Modified by
     * Purpose: Login process followed when user authenticates with fingerprintSwitch
     *          instead of credentials.
     */
    public void loginFingerprint() {
        logAnalytics(FirebaseAnalytics.Event.LOGIN,
                Constants.AnalyticsValues.LOGIN_FINGERPRINT,
                FirebaseAnalytics.Event.SELECT_CONTENT);
        AppLog.d(TAG, "loginFingerprint()");

        showLoading.setValue(true);
        useFingerprint.postValue(true);
    }

    /**
     * Name: login
     * Created by Devin Martinolich 12/4/2019
     * Modified by
     * Purpose: Authentication process that needs to be followed using
     *          login click or fingerprint auth actions.
     */
    private void login(String username, String password){
        AppLog.d(TAG, "login()");

        new Handler().post(() -> {
            String time = "0";

            if (mRestApi == null)
                mRestApi = new RestApiImpl(notificationModelMutableLiveData, BuildConfig.API_DOMAIN);

            NotificationModel notificationModel = new NotificationModel("", false, null);

            if (isNetworkAvailable(BaseMVVM.getAppInstance().getApplicationContext())) {
                Thread t = new Thread(() -> authResponse = mRestApi.doAuthSync(username, password));
                t.start();

                try {
                    t.join();
                } catch (InterruptedException e) {
                    AppLog.e(TAG, e.getMessage());
                    FabricUtils.fabricException(e);
                }

                if (authResponse != null && authResponse.equals("true")) {
                    logAnalytics(FirebaseAnalytics.Event.LOGIN,
                            Constants.AnalyticsValues.LOGIN_SUCCESS,
                            FirebaseAnalytics.Event.SELECT_CONTENT);

                    // Store user/pass in app for the session
                    BaseMVVM.setUsername(username);
                    BaseMVVM.setPassword(password);

                    // Do we need to store the user/pass in shared preferences?
                    if (fingerprintSwitch.getValue() != null && fingerprintSwitch.getValue()) {
                        if (!password.equals(decryptStoredPass())) {
                            encryptPass(password);
                        }

                        // Store the username, we are assuming that the user wants us to because they have
                        // "Use Fingerprint Authentication" switch checked.
                        SharedPrefModel sharedPrefModel = new SharedPrefModel(mSharedPreferences);
                        if (!username.equals(sharedPrefModel.getUsername())) {
                            SharedPrefUtils.setString(BaseMVVM.getAppInstance().getApplicationContext(),
                                    Constants.SharedPrefName.APP_PREFS,
                                    Constants.SharedPrefKeys.USERNAME,
                                    username);
                        }
                    }

                    // We need to store the username as well as the password or fingerprint auth wont work
                    SharedPrefUtils.setString(BaseMVVM.getAppInstance().getApplicationContext(),
                            Constants.SharedPrefName.APP_PREFS,
                            Constants.SharedPrefKeys.USERNAME,
                            username);

                    notificationModel.setNotification(BaseMVVM.getAppInstance().getString(R.string.Auth_Success));
                    notificationModel.setLoginSuccess(true);

                } else if (authResponse != null && authResponse.equals("false")) {
                    notificationModel.setNotification(BaseMVVM.getAppInstance().getString(R.string.Auth_Failure));
                    notificationModel.setLoginSuccess(false);
                } else {
                    notificationModel.setNotification(BaseMVVM.getAppInstance().getString(R.string.Auth_Unknown));
                    notificationModel.setLoginSuccess(false);
                }
            } else {
                notificationModel.setNotification(BaseMVVM.getAppInstance().getString(R.string.error_network_no_internet));
                notificationModel.setLoginSuccess(false);
            }
            notificationModelMutableLiveData.setValue(notificationModel);
        });
    }

    /**
     * Name: setVisibility
     * Created by Devin Martinolich 12/4/2019
     * Modified by
     * Purpose: To handle element visibility in the view based on ternary in ViewModel
     *
     * @param view
     * @param visible
     */
    @BindingAdapter({"visibility"})
    public static void setVisibility(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter({"enabled"})
    public static void setEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected void onLifeCycleResume() {
        AppLog.d(TAG, "-> onLifeCycleResume()");

        /*
            TODO: need to get sharedpref livedata singleton working correctly.
            TODO: in order to get ViewModel to listen to changes we need to use Transformations.switchMap()
        */
        boolean loggedIn = SharedPrefUtils.getBool(BaseMVVM.getAppInstance().getApplicationContext(),
                Constants.SharedPrefName.APP_PREFS,
                Constants.SharedPrefKeys.LOGGED_IN,
                false);

        AppLog.d(TAG, "Logged In: " + loggedIn);

        if (loggedIn && BaseMVVM.getUsername() != null) {
            showLoginBox.setValue(false);
            showFingerprintNav.setValue(false);
        }

        // Make sure we set the username when returning to the fragment assuming the user
        // wanted us to remember it.
        if (rememberUsername.getValue() != null && rememberUsername.getValue()) {
            username.postValue(SharedPrefUtils.getString(BaseMVVM.getAppInstance().getApplicationContext(),
                    Constants.SharedPrefName.APP_PREFS,
                    Constants.SharedPrefKeys.USERNAME,
                    ""));
        }
    }

    @Override
    public void onAuthenticationFailed() {
        AppLog.d(TAG, "-> onAuthenticationFailed()");
        showLoading.setValue(false);
    }

    @Override
    public void onAuthenticationCanceled() {
        AppLog.d(TAG, "-> onAuthenticationCanceled()");
        showLoading.setValue(false);
    }

    @Override
    public void onAuthenticationSucceeded() {
        AppLog.d(TAG, "-> onAuthenticationSucceeded()");

        Boolean passStored = true;
        // Is the password stored & encrypted in shared preferences?
        String pass = decryptStoredPass();
        String user = username.getValue();

        // If its not check what the value on screen is
        if (pass == null && (password.getValue() != null &&
                !password.getValue().equals(""))) {
            pass = password.getValue();
            passStored = false;
        }

        if (user == null || user.equals("")) {
            user = SharedPrefUtils.getString(BaseMVVM.getAppInstance().getApplicationContext(),
                    Constants.SharedPrefName.APP_PREFS,
                    Constants.SharedPrefKeys.USERNAME,
                    "");
        }

        // As long as we have a password to work with lets continue
        if (pass != null) {
            if (!passStored) {
                encryptPass(pass);
            }
            if (!user.equals("")) {
                login(user, pass);
            }
            else {
                // The user authenticated with a fingerprintSwitch but we dont have a password to move forward.
                setAlertModel(new AlertModel(BaseMVVM.getAppInstance().getString(R.string.missing_username_title),
                        BaseMVVM.getAppInstance().getString(R.string.missing_username_body), null));
                showLoading.setValue(false);
            }
        }
        else {
            // The user authenticated with a fingerprintSwitch but we dont have a password to move forward.
            setAlertModel(new AlertModel(BaseMVVM.getAppInstance().getString(R.string.missing_pass_title),
                    BaseMVVM.getAppInstance().getString(R.string.missing_pass_body), null));
            showLoading.setValue(false);
        }
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        AppLog.d(TAG, "-> onAuthenticationError(" + errorCode + ", " + errString + ")");
        showLoading.setValue(false);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            loginClicked();
        }
        return false;
    }
}