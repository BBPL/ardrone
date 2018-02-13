package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import com.google.android.gms.internal.ah.C0114a;
import com.google.android.gms.internal.ah.C0115b;
import java.util.ArrayList;

public class aj implements Creator<C0114a> {
    static void m314a(C0114a c0114a, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, c0114a.versionCode);
        C0065b.m143a(parcel, 2, c0114a.className, false);
        C0065b.m152b(parcel, 3, c0114a.cG, false);
        C0065b.m134C(parcel, d);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m315l(parcel);
    }

    public C0114a m315l(Parcel parcel) {
        ArrayList arrayList = null;
        int c = C0064a.m105c(parcel);
        int i = 0;
        String str = null;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    i = C0064a.m110f(parcel, b);
                    break;
                case 2:
                    str = C0064a.m116l(parcel, b);
                    break;
                case 3:
                    arrayList = C0064a.m106c(parcel, b, C0115b.CREATOR);
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new C0114a(i, str, arrayList);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return m316u(i);
    }

    public C0114a[] m316u(int i) {
        return new C0114a[i];
    }
}
