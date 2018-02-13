package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class LatLngBoundsCreator implements Creator<LatLngBounds> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m1334a(LatLngBounds latLngBounds, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, latLngBounds.m1333i());
        C0065b.m142a(parcel, 2, latLngBounds.southwest, i, false);
        C0065b.m142a(parcel, 3, latLngBounds.northeast, i, false);
        C0065b.m134C(parcel, d);
    }

    public LatLngBounds createFromParcel(Parcel parcel) {
        LatLng latLng = null;
        int c = C0064a.m105c(parcel);
        int i = 0;
        LatLng latLng2 = null;
        while (parcel.dataPosition() < c) {
            int f;
            LatLng latLng3;
            int b = C0064a.m102b(parcel);
            LatLng latLng4;
            switch (C0064a.m117m(b)) {
                case 1:
                    latLng4 = latLng;
                    latLng = latLng2;
                    f = C0064a.m110f(parcel, b);
                    latLng3 = latLng4;
                    break;
                case 2:
                    latLng3 = (LatLng) C0064a.m99a(parcel, b, LatLng.CREATOR);
                    latLng = latLng2;
                    f = i;
                    break;
                case 3:
                    f = i;
                    latLng4 = (LatLng) C0064a.m99a(parcel, b, LatLng.CREATOR);
                    latLng3 = latLng;
                    latLng = latLng4;
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    latLng3 = latLng;
                    latLng = latLng2;
                    f = i;
                    break;
            }
            i = f;
            latLng2 = latLng;
            latLng = latLng3;
        }
        if (parcel.dataPosition() == c) {
            return new LatLngBounds(i, latLng, latLng2);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public LatLngBounds[] newArray(int i) {
        return new LatLngBounds[i];
    }
}
