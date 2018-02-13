package com.google.android.gms.games.multiplayer;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import com.google.android.gms.games.GameEntity;
import java.util.ArrayList;

public class C0095a implements Creator<InvitationEntity> {
    static void m208a(InvitationEntity invitationEntity, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m142a(parcel, 1, invitationEntity.getGame(), i, false);
        C0065b.m153c(parcel, 1000, invitationEntity.m217i());
        C0065b.m143a(parcel, 2, invitationEntity.getInvitationId(), false);
        C0065b.m138a(parcel, 3, invitationEntity.getCreationTimestamp());
        C0065b.m153c(parcel, 4, invitationEntity.aL());
        C0065b.m142a(parcel, 5, invitationEntity.getInviter(), i, false);
        C0065b.m152b(parcel, 6, invitationEntity.getParticipants(), false);
        C0065b.m153c(parcel, 7, invitationEntity.getVariant());
        C0065b.m134C(parcel, d);
    }

    public InvitationEntity[] m209H(int i) {
        return new InvitationEntity[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return mo439p(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return m209H(i);
    }

    public InvitationEntity mo439p(Parcel parcel) {
        int i = 0;
        GameEntity gameEntity = null;
        int c = C0064a.m105c(parcel);
        long j = 0;
        String str = null;
        ParticipantEntity participantEntity = null;
        ArrayList arrayList = null;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    gameEntity = (GameEntity) C0064a.m99a(parcel, b, GameEntity.CREATOR);
                    break;
                case 2:
                    str = C0064a.m116l(parcel, b);
                    break;
                case 3:
                    j = C0064a.m111g(parcel, b);
                    break;
                case 4:
                    i2 = C0064a.m110f(parcel, b);
                    break;
                case 5:
                    participantEntity = (ParticipantEntity) C0064a.m99a(parcel, b, ParticipantEntity.CREATOR);
                    break;
                case 6:
                    arrayList = C0064a.m106c(parcel, b, ParticipantEntity.CREATOR);
                    break;
                case 7:
                    i3 = C0064a.m110f(parcel, b);
                    break;
                case 1000:
                    i = C0064a.m110f(parcel, b);
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new InvitationEntity(i, gameEntity, str, j, i2, participantEntity, arrayList, i3);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }
}
