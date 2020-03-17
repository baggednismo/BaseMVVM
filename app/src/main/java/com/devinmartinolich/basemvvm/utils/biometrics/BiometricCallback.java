package com.devinmartinolich.basemvvm.utils.biometrics;

public interface BiometricCallback {
    void onAuthenticationFailed();

    void onAuthenticationCanceled();

    void onAuthenticationSucceeded();

    void onAuthenticationError(int errorCode, CharSequence errString);
}
