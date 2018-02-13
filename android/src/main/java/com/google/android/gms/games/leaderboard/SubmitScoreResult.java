package com.google.android.gms.games.leaderboard;

import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.internal.C0241r;
import com.google.android.gms.internal.C0241r.C0240a;
import com.google.android.gms.internal.C0242s;
import com.google.android.gms.internal.bd;
import java.util.HashMap;

public final class SubmitScoreResult {
    private static final String[] eB = new String[]{"leaderboardId", "playerId", "timeSpan", "hasResult", "rawScore", "formattedScore", "newBest"};
    private String dx;
    private String eC;
    private HashMap<Integer, Result> eD;
    private int f54p;

    public static final class Result {
        public final String formattedScore;
        public final boolean newBest;
        public final long rawScore;

        public Result(long j, String str, boolean z) {
            this.rawScore = j;
            this.formattedScore = str;
            this.newBest = z;
        }

        public String toString() {
            return C0241r.m1201c(this).m1199a("RawScore", Long.valueOf(this.rawScore)).m1199a("FormattedScore", this.formattedScore).m1199a("NewBest", Boolean.valueOf(this.newBest)).toString();
        }
    }

    public SubmitScoreResult(int i, String str, String str2) {
        this(i, str, str2, new HashMap());
    }

    public SubmitScoreResult(int i, String str, String str2, HashMap<Integer, Result> hashMap) {
        this.f54p = i;
        this.eC = str;
        this.dx = str2;
        this.eD = hashMap;
    }

    public SubmitScoreResult(C0051d c0051d) {
        int i = 0;
        this.f54p = c0051d.getStatusCode();
        this.eD = new HashMap();
        int count = c0051d.getCount();
        C0242s.m1207c(count == 3);
        while (i < count) {
            int e = c0051d.m46e(i);
            if (i == 0) {
                this.eC = c0051d.m44c("leaderboardId", i, e);
                this.dx = c0051d.m44c("playerId", i, e);
            }
            if (c0051d.m45d("hasResult", i, e)) {
                m203a(new Result(c0051d.m41a("rawScore", i, e), c0051d.m44c("formattedScore", i, e), c0051d.m45d("newBest", i, e)), c0051d.m43b("timeSpan", i, e));
            }
            i++;
        }
    }

    private void m203a(Result result, int i) {
        this.eD.put(Integer.valueOf(i), result);
    }

    public String getLeaderboardId() {
        return this.eC;
    }

    public String getPlayerId() {
        return this.dx;
    }

    public Result getScoreResult(int i) {
        return (Result) this.eD.get(Integer.valueOf(i));
    }

    public int getStatusCode() {
        return this.f54p;
    }

    public String toString() {
        C0240a a = C0241r.m1201c(this).m1199a("PlayerId", this.dx).m1199a("StatusCode", Integer.valueOf(this.f54p));
        for (int i = 0; i < 3; i++) {
            Result result = (Result) this.eD.get(Integer.valueOf(i));
            a.m1199a("TimesSpan", bd.m761G(i));
            a.m1199a("Result", result == null ? "null" : result.toString());
        }
        return a.toString();
    }
}
