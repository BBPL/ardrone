package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class C0332d {
    static void m1355a(LatLngBounds latLngBounds, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, latLngBounds.m1333i());
        C0065b.m142a(parcel, 2, latLngBounds.southwest, i, false);
        C0065b.m142a(parcel, 3, latLngBounds.northeast, i, false);
        C0065b.m134C(parcel, d);
    }
}
