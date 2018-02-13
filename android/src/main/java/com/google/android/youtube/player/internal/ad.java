package com.google.android.youtube.player.internal;

import android.app.Activity;
import android.content.Context;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.android.youtube.player.internal.C0385t.C0375a;
import com.google.android.youtube.player.internal.C0385t.C0377b;
import com.google.android.youtube.player.internal.C0442w.C0441a;

public final class ad extends ab {
    public final YouTubeThumbnailLoader mo1242a(C0386b c0386b, YouTubeThumbnailView youTubeThumbnailView) {
        return new C0424p(c0386b, youTubeThumbnailView);
    }

    public final C0386b mo1243a(Context context, String str, C0375a c0375a, C0377b c0377b) {
        return new C0420o(context, str, context.getPackageName(), C0444z.m1739d(context), c0375a, c0377b);
    }

    public final C0390d mo1244a(Activity activity, C0386b c0386b) throws C0441a {
        return C0442w.m1728a(activity, c0386b.mo1318a());
    }
}
