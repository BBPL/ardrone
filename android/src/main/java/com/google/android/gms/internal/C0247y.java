package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class C0247y implements Creator<C0246x> {
    static void m1226a(C0246x c0246x, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, c0246x.getType());
        C0065b.m153c(parcel, 1000, c0246x.m1225i());
        C0065b.m153c(parcel, 2, c0246x.m1219I());
        C0065b.m143a(parcel, 3, c0246x.m1220J(), false);
        C0065b.m143a(parcel, 4, c0246x.m1221K(), false);
        C0065b.m143a(parcel, 5, c0246x.getDisplayName(), false);
        C0065b.m143a(parcel, 6, c0246x.m1222L(), false);
        C0065b.m134C(parcel, d);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m1227e(parcel);
    }

    public C0246x m1227e(Parcel parcel) {
        int i = 0;
        String str = null;
        int c = C0064a.m105c(parcel);
        String str2 = null;
        String str3 = null;
        String str4 = null;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    i2 = C0064a.m110f(parcel, b);
                    break;
                case 2:
                    i3 = C0064a.m110f(parcel, b);
                    break;
                case 3:
                    str = C0064a.m116l(parcel, b);
                    break;
                case 4:
                    str2 = C0064a.m116l(parcel, b);
                    break;
                case 5:
                    str3 = C0064a.m116l(parcel, b);
                    break;
                case 6:
                    str4 = C0064a.m116l(parcel, b);
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
            return new C0246x(i, i2, i3, str, str2, str3, str4);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public C0246x[] m1228n(int i) {
        return new C0246x[i];
    }

    public /* synthetic */ Object[] newArray(int i) {
        return m1228n(i);
    }
}
