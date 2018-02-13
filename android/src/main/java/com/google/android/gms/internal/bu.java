package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.view.View;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.dynamic.C0078c;
import com.google.android.gms.internal.bq.C0178a;
import com.google.android.gms.plus.PlusOneDummyView;

public final class bu {
    private static Context gN;
    private static bq iy;

    public static class C0189a extends Exception {
        public C0189a(String str) {
            super(str);
        }
    }

    public static View m975a(Context context, int i, int i2, String str, int i3) {
        if (str != null) {
            return (View) C0078c.m172a(m977m(context).mo674a(C0078c.m173f(context), i, i2, str, i3));
        }
        try {
            throw new NullPointerException();
        } catch (Exception e) {
            return new PlusOneDummyView(context, i);
        }
    }

    public static View m976a(Context context, int i, int i2, String str, String str2) {
        if (str != null) {
            return (View) C0078c.m172a(m977m(context).mo675a(C0078c.m173f(context), i, i2, str, str2));
        }
        try {
            throw new NullPointerException();
        } catch (Exception e) {
            return new PlusOneDummyView(context, i);
        }
    }

    private static bq m977m(Context context) throws C0189a {
        C0242s.m1208d(context);
        if (iy == null) {
            if (gN == null) {
                gN = GooglePlayServicesUtil.getRemoteContext(context);
                if (gN == null) {
                    throw new C0189a("Could not get remote context.");
                }
            }
            try {
                iy = C0178a.m886Z((IBinder) gN.getClassLoader().loadClass("com.google.android.gms.plus.plusone.PlusOneButtonCreatorImpl").newInstance());
            } catch (ClassNotFoundException e) {
                throw new C0189a("Could not load creator class.");
            } catch (InstantiationException e2) {
                throw new C0189a("Could not instantiate creator.");
            } catch (IllegalAccessException e3) {
                throw new C0189a("Could not access creator.");
            }
        }
        return iy;
    }
}
