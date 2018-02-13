package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import com.google.android.gms.internal.ae.C0113a;
import com.google.android.gms.internal.ah.C0115b;

public class ag implements Creator<C0115b> {
    static void m302a(C0115b c0115b, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, c0115b.versionCode);
        C0065b.m143a(parcel, 2, c0115b.cH, false);
        C0065b.m142a(parcel, 3, c0115b.cI, i, false);
        C0065b.m134C(parcel, d);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m303j(parcel);
    }

    public C0115b m303j(Parcel parcel) {
        C0113a c0113a = null;
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
                    c0113a = (C0113a) C0064a.m99a(parcel, b, C0113a.CREATOR);
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new C0115b(i, str, c0113a);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return m304s(i);
    }

    public C0115b[] m304s(int i) {
        return new C0115b[i];
    }
}
