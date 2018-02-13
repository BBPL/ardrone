package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class TileOverlayOptionsCreator implements Creator<TileOverlayOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m1347a(TileOverlayOptions tileOverlayOptions, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, tileOverlayOptions.m1346i());
        C0065b.m140a(parcel, 2, tileOverlayOptions.bs(), false);
        C0065b.m146a(parcel, 3, tileOverlayOptions.isVisible());
        C0065b.m137a(parcel, 4, tileOverlayOptions.getZIndex());
        C0065b.m134C(parcel, d);
    }

    public TileOverlayOptions createFromParcel(Parcel parcel) {
        boolean z = false;
        int c = C0064a.m105c(parcel);
        IBinder iBinder = null;
        float f = 0.0f;
        int i = 0;
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
                    z = C0064a.m107c(parcel, b);
                    break;
                case 4:
                    f = C0064a.m113i(parcel, b);
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new TileOverlayOptions(i, iBinder, z, f);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public TileOverlayOptions[] newArray(int i) {
        return new TileOverlayOptions[i];
    }
}
