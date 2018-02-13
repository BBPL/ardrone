package com.google.android.gms.internal;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public abstract class C0083j implements SafeParcelable {
    private static final Object bo = new Object();
    private static ClassLoader bp = null;
    private static Integer bq = null;
    private boolean br = false;

    private static boolean m180a(Class<?> cls) {
        boolean z = false;
        try {
            z = SafeParcelable.NULL.equals(cls.getField("NULL").get(null));
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e2) {
        }
        return z;
    }

    protected static boolean m181h(String str) {
        ClassLoader u = C0083j.m182u();
        if (u == null) {
            return true;
        }
        try {
            return C0083j.m180a(u.loadClass(str));
        } catch (Exception e) {
            return false;
        }
    }

    protected static ClassLoader m182u() {
        ClassLoader classLoader;
        synchronized (bo) {
            classLoader = bp;
        }
        return classLoader;
    }

    protected static Integer m183v() {
        Integer num;
        synchronized (bo) {
            num = bq;
        }
        return num;
    }

    protected boolean m184w() {
        return this.br;
    }
}
