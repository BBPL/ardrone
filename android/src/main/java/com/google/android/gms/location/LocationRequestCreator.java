package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class LocationRequestCreator implements Creator<LocationRequest> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m1243a(LocationRequest locationRequest, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, locationRequest.mPriority);
        C0065b.m153c(parcel, 1000, locationRequest.m1242i());
        C0065b.m138a(parcel, 2, locationRequest.fB);
        C0065b.m138a(parcel, 3, locationRequest.fC);
        C0065b.m146a(parcel, 4, locationRequest.fD);
        C0065b.m138a(parcel, 5, locationRequest.fw);
        C0065b.m153c(parcel, 6, locationRequest.fE);
        C0065b.m137a(parcel, 7, locationRequest.fF);
        C0065b.m134C(parcel, d);
    }

    public LocationRequest createFromParcel(Parcel parcel) {
        boolean z = false;
        int c = C0064a.m105c(parcel);
        int i = 102;
        long j = 3600000;
        long j2 = 600000;
        long j3 = Long.MAX_VALUE;
        int i2 = Integer.MAX_VALUE;
        float f = 0.0f;
        int i3 = 0;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    i = C0064a.m110f(parcel, b);
                    break;
                case 2:
                    j = C0064a.m111g(parcel, b);
                    break;
                case 3:
                    j2 = C0064a.m111g(parcel, b);
                    break;
                case 4:
                    z = C0064a.m107c(parcel, b);
                    break;
                case 5:
                    j3 = C0064a.m111g(parcel, b);
                    break;
                case 6:
                    i2 = C0064a.m110f(parcel, b);
                    break;
                case 7:
                    f = C0064a.m113i(parcel, b);
                    break;
                case 1000:
                    i3 = C0064a.m110f(parcel, b);
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new LocationRequest(i3, i, j, j2, z, j3, i2, f);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public LocationRequest[] newArray(int i) {
        return new LocationRequest[i];
    }
}
