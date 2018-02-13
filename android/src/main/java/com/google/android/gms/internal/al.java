package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class al implements Creator<ak> {
    static void m333a(ak akVar, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, akVar.m330i());
        C0065b.m141a(parcel, 2, akVar.al(), false);
        C0065b.m142a(parcel, 3, akVar.am(), i, false);
        C0065b.m134C(parcel, d);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m334m(parcel);
    }

    public ak m334m(Parcel parcel) {
        ah ahVar = null;
        int c = C0064a.m105c(parcel);
        int i = 0;
        Parcel parcel2 = null;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    i = C0064a.m110f(parcel, b);
                    break;
                case 2:
                    parcel2 = C0064a.m130y(parcel, b);
                    break;
                case 3:
                    ahVar = (ah) C0064a.m99a(parcel, b, ah.CREATOR);
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new ak(i, parcel2, ahVar);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return m335v(i);
    }

    public ak[] m335v(int i) {
        return new ak[i];
    }
}
