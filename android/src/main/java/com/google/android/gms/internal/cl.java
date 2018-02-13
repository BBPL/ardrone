package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import com.google.android.gms.internal.cc.C0207g;
import java.util.HashSet;
import java.util.Set;

public class cl implements Creator<C0207g> {
    static void m1099a(C0207g c0207g, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        Set bH = c0207g.bH();
        if (bH.contains(Integer.valueOf(1))) {
            C0065b.m153c(parcel, 1, c0207g.m1065i());
        }
        if (bH.contains(Integer.valueOf(2))) {
            C0065b.m146a(parcel, 2, c0207g.isPrimary());
        }
        if (bH.contains(Integer.valueOf(3))) {
            C0065b.m143a(parcel, 3, c0207g.getValue(), true);
        }
        C0065b.m134C(parcel, d);
    }

    public C0207g m1100G(Parcel parcel) {
        boolean z = false;
        int c = C0064a.m105c(parcel);
        Set hashSet = new HashSet();
        String str = null;
        int i = 0;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    i = C0064a.m110f(parcel, b);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case 2:
                    z = C0064a.m107c(parcel, b);
                    hashSet.add(Integer.valueOf(2));
                    break;
                case 3:
                    str = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(3));
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new C0207g(hashSet, i, z, str);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public C0207g[] ag(int i) {
        return new C0207g[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m1100G(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return ag(i);
    }
}
