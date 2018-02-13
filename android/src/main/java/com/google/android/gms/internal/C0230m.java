package com.google.android.gms.internal;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class C0230m {
    private static final Uri bV = Uri.parse("http://plus.google.com/");
    private static final Uri bW = bV.buildUpon().appendPath("circles").appendPath("find").build();

    public static Intent m1158i(String str) {
        Uri fromParts = Uri.fromParts("package", str, null);
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(fromParts);
        return intent;
    }

    private static Uri m1159j(String str) {
        return Uri.parse("market://details").buildUpon().appendQueryParameter("id", str).build();
    }

    public static Intent m1160k(String str) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(C0230m.m1159j(str));
        intent.setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE);
        intent.addFlags(AccessibilityEventCompat.TYPE_GESTURE_DETECTION_END);
        return intent;
    }

    public static Intent m1161l(String str) {
        Uri parse = Uri.parse("bazaar://search?q=pname:" + str);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(parse);
        intent.setFlags(AccessibilityEventCompat.TYPE_GESTURE_DETECTION_END);
        return intent;
    }
}
