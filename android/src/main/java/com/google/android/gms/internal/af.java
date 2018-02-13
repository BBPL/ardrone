package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import com.google.android.gms.internal.ae.C0113a;

public class af implements Creator<C0113a> {
    static void m299a(C0113a c0113a, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, c0113a.m286i());
        C0065b.m153c(parcel, 2, c0113a.m278R());
        C0065b.m146a(parcel, 3, c0113a.m281X());
        C0065b.m153c(parcel, 4, c0113a.m279S());
        C0065b.m146a(parcel, 5, c0113a.m282Y());
        C0065b.m143a(parcel, 6, c0113a.m283Z(), false);
        C0065b.m153c(parcel, 7, c0113a.aa());
        C0065b.m143a(parcel, 8, c0113a.ac(), false);
        C0065b.m142a(parcel, 9, c0113a.ae(), i, false);
        C0065b.m134C(parcel, d);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m300i(parcel);
    }

    public C0113a m300i(Parcel parcel) {
        String str = null;
        boolean z = false;
        int c = C0064a.m105c(parcel);
        String str2 = null;
        C0248z c0248z = null;
        boolean z2 = false;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    i = C0064a.m110f(parcel, b);
                    break;
                case 2:
                    i2 = C0064a.m110f(parcel, b);
                    break;
                case 3:
                    z = C0064a.m107c(parcel, b);
                    break;
                case 4:
                    i3 = C0064a.m110f(parcel, b);
                    break;
                case 5:
                    z2 = C0064a.m107c(parcel, b);
                    break;
                case 6:
                    str = C0064a.m116l(parcel, b);
                    break;
                case 7:
                    i4 = C0064a.m110f(parcel, b);
                    break;
                case 8:
                    str2 = C0064a.m116l(parcel, b);
                    break;
                case 9:
                    c0248z = (C0248z) C0064a.m99a(parcel, b, C0248z.CREATOR);
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new C0113a(i, i2, z, i3, z2, str, i4, str2, c0248z);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return m301r(i);
    }

    public C0113a[] m301r(int i) {
        return new C0113a[i];
    }
}
