package com.google.android.gms.appstate;

import com.google.android.gms.common.data.C0044b;
import com.google.android.gms.common.data.C0051d;

public final class C0045b extends C0044b implements AppState {
    C0045b(C0051d c0051d, int i) {
        super(c0051d, i);
    }

    public AppState m7a() {
        return new C0043a(this);
    }

    public boolean equals(Object obj) {
        return C0043a.m1a(this, obj);
    }

    public /* synthetic */ Object freeze() {
        return m7a();
    }

    public byte[] getConflictData() {
        return getByteArray("conflict_data");
    }

    public String getConflictVersion() {
        return getString("conflict_version");
    }

    public int getKey() {
        return getInteger("key");
    }

    public byte[] getLocalData() {
        return getByteArray("local_data");
    }

    public String getLocalVersion() {
        return getString("local_version");
    }

    public boolean hasConflict() {
        return !m6e("conflict_version");
    }

    public int hashCode() {
        return C0043a.m0a(this);
    }

    public String toString() {
        return C0043a.m2b(this);
    }
}
