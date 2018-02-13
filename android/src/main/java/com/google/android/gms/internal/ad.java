package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import com.google.android.gms.internal.ab.C0111a;

public class ad implements Creator<C0111a> {
    static void m266a(C0111a c0111a, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, c0111a.versionCode);
        C0065b.m143a(parcel, 2, c0111a.cr, false);
        C0065b.m153c(parcel, 3, c0111a.cs);
        C0065b.m134C(parcel, d);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m267h(parcel);
    }

    public C0111a m267h(Parcel parcel) {
        int i = 0;
        int c = C0064a.m105c(parcel);
        String str = null;
        int i2 = 0;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    i2 = C0064a.m110f(parcel, b);
                    break;
                case 2:
                    str = C0064a.m116l(parcel, b);
                    break;
                case 3:
                    i = C0064a.m110f(parcel, b);
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new C0111a(i2, str, i);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return m268q(i);
    }

    public C0111a[] m268q(int i) {
        return new C0111a[i];
    }
}
