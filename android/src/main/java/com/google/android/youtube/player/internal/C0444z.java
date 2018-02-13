package com.google.android.youtube.player.internal;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import com.google.android.gms.common.GooglePlayServicesUtil;

public final class C0444z {
    private static final Uri f262a = Uri.parse("http://play.google.com/store/apps/details");

    public static int m1731a(Context context, Context context2) {
        int i = 0;
        if (context2 != null) {
            i = context2.getResources().getIdentifier("clientTheme", "style", C0444z.m1733a(context));
        }
        return i == 0 ? VERSION.SDK_INT >= 14 ? 16974120 : VERSION.SDK_INT >= 11 ? 16973931 : 16973829 : i;
    }

    public static Intent m1732a(String str) {
        Uri fromParts = Uri.fromParts("package", str, null);
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(fromParts);
        return intent;
    }

    public static String m1733a(Context context) {
        Intent intent = new Intent("com.google.android.youtube.api.service.START");
        PackageManager packageManager = context.getPackageManager();
        ResolveInfo resolveService = packageManager.resolveService(intent, 0);
        return (resolveService == null || resolveService.serviceInfo == null || resolveService.serviceInfo.packageName == null) ? packageManager.hasSystemFeature("com.google.android.tv") ? "com.google.android.youtube.googletv" : "com.google.android.youtube" : resolveService.serviceInfo.packageName;
    }

    public static boolean m1734a(Context context, String str) {
        try {
            Resources resourcesForApplication = context.getPackageManager().getResourcesForApplication(str);
            if (str.equals("com.google.android.youtube.googletvdev")) {
                str = "com.google.android.youtube.googletv";
            }
            int identifier = resourcesForApplication.getIdentifier("youtube_api_version_code", "integer", str);
            return identifier == 0 || 1000 > resourcesForApplication.getInteger(identifier);
        } catch (NameNotFoundException e) {
            return true;
        }
    }

    public static boolean m1735a(PackageManager packageManager) {
        return packageManager.hasSystemFeature("com.google.android.tv");
    }

    public static Context m1736b(Context context) {
        try {
            return context.createPackageContext(C0444z.m1733a(context), 3);
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    public static Intent m1737b(String str) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(f262a.buildUpon().appendQueryParameter("id", str).build());
        intent.setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE);
        intent.addFlags(AccessibilityEventCompat.TYPE_GESTURE_DETECTION_END);
        return intent;
    }

    public static int m1738c(Context context) {
        return C0444z.m1731a(context, C0444z.m1736b(context));
    }

    public static String m1739d(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Throwable e) {
            throw new IllegalStateException("Cannot retrieve calling Context's PackageInfo", e);
        }
    }
}
