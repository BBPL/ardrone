package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class bj implements Creator<bi> {
    static void m807a(bi biVar, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m143a(parcel, 1, biVar.getRequestId(), false);
        C0065b.m153c(parcel, 1000, biVar.m806i());
        C0065b.m138a(parcel, 2, biVar.getExpirationTime());
        C0065b.m145a(parcel, 3, biVar.aT());
        C0065b.m136a(parcel, 4, biVar.getLatitude());
        C0065b.m136a(parcel, 5, biVar.getLongitude());
        C0065b.m137a(parcel, 6, biVar.aU());
        C0065b.m153c(parcel, 7, biVar.aV());
        C0065b.m134C(parcel, d);
    }

    public bi[] m808R(int i) {
        return new bi[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m809t(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return m808R(i);
    }

    public bi m809t(Parcel parcel) {
        double d = 0.0d;
        int i = 0;
        int c = C0064a.m105c(parcel);
        String str = null;
        float f = 0.0f;
        long j = 0;
        int i2 = 0;
        double d2 = 0.0d;
        short s = (short) 0;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    str = C0064a.m116l(parcel, b);
                    break;
                case 2:
                    j = C0064a.m111g(parcel, b);
                    break;
                case 3:
                    s = C0064a.m109e(parcel, b);
                    break;
                case 4:
                    d = C0064a.m114j(parcel, b);
                    break;
                case 5:
                    d2 = C0064a.m114j(parcel, b);
                    break;
                case 6:
                    f = C0064a.m113i(parcel, b);
                    break;
                case 7:
                    i2 = C0064a.m110f(parcel, b);
                    break;
                case 1000:
                    i = C0064a.m110f(parcel, b);
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new bi(i, str, i2, s, d, d2, f, j);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }
}
