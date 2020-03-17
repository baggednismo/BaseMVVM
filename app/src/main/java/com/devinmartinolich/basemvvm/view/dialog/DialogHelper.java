package com.devinmartinolich.basemvvm.view.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.devinmartinolich.basemvvm.R;
import com.devinmartinolich.basemvvm.databinding.DialogFailureBinding;
import com.devinmartinolich.basemvvm.databinding.DialogSuccessBinding;
import com.devinmartinolich.basemvvm.utils.AppLog;
import com.devinmartinolich.basemvvm.viewmodel.DialogViewModel;

import java.util.Objects;

public class DialogHelper extends BaseDialogFragment {

    public static final int DIALOG_SUCCESS = 100;
    public static final int DIALOG_FAILURE = 101;

    public static final int DIALOG_CLOSE = 107;

    private static final String TAG = "DialogHelper";
    private int mDialogIdentifier;
    private View mView;
    private DialogViewModel dialogViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppLog.d(TAG, "onCreateView()");
        dialogViewModel = new ViewModelProvider(this).get(DialogViewModel.class);

        switch (mDialogIdentifier) {
            case DIALOG_SUCCESS:
                shouldExpandDialog(false, false);
                DialogSuccessBinding successBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_success, container, false);
                mView = successBinding.getRoot();
                successBinding.setDialogViewModel(dialogViewModel);
                break;
            case DIALOG_FAILURE:
                shouldExpandDialog(false, false);
                DialogFailureBinding failureBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_failure, container, false);
                mView = failureBinding.getRoot();
                failureBinding.setDialogViewModel(dialogViewModel);
                break;
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        AppLog.d(TAG, "-> onViewCreated()");
        super.onViewCreated(view, savedInstanceState);

        dialogViewModel.getNavLocation().observe(getViewLifecycleOwner(), navLocation -> {
            AppLog.d(TAG, "navLocation changed");
            switch (navLocation) {
                case R.id.btnClose:
                    getTargetFragment().onActivityResult(getTargetRequestCode(), DIALOG_CLOSE, getActivity().getIntent());
                    dismiss();
                    break;
            }
        });
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (dialog.getWindow() != null)
            // request a window without the title
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        if (mView != null)
            ((ViewGroup) mView.getParent()).removeAllViews();
        super.onDestroyView();
    }

    /**
     * Name: setDialogId
     * Created by Devin Martinolich 1/2/2020
     * Modified by
     * Purpose: To set identifier of the dialog
     *
     * @param mDialogIdentifier as a value of identifier of the dialog
     */
    public void setDialogId(int mDialogIdentifier) {
        AppLog.d(TAG, "setOnDialogIdentifier()");
        this.mDialogIdentifier = mDialogIdentifier;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
