package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class CameraPositionCreator implements Creator<CameraPosition> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m1319a(CameraPosition cameraPosition, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, cameraPosition.m1318i());
        C0065b.m142a(parcel, 2, cameraPosition.target, i, false);
        C0065b.m137a(parcel, 3, cameraPosition.zoom);
        C0065b.m137a(parcel, 4, cameraPosition.tilt);
        C0065b.m137a(parcel, 5, cameraPosition.bearing);
        C0065b.m134C(parcel, d);
    }

    public CameraPosition createFromParcel(Parcel parcel) {
        float f = 0.0f;
        int c = C0064a.m105c(parcel);
        int i = 0;
        LatLng latLng = null;
        float f2 = 0.0f;
        float f3 = 0.0f;
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
                    f = C0064a.m113i(parcel, b);
                    break;
                case 4:
                    f2 = C0064a.m113i(parcel, b);
                    break;
                case 5:
                    f3 = C0064a.m113i(parcel, b);
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new CameraPosition(i, latLng, f, f2, f3);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public CameraPosition[] newArray(int i) {
        return new CameraPosition[i];
    }
}
