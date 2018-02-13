package com.google.android.gms.internal;

import android.util.Log;

public final class C0231n {
    private final String bX;

    public C0231n(String str) {
        this.bX = (String) C0242s.m1208d(str);
    }

    public void m1162a(String str, String str2) {
        if (m1167l(3)) {
            Log.d(str, str2);
        }
    }

    public void m1163a(String str, String str2, Throwable th) {
        if (m1167l(6)) {
            Log.e(str, str2, th);
        }
    }

    public void m1164b(String str, String str2) {
        if (m1167l(5)) {
            Log.w(str, str2);
        }
    }

    public void m1165c(String str, String str2) {
        if (m1167l(6)) {
            Log.e(str, str2);
        }
    }

    public void m1166d(String str, String str2) {
        if (!m1167l(4)) {
        }
    }

    public boolean m1167l(int i) {
        return Log.isLoggable(this.bX, i);
    }
}
