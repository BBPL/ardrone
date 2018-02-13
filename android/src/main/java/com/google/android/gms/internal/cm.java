package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import com.google.android.gms.internal.cc.C0208h;
import java.util.HashSet;
import java.util.Set;

public class cm implements Creator<C0208h> {
    static void m1101a(C0208h c0208h, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        Set bH = c0208h.bH();
        if (bH.contains(Integer.valueOf(1))) {
            C0065b.m153c(parcel, 1, c0208h.m1071i());
        }
        if (bH.contains(Integer.valueOf(3))) {
            C0065b.m153c(parcel, 3, c0208h.cu());
        }
        if (bH.contains(Integer.valueOf(4))) {
            C0065b.m143a(parcel, 4, c0208h.getValue(), true);
        }
        if (bH.contains(Integer.valueOf(5))) {
            C0065b.m143a(parcel, 5, c0208h.getLabel(), true);
        }
        if (bH.contains(Integer.valueOf(6))) {
            C0065b.m153c(parcel, 6, c0208h.getType());
        }
        C0065b.m134C(parcel, d);
    }

    public C0208h m1102H(Parcel parcel) {
        String str = null;
        int i = 0;
        int c = C0064a.m105c(parcel);
        Set hashSet = new HashSet();
        String str2 = null;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    i = C0064a.m110f(parcel, b);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case 3:
                    i3 = C0064a.m110f(parcel, b);
                    hashSet.add(Integer.valueOf(3));
                    break;
                case 4:
                    str2 = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(4));
                    break;
                case 5:
                    str = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(5));
                    break;
                case 6:
                    i2 = C0064a.m110f(parcel, b);
                    hashSet.add(Integer.valueOf(6));
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new C0208h(hashSet, i, str, i2, str2, i3);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public C0208h[] ah(int i) {
        return new C0208h[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m1102H(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return ah(i);
    }
}
