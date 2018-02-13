package com.google.android.gms.games.multiplayer.realtime;

import android.database.CharArrayBuffer;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.ParticipantEntity;
import com.google.android.gms.internal.C0083j;
import com.google.android.gms.internal.C0241r;
import com.google.android.gms.internal.ao;
import com.google.android.gms.internal.av;
import java.util.ArrayList;

public final class RoomEntity extends av implements Room {
    public static final Creator<RoomEntity> CREATOR = new C0104a();
    private final int ab;
    private final String dV;
    private final String di;
    private final long eG;
    private final ArrayList<ParticipantEntity> eJ;
    private final int eK;
    private final Bundle fa;
    private final String fe;
    private final int ff;
    private final int fg;

    static final class C0104a extends C0103b {
        C0104a() {
        }

        public /* synthetic */ Object createFromParcel(Parcel parcel) {
            return mo460s(parcel);
        }

        public RoomEntity mo460s(Parcel parcel) {
            if (av.m185c(C0083j.m183v()) || C0083j.m181h(RoomEntity.class.getCanonicalName())) {
                return super.mo460s(parcel);
            }
            String readString = parcel.readString();
            String readString2 = parcel.readString();
            long readLong = parcel.readLong();
            int readInt = parcel.readInt();
            String readString3 = parcel.readString();
            int readInt2 = parcel.readInt();
            Bundle readBundle = parcel.readBundle();
            int readInt3 = parcel.readInt();
            ArrayList arrayList = new ArrayList(readInt3);
            for (int i = 0; i < readInt3; i++) {
                arrayList.add(ParticipantEntity.CREATOR.createFromParcel(parcel));
            }
            return new RoomEntity(2, readString, readString2, readLong, readInt, readString3, readInt2, readBundle, arrayList, -1);
        }
    }

    RoomEntity(int i, String str, String str2, long j, int i2, String str3, int i3, Bundle bundle, ArrayList<ParticipantEntity> arrayList, int i4) {
        this.ab = i;
        this.dV = str;
        this.fe = str2;
        this.eG = j;
        this.ff = i2;
        this.di = str3;
        this.eK = i3;
        this.fa = bundle;
        this.eJ = arrayList;
        this.fg = i4;
    }

    public RoomEntity(Room room) {
        this.ab = 2;
        this.dV = room.getRoomId();
        this.fe = room.getCreatorId();
        this.eG = room.getCreationTimestamp();
        this.ff = room.getStatus();
        this.di = room.getDescription();
        this.eK = room.getVariant();
        this.fa = room.getAutoMatchCriteria();
        ArrayList participants = room.getParticipants();
        int size = participants.size();
        this.eJ = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            this.eJ.add((ParticipantEntity) ((Participant) participants.get(i)).freeze());
        }
        this.fg = room.getAutoMatchWaitEstimateSeconds();
    }

    static int m235a(Room room) {
        return C0241r.hashCode(room.getRoomId(), room.getCreatorId(), Long.valueOf(room.getCreationTimestamp()), Integer.valueOf(room.getStatus()), room.getDescription(), Integer.valueOf(room.getVariant()), room.getAutoMatchCriteria(), room.getParticipants(), Integer.valueOf(room.getAutoMatchWaitEstimateSeconds()));
    }

    static boolean m236a(Room room, Object obj) {
        if (!(obj instanceof Room)) {
            return false;
        }
        if (room != obj) {
            Room room2 = (Room) obj;
            if (!(C0241r.m1200a(room2.getRoomId(), room.getRoomId()) && C0241r.m1200a(room2.getCreatorId(), room.getCreatorId()) && C0241r.m1200a(Long.valueOf(room2.getCreationTimestamp()), Long.valueOf(room.getCreationTimestamp())) && C0241r.m1200a(Integer.valueOf(room2.getStatus()), Integer.valueOf(room.getStatus())) && C0241r.m1200a(room2.getDescription(), room.getDescription()) && C0241r.m1200a(Integer.valueOf(room2.getVariant()), Integer.valueOf(room.getVariant())) && C0241r.m1200a(room2.getAutoMatchCriteria(), room.getAutoMatchCriteria()) && C0241r.m1200a(room2.getParticipants(), room.getParticipants()) && C0241r.m1200a(Integer.valueOf(room2.getAutoMatchWaitEstimateSeconds()), Integer.valueOf(room.getAutoMatchWaitEstimateSeconds())))) {
                return false;
            }
        }
        return true;
    }

    static String m237b(Room room) {
        return C0241r.m1201c(room).m1199a("RoomId", room.getRoomId()).m1199a("CreatorId", room.getCreatorId()).m1199a("CreationTimestamp", Long.valueOf(room.getCreationTimestamp())).m1199a("RoomStatus", Integer.valueOf(room.getStatus())).m1199a("Description", room.getDescription()).m1199a("Variant", Integer.valueOf(room.getVariant())).m1199a("AutoMatchCriteria", room.getAutoMatchCriteria()).m1199a("Participants", room.getParticipants()).m1199a("AutoMatchWaitEstimateSeconds", Integer.valueOf(room.getAutoMatchWaitEstimateSeconds())).toString();
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return m236a(this, obj);
    }

    public Room freeze() {
        return this;
    }

    public Bundle getAutoMatchCriteria() {
        return this.fa;
    }

    public int getAutoMatchWaitEstimateSeconds() {
        return this.fg;
    }

    public long getCreationTimestamp() {
        return this.eG;
    }

    public String getCreatorId() {
        return this.fe;
    }

    public String getDescription() {
        return this.di;
    }

    public void getDescription(CharArrayBuffer charArrayBuffer) {
        ao.m345b(this.di, charArrayBuffer);
    }

    public String getParticipantId(String str) {
        int size = this.eJ.size();
        for (int i = 0; i < size; i++) {
            Participant participant = (Participant) this.eJ.get(i);
            Player player = participant.getPlayer();
            if (player != null && player.getPlayerId().equals(str)) {
                return participant.getParticipantId();
            }
        }
        return null;
    }

    public ArrayList<String> getParticipantIds() {
        int size = this.eJ.size();
        ArrayList<String> arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(((ParticipantEntity) this.eJ.get(i)).getParticipantId());
        }
        return arrayList;
    }

    public int getParticipantStatus(String str) {
        int size = this.eJ.size();
        for (int i = 0; i < size; i++) {
            Participant participant = (Participant) this.eJ.get(i);
            if (participant.getParticipantId().equals(str)) {
                return participant.getStatus();
            }
        }
        throw new IllegalStateException("Participant " + str + " is not in room " + getRoomId());
    }

    public ArrayList<Participant> getParticipants() {
        return new ArrayList(this.eJ);
    }

    public String getRoomId() {
        return this.dV;
    }

    public int getStatus() {
        return this.ff;
    }

    public int getVariant() {
        return this.eK;
    }

    public int hashCode() {
        return m235a(this);
    }

    public int m240i() {
        return this.ab;
    }

    public boolean isDataValid() {
        return true;
    }

    public String toString() {
        return m237b((Room) this);
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (m184w()) {
            parcel.writeString(this.dV);
            parcel.writeString(this.fe);
            parcel.writeLong(this.eG);
            parcel.writeInt(this.ff);
            parcel.writeString(this.di);
            parcel.writeInt(this.eK);
            parcel.writeBundle(this.fa);
            int size = this.eJ.size();
            parcel.writeInt(size);
            for (int i2 = 0; i2 < size; i2++) {
                ((ParticipantEntity) this.eJ.get(i2)).writeToParcel(parcel, i);
            }
            return;
        }
        C0103b.m231a(this, parcel, i);
    }
}
