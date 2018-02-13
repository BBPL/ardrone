package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.location.C0160a;
import com.google.android.gms.location.C0160a.C0161a;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import java.util.HashMap;

public class bg {
    private final bk<bf> fG;
    private ContentProviderClient fH = null;
    private boolean fI = false;
    private HashMap<LocationListener, C0162b> fJ = new HashMap();
    private final ContentResolver mContentResolver;

    private static class C0159a extends Handler {
        private final LocationListener fK;

        public C0159a(LocationListener locationListener) {
            this.fK = locationListener;
        }

        public C0159a(LocationListener locationListener, Looper looper) {
            super(looper);
            this.fK = locationListener;
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    this.fK.onLocationChanged(new Location((Location) message.obj));
                    return;
                default:
                    Log.e("LocationClientHelper", "unknown message in LocationHandler.handleMessage");
                    return;
            }
        }
    }

    private static class C0162b extends C0161a {
        private Handler fL;

        C0162b(LocationListener locationListener, Looper looper) {
            this.fL = looper == null ? new C0159a(locationListener) : new C0159a(locationListener, looper);
        }

        public void onLocationChanged(Location location) {
            if (this.fL == null) {
                Log.e("LocationClientHelper", "Received a location in client after calling removeLocationUpdates.");
                return;
            }
            Message obtain = Message.obtain();
            obtain.what = 1;
            obtain.obj = location;
            this.fL.sendMessage(obtain);
        }

        public void release() {
            this.fL = null;
        }
    }

    public bg(Context context, bk<bf> bkVar) {
        this.fG = bkVar;
        this.mContentResolver = context.getContentResolver();
    }

    public void aR() {
        if (this.fI) {
            setMockMode(false);
        }
    }

    public Location getLastLocation() {
        this.fG.mo652B();
        try {
            return ((bf) this.fG.mo653C()).aQ();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void removeAllListeners() {
        try {
            synchronized (this.fJ) {
                for (C0160a c0160a : this.fJ.values()) {
                    if (c0160a != null) {
                        ((bf) this.fG.mo653C()).mo644a(c0160a);
                    }
                }
                this.fJ.clear();
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void removeLocationUpdates(PendingIntent pendingIntent) {
        this.fG.mo652B();
        try {
            ((bf) this.fG.mo653C()).mo639a(pendingIntent);
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void removeLocationUpdates(LocationListener locationListener) {
        this.fG.mo652B();
        C0242s.m1205b((Object) locationListener, (Object) "Invalid null listener");
        synchronized (this.fJ) {
            C0160a c0160a = (C0162b) this.fJ.remove(locationListener);
            if (this.fH != null && this.fJ.isEmpty()) {
                this.fH.release();
                this.fH = null;
            }
            if (c0160a != null) {
                c0160a.release();
                try {
                    ((bf) this.fG.mo653C()).mo644a(c0160a);
                } catch (Throwable e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }

    public void requestLocationUpdates(LocationRequest locationRequest, PendingIntent pendingIntent) {
        this.fG.mo652B();
        try {
            ((bf) this.fG.mo653C()).mo642a(locationRequest, pendingIntent);
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void requestLocationUpdates(LocationRequest locationRequest, LocationListener locationListener, Looper looper) {
        this.fG.mo652B();
        if (looper == null) {
            C0242s.m1205b(Looper.myLooper(), (Object) "Can't create handler inside thread that has not called Looper.prepare()");
        }
        synchronized (this.fJ) {
            C0160a c0162b;
            C0162b c0162b2 = (C0162b) this.fJ.get(locationListener);
            if (c0162b2 == null) {
                c0162b = new C0162b(locationListener, looper);
            } else {
                Object obj = c0162b2;
            }
            this.fJ.put(locationListener, c0162b);
            try {
                ((bf) this.fG.mo653C()).mo643a(locationRequest, c0162b);
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public void setMockLocation(Location location) {
        this.fG.mo652B();
        try {
            ((bf) this.fG.mo653C()).setMockLocation(location);
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void setMockMode(boolean z) {
        this.fG.mo652B();
        try {
            ((bf) this.fG.mo653C()).setMockMode(z);
            this.fI = z;
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }
}
