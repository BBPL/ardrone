package com.google.android.gms.games.multiplayer;

import android.os.Parcel;
import com.google.android.gms.common.data.C0044b;
import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.games.C0088b;
import com.google.android.gms.games.Game;
import com.google.android.gms.internal.C0242s;
import java.util.ArrayList;

public final class C0099b extends C0044b implements Invitation {
    private final ArrayList<Participant> eJ;
    private final Game eL;
    private final C0100d eM;

    C0099b(C0051d c0051d, int i, int i2) {
        super(c0051d, i);
        this.eL = new C0088b(c0051d, i);
        this.eJ = new ArrayList(i2);
        String string = getString("external_inviter_id");
        Object obj = null;
        for (int i3 = 0; i3 < i2; i3++) {
            C0100d c0100d = new C0100d(this.S, this.V + i3);
            if (c0100d.getParticipantId().equals(string)) {
                obj = c0100d;
            }
            this.eJ.add(c0100d);
        }
        this.eM = (C0100d) C0242s.m1205b(obj, (Object) "Must have a valid inviter!");
    }

    public int aL() {
        return getInteger("type");
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return InvitationEntity.m213a(this, obj);
    }

    public Invitation freeze() {
        return new InvitationEntity(this);
    }

    public long getCreationTimestamp() {
        return getLong("creation_timestamp");
    }

    public Game getGame() {
        return this.eL;
    }

    public String getInvitationId() {
        return getString("external_invitation_id");
    }

    public Participant getInviter() {
        return this.eM;
    }

    public ArrayList<Participant> getParticipants() {
        return this.eJ;
    }

    public int getVariant() {
        return getInteger("variant");
    }

    public int hashCode() {
        return InvitationEntity.m212a(this);
    }

    public String toString() {
        return InvitationEntity.m214b((Invitation) this);
    }

    public void writeToParcel(Parcel parcel, int i) {
        ((InvitationEntity) freeze()).writeToParcel(parcel, i);
    }
}
