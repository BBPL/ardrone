package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import java.util.ArrayList;

public class bw implements Creator<bv> {
    static void m979a(bv bvVar, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m143a(parcel, 1, bvVar.getDescription(), false);
        C0065b.m153c(parcel, 1000, bvVar.m978i());
        C0065b.m152b(parcel, 2, bvVar.bE(), false);
        C0065b.m152b(parcel, 3, bvVar.bF(), false);
        C0065b.m146a(parcel, 4, bvVar.bG());
        C0065b.m134C(parcel, d);
    }

    public bv[] m980V(int i) {
        return new bv[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m981v(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return m980V(i);
    }

    public bv m981v(Parcel parcel) {
        boolean z = false;
        String str = null;
        int c = C0064a.m105c(parcel);
        ArrayList arrayList = null;
        ArrayList arrayList2 = null;
        int i = 0;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    str = C0064a.m116l(parcel, b);
                    break;
                case 2:
                    arrayList = C0064a.m106c(parcel, b, C0246x.CREATOR);
                    break;
                case 3:
                    arrayList2 = C0064a.m106c(parcel, b, C0246x.CREATOR);
                    break;
                case 4:
                    z = C0064a.m107c(parcel, b);
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
            return new bv(i, str, arrayList, arrayList2, z);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }
}
