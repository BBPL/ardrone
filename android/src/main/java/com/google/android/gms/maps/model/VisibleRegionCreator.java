package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class VisibleRegionCreator implements Creator<VisibleRegion> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m1351a(VisibleRegion visibleRegion, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, visibleRegion.m1350i());
        C0065b.m142a(parcel, 2, visibleRegion.nearLeft, i, false);
        C0065b.m142a(parcel, 3, visibleRegion.nearRight, i, false);
        C0065b.m142a(parcel, 4, visibleRegion.farLeft, i, false);
        C0065b.m142a(parcel, 5, visibleRegion.farRight, i, false);
        C0065b.m142a(parcel, 6, visibleRegion.latLngBounds, i, false);
        C0065b.m134C(parcel, d);
    }

    public VisibleRegion createFromParcel(Parcel parcel) {
        LatLng latLng = null;
        int c = C0064a.m105c(parcel);
        int i = 0;
        LatLng latLng2 = null;
        LatLng latLng3 = null;
        LatLng latLng4 = null;
        LatLngBounds latLngBounds = null;
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
                    latLng2 = (LatLng) C0064a.m99a(parcel, b, LatLng.CREATOR);
                    break;
                case 4:
                    latLng3 = (LatLng) C0064a.m99a(parcel, b, LatLng.CREATOR);
                    break;
                case 5:
                    latLng4 = (LatLng) C0064a.m99a(parcel, b, LatLng.CREATOR);
                    break;
                case 6:
                    latLngBounds = (LatLngBounds) C0064a.m99a(parcel, b, LatLngBounds.CREATOR);
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new VisibleRegion(i, latLng, latLng2, latLng3, latLng4, latLngBounds);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public VisibleRegion[] newArray(int i) {
        return new VisibleRegion[i];
    }
}
