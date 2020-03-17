package com.devinmartinolich.basemvvm.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.devinmartinolich.basemvvm.R;
import com.devinmartinolich.basemvvm.databinding.FragmentMainLoginBinding;
import com.devinmartinolich.basemvvm.model.NavigationModel;
import com.devinmartinolich.basemvvm.utils.AppLog;
import com.devinmartinolich.basemvvm.view.activity.BaseActivity;
import com.devinmartinolich.basemvvm.viewmodel.MainViewModel;
import com.devinmartinolich.basemvvm.viewmodel.factory.MainViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

import static com.devinmartinolich.basemvvm.utils.StringUtils.isTrimmedEmpty;

public class MainFragment extends BaseFragment {

    private static final String TAG = "MainFragment";
    private MainViewModel mainViewModel;
    private Boolean loading = false;
    FragmentMainLoginBinding binding;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        AppLog.d(TAG, "-> onActivityCreated()" );
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppLog.d(TAG, "-> onCreateView()" );
        super.onCreateView(inflater, container, savedInstanceState);

        mainViewModel = new ViewModelProvider(this, new MainViewModelFactory((BaseActivity) getActivity())).get(MainViewModel.class);

        getLifecycle().addObserver(mainViewModel);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_login, container, false);
        mView = binding.getRoot();
        binding.setMainViewModel(mainViewModel);
        binding.setLifecycleOwner(this);

        setActionBarTitle(NavigationModel.Main.name);
        setActionBarBackButton(false);

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AppLog.d(TAG, "-> onViewCreated()" );
        super.onViewCreated(view, savedInstanceState);

        mainViewModel.getShowLoading().observe(getViewLifecycleOwner(), showLoading -> {
            AppLog.d(TAG, "showLoading changed");
            this.loading = showLoading;
//            if (loading) {
//                binding.btnLogin.setText(R.string.loggingIn);
//                binding.btnLogin.setEnabled(false);
//                binding.btnLogin.setAlpha(.5f);
//            } else {
//                binding.btnLogin.setText(getString(R.string.login));
//                binding.btnLogin.setEnabled(true);
//                binding.btnLogin.setAlpha(1);
//            }
        });

        mainViewModel.getNotificationModel().observe(getViewLifecycleOwner(), notificationModel -> {
            AppLog.d(TAG, "NotificationModel changed");

            if (!isTrimmedEmpty(notificationModel.getNotification())) {
                displaySnackbar(mView, notificationModel.getNotification(), Snackbar.LENGTH_LONG);
            }

            if (notificationModel.getLoginSuccess()) {
//                doNavigation(NavigationModel.MyAccountMenu.id);
            } else {
                if (loading) {
                    mainViewModel.setShowLoading(false);
                }
            }
        });

        mainViewModel.getNavLocation().observe(getViewLifecycleOwner(), navLocation -> {
            AppLog.d(TAG, "navLocation changed");
            doNavigation(navLocation);
        });

        mainViewModel.getDialogModel().observe(getViewLifecycleOwner(), dialogModel -> {
            showCustomDialog(dialogModel.getId(), dialogModel.getTag(), dialogModel.getCancelable());
        });

        mainViewModel.getAlertModel().observe(getViewLifecycleOwner(), alertModel -> {
            AppLog.d(TAG, "AlertModel changed");
            if (alertModel.getBody() != null && !alertModel.getBody().equals("")) {
                displayAlert(alertModel.getTitle(), alertModel.getBody(), alertModel.getNegativeBtn(), null);
            }
        });

        mainViewModel.getUseFingerprint().observe(getViewLifecycleOwner(), useFingerprint -> {
            AppLog.d(TAG, "useFingerprint changed");
            displayBiometricPrompt(mainViewModel);
        });
    }

    @Override
    public void onResume() {
        AppLog.d(TAG, "-> onResume()");
        super.onResume();
    }
}