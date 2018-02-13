package com.google.android.gms.internal;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;

public class C0220i implements OnClickListener {
    private final Activity bm;
    private final int bn;
    private final Intent mIntent;

    public C0220i(Activity activity, Intent intent, int i) {
        this.bm = activity;
        this.mIntent = intent;
        this.bn = i;
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        try {
            if (this.mIntent != null) {
                this.bm.startActivityForResult(this.mIntent, this.bn);
            }
            dialogInterface.dismiss();
        } catch (ActivityNotFoundException e) {
            Log.e("SettingsRedirect", "Can't redirect to app settings for Google Play services");
        }
    }
}
