package com.devinmartinolich.basemvvm.viewmodel.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.devinmartinolich.basemvvm.utils.AppLog;
import com.devinmartinolich.basemvvm.view.interfaces.FirebaseAnalyticsListener;
import com.devinmartinolich.basemvvm.viewmodel.MainViewModel;

/**
 * Name: MainViewModelFactory
 * Created by Devin Martinolich 1/30/2020
 * Modified by
 * Purpose: ViewModel Factory to pass additional parameter into ViewModel
 */
public class MainViewModelFactory implements ViewModelProvider.Factory {
    private static final String TAG = "MainViewModelFactory";
    private FirebaseAnalyticsListener listener;

    public MainViewModelFactory(FirebaseAnalyticsListener listener) {
        AppLog.d(TAG, "-> MainViewModelFactory()");
        this.listener = listener;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(listener);
    }
}