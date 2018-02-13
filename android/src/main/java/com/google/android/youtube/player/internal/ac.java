package com.google.android.youtube.player.internal;

import android.text.TextUtils;

public final class ac {
    public static <T> T m1482a(T t) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException("null reference");
    }

    public static <T> T m1483a(T t, Object obj) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(String.valueOf(obj));
    }

    public static String m1484a(String str, Object obj) {
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        throw new IllegalArgumentException(String.valueOf(obj));
    }

    public static void m1485a(boolean z) {
        if (!z) {
            throw new IllegalStateException();
        }
    }
}
