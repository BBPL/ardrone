package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import com.google.android.gms.internal.cc.C0202b;
import com.google.android.gms.internal.cc.C0202b.C0200a;
import com.google.android.gms.internal.cc.C0202b.C0201b;
import java.util.HashSet;
import java.util.Set;

public class cf implements Creator<C0202b> {
    static void m1087a(C0202b c0202b, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        Set bH = c0202b.bH();
        if (bH.contains(Integer.valueOf(1))) {
            C0065b.m153c(parcel, 1, c0202b.m1040i());
        }
        if (bH.contains(Integer.valueOf(2))) {
            C0065b.m142a(parcel, 2, c0202b.cl(), i, true);
        }
        if (bH.contains(Integer.valueOf(3))) {
            C0065b.m142a(parcel, 3, c0202b.cm(), i, true);
        }
        if (bH.contains(Integer.valueOf(4))) {
            C0065b.m153c(parcel, 4, c0202b.getLayout());
        }
        C0065b.m134C(parcel, d);
    }

    public C0202b m1088A(Parcel parcel) {
        C0200a c0200a = null;
        int i = 0;
        int c = C0064a.m105c(parcel);
        Set hashSet = new HashSet();
        C0201b c0201b = null;
        int i2 = 0;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    i = C0064a.m110f(parcel, b);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case 2:
                    C0200a c0200a2 = (C0200a) C0064a.m99a(parcel, b, C0200a.CREATOR);
                    hashSet.add(Integer.valueOf(2));
                    c0200a = c0200a2;
                    break;
                case 3:
                    C0201b c0201b2 = (C0201b) C0064a.m99a(parcel, b, C0201b.CREATOR);
                    hashSet.add(Integer.valueOf(3));
                    c0201b = c0201b2;
                    break;
                case 4:
                    i2 = C0064a.m110f(parcel, b);
                    hashSet.add(Integer.valueOf(4));
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new C0202b(hashSet, i, c0200a, c0201b, i2);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public C0202b[] aa(int i) {
        return new C0202b[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m1088A(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return aa(i);
    }
}
