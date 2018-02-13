package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class C0337i {
    static void m1360a(Tile tile, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, tile.m1342i());
        C0065b.m153c(parcel, 2, tile.width);
        C0065b.m153c(parcel, 3, tile.height);
        C0065b.m147a(parcel, 4, tile.data, false);
        C0065b.m134C(parcel, d);
    }
}
