package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class TileCreator implements Creator<Tile> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m1343a(Tile tile, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, tile.m1342i());
        C0065b.m153c(parcel, 2, tile.width);
        C0065b.m153c(parcel, 3, tile.height);
        C0065b.m147a(parcel, 4, tile.data, false);
        C0065b.m134C(parcel, d);
    }

    public Tile createFromParcel(Parcel parcel) {
        int i = 0;
        int c = C0064a.m105c(parcel);
        byte[] bArr = null;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    i3 = C0064a.m110f(parcel, b);
                    break;
                case 2:
                    i2 = C0064a.m110f(parcel, b);
                    break;
                case 3:
                    i = C0064a.m110f(parcel, b);
                    break;
                case 4:
                    bArr = C0064a.m120o(parcel, b);
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new Tile(i3, i2, i, bArr);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public Tile[] newArray(int i) {
        return new Tile[i];
    }
}
