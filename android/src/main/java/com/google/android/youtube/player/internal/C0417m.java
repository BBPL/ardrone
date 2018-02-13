package com.google.android.youtube.player.internal;

import android.content.Context;
import android.content.res.Resources;
import java.util.Locale;
import java.util.Map;

public final class C0417m {
    public final String f199a;
    public final String f200b;
    public final String f201c;
    public final String f202d;
    public final String f203e;
    public final String f204f;
    public final String f205g;
    public final String f206h;
    public final String f207i;
    public final String f208j;

    public C0417m(Context context) {
        Resources resources = context.getResources();
        Locale locale = (resources == null || resources.getConfiguration() == null || resources.getConfiguration().locale == null) ? Locale.getDefault() : resources.getConfiguration().locale;
        Map a = C0443x.m1729a(locale);
        this.f199a = (String) a.get("error_initializing_player");
        this.f200b = (String) a.get("get_youtube_app_title");
        this.f201c = (String) a.get("get_youtube_app_text");
        this.f202d = (String) a.get("get_youtube_app_action");
        this.f203e = (String) a.get("enable_youtube_app_title");
        this.f204f = (String) a.get("enable_youtube_app_text");
        this.f205g = (String) a.get("enable_youtube_app_action");
        this.f206h = (String) a.get("update_youtube_app_title");
        this.f207i = (String) a.get("update_youtube_app_text");
        this.f208j = (String) a.get("update_youtube_app_action");
    }
}
