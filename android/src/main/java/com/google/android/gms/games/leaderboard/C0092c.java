package com.google.android.gms.games.leaderboard;

import android.database.CharArrayBuffer;
import android.net.Uri;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayerEntity;
import com.google.android.gms.internal.C0241r;
import com.google.android.gms.internal.C0242s;
import com.google.android.gms.internal.ao;

public final class C0092c implements LeaderboardScore {
    private final long er;
    private final String es;
    private final String et;
    private final long eu;
    private final long ev;
    private final String ew;
    private final Uri ex;
    private final Uri ey;
    private final PlayerEntity ez;

    public C0092c(LeaderboardScore leaderboardScore) {
        this.er = leaderboardScore.getRank();
        this.es = (String) C0242s.m1208d(leaderboardScore.getDisplayRank());
        this.et = (String) C0242s.m1208d(leaderboardScore.getDisplayScore());
        this.eu = leaderboardScore.getRawScore();
        this.ev = leaderboardScore.getTimestampMillis();
        this.ew = leaderboardScore.getScoreHolderDisplayName();
        this.ex = leaderboardScore.getScoreHolderIconImageUri();
        this.ey = leaderboardScore.getScoreHolderHiResImageUri();
        Player scoreHolder = leaderboardScore.getScoreHolder();
        this.ez = scoreHolder == null ? null : (PlayerEntity) scoreHolder.freeze();
    }

    static int m204a(LeaderboardScore leaderboardScore) {
        return C0241r.hashCode(Long.valueOf(leaderboardScore.getRank()), leaderboardScore.getDisplayRank(), Long.valueOf(leaderboardScore.getRawScore()), leaderboardScore.getDisplayScore(), Long.valueOf(leaderboardScore.getTimestampMillis()), leaderboardScore.getScoreHolderDisplayName(), leaderboardScore.getScoreHolderIconImageUri(), leaderboardScore.getScoreHolderHiResImageUri(), leaderboardScore.getScoreHolder());
    }

    static boolean m205a(LeaderboardScore leaderboardScore, Object obj) {
        if (!(obj instanceof LeaderboardScore)) {
            return false;
        }
        if (leaderboardScore != obj) {
            LeaderboardScore leaderboardScore2 = (LeaderboardScore) obj;
            if (!(C0241r.m1200a(Long.valueOf(leaderboardScore2.getRank()), Long.valueOf(leaderboardScore.getRank())) && C0241r.m1200a(leaderboardScore2.getDisplayRank(), leaderboardScore.getDisplayRank()) && C0241r.m1200a(Long.valueOf(leaderboardScore2.getRawScore()), Long.valueOf(leaderboardScore.getRawScore())) && C0241r.m1200a(leaderboardScore2.getDisplayScore(), leaderboardScore.getDisplayScore()) && C0241r.m1200a(Long.valueOf(leaderboardScore2.getTimestampMillis()), Long.valueOf(leaderboardScore.getTimestampMillis())) && C0241r.m1200a(leaderboardScore2.getScoreHolderDisplayName(), leaderboardScore.getScoreHolderDisplayName()) && C0241r.m1200a(leaderboardScore2.getScoreHolderIconImageUri(), leaderboardScore.getScoreHolderIconImageUri()) && C0241r.m1200a(leaderboardScore2.getScoreHolderHiResImageUri(), leaderboardScore.getScoreHolderHiResImageUri()) && C0241r.m1200a(leaderboardScore2.getScoreHolder(), leaderboardScore.getScoreHolder()))) {
                return false;
            }
        }
        return true;
    }

    static String m206b(LeaderboardScore leaderboardScore) {
        return C0241r.m1201c(leaderboardScore).m1199a("Rank", Long.valueOf(leaderboardScore.getRank())).m1199a("DisplayRank", leaderboardScore.getDisplayRank()).m1199a("Score", Long.valueOf(leaderboardScore.getRawScore())).m1199a("DisplayScore", leaderboardScore.getDisplayScore()).m1199a("Timestamp", Long.valueOf(leaderboardScore.getTimestampMillis())).m1199a("DisplayName", leaderboardScore.getScoreHolderDisplayName()).m1199a("IconImageUri", leaderboardScore.getScoreHolderIconImageUri()).m1199a("HiResImageUri", leaderboardScore.getScoreHolderHiResImageUri()).m1199a("Player", leaderboardScore.getScoreHolder() == null ? null : leaderboardScore.getScoreHolder()).toString();
    }

    public LeaderboardScore aH() {
        return this;
    }

    public boolean equals(Object obj) {
        return C0092c.m205a(this, obj);
    }

    public /* synthetic */ Object freeze() {
        return aH();
    }

    public String getDisplayRank() {
        return this.es;
    }

    public void getDisplayRank(CharArrayBuffer charArrayBuffer) {
        ao.m345b(this.es, charArrayBuffer);
    }

    public String getDisplayScore() {
        return this.et;
    }

    public void getDisplayScore(CharArrayBuffer charArrayBuffer) {
        ao.m345b(this.et, charArrayBuffer);
    }

    public long getRank() {
        return this.er;
    }

    public long getRawScore() {
        return this.eu;
    }

    public Player getScoreHolder() {
        return this.ez;
    }

    public String getScoreHolderDisplayName() {
        return this.ez == null ? this.ew : this.ez.getDisplayName();
    }

    public void getScoreHolderDisplayName(CharArrayBuffer charArrayBuffer) {
        if (this.ez == null) {
            ao.m345b(this.ew, charArrayBuffer);
        } else {
            this.ez.getDisplayName(charArrayBuffer);
        }
    }

    public Uri getScoreHolderHiResImageUri() {
        return this.ez == null ? this.ey : this.ez.getHiResImageUri();
    }

    public Uri getScoreHolderIconImageUri() {
        return this.ez == null ? this.ex : this.ez.getIconImageUri();
    }

    public long getTimestampMillis() {
        return this.ev;
    }

    public int hashCode() {
        return C0092c.m204a(this);
    }

    public boolean isDataValid() {
        return true;
    }

    public String toString() {
        return C0092c.m206b(this);
    }
}
