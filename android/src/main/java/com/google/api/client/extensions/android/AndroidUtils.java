package com.google.api.client.extensions.android;

import android.os.Build.VERSION;
import com.google.api.client.util.Preconditions;

public class AndroidUtils {
    private AndroidUtils() {
    }

    public static void checkMinimumSdkLevel(int i) {
        Preconditions.checkArgument(isMinimumSdkLevel(i), "running on Android SDK level %s but requires minimum %s", Integer.valueOf(VERSION.SDK_INT), Integer.valueOf(i));
    }

    public static boolean isMinimumSdkLevel(int i) {
        return VERSION.SDK_INT >= i;
    }
}
