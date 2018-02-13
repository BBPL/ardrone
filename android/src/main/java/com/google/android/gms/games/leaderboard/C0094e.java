package com.google.android.gms.games.leaderboard;

import com.google.android.gms.common.data.C0044b;
import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.internal.C0241r;
import com.google.android.gms.internal.bc;
import com.google.android.gms.internal.bd;

public final class C0094e extends C0044b implements LeaderboardVariant {
    C0094e(C0051d c0051d, int i) {
        super(c0051d, i);
    }

    public String aI() {
        return getString("top_page_token_next");
    }

    public String aJ() {
        return getString("window_page_token_prev");
    }

    public String aK() {
        return getString("window_page_token_next");
    }

    public int getCollection() {
        return getInteger("collection");
    }

    public String getDisplayPlayerRank() {
        return getString("player_display_rank");
    }

    public String getDisplayPlayerScore() {
        return getString("player_display_score");
    }

    public long getNumScores() {
        return m6e("total_scores") ? -1 : getLong("total_scores");
    }

    public long getPlayerRank() {
        return m6e("player_rank") ? -1 : getLong("player_rank");
    }

    public long getRawPlayerScore() {
        return m6e("player_raw_score") ? -1 : getLong("player_raw_score");
    }

    public int getTimeSpan() {
        return getInteger("timespan");
    }

    public boolean hasPlayerInfo() {
        return !m6e("player_raw_score");
    }

    public String toString() {
        return C0241r.m1201c(this).m1199a("TimeSpan", bd.m761G(getTimeSpan())).m1199a("Collection", bc.m760G(getCollection())).m1199a("RawPlayerScore", hasPlayerInfo() ? Long.valueOf(getRawPlayerScore()) : "none").m1199a("DisplayPlayerScore", hasPlayerInfo() ? getDisplayPlayerScore() : "none").m1199a("PlayerRank", hasPlayerInfo() ? Long.valueOf(getPlayerRank()) : "none").m1199a("DisplayPlayerRank", hasPlayerInfo() ? getDisplayPlayerRank() : "none").m1199a("NumScores", Long.valueOf(getNumScores())).m1199a("TopPageNextToken", aI()).m1199a("WindowPageNextToken", aK()).m1199a("WindowPagePrevToken", aJ()).toString();
    }
}
