package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.internal.C0145k.C0117b;
import com.google.android.gms.internal.C0145k.C0118c;
import com.google.android.gms.internal.C0145k.C0224d;
import com.google.android.gms.internal.bs.C0182a;
import com.google.android.gms.plus.C0367a;
import com.google.android.gms.plus.PlusClient.OnAccessRevokedListener;
import com.google.android.gms.plus.PlusClient.OnMomentsLoadedListener;
import com.google.android.gms.plus.PlusClient.OnPeopleLoadedListener;
import com.google.android.gms.plus.model.moments.Moment;
import com.google.android.gms.plus.model.moments.MomentBuffer;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class bt extends C0145k<bs> {
    private Person ip;
    private C0367a iq;

    final class C0183a extends bo {
        private final OnMomentsLoadedListener ir;
        final /* synthetic */ bt is;

        public C0183a(bt btVar, OnMomentsLoadedListener onMomentsLoadedListener) {
            this.is = btVar;
            this.ir = onMomentsLoadedListener;
        }

        public void mo669a(C0051d c0051d, String str, String str2) {
            C0051d c0051d2;
            ConnectionResult connectionResult = new ConnectionResult(c0051d.getStatusCode(), c0051d.m54l() != null ? (PendingIntent) c0051d.m54l().getParcelable("pendingIntent") : null);
            if (connectionResult.isSuccess() || c0051d == null) {
                c0051d2 = c0051d;
            } else {
                if (!c0051d.isClosed()) {
                    c0051d.close();
                }
                c0051d2 = null;
            }
            this.is.m528a(new C0184b(this.is, this.ir, connectionResult, c0051d2, str, str2));
        }
    }

    final class C0184b extends C0118c<OnMomentsLoadedListener> {
        final /* synthetic */ bt is;
        private final ConnectionResult it;
        private final String iu;
        private final String iv;

        public C0184b(bt btVar, OnMomentsLoadedListener onMomentsLoadedListener, ConnectionResult connectionResult, C0051d c0051d, String str, String str2) {
            this.is = btVar;
            super(btVar, onMomentsLoadedListener, c0051d);
            this.it = connectionResult;
            this.iu = str;
            this.iv = str2;
        }

        protected void m958a(OnMomentsLoadedListener onMomentsLoadedListener, C0051d c0051d) {
            onMomentsLoadedListener.onMomentsLoaded(this.it, c0051d != null ? new MomentBuffer(c0051d) : null, this.iu, this.iv);
        }
    }

    final class C0185c extends bo {
        final /* synthetic */ bt is;
        private final OnPeopleLoadedListener iw;

        public C0185c(bt btVar, OnPeopleLoadedListener onPeopleLoadedListener) {
            this.is = btVar;
            this.iw = onPeopleLoadedListener;
        }

        public void mo668a(C0051d c0051d, String str) {
            C0051d c0051d2;
            ConnectionResult connectionResult = new ConnectionResult(c0051d.getStatusCode(), c0051d.m54l() != null ? (PendingIntent) c0051d.m54l().getParcelable("pendingIntent") : null);
            if (connectionResult.isSuccess() || c0051d == null) {
                c0051d2 = c0051d;
            } else {
                if (!c0051d.isClosed()) {
                    c0051d.close();
                }
                c0051d2 = null;
            }
            this.is.m528a(new C0186d(this.is, this.iw, connectionResult, c0051d2, str));
        }
    }

    final class C0186d extends C0118c<OnPeopleLoadedListener> {
        final /* synthetic */ bt is;
        private final ConnectionResult it;
        private final String iu;

        public C0186d(bt btVar, OnPeopleLoadedListener onPeopleLoadedListener, ConnectionResult connectionResult, C0051d c0051d, String str) {
            this.is = btVar;
            super(btVar, onPeopleLoadedListener, c0051d);
            this.it = connectionResult;
            this.iu = str;
        }

        protected void m961a(OnPeopleLoadedListener onPeopleLoadedListener, C0051d c0051d) {
            onPeopleLoadedListener.onPeopleLoaded(this.it, c0051d != null ? new PersonBuffer(c0051d) : null, this.iu);
        }
    }

    final class C0187e extends bo {
        final /* synthetic */ bt is;
        private final OnAccessRevokedListener ix;

        public C0187e(bt btVar, OnAccessRevokedListener onAccessRevokedListener) {
            this.is = btVar;
            this.ix = onAccessRevokedListener;
        }

        public void mo670b(int i, Bundle bundle) {
            PendingIntent pendingIntent = null;
            if (bundle != null) {
                pendingIntent = (PendingIntent) bundle.getParcelable("pendingIntent");
            }
            this.is.m528a(new C0188f(this.is, this.ix, new ConnectionResult(i, pendingIntent)));
        }
    }

    final class C0188f extends C0117b<OnAccessRevokedListener> {
        final /* synthetic */ bt is;
        private final ConnectionResult it;

        public C0188f(bt btVar, OnAccessRevokedListener onAccessRevokedListener, ConnectionResult connectionResult) {
            this.is = btVar;
            super(btVar, onAccessRevokedListener);
            this.it = connectionResult;
        }

        protected void m964a(OnAccessRevokedListener onAccessRevokedListener) {
            this.is.disconnect();
            if (onAccessRevokedListener != null) {
                onAccessRevokedListener.onAccessRevoked(this.it);
            }
        }

        protected void mo526d() {
        }
    }

    public bt(Context context, C0367a c0367a, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, connectionCallbacks, onConnectionFailedListener, c0367a.by());
        this.iq = c0367a;
    }

    public boolean m967F(String str) {
        return Arrays.asList(m535x()).contains(str);
    }

    protected void mo532a(int i, IBinder iBinder, Bundle bundle) {
        if (i == 0 && bundle != null && bundle.containsKey("loaded_person")) {
            this.ip = cc.m1074d(bundle.getByteArray("loaded_person"));
        }
        super.mo532a(i, iBinder, bundle);
    }

    protected void mo534a(C0233p c0233p, C0224d c0224d) throws RemoteException {
        Bundle bundle = new Bundle();
        bundle.putBoolean("skip_oob", false);
        bundle.putStringArray("request_visible_actions", this.iq.bz());
        if (this.iq.bA() != null) {
            bundle.putStringArray("required_features", this.iq.bA());
        }
        if (this.iq.bD() != null) {
            bundle.putString("application_name", this.iq.bD());
        }
        c0233p.mo963a(c0224d, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, this.iq.bC(), this.iq.bB(), m535x(), this.iq.getAccountName(), bundle);
    }

    public void m970a(OnPeopleLoadedListener onPeopleLoadedListener, Collection<String> collection) {
        m524B();
        bp c0185c = new C0185c(this, onPeopleLoadedListener);
        try {
            ((bs) m525C()).mo700a(c0185c, new ArrayList(collection));
        } catch (RemoteException e) {
            c0185c.mo668a(C0051d.m40f(8), null);
        }
    }

    public void m971a(OnPeopleLoadedListener onPeopleLoadedListener, String[] strArr) {
        m970a(onPeopleLoadedListener, Arrays.asList(strArr));
    }

    protected bs ac(IBinder iBinder) {
        return C0182a.ab(iBinder);
    }

    protected String mo536b() {
        return "com.google.android.gms.plus.service.START";
    }

    protected /* synthetic */ IInterface mo537c(IBinder iBinder) {
        return ac(iBinder);
    }

    protected String mo538c() {
        return "com.google.android.gms.plus.internal.IPlusService";
    }

    public void clearDefaultAccount() {
        m524B();
        try {
            this.ip = null;
            ((bs) m525C()).clearDefaultAccount();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public String getAccountName() {
        m524B();
        try {
            return ((bs) m525C()).getAccountName();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public Person getCurrentPerson() {
        m524B();
        return this.ip;
    }

    public void loadMoments(OnMomentsLoadedListener onMomentsLoadedListener) {
        loadMoments(onMomentsLoadedListener, 20, null, null, null, "me");
    }

    public void loadMoments(OnMomentsLoadedListener onMomentsLoadedListener, int i, String str, Uri uri, String str2, String str3) {
        m524B();
        Object c0183a = onMomentsLoadedListener != null ? new C0183a(this, onMomentsLoadedListener) : null;
        try {
            ((bs) m525C()).mo685a(c0183a, i, str, uri, str2, str3);
        } catch (RemoteException e) {
            c0183a.mo669a(C0051d.m40f(8), null, null);
        }
    }

    public void loadVisiblePeople(OnPeopleLoadedListener onPeopleLoadedListener, int i, String str) {
        m524B();
        bp c0185c = new C0185c(this, onPeopleLoadedListener);
        try {
            ((bs) m525C()).mo681a(c0185c, 1, i, -1, str);
        } catch (RemoteException e) {
            c0185c.mo668a(C0051d.m40f(8), null);
        }
    }

    public void loadVisiblePeople(OnPeopleLoadedListener onPeopleLoadedListener, String str) {
        loadVisiblePeople(onPeopleLoadedListener, 0, str);
    }

    public void removeMoment(String str) {
        m524B();
        try {
            ((bs) m525C()).removeMoment(str);
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void revokeAccessAndDisconnect(OnAccessRevokedListener onAccessRevokedListener) {
        m524B();
        clearDefaultAccount();
        Object c0187e = new C0187e(this, onAccessRevokedListener);
        try {
            ((bs) m525C()).mo704c(c0187e);
        } catch (RemoteException e) {
            c0187e.mo670b(8, null);
        }
    }

    public void writeMoment(Moment moment) {
        m524B();
        try {
            ((bs) m525C()).mo679a(ak.m317a((bz) moment));
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }
}
