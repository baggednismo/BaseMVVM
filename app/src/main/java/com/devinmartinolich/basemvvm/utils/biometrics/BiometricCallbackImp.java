package com.devinmartinolich.basemvvm.utils.biometrics;

import androidx.biometric.BiometricPrompt;

public class BiometricCallbackImp extends BiometricPrompt.AuthenticationCallback {
    private BiometricCallback biometricCallback;

    public BiometricCallbackImp(BiometricCallback biometricCallback) {
        this.biometricCallback = biometricCallback;
    }

    @Override
    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        biometricCallback.onAuthenticationSucceeded();
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
        biometricCallback.onAuthenticationError(errorCode, errString);
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        biometricCallback.onAuthenticationFailed();
    }
}
