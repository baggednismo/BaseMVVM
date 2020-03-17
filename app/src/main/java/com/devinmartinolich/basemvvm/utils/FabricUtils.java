package com.devinmartinolich.basemvvm.utils;

import com.crashlytics.android.Crashlytics;
import com.devinmartinolich.basemvvm.BaseMVVM;

public class FabricUtils {
    private static final String TAG = "FabricUtils";

    /**
     * Name: FabricUtil fabricException
     * Created by Devin Martinolich on 1/24/18.
     * Modified by Devin Martinolich on 2/11/2020
     * Purpose: To send throwable exception to Fabric
     *
     * @param mException Throwable exception
     */
    public static void fabricException(Throwable mException) {
        AppLog.d(TAG, "-> fabricException(Throwable)");
        if (BaseMVVM.getAppInstance().getCrashlytics() != null) {
            Crashlytics.logException(mException);
        }
    }

    /**
     * Name: FabricUtil fabricExceptionWithMessage
     * Created by Devin Martinolich on 1/24/18.
     * Modified by Devin Martinolich on 2/11/2020
     * Purpose: To send throwable exception to Fabric
     *
     * @param mException Throwable exception
     * @param sMsg String custom message
     */
    public static void fabricExceptionWithMessage(Throwable mException, String sMsg) {
        AppLog.d(TAG, "fabricException(Throwable, Message)");
        if (BaseMVVM.getAppInstance().getCrashlytics() != null) {
            Crashlytics.log(sMsg);
            Crashlytics.logException(mException);
        }
    }
}
