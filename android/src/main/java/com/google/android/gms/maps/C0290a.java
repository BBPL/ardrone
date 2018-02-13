package com.google.android.gms.maps;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class C0290a {
    static void m1281a(GoogleMapOptions googleMapOptions, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, googleMapOptions.m1274i());
        C0065b.m135a(parcel, 2, googleMapOptions.aZ());
        C0065b.m135a(parcel, 3, googleMapOptions.ba());
        C0065b.m153c(parcel, 4, googleMapOptions.getMapType());
        C0065b.m142a(parcel, 5, googleMapOptions.getCamera(), i, false);
        C0065b.m135a(parcel, 6, googleMapOptions.bb());
        C0065b.m135a(parcel, 7, googleMapOptions.bc());
        C0065b.m135a(parcel, 8, googleMapOptions.bd());
        C0065b.m135a(parcel, 9, googleMapOptions.be());
        C0065b.m135a(parcel, 10, googleMapOptions.bf());
        C0065b.m135a(parcel, 11, googleMapOptions.bg());
        C0065b.m134C(parcel, d);
    }
}
