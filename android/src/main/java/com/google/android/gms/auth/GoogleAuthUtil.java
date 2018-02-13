package com.google.android.gms.auth;

import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.C0041R;
import com.google.android.gms.common.C0046a;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.C0108a.C0110a;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.http.HttpHeaders;

public final class GoogleAuthUtil {
    public static final String GOOGLE_ACCOUNT_TYPE = "com.google";
    public static final String KEY_ANDROID_PACKAGE_NAME = "androidPackageName";
    public static final String KEY_CALLER_UID = "callerUid";
    public static final String KEY_REQUEST_ACTIONS = "request_visible_actions";
    @Deprecated
    public static final String KEY_REQUEST_VISIBLE_ACTIVITIES = "request_visible_actions";
    public static final String KEY_SUPPRESS_PROGRESS_SCREEN = "suppressProgressScreen";
    private static final ComponentName f17u = new ComponentName(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE, "com.google.android.gms.auth.GetToken");
    private static final ComponentName f18v = new ComponentName(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE, "com.google.android.gms.recovery.RecoveryService");
    private static final Intent f19w = new Intent().setComponent(f17u);
    private static final Intent f20x = new Intent().setComponent(f18v);

    static {
        if (VERSION.SDK_INT >= 11) {
        }
        if (VERSION.SDK_INT >= 14) {
        }
    }

    private GoogleAuthUtil() {
    }

    private static String m8a(Context context, String str, String str2, Bundle bundle) throws IOException, UserRecoverableNotifiedException, GoogleAuthException {
        if (bundle == null) {
            bundle = new Bundle();
        }
        try {
            return getToken(context, str, str2, bundle);
        } catch (GooglePlayServicesAvailabilityException e) {
            int i;
            PendingIntent errorPendingIntent = GooglePlayServicesUtil.getErrorPendingIntent(e.getConnectionStatusCode(), context, 0);
            Resources resources = context.getResources();
            Notification notification = new Notification(17301642, resources.getString(C0041R.string.auth_client_play_services_err_notification_msg), System.currentTimeMillis());
            notification.flags |= 16;
            CharSequence charSequence = context.getApplicationInfo().name;
            if (TextUtils.isEmpty(charSequence)) {
                charSequence = context.getPackageName();
            }
            CharSequence string = resources.getString(C0041R.string.auth_client_requested_by_msg, new Object[]{charSequence});
            switch (e.getConnectionStatusCode()) {
                case 1:
                    i = C0041R.string.auth_client_needs_installation_title;
                    break;
                case 2:
                    i = C0041R.string.auth_client_needs_update_title;
                    break;
                case 3:
                    i = C0041R.string.auth_client_needs_enabling_title;
                    break;
                default:
                    i = C0041R.string.auth_client_using_bad_version_title;
                    break;
            }
            notification.setLatestEventInfo(context, resources.getString(i), string, errorPendingIntent);
            ((NotificationManager) context.getSystemService("notification")).notify(39789, notification);
            throw new UserRecoverableNotifiedException("User intervention required. Notification has been pushed.");
        } catch (UserRecoverableAuthException e2) {
            throw new UserRecoverableNotifiedException("User intervention required. Notification has been pushed.");
        }
    }

    private static void m9a(Context context) throws GooglePlayServicesAvailabilityException, GoogleAuthException {
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (isGooglePlayServicesAvailable != 0) {
            Intent a = GooglePlayServicesUtil.m18a(context, isGooglePlayServicesAvailable, -1);
            String str = "GooglePlayServices not available due to error " + isGooglePlayServicesAvailable;
            Log.e("GoogleAuthUtil", str);
            if (a == null) {
                throw new GoogleAuthException(str);
            }
            throw new GooglePlayServicesAvailabilityException(isGooglePlayServicesAvailable, "GooglePlayServicesNotAvailable", a);
        }
    }

    private static void m10a(Intent intent) {
        if (intent == null) {
            throw new IllegalArgumentException("Callack cannot be null.");
        }
        try {
            Intent.parseUri(intent.toUri(1), 1);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Parameter callback contains invalid data. It must be serializable using toUri() and parseUri().");
        }
    }

    private static boolean m11a(String str) {
        return "NetworkError".equals(str) || "ServiceUnavailable".equals(str) || HttpHeaders.TIMEOUT.equals(str);
    }

    private static void m12b(Context context) {
        Looper myLooper = Looper.myLooper();
        if (myLooper != null && myLooper == context.getMainLooper()) {
            Throwable illegalStateException = new IllegalStateException("calling this from your main thread can lead to deadlock");
            Log.e("GoogleAuthUtil", "Calling this from your main thread can lead to deadlock and/or ANRs", illegalStateException);
            throw illegalStateException;
        }
    }

    private static boolean m13b(String str) {
        return "BadAuthentication".equals(str) || "CaptchaRequired".equals(str) || "DeviceManagementRequiredOrSyncDisabled".equals(str) || "NeedPermission".equals(str) || "NeedsBrowser".equals(str) || "UserCancel".equals(str) || "AppDownloadRequired".equals(str);
    }

    public static String getToken(Context context, String str, String str2) throws IOException, UserRecoverableAuthException, GoogleAuthException {
        return getToken(context, str, str2, new Bundle());
    }

    public static String getToken(Context context, String str, String str2, Bundle bundle) throws IOException, UserRecoverableAuthException, GoogleAuthException {
        Context applicationContext = context.getApplicationContext();
        m12b(applicationContext);
        m9a(applicationContext);
        Bundle bundle2 = bundle == null ? new Bundle() : new Bundle(bundle);
        if (!bundle2.containsKey(KEY_ANDROID_PACKAGE_NAME)) {
            bundle2.putString(KEY_ANDROID_PACKAGE_NAME, context.getPackageName());
        }
        ServiceConnection c0046a = new C0046a();
        if (context.bindService(f19w, c0046a, 1)) {
            try {
                bundle2 = C0110a.m248a(c0046a.m32e()).mo473a(str, str2, bundle2);
                Object string = bundle2.getString("authtoken");
                if (TextUtils.isEmpty(string)) {
                    String string2 = bundle2.getString("Error");
                    Intent intent = (Intent) bundle2.getParcelable("userRecoveryIntent");
                    if (m13b(string2)) {
                        throw new UserRecoverableAuthException(string2, intent);
                    } else if (m11a(string2)) {
                        throw new IOException(string2);
                    } else {
                        throw new GoogleAuthException(string2);
                    }
                }
                context.unbindService(c0046a);
                return string;
            } catch (Throwable e) {
                Log.i("GoogleAuthUtil", "GMS remote exception ", e);
                throw new IOException("remote exception");
            } catch (InterruptedException e2) {
                throw new GoogleAuthException("Interrupted");
            } catch (Throwable th) {
                context.unbindService(c0046a);
            }
        } else {
            throw new UserRecoverableAuthException("AppDownloadRequired", null);
        }
    }

    public static String getTokenWithNotification(Context context, String str, String str2, Bundle bundle) throws IOException, UserRecoverableNotifiedException, GoogleAuthException {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putBoolean("handle_notification", true);
        return m8a(context, str, str2, bundle);
    }

    public static String getTokenWithNotification(Context context, String str, String str2, Bundle bundle, Intent intent) throws IOException, UserRecoverableNotifiedException, GoogleAuthException {
        m10a(intent);
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putParcelable("callback_intent", intent);
        bundle.putBoolean("handle_notification", true);
        return m8a(context, str, str2, bundle);
    }

    public static String getTokenWithNotification(Context context, String str, String str2, Bundle bundle, String str3, Bundle bundle2) throws IOException, UserRecoverableNotifiedException, GoogleAuthException {
        if (TextUtils.isEmpty(str3)) {
            throw new IllegalArgumentException("Authority cannot be empty or null.");
        }
        if (bundle == null) {
            bundle = new Bundle();
        }
        if (bundle2 == null) {
            bundle2 = new Bundle();
        }
        ContentResolver.validateSyncExtrasBundle(bundle2);
        bundle.putString("authority", str3);
        bundle.putBundle("sync_extras", bundle2);
        bundle.putBoolean("handle_notification", true);
        return m8a(context, str, str2, bundle);
    }

    public static void invalidateToken(Context context, String str) {
        AccountManager.get(context).invalidateAuthToken("com.google", str);
    }
}
