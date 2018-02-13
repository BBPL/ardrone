package com.google.android.gms.appstate;

import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.common.data.DataBuffer;

public final class AppStateBuffer extends DataBuffer<AppState> {
    public AppStateBuffer(C0051d c0051d) {
        super(c0051d);
    }

    public AppState get(int i) {
        return new C0045b(this.S, i);
    }
}
