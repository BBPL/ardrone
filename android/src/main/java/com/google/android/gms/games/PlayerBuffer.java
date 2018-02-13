package com.google.android.gms.games;

import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.common.data.DataBuffer;

public final class PlayerBuffer extends DataBuffer<Player> {
    public PlayerBuffer(C0051d c0051d) {
        super(c0051d);
    }

    public Player get(int i) {
        return new C0089d(this.S, i);
    }
}
