package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class MarkerOptionsCreator implements Creator<MarkerOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m1337a(MarkerOptions markerOptions, Parcel parcel, int i) {
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
        C0065b.m146a(parcel, 10, markerOptions.isFlat());
        C0065b.m137a(parcel, 11, markerOptions.getRotation());
        C0065b.m137a(parcel, 12, markerOptions.getInfoWindowAnchorU());
        C0065b.m137a(parcel, 13, markerOptions.getInfoWindowAnchorV());
        C0065b.m134C(parcel, d);
    }

    public MarkerOptions createFromParcel(Parcel parcel) {
        int c = C0064a.m105c(parcel);
        int i = 0;
        LatLng latLng = null;
        String str = null;
        String str2 = null;
        IBinder iBinder = null;
        float f = 0.0f;
        float f2 = 0.0f;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        float f3 = 0.0f;
        float f4 = 0.5f;
        float f5 = 0.0f;
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
                    str = C0064a.m116l(parcel, b);
                    break;
                case 4:
                    str2 = C0064a.m116l(parcel, b);
                    break;
                case 5:
                    iBinder = C0064a.m118m(parcel, b);
                    break;
                case 6:
                    f = C0064a.m113i(parcel, b);
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
                case 10:
                    z3 = C0064a.m107c(parcel, b);
                    break;
                case 11:
                    f3 = C0064a.m113i(parcel, b);
                    break;
                case 12:
                    f4 = C0064a.m113i(parcel, b);
                    break;
                case 13:
                    f5 = C0064a.m113i(parcel, b);
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new MarkerOptions(i, latLng, str, str2, iBinder, f, f2, z, z2, z3, f3, f4, f5);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public MarkerOptions[] newArray(int i) {
        return new MarkerOptions[i];
    }
}
