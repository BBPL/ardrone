package com.google.android.gms.games.leaderboard;

import android.database.CharArrayBuffer;
import android.net.Uri;
import com.google.android.gms.common.data.C0044b;
import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.internal.C0241r;
import java.util.ArrayList;

public final class C0090a extends C0044b implements Leaderboard {
    private final int eo;

    C0090a(C0051d c0051d, int i, int i2) {
        super(c0051d, i);
        this.eo = i2;
    }

    public String getDisplayName() {
        return getString("name");
    }

    public void getDisplayName(CharArrayBuffer charArrayBuffer) {
        m4a("name", charArrayBuffer);
    }

    public Uri getIconImageUri() {
        return m5d("board_icon_image_uri");
    }

    public String getLeaderboardId() {
        return getString("external_leaderboard_id");
    }

    public int getScoreOrder() {
        return getInteger("score_order");
    }

    public ArrayList<LeaderboardVariant> getVariants() {
        ArrayList<LeaderboardVariant> arrayList = new ArrayList(this.eo);
        for (int i = 0; i < this.eo; i++) {
            arrayList.add(new C0094e(this.S, this.V + i));
        }
        return arrayList;
    }

    public String toString() {
        return C0241r.m1201c(this).m1199a("ID", getLeaderboardId()).m1199a("DisplayName", getDisplayName()).m1199a("IconImageURI", getIconImageUri()).m1199a("ScoreOrder", Integer.valueOf(getScoreOrder())).toString();
    }
}
