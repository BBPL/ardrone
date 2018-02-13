package com.google.android.youtube.player.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.internal.C0385t.C0375a;
import com.google.android.youtube.player.internal.C0385t.C0377b;
import com.google.android.youtube.player.internal.C0387c.C0389a;
import com.google.android.youtube.player.internal.C0405i.C0407a;
import com.parrot.ardronetool.vlib.VideoCodecType;
import java.util.ArrayList;

public abstract class C0419r<T extends IInterface> implements C0385t {
    final Handler f211a;
    private final Context f212b;
    private T f213c;
    private ArrayList<C0375a> f214d;
    private final ArrayList<C0375a> f215e = new ArrayList();
    private boolean f216f = false;
    private ArrayList<C0377b> f217g;
    private boolean f218h = false;
    private final ArrayList<C0428b<?>> f219i = new ArrayList();
    private ServiceConnection f220j;
    private boolean f221k = false;

    final class C0427a extends Handler {
        final /* synthetic */ C0419r f242a;

        C0427a(C0419r c0419r) {
            this.f242a = c0419r;
        }

        public final void handleMessage(Message message) {
            if (message.what == 3) {
                this.f242a.m1654a((YouTubeInitializationResult) message.obj);
            } else if (message.what == 4) {
                synchronized (this.f242a.f214d) {
                    if (this.f242a.f221k && this.f242a.m1661f() && this.f242a.f214d.contains(message.obj)) {
                        ((C0375a) message.obj).mo1224a();
                    }
                }
            } else if (message.what == 2 && !this.f242a.m1661f()) {
            } else {
                if (message.what == 2 || message.what == 1) {
                    ((C0428b) message.obj).m1689a();
                }
            }
        }
    }

    protected abstract class C0428b<TListener> {
        final /* synthetic */ C0419r f243a;
        private TListener f244b;

        public C0428b(C0419r c0419r, TListener tListener) {
            this.f243a = c0419r;
            this.f244b = tListener;
            synchronized (c0419r.f219i) {
                c0419r.f219i.add(this);
            }
        }

        public final void m1689a() {
            Object obj;
            synchronized (this) {
                obj = this.f244b;
            }
            mo1334a(obj);
        }

        protected abstract void mo1334a(TListener tListener);

        public final void m1691b() {
            synchronized (this) {
                this.f244b = null;
            }
        }
    }

    protected final class C0429c extends C0428b<Boolean> {
        public final YouTubeInitializationResult f245b;
        public final IBinder f246c;
        final /* synthetic */ C0419r f247d;

        public C0429c(C0419r c0419r, String str, IBinder iBinder) {
            this.f247d = c0419r;
            super(c0419r, Boolean.valueOf(true));
            this.f245b = C0419r.m1646b(str);
            this.f246c = iBinder;
        }

        protected final /* synthetic */ void mo1334a(Object obj) {
            if (((Boolean) obj) != null) {
                switch (this.f245b) {
                    case SUCCESS:
                        try {
                            if (this.f247d.mo1323b().equals(this.f246c.getInterfaceDescriptor())) {
                                this.f247d.f213c = this.f247d.mo1319a(this.f246c);
                                if (this.f247d.f213c != null) {
                                    this.f247d.m1662g();
                                    return;
                                }
                            }
                        } catch (RemoteException e) {
                        }
                        this.f247d.f212b.unbindService(this.f247d.f220j);
                        this.f247d.f220j = null;
                        this.f247d.f213c = null;
                        this.f247d.m1654a(YouTubeInitializationResult.INTERNAL_ERROR);
                        return;
                    default:
                        this.f247d.m1654a(this.f245b);
                        return;
                }
            }
        }
    }

    protected final class C0430d extends C0389a {
        final /* synthetic */ C0419r f248a;

        protected C0430d(C0419r c0419r) {
            this.f248a = c0419r;
        }

        public final void mo1245a(String str, IBinder iBinder) {
            this.f248a.f211a.sendMessage(this.f248a.f211a.obtainMessage(1, new C0429c(this.f248a, str, iBinder)));
        }
    }

    final class C0431e implements ServiceConnection {
        final /* synthetic */ C0419r f249a;

        C0431e(C0419r c0419r) {
            this.f249a = c0419r;
        }

        public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            this.f249a.m1657b(iBinder);
        }

        public final void onServiceDisconnected(ComponentName componentName) {
            this.f249a.f213c = null;
            this.f249a.m1663h();
        }
    }

    protected C0419r(Context context, C0375a c0375a, C0377b c0377b) {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            throw new IllegalStateException("Clients must be created on the UI thread.");
        }
        this.f212b = (Context) ac.m1482a((Object) context);
        this.f214d = new ArrayList();
        this.f214d.add(ac.m1482a((Object) c0375a));
        this.f217g = new ArrayList();
        this.f217g.add(ac.m1482a((Object) c0377b));
        this.f211a = new C0427a(this);
    }

    private static YouTubeInitializationResult m1646b(String str) {
        try {
            return YouTubeInitializationResult.valueOf(str);
        } catch (IllegalArgumentException e) {
            return YouTubeInitializationResult.UNKNOWN_ERROR;
        } catch (NullPointerException e2) {
            return YouTubeInitializationResult.UNKNOWN_ERROR;
        }
    }

    protected abstract T mo1319a(IBinder iBinder);

    protected final void m1654a(YouTubeInitializationResult youTubeInitializationResult) {
        this.f211a.removeMessages(4);
        synchronized (this.f217g) {
            this.f218h = true;
            ArrayList arrayList = this.f217g;
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                if (this.f221k) {
                    if (this.f217g.contains(arrayList.get(i))) {
                        ((C0377b) arrayList.get(i)).mo1226a(youTubeInitializationResult);
                    }
                    i++;
                } else {
                    return;
                }
            }
            this.f218h = false;
        }
    }

    protected abstract void mo1321a(C0405i c0405i, C0430d c0430d) throws RemoteException;

    protected abstract String mo1323b();

    protected final void m1657b(IBinder iBinder) {
        try {
            mo1321a(C0407a.m1615a(iBinder), new C0430d(this));
        } catch (RemoteException e) {
            Log.w("YouTubeClient", "service died");
        }
    }

    protected abstract String mo1324c();

    public void mo1316d() {
        m1663h();
        this.f221k = false;
        synchronized (this.f219i) {
            int size = this.f219i.size();
            for (int i = 0; i < size; i++) {
                ((C0428b) this.f219i.get(i)).m1691b();
            }
            this.f219i.clear();
        }
        this.f213c = null;
        if (this.f220j != null) {
            this.f212b.unbindService(this.f220j);
            this.f220j = null;
        }
    }

    public final void mo1317e() {
        this.f221k = true;
        YouTubeInitializationResult isYouTubeApiServiceAvailable = YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this.f212b);
        if (isYouTubeApiServiceAvailable != YouTubeInitializationResult.SUCCESS) {
            this.f211a.sendMessage(this.f211a.obtainMessage(3, isYouTubeApiServiceAvailable));
            return;
        }
        Intent intent = new Intent(mo1324c());
        if (this.f220j != null) {
            Log.e("YouTubeClient", "Calling connect() while still connected, missing disconnect().");
            this.f213c = null;
            this.f212b.unbindService(this.f220j);
        }
        this.f220j = new C0431e(this);
        if (!this.f212b.bindService(intent, this.f220j, VideoCodecType.H264_360P_CODEC)) {
            this.f211a.sendMessage(this.f211a.obtainMessage(3, YouTubeInitializationResult.ERROR_CONNECTING_TO_SERVICE));
        }
    }

    public final boolean m1661f() {
        return this.f213c != null;
    }

    protected final void m1662g() {
        boolean z = true;
        synchronized (this.f214d) {
            ac.m1485a(!this.f216f);
            this.f211a.removeMessages(4);
            this.f216f = true;
            if (this.f215e.size() != 0) {
                z = false;
            }
            ac.m1485a(z);
            ArrayList arrayList = this.f214d;
            int size = arrayList.size();
            for (int i = 0; i < size && this.f221k && m1661f(); i++) {
                this.f215e.size();
                if (!this.f215e.contains(arrayList.get(i))) {
                    ((C0375a) arrayList.get(i)).mo1224a();
                }
            }
            this.f215e.clear();
            this.f216f = false;
        }
    }

    protected final void m1663h() {
        this.f211a.removeMessages(4);
        synchronized (this.f214d) {
            this.f216f = true;
            ArrayList arrayList = this.f214d;
            int size = arrayList.size();
            for (int i = 0; i < size && this.f221k; i++) {
                if (this.f214d.contains(arrayList.get(i))) {
                    ((C0375a) arrayList.get(i)).mo1225b();
                }
            }
            this.f216f = false;
        }
    }

    protected final void m1664i() {
        if (!m1661f()) {
            throw new IllegalStateException("Not connected. Call connect() and wait for onConnected() to be called.");
        }
    }

    protected final T m1665j() {
        m1664i();
        return this.f213c;
    }
}
