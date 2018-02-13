package com.google.android.gms.games.leaderboard;

import android.database.CharArrayBuffer;
import android.net.Uri;
import com.google.android.gms.common.data.C0044b;
import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.games.C0089d;
import com.google.android.gms.games.Player;

public final class C0093d extends C0044b implements LeaderboardScore {
    private final C0089d eA;

    C0093d(C0051d c0051d, int i) {
        super(c0051d, i);
        this.eA = new C0089d(c0051d, i);
    }

    public LeaderboardScore aH() {
        return new C0092c(this);
    }

    public boolean equals(Object obj) {
        return C0092c.m205a(this, obj);
    }

    public /* synthetic */ Object freeze() {
        return aH();
    }

    public String getDisplayRank() {
        return getString("display_rank");
    }

    public void getDisplayRank(CharArrayBuffer charArrayBuffer) {
        m4a("display_rank", charArrayBuffer);
    }

    public String getDisplayScore() {
        return getString("display_score");
    }

    public void getDisplayScore(CharArrayBuffer charArrayBuffer) {
        m4a("display_score", charArrayBuffer);
    }

    public long getRank() {
        return getLong("rank");
    }

    public long getRawScore() {
        return getLong("raw_score");
    }

    public Player getScoreHolder() {
        return m6e("external_player_id") ? null : this.eA;
    }

    public String getScoreHolderDisplayName() {
        return m6e("external_player_id") ? getString("default_display_name") : this.eA.getDisplayName();
    }

    public void getScoreHolderDisplayName(CharArrayBuffer charArrayBuffer) {
        if (m6e("external_player_id")) {
            m4a("default_display_name", charArrayBuffer);
        } else {
            this.eA.getDisplayName(charArrayBuffer);
        }
    }

    public Uri getScoreHolderHiResImageUri() {
        return m6e("external_player_id") ? null : this.eA.getHiResImageUri();
    }

    public Uri getScoreHolderIconImageUri() {
        return m6e("external_player_id") ? m5d("default_display_image_uri") : this.eA.getIconImageUri();
    }

    public long getTimestampMillis() {
        return getLong("achieved_timestamp");
    }

    public int hashCode() {
        return C0092c.m204a(this);
    }

    public String toString() {
        return C0092c.m206b(this);
    }
}
