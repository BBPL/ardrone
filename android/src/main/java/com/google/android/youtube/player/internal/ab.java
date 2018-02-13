package com.google.android.youtube.player.internal;

import android.app.Activity;
import android.content.Context;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.android.youtube.player.internal.C0385t.C0375a;
import com.google.android.youtube.player.internal.C0385t.C0377b;
import com.google.android.youtube.player.internal.C0442w.C0441a;

public abstract class ab {
    private static final ab f188a = m1478b();

    public static ab m1477a() {
        return f188a;
    }

    private static ab m1478b() {
        try {
            return (ab) Class.forName("com.google.android.youtube.api.locallylinked.LocallyLinkedFactory").asSubclass(ab.class).newInstance();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        } catch (Throwable e2) {
            throw new IllegalStateException(e2);
        } catch (ClassNotFoundException e3) {
            return new ad();
        }
    }

    public abstract YouTubeThumbnailLoader mo1242a(C0386b c0386b, YouTubeThumbnailView youTubeThumbnailView);

    public abstract C0386b mo1243a(Context context, String str, C0375a c0375a, C0377b c0377b);

    public abstract C0390d mo1244a(Activity activity, C0386b c0386b) throws C0441a;
}
