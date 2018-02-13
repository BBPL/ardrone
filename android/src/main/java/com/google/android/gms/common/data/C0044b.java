package com.google.android.gms.common.data;

import android.database.CharArrayBuffer;
import android.net.Uri;
import com.google.android.gms.internal.C0241r;
import com.google.android.gms.internal.C0242s;

public abstract class C0044b {
    protected final C0051d f14S;
    protected final int f15V;
    private final int f16W;

    public C0044b(C0051d c0051d, int i) {
        this.f14S = (C0051d) C0242s.m1208d(c0051d);
        boolean z = i >= 0 && i < c0051d.getCount();
        C0242s.m1202a(z);
        this.f15V = i;
        this.f16W = c0051d.m46e(this.f15V);
    }

    protected void m4a(String str, CharArrayBuffer charArrayBuffer) {
        this.f14S.m42a(str, this.f15V, this.f16W, charArrayBuffer);
    }

    protected Uri m5d(String str) {
        return this.f14S.m48f(str, this.f15V, this.f16W);
    }

    protected boolean m6e(String str) {
        return this.f14S.m49g(str, this.f15V, this.f16W);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof C0044b)) {
            return false;
        }
        C0044b c0044b = (C0044b) obj;
        return C0241r.m1200a(Integer.valueOf(c0044b.f15V), Integer.valueOf(this.f15V)) && C0241r.m1200a(Integer.valueOf(c0044b.f16W), Integer.valueOf(this.f16W)) && c0044b.f14S == this.f14S;
    }

    protected boolean getBoolean(String str) {
        return this.f14S.m45d(str, this.f15V, this.f16W);
    }

    protected byte[] getByteArray(String str) {
        return this.f14S.m47e(str, this.f15V, this.f16W);
    }

    protected int getInteger(String str) {
        return this.f14S.m43b(str, this.f15V, this.f16W);
    }

    protected long getLong(String str) {
        return this.f14S.m41a(str, this.f15V, this.f16W);
    }

    protected String getString(String str) {
        return this.f14S.m44c(str, this.f15V, this.f16W);
    }

    public int hashCode() {
        return C0241r.hashCode(Integer.valueOf(this.f15V), Integer.valueOf(this.f16W), this.f14S);
    }

    public boolean isDataValid() {
        return !this.f14S.isClosed();
    }
}
