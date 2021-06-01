package com.agora.data;

public class Config {

    private static final String PROVIDER_LEANCLOUD = "LeanCloud";
    private static final String PROVIDER_FIREBASE = "FireBase";

    public static boolean isLeanCloud() {
        return PROVIDER_LEANCLOUD.equals(BuildConfig.DATA_PROVIDER);
    }

    public static boolean isFireBase() {
        return PROVIDER_FIREBASE.equals(BuildConfig.DATA_PROVIDER);
    }
}
