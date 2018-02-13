package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import com.google.android.gms.internal.cc.C0202b.C0201b;
import java.util.HashSet;
import java.util.Set;

public class ch implements Creator<C0201b> {
    static void m1091a(C0201b c0201b, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        Set bH = c0201b.bH();
        if (bH.contains(Integer.valueOf(1))) {
            C0065b.m153c(parcel, 1, c0201b.m1034i());
        }
        if (bH.contains(Integer.valueOf(2))) {
            C0065b.m153c(parcel, 2, c0201b.getHeight());
        }
        if (bH.contains(Integer.valueOf(3))) {
            C0065b.m143a(parcel, 3, c0201b.getUrl(), true);
        }
        if (bH.contains(Integer.valueOf(4))) {
            C0065b.m153c(parcel, 4, c0201b.getWidth());
        }
        C0065b.m134C(parcel, d);
    }

    public C0201b m1092C(Parcel parcel) {
        int i = 0;
        int c = C0064a.m105c(parcel);
        Set hashSet = new HashSet();
        String str = null;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    i = C0064a.m110f(parcel, b);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case 2:
                    i2 = C0064a.m110f(parcel, b);
                    hashSet.add(Integer.valueOf(2));
                    break;
                case 3:
                    str = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(3));
                    break;
                case 4:
                    i3 = C0064a.m110f(parcel, b);
                    hashSet.add(Integer.valueOf(4));
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new C0201b(hashSet, i, i2, str, i3);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public C0201b[] ac(int i) {
        return new C0201b[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m1092C(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return ac(i);
    }
}
