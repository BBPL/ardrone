package com.google.android.youtube.player;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import com.google.android.youtube.player.internal.C0444z;
import com.parrot.freeflight.activities.SplashActivity;
import java.util.List;

public final class YouTubeIntents {
    private YouTubeIntents() {
    }

    private static Uri m1418a(String str) {
        return Uri.parse("http://www.youtube.com/playlist?list=" + str);
    }

    private static String m1419a(Context context) {
        return C0444z.m1735a(context.getPackageManager()) ? "com.google.android.youtube.googletv" : "com.google.android.youtube";
    }

    private static boolean m1420a(Context context, Intent intent) {
        List queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
        return (queryIntentActivities == null || queryIntentActivities.isEmpty()) ? false : true;
    }

    private static boolean m1421a(Context context, Uri uri) {
        return m1420a(context, new Intent("android.intent.action.VIEW", uri).setPackage(m1419a(context)));
    }

    private static Intent m1422b(Context context, Uri uri) {
        return new Intent("android.intent.action.VIEW", uri).setPackage(m1419a(context));
    }

    public static boolean canResolveOpenPlaylistIntent(Context context) {
        return m1421a(context, Uri.parse("http://www.youtube.com/playlist?list="));
    }

    public static boolean canResolvePlayPlaylistIntent(Context context) {
        int installedYouTubeVersionCode = getInstalledYouTubeVersionCode(context);
        boolean z = C0444z.m1735a(context.getPackageManager()) ? installedYouTubeVersionCode >= Integer.MAX_VALUE : installedYouTubeVersionCode >= SplashActivity.SPLASH_TIME;
        return z && canResolveOpenPlaylistIntent(context);
    }

    public static boolean canResolvePlayVideoIntent(Context context) {
        return m1421a(context, Uri.parse("http://www.youtube.com/watch?v="));
    }

    public static boolean canResolveSearchIntent(Context context) {
        return (!C0444z.m1735a(context.getPackageManager()) || getInstalledYouTubeVersionCode(context) >= Integer.MAX_VALUE) ? m1420a(context, new Intent("android.intent.action.SEARCH").setPackage(m1419a(context))) : false;
    }

    public static boolean canResolveUploadIntent(Context context) {
        return m1420a(context, new Intent("com.google.android.youtube.intent.action.UPLOAD").setPackage(m1419a(context)).setType("video/*"));
    }

    public static boolean canResolveUserIntent(Context context) {
        return m1421a(context, Uri.parse("http://www.youtube.com/user/"));
    }

    public static Intent createOpenPlaylistIntent(Context context, String str) {
        Intent b = m1422b(context, m1418a(str));
        b.putExtra("authenticate", false);
        return b;
    }

    public static Intent createPlayPlaylistIntent(Context context, String str) {
        Intent b = m1422b(context, m1418a(str).buildUpon().appendQueryParameter("playnext", "1").build());
        b.putExtra("authenticate", false);
        return b;
    }

    public static Intent createPlayVideoIntent(Context context, String str) {
        return createPlayVideoIntentWithOptions(context, str, false, false);
    }

    public static Intent createPlayVideoIntentWithOptions(Context context, String str, boolean z, boolean z2) {
        return m1422b(context, Uri.parse("http://www.youtube.com/watch?v=" + str)).putExtra("force_fullscreen", z).putExtra("finish_on_ended", z2);
    }

    public static Intent createSearchIntent(Context context, String str) {
        return new Intent("android.intent.action.SEARCH").setPackage(m1419a(context)).putExtra("query", str);
    }

    public static Intent createUploadIntent(Context context, Uri uri) throws IllegalArgumentException {
        if (uri == null) {
            throw new IllegalArgumentException("videoUri parameter cannot be null.");
        } else if (uri.toString().startsWith("content://media/")) {
            return new Intent("com.google.android.youtube.intent.action.UPLOAD").setPackage(m1419a(context)).setDataAndType(uri, "video/*");
        } else {
            throw new IllegalArgumentException("videoUri parameter must be a URI pointing to a valid video file. It must begin with \"content://media/\"");
        }
    }

    public static Intent createUserIntent(Context context, String str) {
        return m1422b(context, Uri.parse("http://www.youtube.com/user/" + str));
    }

    public static int getInstalledYouTubeVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(m1419a(context), 0).versionCode;
        } catch (NameNotFoundException e) {
            return -1;
        }
    }

    public static String getInstalledYouTubeVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(m1419a(context), 0).versionName;
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    public static boolean isYouTubeInstalled(Context context) {
        try {
            context.getPackageManager().getApplicationInfo(m1419a(context), 0);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public final boolean canResolvePlayVideoIntentWithOptions(Context context) {
        int installedYouTubeVersionCode = getInstalledYouTubeVersionCode(context);
        boolean z = C0444z.m1735a(context.getPackageManager()) ? installedYouTubeVersionCode >= Integer.MAX_VALUE : installedYouTubeVersionCode >= 3300;
        return z && canResolvePlayVideoIntent(context);
    }
}
