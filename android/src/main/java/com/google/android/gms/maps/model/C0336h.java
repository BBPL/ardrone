package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class C0336h {
    static void m1359a(PolylineOptions polylineOptions, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, polylineOptions.m1340i());
        C0065b.m152b(parcel, 2, polylineOptions.getPoints(), false);
        C0065b.m137a(parcel, 3, polylineOptions.getWidth());
        C0065b.m153c(parcel, 4, polylineOptions.getColor());
        C0065b.m137a(parcel, 5, polylineOptions.getZIndex());
        C0065b.m146a(parcel, 6, polylineOptions.isVisible());
        C0065b.m146a(parcel, 7, polylineOptions.isGeodesic());
        C0065b.m134C(parcel, d);
    }
}
