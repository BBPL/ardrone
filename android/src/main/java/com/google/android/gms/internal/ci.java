package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import com.google.android.gms.internal.cc.C0203c;
import java.util.HashSet;
import java.util.Set;

public class ci implements Creator<C0203c> {
    static void m1093a(C0203c c0203c, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        Set bH = c0203c.bH();
        if (bH.contains(Integer.valueOf(1))) {
            C0065b.m153c(parcel, 1, c0203c.m1046i());
        }
        if (bH.contains(Integer.valueOf(2))) {
            C0065b.m143a(parcel, 2, c0203c.getUrl(), true);
        }
        C0065b.m134C(parcel, d);
    }

    public C0203c m1094D(Parcel parcel) {
        int c = C0064a.m105c(parcel);
        Set hashSet = new HashSet();
        int i = 0;
        String str = null;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    i = C0064a.m110f(parcel, b);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case 2:
                    str = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(2));
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new C0203c(hashSet, i, str);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public C0203c[] ad(int i) {
        return new C0203c[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m1094D(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return ad(i);
    }
}
