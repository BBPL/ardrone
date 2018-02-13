package com.google.android.gms.games.multiplayer;

import android.database.CharArrayBuffer;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayerEntity;
import com.google.android.gms.internal.C0083j;
import com.google.android.gms.internal.C0241r;
import com.google.android.gms.internal.ao;
import com.google.android.gms.internal.av;

public final class ParticipantEntity extends av implements Participant {
    public static final Creator<ParticipantEntity> CREATOR = new C0098a();
    private final int ab;
    private final String cl;
    private final String dX;
    private final Uri dk;
    private final Uri dl;
    private final int eN;
    private final String eO;
    private final boolean eP;
    private final PlayerEntity eQ;
    private final int eR;

    static final class C0098a extends C0097c {
        C0098a() {
        }

        public /* synthetic */ Object createFromParcel(Parcel parcel) {
            return mo448q(parcel);
        }

        public ParticipantEntity mo448q(Parcel parcel) {
            int i = 0;
            if (av.m185c(C0083j.m183v()) || C0083j.m181h(ParticipantEntity.class.getCanonicalName())) {
                return super.mo448q(parcel);
            }
            String readString = parcel.readString();
            String readString2 = parcel.readString();
            String readString3 = parcel.readString();
            Uri parse = readString3 == null ? null : Uri.parse(readString3);
            String readString4 = parcel.readString();
            Uri parse2 = readString4 == null ? null : Uri.parse(readString4);
            int readInt = parcel.readInt();
            String readString5 = parcel.readString();
            boolean z = parcel.readInt() > 0;
            if (parcel.readInt() > 0) {
                i = 1;
            }
            return new ParticipantEntity(1, readString, readString2, parse, parse2, readInt, readString5, z, i != 0 ? (PlayerEntity) PlayerEntity.CREATOR.createFromParcel(parcel) : null, 7);
        }
    }

    ParticipantEntity(int i, String str, String str2, Uri uri, Uri uri2, int i2, String str3, boolean z, PlayerEntity playerEntity, int i3) {
        this.ab = i;
        this.dX = str;
        this.cl = str2;
        this.dk = uri;
        this.dl = uri2;
        this.eN = i2;
        this.eO = str3;
        this.eP = z;
        this.eQ = playerEntity;
        this.eR = i3;
    }

    public ParticipantEntity(Participant participant) {
        this.ab = 1;
        this.dX = participant.getParticipantId();
        this.cl = participant.getDisplayName();
        this.dk = participant.getIconImageUri();
        this.dl = participant.getHiResImageUri();
        this.eN = participant.getStatus();
        this.eO = participant.aM();
        this.eP = participant.isConnectedToRoom();
        Player player = participant.getPlayer();
        this.eQ = player == null ? null : new PlayerEntity(player);
        this.eR = participant.aN();
    }

    static int m222a(Participant participant) {
        return C0241r.hashCode(participant.getPlayer(), Integer.valueOf(participant.getStatus()), participant.aM(), Boolean.valueOf(participant.isConnectedToRoom()), participant.getDisplayName(), participant.getIconImageUri(), participant.getHiResImageUri(), Integer.valueOf(participant.aN()));
    }

    static boolean m223a(Participant participant, Object obj) {
        if (!(obj instanceof Participant)) {
            return false;
        }
        if (participant != obj) {
            Participant participant2 = (Participant) obj;
            if (!(C0241r.m1200a(participant2.getPlayer(), participant.getPlayer()) && C0241r.m1200a(Integer.valueOf(participant2.getStatus()), Integer.valueOf(participant.getStatus())) && C0241r.m1200a(participant2.aM(), participant.aM()) && C0241r.m1200a(Boolean.valueOf(participant2.isConnectedToRoom()), Boolean.valueOf(participant.isConnectedToRoom())) && C0241r.m1200a(participant2.getDisplayName(), participant.getDisplayName()) && C0241r.m1200a(participant2.getIconImageUri(), participant.getIconImageUri()) && C0241r.m1200a(participant2.getHiResImageUri(), participant.getHiResImageUri()) && C0241r.m1200a(Integer.valueOf(participant2.aN()), Integer.valueOf(participant.aN())))) {
                return false;
            }
        }
        return true;
    }

    static String m224b(Participant participant) {
        return C0241r.m1201c(participant).m1199a("Player", participant.getPlayer()).m1199a("Status", Integer.valueOf(participant.getStatus())).m1199a("ClientAddress", participant.aM()).m1199a("ConnectedToRoom", Boolean.valueOf(participant.isConnectedToRoom())).m1199a("DisplayName", participant.getDisplayName()).m1199a("IconImage", participant.getIconImageUri()).m1199a("HiResImage", participant.getHiResImageUri()).m1199a("Capabilities", Integer.valueOf(participant.aN())).toString();
    }

    public String aM() {
        return this.eO;
    }

    public int aN() {
        return this.eR;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return m223a(this, obj);
    }

    public Participant freeze() {
        return this;
    }

    public String getDisplayName() {
        return this.eQ == null ? this.cl : this.eQ.getDisplayName();
    }

    public void getDisplayName(CharArrayBuffer charArrayBuffer) {
        if (this.eQ == null) {
            ao.m345b(this.cl, charArrayBuffer);
        } else {
            this.eQ.getDisplayName(charArrayBuffer);
        }
    }

    public Uri getHiResImageUri() {
        return this.eQ == null ? this.dl : this.eQ.getHiResImageUri();
    }

    public Uri getIconImageUri() {
        return this.eQ == null ? this.dk : this.eQ.getIconImageUri();
    }

    public String getParticipantId() {
        return this.dX;
    }

    public Player getPlayer() {
        return this.eQ;
    }

    public int getStatus() {
        return this.eN;
    }

    public int hashCode() {
        return m222a(this);
    }

    public int m227i() {
        return this.ab;
    }

    public boolean isConnectedToRoom() {
        return this.eP;
    }

    public boolean isDataValid() {
        return true;
    }

    public String toString() {
        return m224b((Participant) this);
    }

    public void writeToParcel(Parcel parcel, int i) {
        String str = null;
        int i2 = 0;
        if (m184w()) {
            parcel.writeString(this.dX);
            parcel.writeString(this.cl);
            parcel.writeString(this.dk == null ? null : this.dk.toString());
            if (this.dl != null) {
                str = this.dl.toString();
            }
            parcel.writeString(str);
            parcel.writeInt(this.eN);
            parcel.writeString(this.eO);
            parcel.writeInt(this.eP ? 1 : 0);
            if (this.eQ != null) {
                i2 = 1;
            }
            parcel.writeInt(i2);
            if (this.eQ != null) {
                this.eQ.writeToParcel(parcel, i);
                return;
            }
            return;
        }
        C0097c.m218a(this, parcel, i);
    }
}
