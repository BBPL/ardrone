package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class C0329a {
    static void m1352a(CameraPosition cameraPosition, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, cameraPosition.m1318i());
        C0065b.m142a(parcel, 2, cameraPosition.target, i, false);
        C0065b.m137a(parcel, 3, cameraPosition.zoom);
        C0065b.m137a(parcel, 4, cameraPosition.tilt);
        C0065b.m137a(parcel, 5, cameraPosition.bearing);
        C0065b.m134C(parcel, d);
    }
}
