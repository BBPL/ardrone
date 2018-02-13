package com.google.android.gms.games;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class C0085c implements Creator<PlayerEntity> {
    static void m192a(PlayerEntity playerEntity, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m143a(parcel, 1, playerEntity.getPlayerId(), false);
        C0065b.m153c(parcel, 1000, playerEntity.m201i());
        C0065b.m143a(parcel, 2, playerEntity.getDisplayName(), false);
        C0065b.m142a(parcel, 3, playerEntity.getIconImageUri(), i, false);
        C0065b.m142a(parcel, 4, playerEntity.getHiResImageUri(), i, false);
        C0065b.m138a(parcel, 5, playerEntity.getRetrievedTimestamp());
        C0065b.m134C(parcel, d);
    }

    public PlayerEntity[] m193A(int i) {
        return new PlayerEntity[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return mo384o(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return m193A(i);
    }

    public PlayerEntity mo384o(Parcel parcel) {
        String str = null;
        int c = C0064a.m105c(parcel);
        int i = 0;
        long j = 0;
        String str2 = null;
        Uri uri = null;
        Uri uri2 = null;
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
                    j = C0064a.m111g(parcel, b);
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
            return new PlayerEntity(i, str, str2, uri, uri2, j);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }
}
