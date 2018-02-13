package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import java.util.ArrayList;

public class cr implements Creator<cq> {
    static void m1107a(cq cqVar, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1000, cqVar.m1106i());
        C0065b.m152b(parcel, 2, cqVar.cK(), false);
        C0065b.m152b(parcel, 3, cqVar.cL(), false);
        C0065b.m139a(parcel, 4, cqVar.cM(), false);
        C0065b.m146a(parcel, 5, cqVar.cN());
        C0065b.m153c(parcel, 6, cqVar.cJ());
        C0065b.m134C(parcel, d);
    }

    public cq m1108J(Parcel parcel) {
        ArrayList arrayList = null;
        boolean z = false;
        int c = C0064a.m105c(parcel);
        ArrayList arrayList2 = null;
        Bundle bundle = null;
        int i = 0;
        int i2 = 0;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 2:
                    arrayList = C0064a.m106c(parcel, b, C0246x.CREATOR);
                    break;
                case 3:
                    arrayList2 = C0064a.m106c(parcel, b, C0246x.CREATOR);
                    break;
                case 4:
                    bundle = C0064a.m119n(parcel, b);
                    break;
                case 5:
                    z = C0064a.m107c(parcel, b);
                    break;
                case 6:
                    i2 = C0064a.m110f(parcel, b);
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
            return new cq(i, arrayList, arrayList2, bundle, z, i2);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public cq[] aj(int i) {
        return new cq[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m1108J(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return aj(i);
    }
}
