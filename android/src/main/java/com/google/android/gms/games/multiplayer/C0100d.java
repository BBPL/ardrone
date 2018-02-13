package com.google.android.gms.games.multiplayer;

import android.database.CharArrayBuffer;
import android.net.Uri;
import android.os.Parcel;
import com.google.android.gms.common.data.C0044b;
import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.games.C0089d;
import com.google.android.gms.games.Player;

public final class C0100d extends C0044b implements Participant {
    private final C0089d eS;

    public C0100d(C0051d c0051d, int i) {
        super(c0051d, i);
        this.eS = new C0089d(c0051d, i);
    }

    public String aM() {
        return getString("client_address");
    }

    public int aN() {
        return getInteger("capabilities");
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return ParticipantEntity.m223a(this, obj);
    }

    public Participant freeze() {
        return new ParticipantEntity(this);
    }

    public String getDisplayName() {
        return m6e("external_player_id") ? getString("default_display_name") : this.eS.getDisplayName();
    }

    public void getDisplayName(CharArrayBuffer charArrayBuffer) {
        if (m6e("external_player_id")) {
            m4a("default_display_name", charArrayBuffer);
        } else {
            this.eS.getDisplayName(charArrayBuffer);
        }
    }

    public Uri getHiResImageUri() {
        return m6e("external_player_id") ? null : this.eS.getHiResImageUri();
    }

    public Uri getIconImageUri() {
        return m6e("external_player_id") ? m5d("default_display_image_uri") : this.eS.getIconImageUri();
    }

    public String getParticipantId() {
        return getString("external_participant_id");
    }

    public Player getPlayer() {
        return m6e("external_player_id") ? null : this.eS;
    }

    public int getStatus() {
        return getInteger("player_status");
    }

    public int hashCode() {
        return ParticipantEntity.m222a(this);
    }

    public boolean isConnectedToRoom() {
        return getInteger("connected") > 0;
    }

    public String toString() {
        return ParticipantEntity.m224b((Participant) this);
    }

    public void writeToParcel(Parcel parcel, int i) {
        ((ParticipantEntity) freeze()).writeToParcel(parcel, i);
    }
}
