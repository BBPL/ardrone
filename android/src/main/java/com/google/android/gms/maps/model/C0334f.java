package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class C0334f {
    static void m1357a(MarkerOptions markerOptions, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, markerOptions.m1336i());
        C0065b.m142a(parcel, 2, markerOptions.getPosition(), i, false);
        C0065b.m143a(parcel, 3, markerOptions.getTitle(), false);
        C0065b.m143a(parcel, 4, markerOptions.getSnippet(), false);
        C0065b.m140a(parcel, 5, markerOptions.bq(), false);
        C0065b.m137a(parcel, 6, markerOptions.getAnchorU());
        C0065b.m137a(parcel, 7, markerOptions.getAnchorV());
        C0065b.m146a(parcel, 8, markerOptions.isDraggable());
        C0065b.m146a(parcel, 9, markerOptions.isVisible());
        C0065b.m134C(parcel, d);
    }
}
