package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import java.util.HashSet;
import java.util.Set;

public class ca implements Creator<bz> {
    static void m1016a(bz bzVar, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        Set bH = bzVar.bH();
        if (bH.contains(Integer.valueOf(1))) {
            C0065b.m153c(parcel, 1, bzVar.m994i());
        }
        if (bH.contains(Integer.valueOf(2))) {
            C0065b.m143a(parcel, 2, bzVar.getId(), true);
        }
        if (bH.contains(Integer.valueOf(4))) {
            C0065b.m142a(parcel, 4, bzVar.bY(), i, true);
        }
        if (bH.contains(Integer.valueOf(5))) {
            C0065b.m143a(parcel, 5, bzVar.getStartDate(), true);
        }
        if (bH.contains(Integer.valueOf(6))) {
            C0065b.m142a(parcel, 6, bzVar.bZ(), i, true);
        }
        if (bH.contains(Integer.valueOf(7))) {
            C0065b.m143a(parcel, 7, bzVar.getType(), true);
        }
        C0065b.m134C(parcel, d);
    }

    public bz[] m1017X(int i) {
        return new bz[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m1018x(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return m1017X(i);
    }

    public bz m1018x(Parcel parcel) {
        bx bxVar = null;
        int c = C0064a.m105c(parcel);
        Set hashSet = new HashSet();
        int i = 0;
        String str = null;
        bx bxVar2 = null;
        String str2 = null;
        String str3 = null;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            bx bxVar3;
            switch (C0064a.m117m(b)) {
                case 1:
                    i = C0064a.m110f(parcel, b);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case 2:
                    str = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(2));
                    break;
                case 4:
                    bxVar3 = (bx) C0064a.m99a(parcel, b, bx.CREATOR);
                    hashSet.add(Integer.valueOf(4));
                    bxVar = bxVar3;
                    break;
                case 5:
                    str2 = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(5));
                    break;
                case 6:
                    bxVar3 = (bx) C0064a.m99a(parcel, b, bx.CREATOR);
                    hashSet.add(Integer.valueOf(6));
                    bxVar2 = bxVar3;
                    break;
                case 7:
                    str3 = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(7));
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new bz(hashSet, i, str, bxVar, str2, bxVar2, str3);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }
}
