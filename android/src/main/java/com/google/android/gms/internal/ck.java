package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import com.google.android.gms.internal.cc.C0206f;
import java.util.HashSet;
import java.util.Set;

public class ck implements Creator<C0206f> {
    static void m1097a(C0206f c0206f, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        Set bH = c0206f.bH();
        if (bH.contains(Integer.valueOf(1))) {
            C0065b.m153c(parcel, 1, c0206f.m1059i());
        }
        if (bH.contains(Integer.valueOf(2))) {
            C0065b.m143a(parcel, 2, c0206f.getDepartment(), true);
        }
        if (bH.contains(Integer.valueOf(3))) {
            C0065b.m143a(parcel, 3, c0206f.getDescription(), true);
        }
        if (bH.contains(Integer.valueOf(4))) {
            C0065b.m143a(parcel, 4, c0206f.getEndDate(), true);
        }
        if (bH.contains(Integer.valueOf(5))) {
            C0065b.m143a(parcel, 5, c0206f.getLocation(), true);
        }
        if (bH.contains(Integer.valueOf(6))) {
            C0065b.m143a(parcel, 6, c0206f.getName(), true);
        }
        if (bH.contains(Integer.valueOf(7))) {
            C0065b.m146a(parcel, 7, c0206f.isPrimary());
        }
        if (bH.contains(Integer.valueOf(8))) {
            C0065b.m143a(parcel, 8, c0206f.getStartDate(), true);
        }
        if (bH.contains(Integer.valueOf(9))) {
            C0065b.m143a(parcel, 9, c0206f.getTitle(), true);
        }
        if (bH.contains(Integer.valueOf(10))) {
            C0065b.m153c(parcel, 10, c0206f.getType());
        }
        C0065b.m134C(parcel, d);
    }

    public C0206f m1098F(Parcel parcel) {
        boolean z = false;
        String str = null;
        int c = C0064a.m105c(parcel);
        Set hashSet = new HashSet();
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        String str7 = null;
        int i = 0;
        int i2 = 0;
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
                    z = C0064a.m107c(parcel, b);
                    hashSet.add(Integer.valueOf(7));
                    break;
                case 8:
                    str6 = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(8));
                    break;
                case 9:
                    str7 = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(9));
                    break;
                case 10:
                    i2 = C0064a.m110f(parcel, b);
                    hashSet.add(Integer.valueOf(10));
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new C0206f(hashSet, i, str, str2, str3, str4, str5, z, str6, str7, i2);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public C0206f[] af(int i) {
        return new C0206f[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m1098F(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return af(i);
    }
}
