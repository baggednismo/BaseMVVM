package com.devinmartinolich.basemvvm.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.devinmartinolich.basemvvm.BaseMVVM;
import com.devinmartinolich.basemvvm.R;
import com.devinmartinolich.basemvvm.utils.AppLog;
import com.devinmartinolich.basemvvm.utils.Constants;
import com.devinmartinolich.basemvvm.utils.FabricUtils;
import com.devinmartinolich.basemvvm.utils.SharedPrefUtils;
import com.devinmartinolich.basemvvm.view.fragment.BaseFragment;
import com.devinmartinolich.basemvvm.view.fragment.MainFragment;
import com.devinmartinolich.basemvvm.view.interfaces.DialogListener;
import com.devinmartinolich.basemvvm.view.interfaces.FirebaseAnalyticsListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.jetbrains.annotations.NotNull;

public class BaseActivity extends AppCompatActivity implements FirebaseAnalyticsListener {

    private static final String TAG = "BaseActivity";
    private Dialog progressDialog = null;
    protected BaseFragment mLastFragment;
    private String prevTag;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLog.d(TAG, "-> onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");

        BaseMVVM.setFirebaseAnalytics(FirebaseAnalytics.getInstance(this));

        setupActionBar();
        initTimeout();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        AppLog.d(TAG, "-> onPostCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        AppLog.d(TAG, "-> onPostResume() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppLog.d(TAG, "-> onDestroy() called");
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        AppLog.d(TAG, "-> onPostCreate() called with: savedInstanceState = [" + savedInstanceState + "], persistentState = [" + persistentState + "]");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        AppLog.d(TAG, "-> onRestart() called");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        AppLog.d(TAG, "-> onTrimMemory() called with: level = [" + level + "]");
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        AppLog.d(TAG, "-> onAttachFragment() called with: fragment = [" + fragment + "]");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onBackPressed() {
        AppLog.d(TAG, "-> onBackPressed()");

        FragmentManager fragmentManager = getSupportFragmentManager();
        /*
         * To get the last loaded fragment which is currently visible
         */
        if (mLastFragment == null) {
            String lastTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
            mLastFragment = (BaseFragment) fragmentManager.findFragmentByTag(lastTag);
        }

        /*
         * Check if there is more than 1 fragment currently opened
         */
        if (fragmentManager.getBackStackEntryCount() > 1) {
            AppLog.d(TAG, "getBackStackEntryCount() > 1");
            goBack();
        } else {
            AppLog.d(TAG, "else finish");
            finish();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        AppLog.d(TAG, "-> onLowMemory() called");
    }

    @Override
    public void onPause() {
        AppLog.d(TAG, "-> onPause()");
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        AppLog.d(TAG, "-> onNewIntent() called with: intent = [" + intent + "]");
    }

    @Override
    public void onResume() {
        super.onResume();
        AppLog.d(TAG, "-> onResume()");
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        AppLog.d(TAG, "-> onResumeFragments() called");
    }

    /**
     * Name: hideSoftKeyboard
     * Created by Devin Martinolich on 1/1/2018
     * Modified by
     * Purpose: To hide soft keyboard
     */
    public void hideSoftKeyboard() {
        AppLog.d(TAG, "hideSoftKeyboard()");
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Name: showSoftKeyboard
     * Created by Devin Martinolich on 1/1/2018
     * Modified by
     * Purpose: To show soft keyboard
     */
    public void showSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);
        }
    }

    /**
     * Name: showDialog
     * Created by Devin Martinolich on 1/4/2018
     * Modified by Devin Martinolich on 1/21/2020
     * Purpose: Display dialog for show error or warning
     * @param mContext
     * @param sTitle - Nullable: if null it will not show a title in dialog
     * @param sMessage
     * @param sNegativeBtn - Nullable: If null it will not show a negative button in dialog
     * @param mDialogListener
     */
    public void showDialog(Context mContext, @Nullable String sTitle, String sMessage, @Nullable String sNegativeBtn, final DialogListener mDialogListener) {

        hideSoftKeyboard();

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext)
                .setMessage(sMessage)
                .setTitle(sTitle)
                .setPositiveButton(R.string.btn_ok, (dialog, whichButton) -> {
                    dialog.dismiss();
                    if (mDialogListener != null)
                        mDialogListener.onDismiss(Constants.RequestCodes.DIALOG_POSITIVE_BTN);
                });

        if (sNegativeBtn != null) {
            mBuilder.setNegativeButton(sNegativeBtn, (dialog, whichButton) -> {
                dialog.dismiss();
                if (mDialogListener != null)
                    mDialogListener.onDismiss(Constants.RequestCodes.DIALOG_NEGATIVE_BTN);
            });
        }

        AlertDialog errorDialog = mBuilder.create();
        errorDialog.setCancelable(false);
        errorDialog.show();
        errorDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        doKeepDialog(errorDialog);
    }

    /**
     * Name: doKeepDialog
     * Created by Devin Martinolich on 1/4/2018
     * Modified by
     * Purpose: Keep dialog display when screen is rotating
     * @param dialog
     */
    private void doKeepDialog(Dialog dialog) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
    }

    /**
     * Name: showSnackBarWithOK
     * Created by Devin Martinolich on 1/4/2018
     * Modified by
     * Purpose: Display snackbar with one action button
     * @param view
     * @param message
     * @param time
     */
    public void showSnackBarWithOK(View view, final String message, final int time) {
        final Snackbar snackBar = Snackbar.make(view, message, time);
        snackBar.setAction("OK", v -> snackBar.dismiss());
        hideSoftKeyboard();
        snackBar.show();
    }

    /**
     * Name: showSnackBarWithOneActionButton
     * Created by Devin Martinolich on 1/4/2018
     * Modified by
     * Purpose: Display snackbar with one action button and title
     * @param view
     * @param message
     * @param time
     * @param actionTitle
     * @param actionClickListener
     */
    public void showSnackBarWithOneActionButton(View view, String message, int time, String actionTitle, View.OnClickListener actionClickListener) {
        Snackbar snackbar = Snackbar
                .make(view, message, time)
                .setAction(actionTitle, actionClickListener);
        hideSoftKeyboard();
        snackbar.show();
    }

    /**
     * Name: showProgressDialog
     * Created by Devin Martinolich on 1/4/2018
     * Modified by
     * Purpose: To show progress dialog
     *
     * @param context
     */
    public void showProgressDialog(Context context) {
        AppLog.d(TAG, "-> showProgressDialog() called");

        String message = "";
        if (context != null && !((Activity) context).isFinishing()) {
            if (progressDialog == null || !progressDialog.isShowing()) {
                progressDialog = new Dialog(context);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                try {
                    int dividerId = progressDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
                    View divider = progressDialog.findViewById(dividerId);
                    if (divider != null)
                        divider.setBackgroundColor(context.getResources().getColor(R.color.transparent));
                } catch (Exception e) {
                    AppLog.e(TAG, e.getMessage());
                    FabricUtils.fabricException(e);
                    e.printStackTrace();
                }

                try {
                    TextView mTitle = (TextView) progressDialog.findViewById(android.R.id.title);
                    if (mTitle != null)
                        mTitle.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

                    int x = Resources.getSystem().getIdentifier("titleDivider", "id", "android");

                    View titleDivider = progressDialog.findViewById(x);
                    if (titleDivider != null)
                        titleDivider.setBackgroundColor(context.getResources().getColor(R.color.transparent));

                } catch (Exception e) {
                    AppLog.e(TAG, e.getMessage());
                    FabricUtils.fabricException(e);
                    e.printStackTrace();
                }

                progressDialog.setContentView(R.layout.dialog_custom_progressbar);
                TextView tvMessage = (TextView) progressDialog.findViewById(R.id.txtMessage);

                if (!message.equals(""))
                    tvMessage.setText(message);

                progressDialog.setCancelable(true);

                if (!((Activity) context).isFinishing()) {
                    progressDialog.show();
                    blockUserInput(true);
                }
            }
        }
        else
            AppLog.e(TAG, "Context: " + context.toString());
    }

    /**
     * Name: hideProgressDialog
     * Created by Devin Martinolich on 1/4/2018
     * Modified by
     * Purpose: To hide progress dialog
     */
    public void hideProgressDialog() {
        AppLog.d(TAG, "-> hideProgressDialog() called");
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                blockUserInput(false);
                progressDialog.dismiss();
            }
        } catch (Throwable throwable) {
            AppLog.e(TAG, throwable.toString());
            FabricUtils.fabricException(throwable);
        }

        finally {
            progressDialog = null;
        }
    }

    /**
     * Name: blockUserInput
     * Created by Devin Martinolich on 12/3/2019
     * Modified by
     * Purpose: To disable the input on a view completely. This should be used during dialogs
     *          that make API calls to prevent the user from breaking the process.
     */
    public void blockUserInput(Boolean lockControls) {
        if (lockControls)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        else
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    /**
     * Name: loadFragment
     * Created by Devin Martinolich 12/11/2019
     * Modified by
     * Purpose: Load new fragment
     *
     * @param aClass  as class object of the opening fragment
     * @param aTag    as tag of the opening fragment
     * @param aBundle as data to pass to the opening fragment
     */
    public void loadFragment(Class aClass, String aTag, Bundle aBundle) {
        AppLog.d(TAG, "loadFragment()");
        AppLog.d(TAG, "loadFragment() called with: aClass = [" + aClass + "], aTag = [" + aTag + "], aBundle = [" + aBundle + "]");

        BaseFragment fragmentToShow = (BaseFragment) Fragment.instantiate(this, aClass.getName(), aBundle);
        FragmentManager fragmentManager = getSupportFragmentManager();

        int fragmentCount = fragmentManager.getBackStackEntryCount();
        AppLog.d(TAG, "Current fragment: " + (fragmentCount > 0 ? fragmentManager.getBackStackEntryAt(fragmentCount - 1).getName() : "null"));

        /*
         * Only open the fragment once. User can click many times if the device is slow between
         * Fragment transactions. Check the current fragment and don't allow move if its the same.
         * If the user is at the MainFragment already and tries to logout, we need to allow that.
         */
        if (fragmentManager.getBackStackEntryCount() <= 0 ||
                (fragmentCount > 0 &&
                        !fragmentManager.getBackStackEntryAt(fragmentCount - 1).getName().equals(aTag)) ||
                fragmentManager.getBackStackEntryAt(fragmentCount - 1).getName().equals("MainFragment")) {
            AppLog.d(TAG, "We should open the new fragment");
            TransitionSet transitionSet = new TransitionSet();
            transitionSet.setOrdering(TransitionSet.ORDERING_TOGETHER);
            transitionSet.addTransition(new ChangeBounds()).
                    addTransition(new ChangeTransform()).
                    addTransition(new ChangeImageTransform());
            fragmentToShow.setSharedElementEnterTransition(transitionSet);
            fragmentToShow.setEnterTransition(new Fade());

            if (mLastFragment != null)
                mLastFragment.setExitTransition(new Fade());

            fragmentToShow.setSharedElementReturnTransition(transitionSet);

            fragmentManager.beginTransaction()
                    .replace(R.id.flContainer, fragmentToShow, aTag)
                    .addToBackStack(aTag)
                    .commitAllowingStateLoss();
            mLastFragment = fragmentToShow;
            prevTag = aTag;
        }
        else {
            AppLog.d(TAG, "Fragment is already open, user probably clicked a button multiple times.");
        }
    }

    /**
     * Name: goBack
     * Created by Devin Martinolich 12/11/2019
     * Modified by
     * Purpose: Called when moving to previous fragment
     */
    public void goBack() {
        AppLog.d(TAG, "goBack()");
        try
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            /*
             * To get the last loaded fragment which is currently visible
             */
            if (mLastFragment == null) {
                String lastTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
                mLastFragment = (BaseFragment) fragmentManager.findFragmentByTag(lastTag);
            }

            /*
             * This will set the result from current fragment to pass to previous fragment while going back.
             */
//            Object object = mLastFragment.setResult();

            super.onBackPressed();

            String msTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();

            AppLog.d(TAG, msTag);

            mLastFragment = (BaseFragment) fragmentManager.findFragmentByTag(msTag);
            prevTag = msTag;
            /*
             * Pass the result object to current visible fragment received from closing fragment
             */
//            fragmentToResume.getResult(object);

        } catch (Exception e) {
            AppLog.e(TAG, e.getMessage());
            FabricUtils.fabricException(e);
            e.printStackTrace();
        }
    }

    /**
     * Name: popFragment
     * Created by Devin Martinolich 1/2/2020
     * Modified by
     * Purpose: Reset the view back to a specific fragment.
     *
     * @param aClass
     */
    public void popFragment(String aClass, int flag) {
        AppLog.d(TAG, "-> popFragment()");
        getSupportFragmentManager().popBackStack(aClass, flag);
    }

    /**
     * Name: setupActionBar
     * Created by Devin Martinolich 12/12/2019
     * Modified by
     * Purpose: Single method to enable/disable features of the ActionBar
     */
    public void setupActionBar(){
        AppLog.d(TAG, "-> setupActionBar()");
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            AppLog.d(TAG, "ActionBar != null");
            mActionBar.setDisplayShowTitleEnabled(true);
        }
    }

    /**
     * Name: hasTelephony
     * Created by Devin Martinolich 12/16/2019
     * Modified by
     * Purpose: Get the device type as it relates to telephony functionality
     * @return boolean
     */
    public boolean hasTelephony() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
    }

    /**
     * Name: initTimeout
     * Created by Devin Martinolich 12/18/2019
     * Modified by
     * Purpose: Define the action of the runnable and what will happen when
     *          user reaches or exceeds the timeout period.
     * NOTE:    This will also work if the app is paused BUT will not work if the app is destroyed.
     */
    public void initTimeout() {
        AppLog.d(TAG, "-> initTimeout");

        handler = new Handler();
        runnable = () -> {
            AppLog.d(TAG, "User was inactive for " +
                    Constants.DefaultValues.DEFAULT_TIMEOUT_MILLISECONDS +
                    " milliseconds or " +
                    Constants.DefaultValues.DEFAULT_TIMEOUT_MILLISECONDS / 60000 +
                    " minutes.");

            // If the user is logged in
            if (SharedPrefUtils.getBool(BaseMVVM.getAppInstance().getApplicationContext(),
                    Constants.SharedPrefName.APP_PREFS,
                    Constants.SharedPrefKeys.LOGGED_IN,
                    false)) {

                // Set the login flag to false
                SharedPrefUtils.setBool(BaseMVVM.getAppInstance().getApplicationContext(),
                        Constants.SharedPrefName.APP_PREFS,
                        Constants.SharedPrefKeys.LOGGED_IN,
                        false);

                // Return the user back to the login fragment
                loadFragment(MainFragment.class, MainFragment.class.getSimpleName(), null);
            }
        };
        startHandler();
    }

    @Override
    public void onUserInteraction() {
        AppLog.d(TAG, "-> onUserInteraction()");
        super.onUserInteraction();
        stopHandler();
        startHandler();
    }

    /**
     * Name: stopHandler
     * Created by Devin Martinolich 12/18/2019
     * Modified by
     * Purpose: Stop the handler that is tracking the login time.
     */
    public void stopHandler() {
        AppLog.d(TAG, "-> stopHandler()");
        handler.removeCallbacks(runnable);
    }

    /**
     * Name: startHandler
     * Created by Devin Martinolich 12/18/2019
     * Modified by
     * Purpose: Start a new handler to track the timeout period.
     */
    public void startHandler() {
        AppLog.d(TAG, "-> startHandler()");
        handler.postDelayed(runnable, Constants.DefaultValues.DEFAULT_TIMEOUT_MILLISECONDS);
    }

    /**
     * Name: logAnalytics
     * Created by Devin Martinolich 12/10/2019
     * Modified by
     * Purpose: Manually send analytical data to Firebase
     *
     * @param type
     * @param item
     */
    @Override
    public void logAnalytics(String type, String item, @NotNull String event) {
        AppLog.d(TAG, "-> logAnalytics(" + type + ", " + item + ")");
        Bundle bundle = new Bundle();
        bundle.putString(type, item);

        if (BaseMVVM.getFirebaseAnalytics() == null) {
            AppLog.d(TAG, "Firebase Analytics is null...");
        }
        else if (event.length() <= 0){
            AppLog.d(TAG, "Event was empty, unable to log an empty event.");
        }
        else {
            BaseMVVM.getFirebaseAnalytics().logEvent(event, bundle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        AppLog.d(TAG, "-> onCreateOptionsMenu()");

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        if (SharedPrefUtils.getBool(BaseMVVM.getAppInstance().getApplicationContext(),
                Constants.SharedPrefName.APP_PREFS,
                Constants.SharedPrefKeys.LOGGED_IN,
                false)) {
            menu.add(Menu.NONE, Constants.RequestCodes.LOGOUT, Menu.NONE,"Logout");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AppLog.d(TAG, "Menu clicked.");

        switch (item.getItemId()) {
            case Constants.RequestCodes.LOGOUT:
                AppLog.d(TAG, "Logout clicked in menu.");

                // Set the login flag to false
                SharedPrefUtils.setBool(BaseMVVM.getAppInstance().getApplicationContext(),
                        Constants.SharedPrefName.APP_PREFS,
                        Constants.SharedPrefKeys.LOGGED_IN,
                        false);

                // Return the user back to the login fragment
                popFragment(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                loadFragment(MainFragment.class, MainFragment.class.getSimpleName(), null);
                return true;
            case android.R.id.home:
                AppLog.d(TAG, "Back button clicked");
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
