package com.google.android.youtube.player.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.youtube.player.internal.C0385t.C0375a;
import com.google.android.youtube.player.internal.C0385t.C0377b;
import com.google.android.youtube.player.internal.C0419r.C0430d;

public final class C0420o extends C0419r<C0414l> implements C0386b {
    private final String f222b;
    private final String f223c;
    private final String f224d;
    private boolean f225e;

    public C0420o(Context context, String str, String str2, String str3, C0375a c0375a, C0377b c0377b) {
        super(context, c0375a, c0377b);
        this.f222b = (String) ac.m1483a((Object) str, (Object) "developerKey cannot be null");
        this.f223c = ac.m1484a(str2, (Object) "callingPackage cannot be null or empty");
        this.f224d = ac.m1484a(str3, (Object) "callingAppVersion cannot be null or empty");
    }

    private final void m1666k() {
        m1664i();
        if (this.f225e) {
            throw new IllegalStateException("Connection client has been released");
        }
    }

    public final IBinder mo1318a() {
        m1666k();
        try {
            return ((C0414l) m1665j()).mo1313a();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public final C0411k mo1320a(C0408j c0408j) {
        m1666k();
        try {
            return ((C0414l) m1665j()).mo1314a(c0408j);
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    protected final void mo1321a(C0405i c0405i, C0430d c0430d) throws RemoteException {
        c0405i.mo1304a(c0430d, 1000, this.f223c, this.f224d, this.f222b, null);
    }

    public final void mo1322a(boolean z) {
        if (m1661f()) {
            try {
                ((C0414l) m1665j()).mo1315a(z);
            } catch (RemoteException e) {
            }
            this.f225e = true;
        }
    }

    protected final String mo1323b() {
        return "com.google.android.youtube.player.internal.IYouTubeService";
    }

    protected final String mo1324c() {
        return "com.google.android.youtube.api.service.START";
    }

    public final void mo1316d() {
        if (!this.f225e) {
            mo1322a(true);
        }
        super.mo1316d();
    }
}
