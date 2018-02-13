package com.google.android.gms.maps;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import com.google.android.gms.maps.model.CameraPosition;

public class GoogleMapOptionsCreator implements Creator<GoogleMapOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m1275a(GoogleMapOptions googleMapOptions, Parcel parcel, int i) {
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

    public GoogleMapOptions createFromParcel(Parcel parcel) {
        int i = 0;
        int c = C0064a.m105c(parcel);
        CameraPosition cameraPosition = null;
        int i2 = 0;
        byte b = (byte) 0;
        byte b2 = (byte) 0;
        byte b3 = (byte) 0;
        byte b4 = (byte) 0;
        byte b5 = (byte) 0;
        byte b6 = (byte) 0;
        byte b7 = (byte) 0;
        byte b8 = (byte) 0;
        while (parcel.dataPosition() < c) {
            int b9 = C0064a.m102b(parcel);
            switch (C0064a.m117m(b9)) {
                case 1:
                    i = C0064a.m110f(parcel, b9);
                    break;
                case 2:
                    b = C0064a.m108d(parcel, b9);
                    break;
                case 3:
                    b2 = C0064a.m108d(parcel, b9);
                    break;
                case 4:
                    i2 = C0064a.m110f(parcel, b9);
                    break;
                case 5:
                    cameraPosition = (CameraPosition) C0064a.m99a(parcel, b9, CameraPosition.CREATOR);
                    break;
                case 6:
                    b3 = C0064a.m108d(parcel, b9);
                    break;
                case 7:
                    b4 = C0064a.m108d(parcel, b9);
                    break;
                case 8:
                    b5 = C0064a.m108d(parcel, b9);
                    break;
                case 9:
                    b6 = C0064a.m108d(parcel, b9);
                    break;
                case 10:
                    b7 = C0064a.m108d(parcel, b9);
                    break;
                case 11:
                    b8 = C0064a.m108d(parcel, b9);
                    break;
                default:
                    C0064a.m103b(parcel, b9);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new GoogleMapOptions(i, b, b2, i2, cameraPosition, b3, b4, b5, b6, b7, b8);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public GoogleMapOptions[] newArray(int i) {
        return new GoogleMapOptions[i];
    }
}
