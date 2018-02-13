package com.google.android.gms.games.multiplayer;

import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.common.data.C0053f;

public final class InvitationBuffer extends C0053f<Invitation> {
    public InvitationBuffer(C0051d c0051d) {
        super(c0051d);
    }

    protected /* synthetic */ Object mo410a(int i, int i2) {
        return getEntry(i, i2);
    }

    protected Invitation getEntry(int i, int i2) {
        return new C0099b(this.S, i, i2);
    }

    protected String getPrimaryDataMarkerColumn() {
        return "external_invitation_id";
    }
}
