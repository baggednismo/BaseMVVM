package com.devinmartinolich.basemvvm.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.devinmartinolich.basemvvm.BaseMVVM;
import com.devinmartinolich.basemvvm.R;
import com.devinmartinolich.basemvvm.model.NavigationModel;
import com.devinmartinolich.basemvvm.utils.AppLog;
import com.devinmartinolich.basemvvm.utils.Constants;
import com.devinmartinolich.basemvvm.utils.FabricUtils;
import com.devinmartinolich.basemvvm.utils.biometrics.BiometricCallback;
import com.devinmartinolich.basemvvm.utils.biometrics.BiometricCallbackImp;
import com.devinmartinolich.basemvvm.view.activity.BaseActivity;
import com.devinmartinolich.basemvvm.view.dialog.DialogHelper;
import com.devinmartinolich.basemvvm.view.interfaces.DialogListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;
import java.util.concurrent.Executor;

import static com.devinmartinolich.basemvvm.utils.Constants.DefaultValues.FRAGMENT_LOAD_DELAY;

public class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";
    private DialogHelper mDialogHelper;
    protected View mView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AppLog.d(TAG, "-> onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppLog.d(TAG, "-> onCreateView()");
        setHasOptionsMenu(true);
        getActivityActionBar().setDisplayShowTitleEnabled(true);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * Name: getActivityActionBar
     * Created by Devin Martinolich 12/20/2019
     * Modified by
     * Purpose: Provide the applications ActionBar
     *
     * @return ActionBar
     */
    protected ActionBar getActivityActionBar() {
        AppLog.d(TAG, "-> getActivityActionBar()");
        return ((getActivity())!= null) ? ((BaseActivity) getActivity()).getSupportActionBar() : null;
    }

    /**
     * Name: displaySnackbar
     * Created by Devin Martinolich 12/20/2019
     * Modified by
     * Purpose: Show a self dismissible SnackBar to the user
     *
     * @param view
     * @param sMessage
     * @param length
     */
    public void displaySnackbar(View view, String sMessage, int length) {
        AppLog.d(TAG, "-> displaySnackbar()");
        try {
            ((BaseActivity) Objects.requireNonNull(getActivity())).showSnackBarWithOK(view, sMessage, length);
        } catch (Exception e) {
            AppLog.e(TAG, e.getMessage());
            FabricUtils.fabricException(e);
            e.printStackTrace();
        }
    }

    /**
     * Name: displayAlert
     * Created by Devin Martinolich 12/30/2019
     * Modified by
     * Purpose: Show a self dismissible SnackBar to the user
     *
     * @param sTitle
     * @param sMessage
     * @param mDialogListener
     */
    public void displayAlert(String sTitle, String sMessage, String sNegativeBtn, final DialogListener mDialogListener) {
        AppLog.d(TAG, "-> displayAlert()");
        try {
            ((BaseActivity) Objects.requireNonNull(getActivity())).showDialog(getContext(), sTitle, sMessage, sNegativeBtn, mDialogListener);
        } catch (Exception e) {
            AppLog.e(TAG, e.getMessage());
            FabricUtils.fabricException(e);
            e.printStackTrace();
        }
    }

    public void showCustomDialog(int id, String tag, Boolean cancelable) {
        mDialogHelper = new DialogHelper();
        mDialogHelper.setDialogId(id);
        mDialogHelper.setTargetFragment(this, id);
        mDialogHelper.setCancelable(cancelable);
        mDialogHelper.show(getActivity().getSupportFragmentManager(), tag);
    }

    /**
     * Name: doNavigation
     * Created by Devin Martinolich 12/11/2019
     * Modified by
     * Purpose: Receive new fragment request and validate before loading
     *
     * @param id
     */
    public void doNavigation(int id) {
        AppLog.d(TAG, "-> doNavigation()");
        Class classObj = null;
        Bundle bundle = new Bundle();
        switch (id) {
            case NavigationModel.Main.id:
                AppLog.d(TAG, "Go back to the Main fragment...");
                ((BaseActivity) getActivity()).popFragment("MainFragment", 0);
                break;
            default:
                AppLog.d(TAG, "Unknown ID. Cannot load new fragment.");
                break;
        }

        if (classObj != null)
        {
            // Inner classes in Java capture ("close over") the lexical scope in which they are
            // defined. But they only capture variables that are declared "final".
            // need to declare the class selected as final to add a post delay on new Runnable
            final Class newClassObj = classObj;
            Handler mHandlerLoadFragmentDelay = new Handler();
            final Bundle finalBundle = bundle;

            mHandlerLoadFragmentDelay.postDelayed(() -> {
                loadFragment(newClassObj, newClassObj.getSimpleName(), finalBundle);
            }, FRAGMENT_LOAD_DELAY);
        }
    }

    /**
     * Name: loadFragment
     * Created by Devin Martinolich 12/11/2019
     * Modified by
     * Purpose: To load fragment on BaseActivity
     *
     * @param aClass  fragment name to load
     * @param aTag    fragment identity tag
     * @param aBundle bundle need to pass on fragment.
     */
    protected void loadFragment(Class aClass, String aTag, Bundle aBundle) {
        AppLog.d(TAG, "-> loadFragment() called with: aTag = [" + aTag + "]");

        if (getActivity() != null) {
            ((BaseActivity) getActivity()).loadFragment(aClass, aTag, aBundle);
        }
    }

    /**
     * Name: setActionBarTitle
     * Created by Devin Martinolich 12/12/2019
     * Modified by
     * Purpose: Update ActionBar title for the fragment
     *
     * @param title  New title for the ActionBar
     */
    public void setActionBarTitle(String title) {
        AppLog.d(TAG, "-> setActionBarTitle()");
        getActivityActionBar().setTitle(title);
    }

    /**
     * Name: setActionBarBackButton
     * Created by Devin Martinolich 12/13/2019
     * Modified by
     * Purpose: Enable/Disable the back button near the title of ActionBar
     *
     * @param bool  enable or disable the back button
     */
    public void setActionBarBackButton(Boolean bool) {
        AppLog.d(TAG, "-> setActionBarBackButton()");
        getActivityActionBar().setDisplayHomeAsUpEnabled(bool);
    }

    /**
     * Name: makeCall
     * Created by Devin Martinolich 12/16/2019
     * Modified by
     * Purpose: Send intent to OS to make a phone call
     * @param number
     */
    public void makeCall(int error, String number) {
        AppLog.d(TAG, "-> makeCall()");

        if (((BaseActivity) getActivity()).hasTelephony()) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        }
        else {
            displayAlert(getResources().getString(R.string.error),
                    getResources().getString(error),
                    null,
                    null);
        }
    }

    /**
     * Name: sendEmail
     * Created by Devin Martinolich 12/16/2019
     * Modified by
     * Purpose: Send intent to OS to draft a new email
     * @param destination
     */
    public void sendEmail(String destination) {
        AppLog.d(TAG, "-> sendEmail()");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("mailto:" + destination));
        startActivity(intent);
    }

    /**
     * Name: requestPermission
     * Created by Devin Martinolich 12/23/2019
     * Modified by
     * Purpose: Get the permission from the user.
     *
     * @param permission - String array of permissions being requested
     * @param requestCode - custom request code
     */
    private void requestPermission(String[] permission, int requestCode) {
        AppLog.d(TAG, "-> requestPermission()");

        for (String perm : permission) {
            if (ContextCompat.checkSelfPermission(getActivity(), perm) != PackageManager.PERMISSION_GRANTED) {
                AppLog.d(TAG, "Requesting permission from user.");
                switch (perm) {
                    case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                        requestPermissions(permission, requestCode);
                        break;
                    case Manifest.permission.CAMERA:
                        requestPermissions(permission, requestCode);
                        break;
                }
            } else {
                AppLog.d(TAG, "Permission already granted by user.");
            }
        }
    }

    /**
     * Name: checkCurrentPermission
     * Created by Devin Martinolich 12/23/2019
     * Modified by
     * Purpose: Check what the current permission value is for the app.
     *
     * @return permission
     */
    private Boolean checkCurrentPermission(String permission) {
        AppLog.d(TAG, "-> checkCurrentPermission()");

        Boolean granted = ContextCompat.checkSelfPermission(getActivity(),
                permission) == PackageManager.PERMISSION_GRANTED;

        AppLog.d(TAG, "User already set the permission: " + permission);

        return granted;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AppLog.d(TAG, "-> onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.RequestCodes.REQUEST_PERMISSIONS_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    AppLog.d(TAG, "User granted storage permission.");
                }
                else {
                    AppLog.d(TAG, "User did NOT grant storage permission...");
                    displaySnackbar(mView, "User did NOT grant storage permission...", Snackbar.LENGTH_LONG);
                }
                break;
            case Constants.RequestCodes.REQUEST_PERMISSIONS_CAMERA:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        AppLog.d(TAG, "User granted camera permission.");
                    }
                    else {
                        AppLog.d(TAG, "User did NOT grant camera permission...");
                        displaySnackbar(mView, "User did NOT grant camera permission...", Snackbar.LENGTH_LONG);
                    }
                }
                break;
        }
    }

    /**
     * Name: showPdf
     * Created by Devin Martinolich 12/23/2019
     * Modified by
     * Purpose: Open a local file in a
     *
     * @param path
     */
    public void showPdf(Uri path) {
        AppLog.d(TAG, "-> showPdf()");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(path, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |
                Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (getContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0) {
            getContext().startActivity(intent);
        } else {
            displayAlert(getResources().getString(R.string.error_no_pdf_viewer_title),
                    getResources().getString(R.string.error_no_pdf_viewer_body),
                    null,
                    null);
        }
    }

    /**
     * Name: openUrl
     * Created by Devin Martinolich 12/26/2019
     * Modified by
     * Purpose: Navigate the user to an external resource.
     *
     * @param url
     */
    public void openURL(String url){
        AppLog.d(TAG, "-> openURL()");

        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    /**
     * Name: displayBiometricPrompt
     * Created by Devin Martinolich 1/7/2020
     * Modified by
     * Purpose: Show the biometric prompt to the user for authentication.
     *
     * Note: This is only useful for API 28 devices. Older devices must use
     *       deprecated classes for authentication.
     *
     * @param callback
     */
    public void displayBiometricPrompt(final BiometricCallback callback) {
        Executor executor = ContextCompat.getMainExecutor(getContext());
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricCallbackImp(callback));
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.biometrics_login_title))
                .setSubtitle(getString(R.string.biometrics_login_body))
                .setNegativeButtonText(getString(R.string.biometrics_login_negative_btn))
                .build();
        biometricPrompt.authenticate(promptInfo);
    }

    /**
     * Name: logFragmentAnalytics
     * Created by Devin Martinolich 2/11/2020
     * Modified by
     *
     * Purpose: Firebase Analytics doesn't track fragment changes. Adding this to log
     *          the fragment that gets loaded in the view.
     */
    private void logFragmentAnalytics() {
        AppLog.d(TAG, "-> logFragmentAnalytics()");
        String name = getActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.flContainer)
                .getClass()
                .getSimpleName();

        BaseMVVM.getFirebaseAnalytics().setCurrentScreen(getActivity(), name, name);
    }

    @Override
    public void onResume() {
        logFragmentAnalytics();
        super.onResume();
    }
}
