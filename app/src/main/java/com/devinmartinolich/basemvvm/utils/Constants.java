package com.devinmartinolich.basemvvm.utils;

public class Constants {
    public static class SharedPrefName {
        public static final String APP_PREFS = "AppPrefs";
    }

    public static class SharedPrefKeys {
        public static final String LOGGED_IN = "LOGGED_IN";
        public static final String AUTH_TOKEN = "AUTH_TOKEN";
        public static final String USERNAME = "USERNAME";
        public static final String REMEMBER_USERNAME = "REMEMBER_USERNAME";
        public static final String FINGERPRINT = "FINGERPRINT";
        public static final String PASSWORD = "PASSWORD";
        public static final String IVPASS = "IVPASS";
    }

    public static class CipherKeys {
    }

    public static class RequestCodes {
        public static final int REQUEST_PERMISSIONS_FINGERPRINT = 101;
        public static final int REQUEST_PERMISSIONS_EXTERNAL_STORAGE = 102;
        public static final int REQUEST_PERMISSIONS_CAMERA = 103;
        public static final int REQUEST_IMAGE_CAPTURE = 104;
        public static final int LOGOUT = 105;
        public static final int DIALOG_POSITIVE_BTN = 106;
        public static final int DIALOG_NEGATIVE_BTN = 107;
    }

    public static class DefaultValues {
        public static final int FRAGMENT_LOAD_DELAY = 1000;
        public static final int DEFAULT_TIMEOUT_MILLISECONDS = 30 * 60000; //30 minutes in milliseconds
        public static final int DEFAULT_API_CONNECTION_TIMEOUT_MILLISECONDS = 2 * 60000; //2 minutes in milliseconds
        public static final int DEFAULT_API_READ_TIMEOUT_MILLISECONDS = 2 * 60000; //2 minutes in milliseconds
        public static final int DEFAULT_API_WRITE_TIMEOUT_MILLISECONDS = 2 * 60000; //2 minutes in milliseconds
        public static final int DEFAULT_IMAGE_COMPRESSION_QUALITY = 100;
        public static final int DEFAULT_PHOTO_WIDTH = 1024;
    }

    public static class BundleKeys {
        public static final String DIALOG_TAG_SUCCESS = "DIALOG_SUCCESSFUL";
        public static final String DIALOG_TAG_FAILURE = "DIALOG_FAILURE";
    }

    public static class AnalyticsValues {
        public static final String LOGIN_MANUAL = "LOGIN_MANUAL";
        public static final String LOGIN_FINGERPRINT = "LOGIN_FINGERPRINT";
        public static final String LOGIN_SUCCESS = "LOGIN_SUCCESS";
    }

    public static class AnalyticsKeys {
    }

    public static class AnalyticsEvents {
    }
}
