package com.devinmartinolich.basemvvm.view.activity;

import android.os.Bundle;

import com.devinmartinolich.basemvvm.R;
import com.devinmartinolich.basemvvm.utils.AppLog;
import com.devinmartinolich.basemvvm.view.fragment.MainFragment;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppLog.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.root_activity_layout);

        loadFragment(MainFragment.class, MainFragment.class.getSimpleName(), getIntent().getExtras());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
