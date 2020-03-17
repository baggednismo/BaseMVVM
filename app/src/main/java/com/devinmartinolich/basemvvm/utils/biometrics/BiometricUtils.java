package com.devinmartinolich.basemvvm.utils.biometrics;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.biometric.BiometricManager;
import androidx.core.app.ActivityCompat;

import com.devinmartinolich.basemvvm.utils.AppLog;

public class BiometricUtils {
    private static final String TAG = "BiometricUtils";

    /**
     * Name: isBiometricPromptEnabled
     * Created by Devin Martinolich 12/4/2019
     * Modified by
     * Purpose: Check if the android version on device is >= Pie, since BiometricPrompt is only
     *          supported from Android 9.0+.
     * Note:    This was the case in Alpha and Beta, BiometricPrompt should be backwards compatible
     *          now. Supporting 23 or higher
     * @return  Boolean if BiometricPrompt is capable of running
     * */
    public static boolean isBiometricPromptEnabled() {
        AppLog.d(TAG, "isBiometricPromptEnabled()");

        Boolean enabled = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;

        if (enabled) {
            AppLog.d("isBiometricPromptEnabled()", "True - Biometric prompt is enabled");
        } else {
            AppLog.d("isBiometricPromptEnabled()", "False - Biometric prompt is NOT enabled");
        }

        return enabled;
    }

    /**
     * Name: isSdkVersionSupported
     * Created by Devin Martinolich 12/4/2019
     * Modified by
     * Purpose: Check if the android version on device is greater than Marshmallow, since
     *          fingerprint authentication is only supported from Android 6.0.
     * Note:    If your project's minSdkversion is 23 or higher, then you won't need to
     *          perform this check.
     * @return  Boolean if the android platform supports fingerprint
     * */
    public static boolean isSdkVersionSupported() {
        AppLog.d(TAG, "isSdkVersionSupported()");

        Boolean supported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;

        if (supported) {
            AppLog.d("isSdkVersionSupported()", "True - SDK version is supported");
        } else {
            AppLog.d("isSdkVersionSupported()", "False - SDK version is NOT supported");
        }

        return supported;
    }

    /**
     * Name: isHardwareSupported
     * Created by Devin Martinolich 12/4/2019
     * Modified by
     * Purpose: Check if the device has fingerprint sensors.
     * Note:    If you marked android.hardware.fingerprint as something that your app requires
     *          (android:required="true"), then you don't need to perform this check.
     * @return  Boolean if the device has a fingerprint reader
     * */
    public static boolean isHardwareSupported(Context context) {
        AppLog.d(TAG, "isHardwareSupported()");

        BiometricManager biometricManager = BiometricManager.from(context);

        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE) {
            AppLog.d("isHardwareSupported()", "False - BIOMETRIC_ERROR_NO_HARDWARE");
        } else if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE) {
            AppLog.d("isHardwareSupported()", "False - BIOMETRIC_ERROR_HW_UNAVAILABLE");
        } else {
            AppLog.d("isHardwareSupported()", "True - Device has fingerprint hardware and can be used.");
        }

        return !(biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE
                || biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE);
    }

    /**
     * Name: isFingerprintAvailable
     * Created by Devin Martinolich 12/4/2019
     * Modified by
     * Purpose: Fingerprint authentication can be matched with a registered fingerprint of the
     *          user. So we need to perform this check in order to enable fingerprint
     *          authentication.
     * @return  Boolean if the user has a fingerprint enrolled on device
     * */
    public static boolean isFingerprintAvailable(Context context) {
        AppLog.d(TAG, "isFingerprintAvailable()");

        BiometricManager biometricManager = BiometricManager.from(context);

        if (biometricManager.canAuthenticate() != BiometricManager.BIOMETRIC_SUCCESS) {
            AppLog.d("isFingerprintAvailable", "False - User has not registered a fingerprint.");
        } else {
            AppLog.d("isFingerprintAvailable", "True - Device has fingerprint registered.");
        }

        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS;
    }

    /**
     * Name: isPermissionGranted
     * Created by Devin Martinolich 12/4/2019
     * Modified by
     * Purpose: Check if the permission has been added to the app. This permission will be
     *          granted as soon as the user installs the app on their device.
     * */
    public static boolean isPermissionGranted(Context context) {
        AppLog.d(TAG, "isPermissionGranted()");

        Boolean granted = ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) ==
                PackageManager.PERMISSION_GRANTED;

        if (granted) {
            AppLog.d("isPermissionGranted()", "True - User has already granted fingerprint hardware permission.");
        } else {
            AppLog.d("isPermissionGranted()", "False - User has NOT granted fingerprint hardware permission.");
        }

        return granted;
    }
}
