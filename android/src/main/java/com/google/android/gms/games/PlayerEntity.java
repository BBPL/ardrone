package com.google.android.gms.games;

import android.database.CharArrayBuffer;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.C0083j;
import com.google.android.gms.internal.C0219h;
import com.google.android.gms.internal.C0241r;
import com.google.android.gms.internal.ao;
import com.google.android.gms.internal.av;

public final class PlayerEntity extends av implements Player {
    public static final Creator<PlayerEntity> CREATOR = new C0086a();
    private final int ab;
    private final String cl;
    private final Uri dk;
    private final Uri dl;
    private final String dx;
    private final long dy;

    static final class C0086a extends C0085c {
        C0086a() {
        }

        public /* synthetic */ Object createFromParcel(Parcel parcel) {
            return mo384o(parcel);
        }

        public PlayerEntity mo384o(Parcel parcel) {
            Uri uri = null;
            if (av.m185c(C0083j.m183v()) || C0083j.m181h(PlayerEntity.class.getCanonicalName())) {
                return super.mo384o(parcel);
            }
            String readString = parcel.readString();
            String readString2 = parcel.readString();
            String readString3 = parcel.readString();
            String readString4 = parcel.readString();
            Uri parse = readString3 == null ? null : Uri.parse(readString3);
            if (readString4 != null) {
                uri = Uri.parse(readString4);
            }
            return new PlayerEntity(1, readString, readString2, parse, uri, parcel.readLong());
        }
    }

    PlayerEntity(int i, String str, String str2, Uri uri, Uri uri2, long j) {
        this.ab = i;
        this.dx = str;
        this.cl = str2;
        this.dk = uri;
        this.dl = uri2;
        this.dy = j;
    }

    public PlayerEntity(Player player) {
        boolean z = true;
        this.ab = 1;
        this.dx = player.getPlayerId();
        this.cl = player.getDisplayName();
        this.dk = player.getIconImageUri();
        this.dl = player.getHiResImageUri();
        this.dy = player.getRetrievedTimestamp();
        C0219h.m1134b(this.dx);
        C0219h.m1134b(this.cl);
        if (this.dy <= 0) {
            z = false;
        }
        C0219h.m1132a(z);
    }

    static int m196a(Player player) {
        return C0241r.hashCode(player.getPlayerId(), player.getDisplayName(), player.getIconImageUri(), player.getHiResImageUri(), Long.valueOf(player.getRetrievedTimestamp()));
    }

    static boolean m197a(Player player, Object obj) {
        if (!(obj instanceof Player)) {
            return false;
        }
        if (player != obj) {
            Player player2 = (Player) obj;
            if (!(C0241r.m1200a(player2.getPlayerId(), player.getPlayerId()) && C0241r.m1200a(player2.getDisplayName(), player.getDisplayName()) && C0241r.m1200a(player2.getIconImageUri(), player.getIconImageUri()) && C0241r.m1200a(player2.getHiResImageUri(), player.getHiResImageUri()) && C0241r.m1200a(Long.valueOf(player2.getRetrievedTimestamp()), Long.valueOf(player.getRetrievedTimestamp())))) {
                return false;
            }
        }
        return true;
    }

    static String m198b(Player player) {
        return C0241r.m1201c(player).m1199a("PlayerId", player.getPlayerId()).m1199a("DisplayName", player.getDisplayName()).m1199a("IconImageUri", player.getIconImageUri()).m1199a("HiResImageUri", player.getHiResImageUri()).m1199a("RetrievedTimestamp", Long.valueOf(player.getRetrievedTimestamp())).toString();
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return m197a(this, obj);
    }

    public Player freeze() {
        return this;
    }

    public String getDisplayName() {
        return this.cl;
    }

    public void getDisplayName(CharArrayBuffer charArrayBuffer) {
        ao.m345b(this.cl, charArrayBuffer);
    }

    public Uri getHiResImageUri() {
        return this.dl;
    }

    public Uri getIconImageUri() {
        return this.dk;
    }

    public String getPlayerId() {
        return this.dx;
    }

    public long getRetrievedTimestamp() {
        return this.dy;
    }

    public boolean hasHiResImage() {
        return getHiResImageUri() != null;
    }

    public boolean hasIconImage() {
        return getIconImageUri() != null;
    }

    public int hashCode() {
        return m196a(this);
    }

    public int m201i() {
        return this.ab;
    }

    public boolean isDataValid() {
        return true;
    }

    public String toString() {
        return m198b((Player) this);
    }

    public void writeToParcel(Parcel parcel, int i) {
        String str = null;
        if (m184w()) {
            parcel.writeString(this.dx);
            parcel.writeString(this.cl);
            parcel.writeString(this.dk == null ? null : this.dk.toString());
            if (this.dl != null) {
                str = this.dl.toString();
            }
            parcel.writeString(str);
            parcel.writeLong(this.dy);
            return;
        }
        C0085c.m192a(this, parcel, i);
    }
}
