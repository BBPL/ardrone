package com.google.android.gms.games;

import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.common.data.DataBuffer;

public final class GameBuffer extends DataBuffer<Game> {
    public GameBuffer(C0051d c0051d) {
        super(c0051d);
    }

    public Game get(int i) {
        return new C0088b(this.S, i);
    }
}
