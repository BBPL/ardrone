package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class CircleOptionsCreator implements Creator<CircleOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m1321a(CircleOptions circleOptions, Parcel parcel, int i) {
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

    public CircleOptions createFromParcel(Parcel parcel) {
        float f = 0.0f;
        boolean z = false;
        int c = C0064a.m105c(parcel);
        LatLng latLng = null;
        double d = 0.0d;
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
                    latLng = (LatLng) C0064a.m99a(parcel, b, LatLng.CREATOR);
                    break;
                case 3:
                    d = C0064a.m114j(parcel, b);
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
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new CircleOptions(i, latLng, d, f, i2, i3, f2, z);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public CircleOptions[] newArray(int i) {
        return new CircleOptions[i];
    }
}
