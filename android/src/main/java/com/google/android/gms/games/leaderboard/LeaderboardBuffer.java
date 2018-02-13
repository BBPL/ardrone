package com.google.android.gms.games.leaderboard;

import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.common.data.C0053f;

public final class LeaderboardBuffer extends C0053f<Leaderboard> {
    public LeaderboardBuffer(C0051d c0051d) {
        super(c0051d);
    }

    protected /* synthetic */ Object mo410a(int i, int i2) {
        return getEntry(i, i2);
    }

    protected Leaderboard getEntry(int i, int i2) {
        return new C0090a(this.S, i, i2);
    }

    protected String getPrimaryDataMarkerColumn() {
        return "external_leaderboard_id";
    }
}
