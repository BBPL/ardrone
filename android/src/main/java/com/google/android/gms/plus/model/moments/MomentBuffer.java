package com.google.android.gms.plus.model.moments;

import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.common.data.DataBuffer;
import com.google.android.gms.internal.cb;

public final class MomentBuffer extends DataBuffer<Moment> {
    public MomentBuffer(C0051d c0051d) {
        super(c0051d);
    }

    public Moment get(int i) {
        return new cb(this.S, i);
    }
}
