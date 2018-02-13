package com.parrot.freeflight.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public final class InternetUtils {
    private static final String TAG = InternetUtils.class.getSimpleName();

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isAvailable() && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
