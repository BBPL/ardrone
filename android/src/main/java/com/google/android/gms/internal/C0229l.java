package com.google.android.gms.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.C0145k.C0225e;
import com.parrot.ardronetool.vlib.VideoCodecType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public final class C0229l implements Callback {
    private static final Object bJ = new Object();
    private static C0229l bK;
    private final Context bL;
    private final HashMap<String, C0228a> bM = new HashMap();
    private final Handler mHandler;

    final class C0228a {
        private final String bN;
        private final C0227a bO = new C0227a(this);
        private final HashSet<C0225e> bP = new HashSet();
        private boolean bQ;
        private IBinder bR;
        private ComponentName bS;
        final /* synthetic */ C0229l bT;
        private int mState = 0;

        public class C0227a implements ServiceConnection {
            final /* synthetic */ C0228a bU;

            public C0227a(C0228a c0228a) {
                this.bU = c0228a;
            }

            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                synchronized (this.bU.bT.bM) {
                    this.bU.bR = iBinder;
                    this.bU.bS = componentName;
                    Iterator it = this.bU.bP.iterator();
                    while (it.hasNext()) {
                        ((C0225e) it.next()).onServiceConnected(componentName, iBinder);
                    }
                    this.bU.mState = 1;
                }
            }

            public void onServiceDisconnected(ComponentName componentName) {
                synchronized (this.bU.bT.bM) {
                    this.bU.bR = null;
                    this.bU.bS = componentName;
                    Iterator it = this.bU.bP.iterator();
                    while (it.hasNext()) {
                        ((C0225e) it.next()).onServiceDisconnected(componentName);
                    }
                    this.bU.mState = 2;
                }
            }
        }

        public C0228a(C0229l c0229l, String str) {
            this.bT = c0229l;
            this.bN = str;
        }

        public C0227a m1147F() {
            return this.bO;
        }

        public String m1148G() {
            return this.bN;
        }

        public boolean m1149H() {
            return this.bP.isEmpty();
        }

        public void m1150a(C0225e c0225e) {
            this.bP.add(c0225e);
        }

        public void m1151b(C0225e c0225e) {
            this.bP.remove(c0225e);
        }

        public void m1152b(boolean z) {
            this.bQ = z;
        }

        public boolean m1153c(C0225e c0225e) {
            return this.bP.contains(c0225e);
        }

        public IBinder getBinder() {
            return this.bR;
        }

        public ComponentName getComponentName() {
            return this.bS;
        }

        public int getState() {
            return this.mState;
        }

        public boolean isBound() {
            return this.bQ;
        }
    }

    private C0229l(Context context) {
        this.mHandler = new Handler(context.getMainLooper(), this);
        this.bL = context.getApplicationContext();
    }

    public static C0229l m1155g(Context context) {
        synchronized (bJ) {
            if (bK == null) {
                bK = new C0229l(context.getApplicationContext());
            }
        }
        return bK;
    }

    public boolean m1156a(String str, C0225e c0225e) {
        boolean isBound;
        synchronized (this.bM) {
            C0228a c0228a = (C0228a) this.bM.get(str);
            if (c0228a != null) {
                this.mHandler.removeMessages(0, c0228a);
                if (!c0228a.m1153c(c0225e)) {
                    c0228a.m1150a((C0225e) c0225e);
                    switch (c0228a.getState()) {
                        case 1:
                            c0225e.onServiceConnected(c0228a.getComponentName(), c0228a.getBinder());
                            break;
                        case 2:
                            c0228a.m1152b(this.bL.bindService(new Intent(str).setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE), c0228a.m1147F(), VideoCodecType.H264_360P_CODEC));
                            break;
                        default:
                            break;
                    }
                }
                throw new IllegalStateException("Trying to bind a GmsServiceConnection that was already connected before.  startServiceAction=" + str);
            }
            c0228a = new C0228a(this, str);
            c0228a.m1150a((C0225e) c0225e);
            c0228a.m1152b(this.bL.bindService(new Intent(str).setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE), c0228a.m1147F(), VideoCodecType.H264_360P_CODEC));
            this.bM.put(str, c0228a);
            isBound = c0228a.isBound();
        }
        return isBound;
    }

    public void m1157b(String str, C0225e c0225e) {
        synchronized (this.bM) {
            C0228a c0228a = (C0228a) this.bM.get(str);
            if (c0228a == null) {
                throw new IllegalStateException("Nonexistent connection status for service action: " + str);
            } else if (c0228a.m1153c(c0225e)) {
                c0228a.m1151b((C0225e) c0225e);
                if (c0228a.m1149H()) {
                    this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0, c0228a), 5000);
                }
            } else {
                throw new IllegalStateException("Trying to unbind a GmsServiceConnection  that was not bound before.  startServiceAction=" + str);
            }
        }
    }

    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 0:
                C0228a c0228a = (C0228a) message.obj;
                synchronized (this.bM) {
                    if (c0228a.m1149H()) {
                        this.bL.unbindService(c0228a.m1147F());
                        this.bM.remove(c0228a.m1148G());
                    }
                }
                return true;
            default:
                return false;
        }
    }
}
