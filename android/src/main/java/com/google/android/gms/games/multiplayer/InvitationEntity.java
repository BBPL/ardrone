package com.google.android.gms.games.multiplayer;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.GameEntity;
import com.google.android.gms.internal.C0083j;
import com.google.android.gms.internal.C0241r;
import com.google.android.gms.internal.C0242s;
import com.google.android.gms.internal.av;
import java.util.ArrayList;

public final class InvitationEntity extends av implements Invitation {
    public static final Creator<InvitationEntity> CREATOR = new C0096a();
    private final int ab;
    private final GameEntity eE;
    private final String eF;
    private final long eG;
    private final int eH;
    private final ParticipantEntity eI;
    private final ArrayList<ParticipantEntity> eJ;
    private final int eK;

    static final class C0096a extends C0095a {
        C0096a() {
        }

        public /* synthetic */ Object createFromParcel(Parcel parcel) {
            return mo439p(parcel);
        }

        public InvitationEntity mo439p(Parcel parcel) {
            if (av.m185c(C0083j.m183v()) || C0083j.m181h(InvitationEntity.class.getCanonicalName())) {
                return super.mo439p(parcel);
            }
            GameEntity gameEntity = (GameEntity) GameEntity.CREATOR.createFromParcel(parcel);
            String readString = parcel.readString();
            long readLong = parcel.readLong();
            int readInt = parcel.readInt();
            ParticipantEntity participantEntity = (ParticipantEntity) ParticipantEntity.CREATOR.createFromParcel(parcel);
            int readInt2 = parcel.readInt();
            ArrayList arrayList = new ArrayList(readInt2);
            for (int i = 0; i < readInt2; i++) {
                arrayList.add(ParticipantEntity.CREATOR.createFromParcel(parcel));
            }
            return new InvitationEntity(1, gameEntity, readString, readLong, readInt, participantEntity, arrayList, -1);
        }
    }

    InvitationEntity(int i, GameEntity gameEntity, String str, long j, int i2, ParticipantEntity participantEntity, ArrayList<ParticipantEntity> arrayList, int i3) {
        this.ab = i;
        this.eE = gameEntity;
        this.eF = str;
        this.eG = j;
        this.eH = i2;
        this.eI = participantEntity;
        this.eJ = arrayList;
        this.eK = i3;
    }

    InvitationEntity(Invitation invitation) {
        this.ab = 1;
        this.eE = new GameEntity(invitation.getGame());
        this.eF = invitation.getInvitationId();
        this.eG = invitation.getCreationTimestamp();
        this.eH = invitation.aL();
        this.eK = invitation.getVariant();
        String participantId = invitation.getInviter().getParticipantId();
        Object obj = null;
        ArrayList participants = invitation.getParticipants();
        int size = participants.size();
        this.eJ = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            Participant participant = (Participant) participants.get(i);
            if (participant.getParticipantId().equals(participantId)) {
                obj = participant;
            }
            this.eJ.add((ParticipantEntity) participant.freeze());
        }
        C0242s.m1205b(obj, (Object) "Must have a valid inviter!");
        this.eI = (ParticipantEntity) obj.freeze();
    }

    static int m212a(Invitation invitation) {
        return C0241r.hashCode(invitation.getGame(), invitation.getInvitationId(), Long.valueOf(invitation.getCreationTimestamp()), Integer.valueOf(invitation.aL()), invitation.getInviter(), invitation.getParticipants(), Integer.valueOf(invitation.getVariant()));
    }

    static boolean m213a(Invitation invitation, Object obj) {
        if (!(obj instanceof Invitation)) {
            return false;
        }
        if (invitation != obj) {
            Invitation invitation2 = (Invitation) obj;
            if (!(C0241r.m1200a(invitation2.getGame(), invitation.getGame()) && C0241r.m1200a(invitation2.getInvitationId(), invitation.getInvitationId()) && C0241r.m1200a(Long.valueOf(invitation2.getCreationTimestamp()), Long.valueOf(invitation.getCreationTimestamp())) && C0241r.m1200a(Integer.valueOf(invitation2.aL()), Integer.valueOf(invitation.aL())) && C0241r.m1200a(invitation2.getInviter(), invitation.getInviter()) && C0241r.m1200a(invitation2.getParticipants(), invitation.getParticipants()) && C0241r.m1200a(Integer.valueOf(invitation2.getVariant()), Integer.valueOf(invitation.getVariant())))) {
                return false;
            }
        }
        return true;
    }

    static String m214b(Invitation invitation) {
        return C0241r.m1201c(invitation).m1199a("Game", invitation.getGame()).m1199a("InvitationId", invitation.getInvitationId()).m1199a("CreationTimestamp", Long.valueOf(invitation.getCreationTimestamp())).m1199a("InvitationType", Integer.valueOf(invitation.aL())).m1199a("Inviter", invitation.getInviter()).m1199a("Participants", invitation.getParticipants()).m1199a("Variant", Integer.valueOf(invitation.getVariant())).toString();
    }

    public int aL() {
        return this.eH;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return m213a(this, obj);
    }

    public Invitation freeze() {
        return this;
    }

    public long getCreationTimestamp() {
        return this.eG;
    }

    public Game getGame() {
        return this.eE;
    }

    public String getInvitationId() {
        return this.eF;
    }

    public Participant getInviter() {
        return this.eI;
    }

    public ArrayList<Participant> getParticipants() {
        return new ArrayList(this.eJ);
    }

    public int getVariant() {
        return this.eK;
    }

    public int hashCode() {
        return m212a(this);
    }

    public int m217i() {
        return this.ab;
    }

    public boolean isDataValid() {
        return true;
    }

    public String toString() {
        return m214b((Invitation) this);
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (m184w()) {
            this.eE.writeToParcel(parcel, i);
            parcel.writeString(this.eF);
            parcel.writeLong(this.eG);
            parcel.writeInt(this.eH);
            this.eI.writeToParcel(parcel, i);
            int size = this.eJ.size();
            parcel.writeInt(size);
            for (int i2 = 0; i2 < size; i2++) {
                ((ParticipantEntity) this.eJ.get(i2)).writeToParcel(parcel, i);
            }
            return;
        }
        C0095a.m208a(this, parcel, i);
    }
}
