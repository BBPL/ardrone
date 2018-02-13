package com.google.android.gms.internal;

public final class C0242s {
    public static void m1202a(boolean z) {
        if (!z) {
            throw new IllegalStateException();
        }
    }

    public static void m1203a(boolean z, Object obj) {
        if (!z) {
            throw new IllegalStateException(String.valueOf(obj));
        }
    }

    public static void m1204a(boolean z, String str, Object... objArr) {
        if (!z) {
            throw new IllegalArgumentException(String.format(str, objArr));
        }
    }

    public static <T> T m1205b(T t, Object obj) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(String.valueOf(obj));
    }

    public static void m1206b(boolean z, Object obj) {
        if (!z) {
            throw new IllegalArgumentException(String.valueOf(obj));
        }
    }

    public static void m1207c(boolean z) {
        if (!z) {
            throw new IllegalArgumentException();
        }
    }

    public static <T> T m1208d(T t) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException("null reference");
    }
}
