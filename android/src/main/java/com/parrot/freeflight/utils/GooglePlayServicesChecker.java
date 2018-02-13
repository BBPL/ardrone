package com.parrot.freeflight.utils;

import android.app.Activity;
import android.widget.Toast;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class GooglePlayServicesChecker {
    public static boolean check(Activity activity, int i) {
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (isGooglePlayServicesAvailable == 0) {
            return true;
        }
        if (GooglePlayServicesUtil.isUserRecoverableError(isGooglePlayServicesAvailable)) {
            GooglePlayServicesUtil.getErrorDialog(isGooglePlayServicesAvailable, activity, i).show();
        } else {
            Toast.makeText(activity, "No Google Play Services available", 1).show();
        }
        return false;
    }
}
