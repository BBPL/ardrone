package com.google.android.gms.dynamic;

import android.content.Context;
import android.os.IBinder;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.C0242s;

public abstract class C0080e<T> {
    private final String dd;
    private T de;

    public static class C0079a extends Exception {
        public C0079a(String str) {
            super(str);
        }

        public C0079a(String str, Throwable th) {
            super(str, th);
        }
    }

    protected C0080e(String str) {
        this.dd = str;
    }

    protected final T m174h(Context context) throws C0079a {
        if (this.de == null) {
            C0242s.m1208d(context);
            Context remoteContext = GooglePlayServicesUtil.getRemoteContext(context);
            if (remoteContext == null) {
                throw new C0079a("Could not get remote context.");
            }
            try {
                this.de = mo973k((IBinder) remoteContext.getClassLoader().loadClass(this.dd).newInstance());
            } catch (ClassNotFoundException e) {
                throw new C0079a("Could not load creator class.");
            } catch (InstantiationException e2) {
                throw new C0079a("Could not instantiate creator.");
            } catch (IllegalAccessException e3) {
                throw new C0079a("Could not access creator.");
            }
        }
        return this.de;
    }

    protected abstract T mo973k(IBinder iBinder);
}
