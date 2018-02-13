package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class C0335g {
    static void m1358a(PolygonOptions polygonOptions, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, polygonOptions.m1338i());
        C0065b.m152b(parcel, 2, polygonOptions.getPoints(), false);
        C0065b.m154c(parcel, 3, polygonOptions.br(), false);
        C0065b.m137a(parcel, 4, polygonOptions.getStrokeWidth());
        C0065b.m153c(parcel, 5, polygonOptions.getStrokeColor());
        C0065b.m153c(parcel, 6, polygonOptions.getFillColor());
        C0065b.m137a(parcel, 7, polygonOptions.getZIndex());
        C0065b.m146a(parcel, 8, polygonOptions.isVisible());
        C0065b.m146a(parcel, 9, polygonOptions.isGeodesic());
        C0065b.m134C(parcel, d);
    }
}
