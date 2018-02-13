package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import com.google.android.gms.internal.cc.C0204d;
import java.util.HashSet;
import java.util.Set;

public class cj implements Creator<C0204d> {
    static void m1095a(C0204d c0204d, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        Set bH = c0204d.bH();
        if (bH.contains(Integer.valueOf(1))) {
            C0065b.m153c(parcel, 1, c0204d.m1052i());
        }
        if (bH.contains(Integer.valueOf(2))) {
            C0065b.m143a(parcel, 2, c0204d.getFamilyName(), true);
        }
        if (bH.contains(Integer.valueOf(3))) {
            C0065b.m143a(parcel, 3, c0204d.getFormatted(), true);
        }
        if (bH.contains(Integer.valueOf(4))) {
            C0065b.m143a(parcel, 4, c0204d.getGivenName(), true);
        }
        if (bH.contains(Integer.valueOf(5))) {
            C0065b.m143a(parcel, 5, c0204d.getHonorificPrefix(), true);
        }
        if (bH.contains(Integer.valueOf(6))) {
            C0065b.m143a(parcel, 6, c0204d.getHonorificSuffix(), true);
        }
        if (bH.contains(Integer.valueOf(7))) {
            C0065b.m143a(parcel, 7, c0204d.getMiddleName(), true);
        }
        C0065b.m134C(parcel, d);
    }

    public C0204d m1096E(Parcel parcel) {
        String str = null;
        int c = C0064a.m105c(parcel);
        Set hashSet = new HashSet();
        int i = 0;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
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
                case 3:
                    str2 = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(3));
                    break;
                case 4:
                    str3 = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(4));
                    break;
                case 5:
                    str4 = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(5));
                    break;
                case 6:
                    str5 = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(6));
                    break;
                case 7:
                    str6 = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(7));
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new C0204d(hashSet, i, str, str2, str3, str4, str5, str6);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public C0204d[] ae(int i) {
        return new C0204d[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m1096E(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return ae(i);
    }
}
