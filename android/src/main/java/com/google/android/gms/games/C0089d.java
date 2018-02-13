package com.google.android.gms.games;

import android.database.CharArrayBuffer;
import android.net.Uri;
import android.os.Parcel;
import com.google.android.gms.common.data.C0044b;
import com.google.android.gms.common.data.C0051d;

public final class C0089d extends C0044b implements Player {
    public C0089d(C0051d c0051d, int i) {
        super(c0051d, i);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return PlayerEntity.m197a(this, obj);
    }

    public Player freeze() {
        return new PlayerEntity(this);
    }

    public String getDisplayName() {
        return getString("profile_name");
    }

    public void getDisplayName(CharArrayBuffer charArrayBuffer) {
        m4a("profile_name", charArrayBuffer);
    }

    public Uri getHiResImageUri() {
        return m5d("profile_hi_res_image_uri");
    }

    public Uri getIconImageUri() {
        return m5d("profile_icon_image_uri");
    }

    public String getPlayerId() {
        return getString("external_player_id");
    }

    public long getRetrievedTimestamp() {
        return getLong("last_updated");
    }

    public boolean hasHiResImage() {
        return getHiResImageUri() != null;
    }

    public boolean hasIconImage() {
        return getIconImageUri() != null;
    }

    public int hashCode() {
        return PlayerEntity.m196a(this);
    }

    public String toString() {
        return PlayerEntity.m198b((Player) this);
    }

    public void writeToParcel(Parcel parcel, int i) {
        ((PlayerEntity) freeze()).writeToParcel(parcel, i);
    }
}
