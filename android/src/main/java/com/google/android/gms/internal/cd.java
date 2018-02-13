package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import com.google.android.gms.internal.cc.C0199a;
import com.google.android.gms.internal.cc.C0202b;
import com.google.android.gms.internal.cc.C0203c;
import com.google.android.gms.internal.cc.C0204d;
import com.google.android.gms.internal.cc.C0206f;
import com.google.android.gms.internal.cc.C0207g;
import com.google.android.gms.internal.cc.C0208h;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class cd implements Creator<cc> {
    static void m1081a(cc ccVar, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        Set bH = ccVar.bH();
        if (bH.contains(Integer.valueOf(1))) {
            C0065b.m153c(parcel, 1, ccVar.m1078i());
        }
        if (bH.contains(Integer.valueOf(2))) {
            C0065b.m143a(parcel, 2, ccVar.getAboutMe(), true);
        }
        if (bH.contains(Integer.valueOf(3))) {
            C0065b.m142a(parcel, 3, ccVar.cc(), i, true);
        }
        if (bH.contains(Integer.valueOf(4))) {
            C0065b.m143a(parcel, 4, ccVar.getBirthday(), true);
        }
        if (bH.contains(Integer.valueOf(5))) {
            C0065b.m143a(parcel, 5, ccVar.getBraggingRights(), true);
        }
        if (bH.contains(Integer.valueOf(6))) {
            C0065b.m153c(parcel, 6, ccVar.getCircledByCount());
        }
        if (bH.contains(Integer.valueOf(7))) {
            C0065b.m142a(parcel, 7, ccVar.cd(), i, true);
        }
        if (bH.contains(Integer.valueOf(8))) {
            C0065b.m143a(parcel, 8, ccVar.getCurrentLocation(), true);
        }
        if (bH.contains(Integer.valueOf(9))) {
            C0065b.m143a(parcel, 9, ccVar.getDisplayName(), true);
        }
        if (bH.contains(Integer.valueOf(12))) {
            C0065b.m153c(parcel, 12, ccVar.getGender());
        }
        if (bH.contains(Integer.valueOf(14))) {
            C0065b.m143a(parcel, 14, ccVar.getId(), true);
        }
        if (bH.contains(Integer.valueOf(15))) {
            C0065b.m142a(parcel, 15, ccVar.ce(), i, true);
        }
        if (bH.contains(Integer.valueOf(16))) {
            C0065b.m146a(parcel, 16, ccVar.isPlusUser());
        }
        if (bH.contains(Integer.valueOf(19))) {
            C0065b.m142a(parcel, 19, ccVar.cf(), i, true);
        }
        if (bH.contains(Integer.valueOf(18))) {
            C0065b.m143a(parcel, 18, ccVar.getLanguage(), true);
        }
        if (bH.contains(Integer.valueOf(21))) {
            C0065b.m153c(parcel, 21, ccVar.getObjectType());
        }
        if (bH.contains(Integer.valueOf(20))) {
            C0065b.m143a(parcel, 20, ccVar.getNickname(), true);
        }
        if (bH.contains(Integer.valueOf(23))) {
            C0065b.m152b(parcel, 23, ccVar.ch(), true);
        }
        if (bH.contains(Integer.valueOf(22))) {
            C0065b.m152b(parcel, 22, ccVar.cg(), true);
        }
        if (bH.contains(Integer.valueOf(25))) {
            C0065b.m153c(parcel, 25, ccVar.getRelationshipStatus());
        }
        if (bH.contains(Integer.valueOf(24))) {
            C0065b.m153c(parcel, 24, ccVar.getPlusOneCount());
        }
        if (bH.contains(Integer.valueOf(27))) {
            C0065b.m143a(parcel, 27, ccVar.getUrl(), true);
        }
        if (bH.contains(Integer.valueOf(26))) {
            C0065b.m143a(parcel, 26, ccVar.getTagline(), true);
        }
        if (bH.contains(Integer.valueOf(29))) {
            C0065b.m146a(parcel, 29, ccVar.isVerified());
        }
        if (bH.contains(Integer.valueOf(28))) {
            C0065b.m152b(parcel, 28, ccVar.ci(), true);
        }
        C0065b.m134C(parcel, d);
    }

    public cc[] m1082Y(int i) {
        return new cc[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m1083y(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return m1082Y(i);
    }

    public cc m1083y(Parcel parcel) {
        int c = C0064a.m105c(parcel);
        Set hashSet = new HashSet();
        int i = 0;
        String str = null;
        C0199a c0199a = null;
        String str2 = null;
        String str3 = null;
        int i2 = 0;
        C0202b c0202b = null;
        String str4 = null;
        String str5 = null;
        int i3 = 0;
        String str6 = null;
        C0203c c0203c = null;
        boolean z = false;
        String str7 = null;
        C0204d c0204d = null;
        String str8 = null;
        int i4 = 0;
        List list = null;
        List list2 = null;
        int i5 = 0;
        int i6 = 0;
        String str9 = null;
        String str10 = null;
        List list3 = null;
        boolean z2 = false;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    i = C0064a.m110f(parcel, b);
                    hashSet.add(Integer.valueOf(1));
                    break;
                case 2:
                    str = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(2));
                    break;
                case 3:
                    C0199a c0199a2 = (C0199a) C0064a.m99a(parcel, b, C0199a.CREATOR);
                    hashSet.add(Integer.valueOf(3));
                    c0199a = c0199a2;
                    break;
                case 4:
                    str2 = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(4));
                    break;
                case 5:
                    str3 = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(5));
                    break;
                case 6:
                    i2 = C0064a.m110f(parcel, b);
                    hashSet.add(Integer.valueOf(6));
                    break;
                case 7:
                    C0202b c0202b2 = (C0202b) C0064a.m99a(parcel, b, C0202b.CREATOR);
                    hashSet.add(Integer.valueOf(7));
                    c0202b = c0202b2;
                    break;
                case 8:
                    str4 = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(8));
                    break;
                case 9:
                    str5 = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(9));
                    break;
                case 12:
                    i3 = C0064a.m110f(parcel, b);
                    hashSet.add(Integer.valueOf(12));
                    break;
                case 14:
                    str6 = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(14));
                    break;
                case 15:
                    C0203c c0203c2 = (C0203c) C0064a.m99a(parcel, b, C0203c.CREATOR);
                    hashSet.add(Integer.valueOf(15));
                    c0203c = c0203c2;
                    break;
                case 16:
                    z = C0064a.m107c(parcel, b);
                    hashSet.add(Integer.valueOf(16));
                    break;
                case 18:
                    str7 = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(18));
                    break;
                case 19:
                    C0204d c0204d2 = (C0204d) C0064a.m99a(parcel, b, (Creator) C0204d.CREATOR);
                    hashSet.add(Integer.valueOf(19));
                    c0204d = c0204d2;
                    break;
                case 20:
                    str8 = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(20));
                    break;
                case 21:
                    i4 = C0064a.m110f(parcel, b);
                    hashSet.add(Integer.valueOf(21));
                    break;
                case 22:
                    list = C0064a.m106c(parcel, b, C0206f.CREATOR);
                    hashSet.add(Integer.valueOf(22));
                    break;
                case 23:
                    list2 = C0064a.m106c(parcel, b, C0207g.CREATOR);
                    hashSet.add(Integer.valueOf(23));
                    break;
                case 24:
                    i5 = C0064a.m110f(parcel, b);
                    hashSet.add(Integer.valueOf(24));
                    break;
                case 25:
                    i6 = C0064a.m110f(parcel, b);
                    hashSet.add(Integer.valueOf(25));
                    break;
                case 26:
                    str9 = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(26));
                    break;
                case 27:
                    str10 = C0064a.m116l(parcel, b);
                    hashSet.add(Integer.valueOf(27));
                    break;
                case 28:
                    list3 = C0064a.m106c(parcel, b, C0208h.CREATOR);
                    hashSet.add(Integer.valueOf(28));
                    break;
                case 29:
                    z2 = C0064a.m107c(parcel, b);
                    hashSet.add(Integer.valueOf(29));
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new cc(hashSet, i, str, c0199a, str2, str3, i2, c0202b, str4, str5, i3, str6, c0203c, z, str7, c0204d, str8, i4, list, list2, i5, i6, str9, str10, list3, z2);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }
}
