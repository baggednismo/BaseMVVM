package com.devinmartinolich.basemvvm.view.dialog;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.devinmartinolich.basemvvm.utils.AppLog;
import com.devinmartinolich.basemvvm.utils.FabricUtils;

/**
 * Name: BaseDialogFragment
 * Create by Devin Martinolich 1/2/2020
 * Modified by
 * Purpose: Provide DialogFragments a parent for recyclable code
 */
public class BaseDialogFragment extends DialogFragment {
    private static final String TAG = "BaseDialogFragment";
    private boolean shouldExpandWidth, shouldExpandHeight;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLog.d(TAG, "-> onCreate()");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppLog.d(TAG, "-> onViewCreated()");
    }

    protected void shouldExpandDialog(boolean shouldExpandWidth, boolean shouldExpandHeight) {
        AppLog.d(TAG, "-> shouldExpandDialog()");

        this.shouldExpandWidth = shouldExpandWidth;
        this.shouldExpandHeight = shouldExpandHeight;
    }

    /**
     * Name: getScreenSizeInPixes
     * Created by Devin Martinolich 1/3/2020
     * Modified by
     * Purpose: To get screen size in pixels
     *
     * @return
     */
    private int[] getScreenSizeInPixes() {
        AppLog.d(TAG, "-> getScreenSizeInPixes()");
        try {
            DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
            int dpHeight = (int) (displayMetrics.heightPixels * .90);
            int dpWidth = (int) (displayMetrics.widthPixels * .90);
            AppLog.d("dpHeight-----", String.valueOf(displayMetrics.heightPixels / displayMetrics.density));
            AppLog.d("dpWidth------", String.valueOf(displayMetrics.widthPixels / displayMetrics.density));
            AppLog.d("pixelHeight-----", String.valueOf(dpHeight));
            AppLog.d("pixelWidth------", String.valueOf(dpWidth));
            return new int[]{dpWidth, dpHeight};
        } catch (Exception e) {
            AppLog.e(TAG, e.getMessage());
            FabricUtils.fabricException(e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Name: expandDialog
     * Created by Devin Martinolich 1/3/2020
     * Modified by
     * Purpose: To expand the dialog
     */
    private void expandDialog() {
        AppLog.d(TAG, "-> expandDialog()");
        try {
            int[] screenSize = getScreenSizeInPixes();
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            if (screenSize != null) {
                if (shouldExpandWidth && shouldExpandHeight) {
                    params.width = screenSize[0];
                    params.height = screenSize[1];
                } else if (shouldExpandWidth) {
                    params.width = screenSize[0];
                } else {
                    params.height = screenSize[1];
                }
            } else {
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
            }
            getDialog().getWindow().setAttributes(params);
        } catch (Exception e) {
            AppLog.e(TAG, e.getMessage());
            FabricUtils.fabricException(e);
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (shouldExpandWidth || shouldExpandHeight)
            expandDialog();
    }
}
