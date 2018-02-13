package com.google.android.gms.internal;

import android.content.Intent;
import android.net.Uri;
import com.google.android.gms.common.GooglePlayServicesUtil;
import org.apache.http.cookie.ClientCookie;

public final class aw {
    public static final Intent m560b(Intent intent) {
        intent.setData(Uri.fromParts(ClientCookie.VERSION_ATTR, Integer.toString(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE), null));
        return intent;
    }
}
