package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.internal.C0222o.C0223a;
import com.google.android.gms.internal.C0233p.C0235a;
import java.util.ArrayList;

public abstract class C0145k<T extends IInterface> implements GooglePlayServicesClient {
    public static final String[] bD = new String[]{"service_esmobile", "service_googleme"};
    boolean bA = false;
    boolean bB = false;
    private final Object bC = new Object();
    private T bs;
    private ArrayList<ConnectionCallbacks> bt;
    final ArrayList<ConnectionCallbacks> bu = new ArrayList();
    private boolean bv = false;
    private ArrayList<OnConnectionFailedListener> bw;
    private boolean bx = false;
    private final ArrayList<C0117b<?>> by = new ArrayList();
    private C0225e bz;
    private final String[] f60f;
    private final Context mContext;
    final Handler mHandler;

    protected abstract class C0117b<TListener> {
        final /* synthetic */ C0145k bE;
        private boolean bF = false;
        private TListener mListener;

        public C0117b(C0145k c0145k, TListener tListener) {
            this.bE = c0145k;
            this.mListener = tListener;
        }

        public void mo523D() {
            synchronized (this) {
                Object obj = this.mListener;
                if (this.bF) {
                    Log.w("GmsClient", "Callback proxy " + this + " being reused. This is not safe.");
                }
            }
            if (obj != null) {
                try {
                    mo525a(obj);
                } catch (RuntimeException e) {
                    mo526d();
                    throw e;
                }
            }
            mo526d();
            synchronized (this) {
                this.bF = true;
            }
            unregister();
        }

        public void mo524E() {
            synchronized (this) {
                this.mListener = null;
            }
        }

        protected abstract void mo525a(TListener tListener);

        protected abstract void mo526d();

        public void unregister() {
            mo524E();
            synchronized (this.bE.by) {
                this.bE.by.remove(this);
            }
        }
    }

    public abstract class C0118c<TListener> extends C0117b<TListener> {
        private final C0051d f56S;
        final /* synthetic */ C0145k bE;

        public C0118c(C0145k c0145k, TListener tListener, C0051d c0051d) {
            this.bE = c0145k;
            super(c0145k, tListener);
            this.f56S = c0051d;
        }

        public /* bridge */ /* synthetic */ void mo523D() {
            super.mo523D();
        }

        public /* bridge */ /* synthetic */ void mo524E() {
            super.mo524E();
        }

        protected final void mo525a(TListener tListener) {
            mo528a(tListener, this.f56S);
        }

        protected abstract void mo528a(TListener tListener, C0051d c0051d);

        protected void mo526d() {
            if (this.f56S != null) {
                this.f56S.close();
            }
        }

        public /* bridge */ /* synthetic */ void unregister() {
            super.unregister();
        }
    }

    final class C0221a extends Handler {
        final /* synthetic */ C0145k bE;

        public C0221a(C0145k c0145k, Looper looper) {
            this.bE = c0145k;
            super(looper);
        }

        public void handleMessage(Message message) {
            if (message.what != 1 || this.bE.isConnecting()) {
                synchronized (this.bE.bC) {
                    this.bE.bB = false;
                }
                if (message.what == 3) {
                    this.bE.mo533a(new ConnectionResult(((Integer) message.obj).intValue(), null));
                    return;
                } else if (message.what == 4) {
                    synchronized (this.bE.bt) {
                        if (this.bE.bA && this.bE.isConnected() && this.bE.bt.contains(message.obj)) {
                            ((ConnectionCallbacks) message.obj).onConnected(this.bE.mo540z());
                        }
                    }
                    return;
                } else if (message.what == 2 && !this.bE.isConnected()) {
                    C0117b c0117b = (C0117b) message.obj;
                    c0117b.mo526d();
                    c0117b.unregister();
                    return;
                } else if (message.what == 2 || message.what == 1) {
                    ((C0117b) message.obj).mo523D();
                    return;
                } else {
                    Log.wtf("GmsClient", "Don't know how to handle this message.");
                    return;
                }
            }
            c0117b = (C0117b) message.obj;
            c0117b.mo526d();
            c0117b.unregister();
        }
    }

    public static final class C0224d extends C0223a {
        private C0145k bG;

        public C0224d(C0145k c0145k) {
            this.bG = c0145k;
        }

        public void mo958b(int i, IBinder iBinder, Bundle bundle) {
            C0242s.m1205b((Object) "onPostInitComplete can be called only once per call to getServiceFromBroker", this.bG);
            this.bG.mo532a(i, iBinder, bundle);
            this.bG = null;
        }
    }

    final class C0225e implements ServiceConnection {
        final /* synthetic */ C0145k bE;

        C0225e(C0145k c0145k) {
            this.bE = c0145k;
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            this.bE.m534f(iBinder);
        }

        public void onServiceDisconnected(ComponentName componentName) {
            this.bE.bs = null;
            this.bE.m523A();
        }
    }

    protected final class C0226f extends C0117b<Boolean> {
        final /* synthetic */ C0145k bE;
        public final Bundle bH;
        public final IBinder bI;
        public final int statusCode;

        public C0226f(C0145k c0145k, int i, IBinder iBinder, Bundle bundle) {
            this.bE = c0145k;
            super(c0145k, Boolean.valueOf(true));
            this.statusCode = i;
            this.bI = iBinder;
            this.bH = bundle;
        }

        protected void m1140a(Boolean bool) {
            if (bool != null) {
                switch (this.statusCode) {
                    case 0:
                        try {
                            if (this.bE.mo538c().equals(this.bI.getInterfaceDescriptor())) {
                                this.bE.bs = this.bE.mo537c(this.bI);
                                if (this.bE.bs != null) {
                                    this.bE.mo539y();
                                    return;
                                }
                            }
                        } catch (RemoteException e) {
                        }
                        C0229l.m1155g(this.bE.mContext).m1157b(this.bE.mo536b(), this.bE.bz);
                        this.bE.bz = null;
                        this.bE.bs = null;
                        this.bE.mo533a(new ConnectionResult(8, null));
                        return;
                    case 10:
                        throw new IllegalStateException("A fatal developer error has occurred. Check the logs for further information.");
                    default:
                        PendingIntent pendingIntent = this.bH != null ? (PendingIntent) this.bH.getParcelable("pendingIntent") : null;
                        if (this.bE.bz != null) {
                            C0229l.m1155g(this.bE.mContext).m1157b(this.bE.mo536b(), this.bE.bz);
                            this.bE.bz = null;
                        }
                        this.bE.bs = null;
                        this.bE.mo533a(new ConnectionResult(this.statusCode, pendingIntent));
                        return;
                }
            }
        }

        protected void mo526d() {
        }
    }

    protected C0145k(Context context, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, String... strArr) {
        this.mContext = (Context) C0242s.m1208d(context);
        this.bt = new ArrayList();
        this.bt.add(C0242s.m1208d(connectionCallbacks));
        this.bw = new ArrayList();
        this.bw.add(C0242s.m1208d(onConnectionFailedListener));
        this.mHandler = new C0221a(this, context.getMainLooper());
        mo535a(strArr);
        this.f60f = strArr;
    }

    protected final void m523A() {
        this.mHandler.removeMessages(4);
        synchronized (this.bt) {
            this.bv = true;
            ArrayList arrayList = this.bt;
            int size = arrayList.size();
            for (int i = 0; i < size && this.bA; i++) {
                if (this.bt.contains(arrayList.get(i))) {
                    ((ConnectionCallbacks) arrayList.get(i)).onDisconnected();
                }
            }
            this.bv = false;
        }
    }

    protected final void m524B() {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected. Call connect() and wait for onConnected() to be called.");
        }
    }

    protected final T m525C() {
        m524B();
        return this.bs;
    }

    protected void mo532a(int i, IBinder iBinder, Bundle bundle) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(1, new C0226f(this, i, iBinder, bundle)));
    }

    protected void mo533a(ConnectionResult connectionResult) {
        this.mHandler.removeMessages(4);
        synchronized (this.bw) {
            this.bx = true;
            ArrayList arrayList = this.bw;
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                if (this.bA) {
                    if (this.bw.contains(arrayList.get(i))) {
                        ((OnConnectionFailedListener) arrayList.get(i)).onConnectionFailed(connectionResult);
                    }
                    i++;
                } else {
                    return;
                }
            }
            this.bx = false;
        }
    }

    public final void m528a(C0117b<?> c0117b) {
        synchronized (this.by) {
            this.by.add(c0117b);
        }
        this.mHandler.sendMessage(this.mHandler.obtainMessage(2, c0117b));
    }

    protected abstract void mo534a(C0233p c0233p, C0224d c0224d) throws RemoteException;

    protected void mo535a(String... strArr) {
    }

    protected abstract String mo536b();

    protected abstract T mo537c(IBinder iBinder);

    protected abstract String mo538c();

    public void connect() {
        this.bA = true;
        synchronized (this.bC) {
            this.bB = true;
        }
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.mContext);
        if (isGooglePlayServicesAvailable != 0) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(3, Integer.valueOf(isGooglePlayServicesAvailable)));
            return;
        }
        if (this.bz != null) {
            Log.e("GmsClient", "Calling connect() while still connected, missing disconnect().");
            this.bs = null;
            C0229l.m1155g(this.mContext).m1157b(mo536b(), this.bz);
        }
        this.bz = new C0225e(this);
        if (!C0229l.m1155g(this.mContext).m1156a(mo536b(), this.bz)) {
            Log.e("GmsClient", "unable to connect to service: " + mo536b());
            this.mHandler.sendMessage(this.mHandler.obtainMessage(3, Integer.valueOf(9)));
        }
    }

    public void disconnect() {
        this.bA = false;
        synchronized (this.bC) {
            this.bB = false;
        }
        synchronized (this.by) {
            int size = this.by.size();
            for (int i = 0; i < size; i++) {
                ((C0117b) this.by.get(i)).mo524E();
            }
            this.by.clear();
        }
        this.bs = null;
        if (this.bz != null) {
            C0229l.m1155g(this.mContext).m1157b(mo536b(), this.bz);
            this.bz = null;
        }
    }

    protected final void m534f(IBinder iBinder) {
        try {
            mo534a(C0235a.m1195h(iBinder), new C0224d(this));
        } catch (RemoteException e) {
            Log.w("GmsClient", "service died");
        }
    }

    public final Context getContext() {
        return this.mContext;
    }

    public boolean isConnected() {
        return this.bs != null;
    }

    public boolean isConnecting() {
        boolean z;
        synchronized (this.bC) {
            z = this.bB;
        }
        return z;
    }

    public boolean isConnectionCallbacksRegistered(ConnectionCallbacks connectionCallbacks) {
        boolean contains;
        C0242s.m1208d(connectionCallbacks);
        synchronized (this.bt) {
            contains = this.bt.contains(connectionCallbacks);
        }
        return contains;
    }

    public boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener onConnectionFailedListener) {
        boolean contains;
        C0242s.m1208d(onConnectionFailedListener);
        synchronized (this.bw) {
            contains = this.bw.contains(onConnectionFailedListener);
        }
        return contains;
    }

    public void registerConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        C0242s.m1208d(connectionCallbacks);
        synchronized (this.bt) {
            if (this.bt.contains(connectionCallbacks)) {
                Log.w("GmsClient", "registerConnectionCallbacks(): listener " + connectionCallbacks + " is already registered");
            } else {
                if (this.bv) {
                    this.bt = new ArrayList(this.bt);
                }
                this.bt.add(connectionCallbacks);
            }
        }
        if (isConnected()) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(4, connectionCallbacks));
        }
    }

    public void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        C0242s.m1208d(onConnectionFailedListener);
        synchronized (this.bw) {
            if (this.bw.contains(onConnectionFailedListener)) {
                Log.w("GmsClient", "registerConnectionFailedListener(): listener " + onConnectionFailedListener + " is already registered");
            } else {
                if (this.bx) {
                    this.bw = new ArrayList(this.bw);
                }
                this.bw.add(onConnectionFailedListener);
            }
        }
    }

    public void unregisterConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        C0242s.m1208d(connectionCallbacks);
        synchronized (this.bt) {
            if (this.bt != null) {
                if (this.bv) {
                    this.bt = new ArrayList(this.bt);
                }
                if (!this.bt.remove(connectionCallbacks)) {
                    Log.w("GmsClient", "unregisterConnectionCallbacks(): listener " + connectionCallbacks + " not found");
                } else if (this.bv && !this.bu.contains(connectionCallbacks)) {
                    this.bu.add(connectionCallbacks);
                }
            }
        }
    }

    public void unregisterConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        C0242s.m1208d(onConnectionFailedListener);
        synchronized (this.bw) {
            if (this.bw != null) {
                if (this.bx) {
                    this.bw = new ArrayList(this.bw);
                }
                if (!this.bw.remove(onConnectionFailedListener)) {
                    Log.w("GmsClient", "unregisterConnectionFailedListener(): listener " + onConnectionFailedListener + " not found");
                }
            }
        }
    }

    public final String[] m535x() {
        return this.f60f;
    }

    protected void mo539y() {
        boolean z = true;
        synchronized (this.bt) {
            C0242s.m1202a(!this.bv);
            this.mHandler.removeMessages(4);
            this.bv = true;
            if (this.bu.size() != 0) {
                z = false;
            }
            C0242s.m1202a(z);
            Bundle z2 = mo540z();
            ArrayList arrayList = this.bt;
            int size = arrayList.size();
            for (int i = 0; i < size && this.bA && isConnected(); i++) {
                this.bu.size();
                if (!this.bu.contains(arrayList.get(i))) {
                    ((ConnectionCallbacks) arrayList.get(i)).onConnected(z2);
                }
            }
            this.bu.clear();
            this.bv = false;
        }
    }

    protected Bundle mo540z() {
        return null;
    }
}
