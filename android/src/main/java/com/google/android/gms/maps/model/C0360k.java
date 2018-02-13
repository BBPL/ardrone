package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class C0360k {
    static void m1395a(VisibleRegion visibleRegion, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, visibleRegion.m1350i());
        C0065b.m142a(parcel, 2, visibleRegion.nearLeft, i, false);
        C0065b.m142a(parcel, 3, visibleRegion.nearRight, i, false);
        C0065b.m142a(parcel, 4, visibleRegion.farLeft, i, false);
        C0065b.m142a(parcel, 5, visibleRegion.farRight, i, false);
        C0065b.m142a(parcel, 6, visibleRegion.latLngBounds, i, false);
        C0065b.m134C(parcel, d);
    }
}
