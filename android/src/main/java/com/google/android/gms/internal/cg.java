package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import com.google.android.gms.internal.cc.C0202b.C0200a;
import java.util.HashSet;
import java.util.Set;

public class cg implements Creator<C0200a> {
    static void m1089a(C0200a c0200a, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        Set bH = c0200a.bH();
        if (bH.contains(Integer.valueOf(1))) {
            C0065b.m153c(parcel, 1, c0200a.m1028i());
        }
        if (bH.contains(Integer.valueOf(2))) {
            C0065b.m153c(parcel, 2, c0200a.getLeftImageOffset());
        }
        if (bH.contains(Integer.valueOf(3))) {
            C0065b.m153c(parcel, 3, c0200a.getTopImageOffset());
        }
        C0065b.m134C(parcel, d);
    }

    public C0200a m1090B(Parcel parcel) {
        int i = 0;
        int c = C0064a.m105c(parcel);
        Set hashSet = new HashSet();
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    i3 = C0064a.m110f(parcel, b);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case 2:
                    i2 = C0064a.m110f(parcel, b);
                    hashSet.add(Integer.valueOf(2));
                    break;
                case 3:
                    i = C0064a.m110f(parcel, b);
                    hashSet.add(Integer.valueOf(3));
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new C0200a(hashSet, i3, i2, i);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public C0200a[] ab(int i) {
        return new C0200a[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m1090B(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return ab(i);
    }
}
