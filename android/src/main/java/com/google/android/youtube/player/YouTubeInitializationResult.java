package com.google.android.youtube.player;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import com.google.android.youtube.player.internal.C0417m;
import com.google.android.youtube.player.internal.C0444z;
import com.google.android.youtube.player.internal.ac;

public enum YouTubeInitializationResult {
    SUCCESS,
    INTERNAL_ERROR,
    UNKNOWN_ERROR,
    SERVICE_MISSING,
    SERVICE_VERSION_UPDATE_REQUIRED,
    SERVICE_DISABLED,
    SERVICE_INVALID,
    ERROR_CONNECTING_TO_SERVICE,
    CLIENT_LIBRARY_UPDATE_REQUIRED,
    NETWORK_ERROR,
    DEVELOPER_KEY_INVALID,
    INVALID_APPLICATION_SIGNATURE;

    private static final class C0372a implements OnClickListener {
        private final Activity f139a;
        private final Intent f140b;
        private final int f141c;

        public C0372a(Activity activity, Intent intent, int i) {
            this.f139a = (Activity) ac.m1483a((Object) activity, (Object) "activity cannot be null");
            this.f140b = (Intent) ac.m1483a((Object) intent, (Object) "intent cannot be null");
            this.f141c = ((Integer) ac.m1483a(Integer.valueOf(i), (Object) "requestCode cannot be null")).intValue();
        }

        public final void onClick(DialogInterface dialogInterface, int i) {
            try {
                this.f139a.startActivityForResult(this.f140b, this.f141c);
                dialogInterface.dismiss();
            } catch (ActivityNotFoundException e) {
                Log.e("YouTubeAndroidPlayerAPI", String.format("Can't perform resolution for YouTubeInitalizationError", new Object[]{e}));
            }
        }
    }

    public final Dialog getErrorDialog(Activity activity, int i) {
        return getErrorDialog(activity, i, null);
    }

    public final Dialog getErrorDialog(Activity activity, int i, OnCancelListener onCancelListener) {
        Intent b;
        Builder builder = new Builder(activity);
        if (onCancelListener != null) {
            builder.setOnCancelListener(onCancelListener);
        }
        switch (this) {
            case SERVICE_MISSING:
            case SERVICE_VERSION_UPDATE_REQUIRED:
                b = C0444z.m1737b(C0444z.m1733a((Context) activity));
                break;
            case SERVICE_DISABLED:
                b = C0444z.m1732a(C0444z.m1733a((Context) activity));
                break;
            default:
                b = null;
                break;
        }
        OnClickListener c0372a = new C0372a(activity, b, i);
        C0417m c0417m = new C0417m(activity);
        switch (this) {
            case SERVICE_MISSING:
                return builder.setTitle(c0417m.f200b).setMessage(c0417m.f201c).setPositiveButton(c0417m.f202d, c0372a).create();
            case SERVICE_DISABLED:
                return builder.setTitle(c0417m.f203e).setMessage(c0417m.f204f).setPositiveButton(c0417m.f205g, c0372a).create();
            case SERVICE_VERSION_UPDATE_REQUIRED:
                return builder.setTitle(c0417m.f206h).setMessage(c0417m.f207i).setPositiveButton(c0417m.f208j, c0372a).create();
            default:
                throw new IllegalArgumentException("Unexpected errorReason: " + name());
        }
    }

    public final boolean isUserRecoverableError() {
        switch (this) {
            case SERVICE_MISSING:
            case SERVICE_DISABLED:
            case SERVICE_VERSION_UPDATE_REQUIRED:
                return true;
            default:
                return false;
        }
    }
}
