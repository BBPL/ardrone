package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import java.util.ArrayList;
import java.util.List;

public class PolygonOptionsCreator implements Creator<PolygonOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m1339a(PolygonOptions polygonOptions, Parcel parcel, int i) {
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

    public PolygonOptions createFromParcel(Parcel parcel) {
        float f = 0.0f;
        boolean z = false;
        int c = C0064a.m105c(parcel);
        List list = null;
        List arrayList = new ArrayList();
        boolean z2 = false;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        float f2 = 0.0f;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    i = C0064a.m110f(parcel, b);
                    break;
                case 2:
                    list = C0064a.m106c(parcel, b, LatLng.CREATOR);
                    break;
                case 3:
                    C0064a.m101a(parcel, b, arrayList, getClass().getClassLoader());
                    break;
                case 4:
                    f = C0064a.m113i(parcel, b);
                    break;
                case 5:
                    i2 = C0064a.m110f(parcel, b);
                    break;
                case 6:
                    i3 = C0064a.m110f(parcel, b);
                    break;
                case 7:
                    f2 = C0064a.m113i(parcel, b);
                    break;
                case 8:
                    z = C0064a.m107c(parcel, b);
                    break;
                case 9:
                    z2 = C0064a.m107c(parcel, b);
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new PolygonOptions(i, list, arrayList, f, i2, i3, f2, z, z2);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public PolygonOptions[] newArray(int i) {
        return new PolygonOptions[i];
    }
}
