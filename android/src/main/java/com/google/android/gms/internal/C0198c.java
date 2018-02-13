package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.appstate.AppState;
import com.google.android.gms.appstate.AppStateBuffer;
import com.google.android.gms.appstate.OnSignOutCompleteListener;
import com.google.android.gms.appstate.OnStateDeletedListener;
import com.google.android.gms.appstate.OnStateListLoadedListener;
import com.google.android.gms.appstate.OnStateLoadedListener;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.internal.C0145k.C0117b;
import com.google.android.gms.internal.C0145k.C0118c;
import com.google.android.gms.internal.C0145k.C0224d;
import com.google.android.gms.internal.C0210e.C0212a;

public final class C0198c extends C0145k<C0210e> {
    private final String f89g;

    final class C0190a extends C0151b {
        private final OnStateDeletedListener f74n;
        final /* synthetic */ C0198c f75o;

        public C0190a(C0198c c0198c, OnStateDeletedListener onStateDeletedListener) {
            this.f75o = c0198c;
            this.f74n = (OnStateDeletedListener) C0242s.m1205b((Object) onStateDeletedListener, (Object) "Listener must not be null");
        }

        public void onStateDeleted(int i, int i2) {
            this.f75o.m528a(new C0191b(this.f75o, this.f74n, i, i2));
        }
    }

    final class C0191b extends C0117b<OnStateDeletedListener> {
        final /* synthetic */ C0198c f76o;
        private final int f77p;
        private final int f78q;

        public C0191b(C0198c c0198c, OnStateDeletedListener onStateDeletedListener, int i, int i2) {
            this.f76o = c0198c;
            super(c0198c, onStateDeletedListener);
            this.f77p = i;
            this.f78q = i2;
        }

        public void m997a(OnStateDeletedListener onStateDeletedListener) {
            onStateDeletedListener.onStateDeleted(this.f77p, this.f78q);
        }

        protected void mo526d() {
        }
    }

    final class C0192c extends C0151b {
        final /* synthetic */ C0198c f79o;
        private final OnStateListLoadedListener f80r;

        public C0192c(C0198c c0198c, OnStateListLoadedListener onStateListLoadedListener) {
            this.f79o = c0198c;
            this.f80r = (OnStateListLoadedListener) C0242s.m1205b((Object) onStateListLoadedListener, (Object) "Listener must not be null");
        }

        public void mo624a(C0051d c0051d) {
            this.f79o.m528a(new C0193d(this.f79o, this.f80r, c0051d));
        }
    }

    final class C0193d extends C0118c<OnStateListLoadedListener> {
        final /* synthetic */ C0198c f81o;

        public C0193d(C0198c c0198c, OnStateListLoadedListener onStateListLoadedListener, C0051d c0051d) {
            this.f81o = c0198c;
            super(c0198c, onStateListLoadedListener, c0051d);
        }

        public void m1001a(OnStateListLoadedListener onStateListLoadedListener, C0051d c0051d) {
            onStateListLoadedListener.onStateListLoaded(c0051d.getStatusCode(), new AppStateBuffer(c0051d));
        }
    }

    final class C0194e extends C0151b {
        final /* synthetic */ C0198c f82o;
        private final OnStateLoadedListener f83s;

        public C0194e(C0198c c0198c, OnStateLoadedListener onStateLoadedListener) {
            this.f82o = c0198c;
            this.f83s = (OnStateLoadedListener) C0242s.m1205b((Object) onStateLoadedListener, (Object) "Listener must not be null");
        }

        public void mo623a(int i, C0051d c0051d) {
            this.f82o.m528a(new C0195f(this.f82o, this.f83s, i, c0051d));
        }
    }

    final class C0195f extends C0118c<OnStateLoadedListener> {
        final /* synthetic */ C0198c f84o;
        private final int f85q;

        public C0195f(C0198c c0198c, OnStateLoadedListener onStateLoadedListener, int i, C0051d c0051d) {
            this.f84o = c0198c;
            super(c0198c, onStateLoadedListener, c0051d);
            this.f85q = i;
        }

        public void m1004a(OnStateLoadedListener onStateLoadedListener, C0051d c0051d) {
            String str = null;
            AppStateBuffer appStateBuffer = new AppStateBuffer(c0051d);
            try {
                byte[] localData;
                byte[] conflictData;
                if (appStateBuffer.getCount() > 0) {
                    AppState appState = appStateBuffer.get(0);
                    str = appState.getConflictVersion();
                    localData = appState.getLocalData();
                    conflictData = appState.getConflictData();
                } else {
                    localData = null;
                    conflictData = null;
                }
                appStateBuffer.close();
                int statusCode = c0051d.getStatusCode();
                if (statusCode == 2000) {
                    onStateLoadedListener.onStateConflict(this.f85q, str, localData, conflictData);
                } else {
                    onStateLoadedListener.onStateLoaded(statusCode, this.f85q, localData);
                }
            } catch (Throwable th) {
                appStateBuffer.close();
            }
        }
    }

    final class C0196g extends C0151b {
        final /* synthetic */ C0198c f86o;
        private final OnSignOutCompleteListener f87t;

        public C0196g(C0198c c0198c, OnSignOutCompleteListener onSignOutCompleteListener) {
            this.f86o = c0198c;
            this.f87t = (OnSignOutCompleteListener) C0242s.m1205b((Object) onSignOutCompleteListener, (Object) "Listener must not be null");
        }

        public void onSignOutComplete() {
            this.f86o.m528a(new C0197h(this.f86o, this.f87t));
        }
    }

    final class C0197h extends C0117b<OnSignOutCompleteListener> {
        final /* synthetic */ C0198c f88o;

        public C0197h(C0198c c0198c, OnSignOutCompleteListener onSignOutCompleteListener) {
            this.f88o = c0198c;
            super(c0198c, onSignOutCompleteListener);
        }

        public void m1006a(OnSignOutCompleteListener onSignOutCompleteListener) {
            onSignOutCompleteListener.onSignOutComplete();
        }

        protected void mo526d() {
        }
    }

    public C0198c(Context context, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, String str, String[] strArr) {
        super(context, connectionCallbacks, onConnectionFailedListener, strArr);
        this.f89g = (String) C0242s.m1208d(str);
    }

    public void m1009a(OnStateLoadedListener onStateLoadedListener, int i, byte[] bArr) {
        if (onStateLoadedListener == null) {
            C0149d c0149d = null;
        } else {
            Object c0194e = new C0194e(this, onStateLoadedListener);
        }
        try {
            ((C0210e) m525C()).mo952a(c0149d, i, bArr);
        } catch (RemoteException e) {
            Log.w("AppStateClient", "service died");
        }
    }

    protected void mo534a(C0233p c0233p, C0224d c0224d) throws RemoteException {
        c0233p.mo962a(c0224d, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, getContext().getPackageName(), this.f89g, m535x());
    }

    protected void mo535a(String... strArr) {
        boolean z = false;
        for (String equals : strArr) {
            if (equals.equals(Scopes.APP_STATE)) {
                z = true;
            }
        }
        C0242s.m1203a(z, String.format("AppStateClient requires %s to function.", new Object[]{Scopes.APP_STATE}));
    }

    protected C0210e m1012b(IBinder iBinder) {
        return C0212a.m1126e(iBinder);
    }

    protected String mo536b() {
        return "com.google.android.gms.appstate.service.START";
    }

    protected /* synthetic */ IInterface mo537c(IBinder iBinder) {
        return m1012b(iBinder);
    }

    protected String mo538c() {
        return "com.google.android.gms.appstate.internal.IAppStateService";
    }

    public void deleteState(OnStateDeletedListener onStateDeletedListener, int i) {
        try {
            ((C0210e) m525C()).mo954b(new C0190a(this, onStateDeletedListener), i);
        } catch (RemoteException e) {
            Log.w("AppStateClient", "service died");
        }
    }

    public int getMaxNumKeys() {
        try {
            return ((C0210e) m525C()).getMaxNumKeys();
        } catch (RemoteException e) {
            Log.w("AppStateClient", "service died");
            return 2;
        }
    }

    public int getMaxStateSize() {
        try {
            return ((C0210e) m525C()).getMaxStateSize();
        } catch (RemoteException e) {
            Log.w("AppStateClient", "service died");
            return 2;
        }
    }

    public void listStates(OnStateListLoadedListener onStateListLoadedListener) {
        try {
            ((C0210e) m525C()).mo949a(new C0192c(this, onStateListLoadedListener));
        } catch (RemoteException e) {
            Log.w("AppStateClient", "service died");
        }
    }

    public void loadState(OnStateLoadedListener onStateLoadedListener, int i) {
        try {
            ((C0210e) m525C()).mo950a(new C0194e(this, onStateLoadedListener), i);
        } catch (RemoteException e) {
            Log.w("AppStateClient", "service died");
        }
    }

    public void resolveState(OnStateLoadedListener onStateLoadedListener, int i, String str, byte[] bArr) {
        try {
            ((C0210e) m525C()).mo951a(new C0194e(this, onStateLoadedListener), i, str, bArr);
        } catch (RemoteException e) {
            Log.w("AppStateClient", "service died");
        }
    }

    public void signOut(OnSignOutCompleteListener onSignOutCompleteListener) {
        if (onSignOutCompleteListener == null) {
            C0149d c0149d = null;
        } else {
            Object c0196g = new C0196g(this, onSignOutCompleteListener);
        }
        try {
            ((C0210e) m525C()).mo953b(c0149d);
        } catch (RemoteException e) {
            Log.w("AppStateClient", "service died");
        }
    }
}
