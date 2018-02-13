package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class aa implements Creator<C0248z> {
    static void m249a(C0248z c0248z, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, c0248z.m1232i());
        C0065b.m142a(parcel, 2, c0248z.m1230O(), i, false);
        C0065b.m134C(parcel, d);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m250f(parcel);
    }

    public C0248z m250f(Parcel parcel) {
        int c = C0064a.m105c(parcel);
        int i = 0;
        ab abVar = null;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    i = C0064a.m110f(parcel, b);
                    break;
                case 2:
                    abVar = (ab) C0064a.m99a(parcel, b, ab.CREATOR);
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new C0248z(i, abVar);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return m251o(i);
    }

    public C0248z[] m251o(int i) {
        return new C0248z[i];
    }
}
