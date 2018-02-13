package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import java.util.List;

public class PolylineOptionsCreator implements Creator<PolylineOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m1341a(PolylineOptions polylineOptions, Parcel parcel, int i) {
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

    public PolylineOptions createFromParcel(Parcel parcel) {
        float f = 0.0f;
        boolean z = false;
        int c = C0064a.m105c(parcel);
        List list = null;
        boolean z2 = false;
        int i = 0;
        int i2 = 0;
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
                    f = C0064a.m113i(parcel, b);
                    break;
                case 4:
                    i2 = C0064a.m110f(parcel, b);
                    break;
                case 5:
                    f2 = C0064a.m113i(parcel, b);
                    break;
                case 6:
                    z = C0064a.m107c(parcel, b);
                    break;
                case 7:
                    z2 = C0064a.m107c(parcel, b);
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new PolylineOptions(i, list, f, i2, f2, z, z2);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public PolylineOptions[] newArray(int i) {
        return new PolylineOptions[i];
    }
}
