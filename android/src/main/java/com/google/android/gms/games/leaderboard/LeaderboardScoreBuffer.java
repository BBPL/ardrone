package com.google.android.gms.games.leaderboard;

import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.common.data.DataBuffer;

public final class LeaderboardScoreBuffer extends DataBuffer<LeaderboardScore> {
    private final C0091b ep;

    public LeaderboardScoreBuffer(C0051d c0051d) {
        super(c0051d);
        this.ep = new C0091b(c0051d.m54l());
    }

    public C0091b aF() {
        return this.ep;
    }

    public LeaderboardScore get(int i) {
        return new C0093d(this.S, i);
    }
}
