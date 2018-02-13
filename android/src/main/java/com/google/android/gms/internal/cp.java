package com.google.android.gms.internal;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import java.util.List;

public class cp implements Creator<co> {
    static void m1104a(co coVar, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m143a(parcel, 1, coVar.getId(), false);
        C0065b.m153c(parcel, 1000, coVar.m1103i());
        C0065b.m152b(parcel, 2, coVar.cB(), false);
        C0065b.m152b(parcel, 3, coVar.cC(), false);
        C0065b.m142a(parcel, 4, coVar.cD(), i, false);
        C0065b.m143a(parcel, 5, coVar.cE(), false);
        C0065b.m143a(parcel, 6, coVar.cF(), false);
        C0065b.m143a(parcel, 7, coVar.cG(), false);
        C0065b.m139a(parcel, 8, coVar.cH(), false);
        C0065b.m139a(parcel, 9, coVar.cI(), false);
        C0065b.m153c(parcel, 10, coVar.cJ());
        C0065b.m134C(parcel, d);
    }

    public co m1105I(Parcel parcel) {
        int i = 0;
        String str = null;
        int c = C0064a.m105c(parcel);
        List list = null;
        List list2 = null;
        Uri uri = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        Bundle bundle = null;
        Bundle bundle2 = null;
        int i2 = 0;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    str = C0064a.m116l(parcel, b);
                    break;
                case 2:
                    list = C0064a.m106c(parcel, b, C0246x.CREATOR);
                    break;
                case 3:
                    list2 = C0064a.m106c(parcel, b, Uri.CREATOR);
                    break;
                case 4:
                    uri = (Uri) C0064a.m99a(parcel, b, Uri.CREATOR);
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
                case 8:
                    bundle = C0064a.m119n(parcel, b);
                    break;
                case 9:
                    bundle2 = C0064a.m119n(parcel, b);
                    break;
                case 10:
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
            return new co(i, str, list, list2, uri, str2, str3, str4, bundle, bundle2, i2);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public co[] ai(int i) {
        return new co[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m1105I(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return ai(i);
    }
}
