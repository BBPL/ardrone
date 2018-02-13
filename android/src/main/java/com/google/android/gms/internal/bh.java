package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.C0145k.C0117b;
import com.google.android.gms.internal.C0145k.C0224d;
import com.google.android.gms.internal.be.C0156a;
import com.google.android.gms.internal.bf.C0158a;
import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;
import com.google.android.gms.location.LocationClient.OnRemoveGeofencesResultListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationStatusCodes;
import java.util.List;

public class bh extends C0145k<bf> {
    private final bk<bf> fG = new C0166c();
    private final bg fM;
    private final String fN;

    private final class C0164a extends C0117b<OnAddGeofencesResultListener> {
        private final String[] fO;
        final /* synthetic */ bh fP;
        private final int f66p;

        public C0164a(bh bhVar, OnAddGeofencesResultListener onAddGeofencesResultListener, int i, String[] strArr) {
            this.fP = bhVar;
            super(bhVar, onAddGeofencesResultListener);
            this.f66p = LocationStatusCodes.m1244O(i);
            this.fO = strArr;
        }

        protected void m783a(OnAddGeofencesResultListener onAddGeofencesResultListener) {
            if (onAddGeofencesResultListener != null) {
                onAddGeofencesResultListener.onAddGeofencesResult(this.f66p, this.fO);
            }
        }

        protected void mo526d() {
        }
    }

    private static final class C0165b extends C0156a {
        private OnAddGeofencesResultListener fQ;
        private OnRemoveGeofencesResultListener fR;
        private bh fS;

        public C0165b(OnAddGeofencesResultListener onAddGeofencesResultListener, bh bhVar) {
            this.fQ = onAddGeofencesResultListener;
            this.fR = null;
            this.fS = bhVar;
        }

        public C0165b(OnRemoveGeofencesResultListener onRemoveGeofencesResultListener, bh bhVar) {
            this.fR = onRemoveGeofencesResultListener;
            this.fQ = null;
            this.fS = bhVar;
        }

        public void onAddGeofencesResult(int i, String[] strArr) throws RemoteException {
            if (this.fS == null) {
                Log.wtf("LocationClientImpl", "onAddGeofenceResult called multiple times");
                return;
            }
            bh bhVar = this.fS;
            bh bhVar2 = this.fS;
            bhVar2.getClass();
            bhVar.m528a(new C0164a(bhVar2, this.fQ, i, strArr));
            this.fS = null;
            this.fQ = null;
            this.fR = null;
        }

        public void onRemoveGeofencesByPendingIntentResult(int i, PendingIntent pendingIntent) {
            if (this.fS == null) {
                Log.wtf("LocationClientImpl", "onRemoveGeofencesByPendingIntentResult called multiple times");
                return;
            }
            bh bhVar = this.fS;
            bh bhVar2 = this.fS;
            bhVar2.getClass();
            bhVar.m528a(new C0167d(bhVar2, 1, this.fR, i, pendingIntent));
            this.fS = null;
            this.fQ = null;
            this.fR = null;
        }

        public void onRemoveGeofencesByRequestIdsResult(int i, String[] strArr) {
            if (this.fS == null) {
                Log.wtf("LocationClientImpl", "onRemoveGeofencesByRequestIdsResult called multiple times");
                return;
            }
            bh bhVar = this.fS;
            bh bhVar2 = this.fS;
            bhVar2.getClass();
            bhVar.m528a(new C0167d(bhVar2, 2, this.fR, i, strArr));
            this.fS = null;
            this.fQ = null;
            this.fR = null;
        }
    }

    private final class C0166c implements bk<bf> {
        final /* synthetic */ bh fP;

        private C0166c(bh bhVar) {
            this.fP = bhVar;
        }

        public void mo652B() {
            this.fP.m524B();
        }

        public /* synthetic */ IInterface mo653C() {
            return aS();
        }

        public bf aS() {
            return (bf) this.fP.m525C();
        }
    }

    private final class C0167d extends C0117b<OnRemoveGeofencesResultListener> {
        private final String[] fO;
        final /* synthetic */ bh fP;
        private final int fT;
        private final PendingIntent mPendingIntent;
        private final int f67p;

        public C0167d(bh bhVar, int i, OnRemoveGeofencesResultListener onRemoveGeofencesResultListener, int i2, PendingIntent pendingIntent) {
            boolean z = true;
            this.fP = bhVar;
            super(bhVar, onRemoveGeofencesResultListener);
            if (i != 1) {
                z = false;
            }
            C0219h.m1132a(z);
            this.fT = i;
            this.f67p = LocationStatusCodes.m1244O(i2);
            this.mPendingIntent = pendingIntent;
            this.fO = null;
        }

        public C0167d(bh bhVar, int i, OnRemoveGeofencesResultListener onRemoveGeofencesResultListener, int i2, String[] strArr) {
            this.fP = bhVar;
            super(bhVar, onRemoveGeofencesResultListener);
            C0219h.m1132a(i == 2);
            this.fT = i;
            this.f67p = LocationStatusCodes.m1244O(i2);
            this.fO = strArr;
            this.mPendingIntent = null;
        }

        protected void m790a(OnRemoveGeofencesResultListener onRemoveGeofencesResultListener) {
            if (onRemoveGeofencesResultListener != null) {
                switch (this.fT) {
                    case 1:
                        onRemoveGeofencesResultListener.onRemoveGeofencesByPendingIntentResult(this.f67p, this.mPendingIntent);
                        return;
                    case 2:
                        onRemoveGeofencesResultListener.onRemoveGeofencesByRequestIdsResult(this.f67p, this.fO);
                        return;
                    default:
                        Log.wtf("LocationClientImpl", "Unsupported action: " + this.fT);
                        return;
                }
            }
        }

        protected void mo526d() {
        }
    }

    public bh(Context context, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, String str) {
        super(context, connectionCallbacks, onConnectionFailedListener, new String[0]);
        this.fM = new bg(context, this.fG);
        this.fN = str;
    }

    protected void mo534a(C0233p c0233p, C0224d c0224d) throws RemoteException {
        Bundle bundle = new Bundle();
        bundle.putString("client_name", this.fN);
        c0233p.mo968e(c0224d, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, getContext().getPackageName(), bundle);
    }

    public void addGeofences(List<bi> list, PendingIntent pendingIntent, OnAddGeofencesResultListener onAddGeofencesResultListener) {
        m524B();
        boolean z = list != null && list.size() > 0;
        C0242s.m1206b(z, (Object) "At least one geofence must be specified.");
        C0242s.m1205b((Object) pendingIntent, (Object) "PendingIntent must be specified.");
        C0242s.m1205b((Object) onAddGeofencesResultListener, (Object) "OnAddGeofencesResultListener not provided.");
        if (onAddGeofencesResultListener == null) {
            be beVar = null;
        } else {
            Object c0165b = new C0165b(onAddGeofencesResultListener, this);
        }
        try {
            ((bf) m525C()).mo645a(list, pendingIntent, beVar, getContext().getPackageName());
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    protected String mo536b() {
        return "com.google.android.location.internal.GoogleLocationManagerService.START";
    }

    protected /* synthetic */ IInterface mo537c(IBinder iBinder) {
        return m799s(iBinder);
    }

    protected String mo538c() {
        return "com.google.android.gms.location.internal.IGoogleLocationManagerService";
    }

    public void disconnect() {
        synchronized (this.fM) {
            if (isConnected()) {
                this.fM.removeAllListeners();
                this.fM.aR();
            }
            super.disconnect();
        }
    }

    public Location getLastLocation() {
        return this.fM.getLastLocation();
    }

    public void removeActivityUpdates(PendingIntent pendingIntent) {
        m524B();
        C0242s.m1208d(pendingIntent);
        try {
            ((bf) m525C()).removeActivityUpdates(pendingIntent);
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void removeGeofences(PendingIntent pendingIntent, OnRemoveGeofencesResultListener onRemoveGeofencesResultListener) {
        m524B();
        C0242s.m1205b((Object) pendingIntent, (Object) "PendingIntent must be specified.");
        C0242s.m1205b((Object) onRemoveGeofencesResultListener, (Object) "OnRemoveGeofencesResultListener not provided.");
        if (onRemoveGeofencesResultListener == null) {
            be beVar = null;
        } else {
            Object c0165b = new C0165b(onRemoveGeofencesResultListener, this);
        }
        try {
            ((bf) m525C()).mo640a(pendingIntent, beVar, getContext().getPackageName());
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void removeGeofences(List<String> list, OnRemoveGeofencesResultListener onRemoveGeofencesResultListener) {
        m524B();
        boolean z = list != null && list.size() > 0;
        C0242s.m1206b(z, (Object) "geofenceRequestIds can't be null nor empty.");
        C0242s.m1205b((Object) onRemoveGeofencesResultListener, (Object) "OnRemoveGeofencesResultListener not provided.");
        String[] strArr = (String[]) list.toArray(new String[0]);
        if (onRemoveGeofencesResultListener == null) {
            be beVar = null;
        } else {
            Object c0165b = new C0165b(onRemoveGeofencesResultListener, this);
        }
        try {
            ((bf) m525C()).mo646a(strArr, beVar, getContext().getPackageName());
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void removeLocationUpdates(PendingIntent pendingIntent) {
        this.fM.removeLocationUpdates(pendingIntent);
    }

    public void removeLocationUpdates(LocationListener locationListener) {
        this.fM.removeLocationUpdates(locationListener);
    }

    public void requestActivityUpdates(long j, PendingIntent pendingIntent) {
        boolean z = true;
        m524B();
        C0242s.m1208d(pendingIntent);
        if (j < 0) {
            z = false;
        }
        C0242s.m1206b(z, (Object) "detectionIntervalMillis must be >= 0");
        try {
            ((bf) m525C()).mo638a(j, true, pendingIntent);
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void requestLocationUpdates(LocationRequest locationRequest, PendingIntent pendingIntent) {
        this.fM.requestLocationUpdates(locationRequest, pendingIntent);
    }

    public void requestLocationUpdates(LocationRequest locationRequest, LocationListener locationListener) {
        requestLocationUpdates(locationRequest, locationListener, null);
    }

    public void requestLocationUpdates(LocationRequest locationRequest, LocationListener locationListener, Looper looper) {
        synchronized (this.fM) {
            this.fM.requestLocationUpdates(locationRequest, locationListener, looper);
        }
    }

    protected bf m799s(IBinder iBinder) {
        return C0158a.m781r(iBinder);
    }

    public void setMockLocation(Location location) {
        this.fM.setMockLocation(location);
    }

    public void setMockMode(boolean z) {
        this.fM.setMockMode(z);
    }
}
