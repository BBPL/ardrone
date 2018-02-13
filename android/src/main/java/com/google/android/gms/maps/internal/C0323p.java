package com.google.android.gms.maps.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.dynamic.C0078c;
import com.google.android.gms.internal.C0242s;
import com.google.android.gms.maps.internal.C0306c.C0308a;
import com.google.android.gms.maps.model.RuntimeRemoteException;

public class C0323p {
    private static Context gN;
    private static C0306c gO;

    private static <T> T m1311a(ClassLoader classLoader, String str) {
        try {
            return C0323p.m1312c(((ClassLoader) C0242s.m1208d(classLoader)).loadClass(str));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Unable to find dynamic class " + str);
        }
    }

    private static Class<?> bm() {
        try {
            return Class.forName("com.google.android.gms.maps.internal.CreatorImpl");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static <T> T m1312c(Class<?> cls) {
        try {
            return cls.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalStateException("Unable to instantiate the dynamic class " + cls.getName());
        } catch (IllegalAccessException e2) {
            throw new IllegalStateException("Unable to call the default constructor of " + cls.getName());
        }
    }

    private static Context getRemoteContext(Context context) {
        if (gN == null) {
            if (C0323p.bm() != null) {
                gN = context;
            } else {
                gN = GooglePlayServicesUtil.getRemoteContext(context);
            }
        }
        return gN;
    }

    public static C0306c m1313i(Context context) throws GooglePlayServicesNotAvailableException {
        C0242s.m1208d(context);
        C0323p.m1315k(context);
        if (gO == null) {
            C0323p.m1316l(context);
        }
        if (gO != null) {
            return gO;
        }
        gO = C0308a.m1298v((IBinder) C0323p.m1311a(C0323p.getRemoteContext(context).getClassLoader(), "com.google.android.gms.maps.internal.CreatorImpl"));
        C0323p.m1314j(context);
        return gO;
    }

    private static void m1314j(Context context) {
        try {
            gO.mo1096a(C0078c.m173f(C0323p.getRemoteContext(context).getResources()), (int) GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public static void m1315k(Context context) throws GooglePlayServicesNotAvailableException {
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (isGooglePlayServicesAvailable != 0) {
            throw new GooglePlayServicesNotAvailableException(isGooglePlayServicesAvailable);
        }
    }

    private static void m1316l(Context context) {
        Class bm = C0323p.bm();
        if (bm != null) {
            Log.i(C0323p.class.getSimpleName(), "Making Creator statically");
            gO = (C0306c) C0323p.m1312c(bm);
            C0323p.m1314j(context);
        }
    }
}
