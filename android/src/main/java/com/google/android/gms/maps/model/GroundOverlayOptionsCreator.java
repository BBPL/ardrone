package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class GroundOverlayOptionsCreator implements Creator<GroundOverlayOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m1324a(GroundOverlayOptions groundOverlayOptions, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, groundOverlayOptions.m1323i());
        C0065b.m140a(parcel, 2, groundOverlayOptions.bp(), false);
        C0065b.m142a(parcel, 3, groundOverlayOptions.getLocation(), i, false);
        C0065b.m137a(parcel, 4, groundOverlayOptions.getWidth());
        C0065b.m137a(parcel, 5, groundOverlayOptions.getHeight());
        C0065b.m142a(parcel, 6, groundOverlayOptions.getBounds(), i, false);
        C0065b.m137a(parcel, 7, groundOverlayOptions.getBearing());
        C0065b.m137a(parcel, 8, groundOverlayOptions.getZIndex());
        C0065b.m146a(parcel, 9, groundOverlayOptions.isVisible());
        C0065b.m137a(parcel, 10, groundOverlayOptions.getTransparency());
        C0065b.m137a(parcel, 11, groundOverlayOptions.getAnchorU());
        C0065b.m137a(parcel, 12, groundOverlayOptions.getAnchorV());
        C0065b.m134C(parcel, d);
    }

    public GroundOverlayOptions createFromParcel(Parcel parcel) {
        int c = C0064a.m105c(parcel);
        int i = 0;
        IBinder iBinder = null;
        LatLng latLng = null;
        float f = 0.0f;
        float f2 = 0.0f;
        LatLngBounds latLngBounds = null;
        float f3 = 0.0f;
        float f4 = 0.0f;
        boolean z = false;
        float f5 = 0.0f;
        float f6 = 0.0f;
        float f7 = 0.0f;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    i = C0064a.m110f(parcel, b);
                    break;
                case 2:
                    iBinder = C0064a.m118m(parcel, b);
                    break;
                case 3:
                    latLng = (LatLng) C0064a.m99a(parcel, b, LatLng.CREATOR);
                    break;
                case 4:
                    f = C0064a.m113i(parcel, b);
                    break;
                case 5:
                    f2 = C0064a.m113i(parcel, b);
                    break;
                case 6:
                    latLngBounds = (LatLngBounds) C0064a.m99a(parcel, b, LatLngBounds.CREATOR);
                    break;
                case 7:
                    f3 = C0064a.m113i(parcel, b);
                    break;
                case 8:
                    f4 = C0064a.m113i(parcel, b);
                    break;
                case 9:
                    z = C0064a.m107c(parcel, b);
                    break;
                case 10:
                    f5 = C0064a.m113i(parcel, b);
                    break;
                case 11:
                    f6 = C0064a.m113i(parcel, b);
                    break;
                case 12:
                    f7 = C0064a.m113i(parcel, b);
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new GroundOverlayOptions(i, iBinder, latLng, f, f2, latLngBounds, f3, f4, z, f5, f6, f7);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public GroundOverlayOptions[] newArray(int i) {
        return new GroundOverlayOptions[i];
    }
}
