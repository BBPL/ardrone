package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class C0330b {
    static void m1353a(CircleOptions circleOptions, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, circleOptions.m1320i());
        C0065b.m142a(parcel, 2, circleOptions.getCenter(), i, false);
        C0065b.m136a(parcel, 3, circleOptions.getRadius());
        C0065b.m137a(parcel, 4, circleOptions.getStrokeWidth());
        C0065b.m153c(parcel, 5, circleOptions.getStrokeColor());
        C0065b.m153c(parcel, 6, circleOptions.getFillColor());
        C0065b.m137a(parcel, 7, circleOptions.getZIndex());
        C0065b.m146a(parcel, 8, circleOptions.isVisible());
        C0065b.m134C(parcel, d);
    }
}
