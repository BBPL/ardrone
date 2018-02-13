package com.google.android.gms.plus;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class C0368b implements Creator<C0367a> {
    static void m1404a(C0367a c0367a, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m143a(parcel, 1, c0367a.getAccountName(), false);
        C0065b.m153c(parcel, 1000, c0367a.m1403i());
        C0065b.m149a(parcel, 2, c0367a.by(), false);
        C0065b.m149a(parcel, 3, c0367a.bz(), false);
        C0065b.m149a(parcel, 4, c0367a.bA(), false);
        C0065b.m143a(parcel, 5, c0367a.bB(), false);
        C0065b.m143a(parcel, 6, c0367a.bC(), false);
        C0065b.m143a(parcel, 7, c0367a.bD(), false);
        C0065b.m134C(parcel, d);
    }

    public C0367a[] m1405U(int i) {
        return new C0367a[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m1406u(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return m1405U(i);
    }

    public C0367a m1406u(Parcel parcel) {
        String str = null;
        int c = C0064a.m105c(parcel);
        int i = 0;
        String[] strArr = null;
        String[] strArr2 = null;
        String[] strArr3 = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    str = C0064a.m116l(parcel, b);
                    break;
                case 2:
                    strArr = C0064a.m128w(parcel, b);
                    break;
                case 3:
                    strArr2 = C0064a.m128w(parcel, b);
                    break;
                case 4:
                    strArr3 = C0064a.m128w(parcel, b);
                    break;
                case 5:
                    str2 = C0064a.m116l(parcel, b);
                    break;
                case 6:
                    str3 = C0064a.m116l(parcel, b);
                    break;
                case 7:
                    str4 = C0064a.m116l(parcel, b);
                    break;
                case 1000:
                    i = C0064a.m110f(parcel, b);
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new C0367a(i, str, strArr, strArr2, strArr3, str2, str3, str4);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }
}
