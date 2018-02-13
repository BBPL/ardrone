package com.google.android.gms.common.data;

import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class C0052e implements Creator<C0051d> {
    static void m55a(C0051d c0051d, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m149a(parcel, 1, c0051d.m52j(), false);
        C0065b.m153c(parcel, 1000, c0051d.m51i());
        C0065b.m148a(parcel, 2, c0051d.m53k(), i, false);
        C0065b.m153c(parcel, 3, c0051d.getStatusCode());
        C0065b.m139a(parcel, 4, c0051d.m54l(), false);
        C0065b.m134C(parcel, d);
    }

    public C0051d m56a(Parcel parcel) {
        int i = 0;
        String[] strArr = null;
        int c = C0064a.m105c(parcel);
        CursorWindow[] cursorWindowArr = null;
        Bundle bundle = null;
        int i2 = 0;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    strArr = C0064a.m128w(parcel, b);
                    break;
                case 2:
                    cursorWindowArr = (CursorWindow[]) C0064a.m104b(parcel, b, CursorWindow.CREATOR);
                    break;
                case 3:
                    i2 = C0064a.m110f(parcel, b);
                    break;
                case 4:
                    bundle = C0064a.m119n(parcel, b);
                    break;
                case 1000:
                    i = C0064a.m110f(parcel, b);
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() != c) {
            throw new C0063a("Overread allowed size end=" + c, parcel);
        }
        C0051d c0051d = new C0051d(i, strArr, cursorWindowArr, i2, bundle);
        c0051d.m50h();
        return c0051d;
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m56a(parcel);
    }

    public C0051d[] m57g(int i) {
        return new C0051d[i];
    }

    public /* synthetic */ Object[] newArray(int i) {
        return m57g(i);
    }
}
