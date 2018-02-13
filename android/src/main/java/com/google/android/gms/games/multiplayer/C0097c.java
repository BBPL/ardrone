package com.google.android.gms.games.multiplayer;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import com.google.android.gms.games.PlayerEntity;

public class C0097c implements Creator<ParticipantEntity> {
    static void m218a(ParticipantEntity participantEntity, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m143a(parcel, 1, participantEntity.getParticipantId(), false);
        C0065b.m153c(parcel, 1000, participantEntity.m227i());
        C0065b.m143a(parcel, 2, participantEntity.getDisplayName(), false);
        C0065b.m142a(parcel, 3, participantEntity.getIconImageUri(), i, false);
        C0065b.m142a(parcel, 4, participantEntity.getHiResImageUri(), i, false);
        C0065b.m153c(parcel, 5, participantEntity.getStatus());
        C0065b.m143a(parcel, 6, participantEntity.aM(), false);
        C0065b.m146a(parcel, 7, participantEntity.isConnectedToRoom());
        C0065b.m142a(parcel, 8, participantEntity.getPlayer(), i, false);
        C0065b.m153c(parcel, 9, participantEntity.aN());
        C0065b.m134C(parcel, d);
    }

    public ParticipantEntity[] m219I(int i) {
        return new ParticipantEntity[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return mo448q(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return m219I(i);
    }

    public ParticipantEntity mo448q(Parcel parcel) {
        boolean z = false;
        String str = null;
        int c = C0064a.m105c(parcel);
        String str2 = null;
        Uri uri = null;
        Uri uri2 = null;
        String str3 = null;
        PlayerEntity playerEntity = null;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    str = C0064a.m116l(parcel, b);
                    break;
                case 2:
                    str2 = C0064a.m116l(parcel, b);
                    break;
                case 3:
                    uri = (Uri) C0064a.m99a(parcel, b, Uri.CREATOR);
                    break;
                case 4:
                    uri2 = (Uri) C0064a.m99a(parcel, b, Uri.CREATOR);
                    break;
                case 5:
                    i2 = C0064a.m110f(parcel, b);
                    break;
                case 6:
                    str3 = C0064a.m116l(parcel, b);
                    break;
                case 7:
                    z = C0064a.m107c(parcel, b);
                    break;
                case 8:
                    playerEntity = (PlayerEntity) C0064a.m99a(parcel, b, PlayerEntity.CREATOR);
                    break;
                case 9:
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
            return new ParticipantEntity(i, str, str2, uri, uri2, i2, str3, z, playerEntity, i3);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }
}
