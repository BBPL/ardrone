package com.google.android.gms.internal;

import android.os.Build.VERSION;

public final class as {
    public static boolean an() {
        return m349x(11);
    }

    public static boolean ao() {
        return m349x(12);
    }

    public static boolean ap() {
        return m349x(13);
    }

    public static boolean aq() {
        return m349x(14);
    }

    public static boolean ar() {
        return m349x(16);
    }

    public static boolean as() {
        return m349x(17);
    }

    private static boolean m349x(int i) {
        return VERSION.SDK_INT >= i;
    }
}
