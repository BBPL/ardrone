package com.google.android.youtube.player.internal;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.android.youtube.player.internal.C0408j.C0410a;

public final class C0424p extends C0382a {
    private final Handler f236a = new Handler(Looper.getMainLooper());
    private C0386b f237b;
    private C0411k f238c;
    private boolean f239d;
    private boolean f240e;

    private final class C0423a extends C0410a {
        final /* synthetic */ C0424p f235a;

        private C0423a(C0424p c0424p) {
            this.f235a = c0424p;
        }

        public final void mo1305a(Bitmap bitmap, String str, boolean z, boolean z2) {
            final boolean z3 = z;
            final boolean z4 = z2;
            final Bitmap bitmap2 = bitmap;
            final String str2 = str;
            this.f235a.f236a.post(new Runnable(this) {
                final /* synthetic */ C0423a f230e;

                public final void run() {
                    this.f230e.f235a.f239d = z3;
                    this.f230e.f235a.f240e = z4;
                    this.f230e.f235a.m1465a(bitmap2, str2);
                }
            });
        }

        public final void mo1306a(final String str, final boolean z, final boolean z2) {
            this.f235a.f236a.post(new Runnable(this) {
                final /* synthetic */ C0423a f234d;

                public final void run() {
                    this.f234d.f235a.f239d = z;
                    this.f234d.f235a.f240e = z2;
                    this.f234d.f235a.m1470b(str);
                }
            });
        }
    }

    public C0424p(C0386b c0386b, YouTubeThumbnailView youTubeThumbnailView) {
        super(youTubeThumbnailView);
        this.f237b = (C0386b) ac.m1483a((Object) c0386b, (Object) "connectionClient cannot be null");
        this.f238c = c0386b.mo1320a(new C0423a());
    }

    public final void mo1325a(String str) {
        try {
            this.f238c.mo1308a(str);
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public final void mo1326a(String str, int i) {
        try {
            this.f238c.mo1309a(str, i);
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    protected final boolean mo1327a() {
        return super.mo1327a() && this.f238c != null;
    }

    public final void mo1328b() {
        try {
            this.f238c.mo1307a();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public final void mo1329c() {
        try {
            this.f238c.mo1310b();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public final void mo1330d() {
        try {
            this.f238c.mo1311c();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public final boolean mo1331e() {
        return this.f240e;
    }

    public final boolean mo1332f() {
        return this.f239d;
    }

    public final void mo1333g() {
        try {
            this.f238c.mo1312d();
        } catch (RemoteException e) {
        }
        this.f237b.mo1316d();
        this.f238c = null;
        this.f237b = null;
    }
}
