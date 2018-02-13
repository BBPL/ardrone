package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import com.google.android.gms.internal.ah.C0114a;
import java.util.ArrayList;

public class ai implements Creator<ah> {
    static void m311a(ah ahVar, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, ahVar.m309i());
        C0065b.m152b(parcel, 2, ahVar.ai(), false);
        C0065b.m143a(parcel, 3, ahVar.aj(), false);
        C0065b.m134C(parcel, d);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m312k(parcel);
    }

    public ah m312k(Parcel parcel) {
        String str = null;
        int c = C0064a.m105c(parcel);
        int i = 0;
        ArrayList arrayList = null;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    i = C0064a.m110f(parcel, b);
                    break;
                case 2:
                    arrayList = C0064a.m106c(parcel, b, C0114a.CREATOR);
                    break;
                case 3:
                    str = C0064a.m116l(parcel, b);
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new ah(i, arrayList, str);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return m313t(i);
    }

    public ah[] m313t(int i) {
        return new ah[i];
    }
}
