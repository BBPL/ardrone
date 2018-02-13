package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class LatLngCreator implements Creator<LatLng> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m1335a(LatLng latLng, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, latLng.m1325i());
        C0065b.m136a(parcel, 2, latLng.latitude);
        C0065b.m136a(parcel, 3, latLng.longitude);
        C0065b.m134C(parcel, d);
    }

    public LatLng createFromParcel(Parcel parcel) {
        double d = 0.0d;
        int c = C0064a.m105c(parcel);
        int i = 0;
        double d2 = 0.0d;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    i = C0064a.m110f(parcel, b);
                    break;
                case 2:
                    d = C0064a.m114j(parcel, b);
                    break;
                case 3:
                    d2 = C0064a.m114j(parcel, b);
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new LatLng(i, d, d2);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public LatLng[] newArray(int i) {
        return new LatLng[i];
    }
}
