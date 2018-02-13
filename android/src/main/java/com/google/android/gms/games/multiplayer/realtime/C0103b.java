package com.google.android.gms.games.multiplayer.realtime;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import com.google.android.gms.games.multiplayer.ParticipantEntity;
import java.util.ArrayList;

public class C0103b implements Creator<RoomEntity> {
    static void m231a(RoomEntity roomEntity, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m143a(parcel, 1, roomEntity.getRoomId(), false);
        C0065b.m153c(parcel, 1000, roomEntity.m240i());
        C0065b.m143a(parcel, 2, roomEntity.getCreatorId(), false);
        C0065b.m138a(parcel, 3, roomEntity.getCreationTimestamp());
        C0065b.m153c(parcel, 4, roomEntity.getStatus());
        C0065b.m143a(parcel, 5, roomEntity.getDescription(), false);
        C0065b.m153c(parcel, 6, roomEntity.getVariant());
        C0065b.m139a(parcel, 7, roomEntity.getAutoMatchCriteria(), false);
        C0065b.m152b(parcel, 8, roomEntity.getParticipants(), false);
        C0065b.m153c(parcel, 9, roomEntity.getAutoMatchWaitEstimateSeconds());
        C0065b.m134C(parcel, d);
    }

    public RoomEntity[] m232K(int i) {
        return new RoomEntity[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return mo460s(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return m232K(i);
    }

    public RoomEntity mo460s(Parcel parcel) {
        int i = 0;
        String str = null;
        int c = C0064a.m105c(parcel);
        long j = 0;
        String str2 = null;
        String str3 = null;
        Bundle bundle = null;
        ArrayList arrayList = null;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
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
                    j = C0064a.m111g(parcel, b);
                    break;
                case 4:
                    i2 = C0064a.m110f(parcel, b);
                    break;
                case 5:
                    str3 = C0064a.m116l(parcel, b);
                    break;
                case 6:
                    i3 = C0064a.m110f(parcel, b);
                    break;
                case 7:
                    bundle = C0064a.m119n(parcel, b);
                    break;
                case 8:
                    arrayList = C0064a.m106c(parcel, b, ParticipantEntity.CREATOR);
                    break;
                case 9:
                    i4 = C0064a.m110f(parcel, b);
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
            return new RoomEntity(i, str, str2, j, i2, str3, i3, bundle, arrayList, i4);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }
}
