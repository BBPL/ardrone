package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class C0359j {
    static void m1394a(TileOverlayOptions tileOverlayOptions, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, tileOverlayOptions.m1346i());
        C0065b.m140a(parcel, 2, tileOverlayOptions.bs(), false);
        C0065b.m146a(parcel, 3, tileOverlayOptions.isVisible());
        C0065b.m137a(parcel, 4, tileOverlayOptions.getZIndex());
        C0065b.m134C(parcel, d);
    }
}
