package com.google.android.gms.games.achievement;

import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.common.data.DataBuffer;

public final class AchievementBuffer extends DataBuffer<Achievement> {
    public AchievementBuffer(C0051d c0051d) {
        super(c0051d);
    }

    public Achievement get(int i) {
        return new C0087a(this.S, i);
    }
}
