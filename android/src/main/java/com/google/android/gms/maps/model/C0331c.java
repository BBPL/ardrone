package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class C0331c {
    static void m1354a(GroundOverlayOptions groundOverlayOptions, Parcel parcel, int i) {
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
}
