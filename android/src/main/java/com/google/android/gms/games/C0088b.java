package com.google.android.gms.games;

import android.database.CharArrayBuffer;
import android.net.Uri;
import android.os.Parcel;
import com.google.android.gms.common.data.C0044b;
import com.google.android.gms.common.data.C0051d;

public final class C0088b extends C0044b implements Game {
    public C0088b(C0051d c0051d, int i) {
        super(c0051d, i);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return GameEntity.m187a(this, obj);
    }

    public Game freeze() {
        return new GameEntity(this);
    }

    public int getAchievementTotalCount() {
        return getInteger("achievement_total_count");
    }

    public String getApplicationId() {
        return getString("external_game_id");
    }

    public String getDescription() {
        return getString("game_description");
    }

    public void getDescription(CharArrayBuffer charArrayBuffer) {
        m4a("game_description", charArrayBuffer);
    }

    public String getDeveloperName() {
        return getString("developer_name");
    }

    public void getDeveloperName(CharArrayBuffer charArrayBuffer) {
        m4a("developer_name", charArrayBuffer);
    }

    public String getDisplayName() {
        return getString("display_name");
    }

    public void getDisplayName(CharArrayBuffer charArrayBuffer) {
        m4a("display_name", charArrayBuffer);
    }

    public Uri getFeaturedImageUri() {
        return m5d("featured_image_uri");
    }

    public int getGameplayAclStatus() {
        return getInteger("gameplay_acl_status");
    }

    public Uri getHiResImageUri() {
        return m5d("game_hi_res_image_uri");
    }

    public Uri getIconImageUri() {
        return m5d("game_icon_image_uri");
    }

    public String getInstancePackageName() {
        return getString("package_name");
    }

    public int getLeaderboardCount() {
        return getInteger("leaderboard_count");
    }

    public String getPrimaryCategory() {
        return getString("primary_category");
    }

    public String getSecondaryCategory() {
        return getString("secondary_category");
    }

    public int hashCode() {
        return GameEntity.m186a(this);
    }

    public boolean isInstanceInstalled() {
        return getInteger("installed") > 0;
    }

    public boolean isPlayEnabledGame() {
        return getBoolean("play_enabled_game");
    }

    public String toString() {
        return GameEntity.m188b((Game) this);
    }

    public void writeToParcel(Parcel parcel, int i) {
        ((GameEntity) freeze()).writeToParcel(parcel, i);
    }
}
