package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class C0333e {
    static void m1356a(LatLng latLng, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, latLng.m1325i());
        C0065b.m136a(parcel, 2, latLng.latitude);
        C0065b.m136a(parcel, 3, latLng.longitude);
        C0065b.m134C(parcel, d);
    }
}
