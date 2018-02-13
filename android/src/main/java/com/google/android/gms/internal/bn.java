package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.C0145k.C0117b;
import com.google.android.gms.internal.C0145k.C0224d;
import com.google.android.gms.internal.bl.C0169a;
import com.google.android.gms.internal.bm.C0171a;
import com.google.android.gms.panorama.PanoramaClient.C0361a;
import com.google.android.gms.panorama.PanoramaClient.OnPanoramaInfoLoadedListener;

public class bn extends C0145k<bm> {

    final class C0172a extends C0117b<C0361a> {
        public final ConnectionResult hO;
        public final Intent hP;
        final /* synthetic */ bn hQ;
        public final int type;

        public C0172a(bn bnVar, C0361a c0361a, ConnectionResult connectionResult, int i, Intent intent) {
            this.hQ = bnVar;
            super(bnVar, c0361a);
            this.hO = connectionResult;
            this.type = i;
            this.hP = intent;
        }

        protected void m816a(C0361a c0361a) {
            if (c0361a != null) {
                c0361a.m1396a(this.hO, this.type, this.hP);
            }
        }

        protected void mo526d() {
        }
    }

    final class C0173b extends C0169a {
        final /* synthetic */ bn hQ;
        private final C0361a hR = null;
        private final OnPanoramaInfoLoadedListener hS;
        private final Uri hT;

        public C0173b(bn bnVar, OnPanoramaInfoLoadedListener onPanoramaInfoLoadedListener, Uri uri) {
            this.hQ = bnVar;
            this.hS = onPanoramaInfoLoadedListener;
            this.hT = uri;
        }

        public void mo655a(int i, Bundle bundle, int i2, Intent intent) {
            if (this.hT != null) {
                this.hQ.getContext().revokeUriPermission(this.hT, 1);
            }
            PendingIntent pendingIntent = null;
            if (bundle != null) {
                pendingIntent = (PendingIntent) bundle.getParcelable("pendingIntent");
            }
            ConnectionResult connectionResult = new ConnectionResult(i, pendingIntent);
            if (this.hR != null) {
                this.hQ.m528a(new C0172a(this.hQ, this.hR, connectionResult, i2, intent));
            } else {
                this.hQ.m528a(new C0174c(this.hQ, this.hS, connectionResult, intent));
            }
        }
    }

    final class C0174c extends C0117b<OnPanoramaInfoLoadedListener> {
        private final ConnectionResult hO;
        private final Intent hP;
        final /* synthetic */ bn hQ;

        public C0174c(bn bnVar, OnPanoramaInfoLoadedListener onPanoramaInfoLoadedListener, ConnectionResult connectionResult, Intent intent) {
            this.hQ = bnVar;
            super(bnVar, onPanoramaInfoLoadedListener);
            this.hO = connectionResult;
            this.hP = intent;
        }

        protected void m820a(OnPanoramaInfoLoadedListener onPanoramaInfoLoadedListener) {
            if (onPanoramaInfoLoadedListener != null) {
                onPanoramaInfoLoadedListener.onPanoramaInfoLoaded(this.hO, this.hP);
            }
        }

        protected void mo526d() {
        }
    }

    public bn(Context context, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, connectionCallbacks, onConnectionFailedListener, (String[]) null);
    }

    public bm m823X(IBinder iBinder) {
        return C0171a.m815W(iBinder);
    }

    public void m824a(C0173b c0173b, Uri uri, Bundle bundle, boolean z) {
        m524B();
        if (z) {
            getContext().grantUriPermission(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE, uri, 1);
        }
        try {
            ((bm) m525C()).mo656a(c0173b, uri, bundle, z);
        } catch (RemoteException e) {
            c0173b.mo655a(8, null, 0, null);
        }
    }

    protected void mo534a(C0233p c0233p, C0224d c0224d) throws RemoteException {
        c0233p.mo961a(c0224d, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, getContext().getPackageName(), new Bundle());
    }

    public void m826a(OnPanoramaInfoLoadedListener onPanoramaInfoLoadedListener, Uri uri, boolean z) {
        m824a(new C0173b(this, onPanoramaInfoLoadedListener, z ? uri : null), uri, null, z);
    }

    protected String mo536b() {
        return "com.google.android.gms.panorama.service.START";
    }

    public /* synthetic */ IInterface mo537c(IBinder iBinder) {
        return m823X(iBinder);
    }

    protected String mo538c() {
        return "com.google.android.gms.panorama.internal.IPanoramaService";
    }
}
