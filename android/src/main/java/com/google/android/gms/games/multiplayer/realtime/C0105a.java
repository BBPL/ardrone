package com.google.android.gms.games.multiplayer.realtime;

import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.common.data.C0053f;

public final class C0105a extends C0053f<Room> {
    public C0105a(C0051d c0051d) {
        super(c0051d);
    }

    protected /* synthetic */ Object mo410a(int i, int i2) {
        return m242b(i, i2);
    }

    protected Room m242b(int i, int i2) {
        return new C0106c(this.S, i, i2);
    }

    protected String getPrimaryDataMarkerColumn() {
        return "external_match_id";
    }
}
