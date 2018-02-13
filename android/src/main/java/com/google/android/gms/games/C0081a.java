package com.google.android.gms.games;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class C0081a implements Creator<GameEntity> {
    static void m176a(GameEntity gameEntity, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m143a(parcel, 1, gameEntity.getApplicationId(), false);
        C0065b.m143a(parcel, 2, gameEntity.getDisplayName(), false);
        C0065b.m143a(parcel, 3, gameEntity.getPrimaryCategory(), false);
        C0065b.m143a(parcel, 4, gameEntity.getSecondaryCategory(), false);
        C0065b.m143a(parcel, 5, gameEntity.getDescription(), false);
        C0065b.m143a(parcel, 6, gameEntity.getDeveloperName(), false);
        C0065b.m142a(parcel, 7, gameEntity.getIconImageUri(), i, false);
        C0065b.m142a(parcel, 8, gameEntity.getHiResImageUri(), i, false);
        C0065b.m142a(parcel, 9, gameEntity.getFeaturedImageUri(), i, false);
        C0065b.m146a(parcel, 10, gameEntity.isPlayEnabledGame());
        C0065b.m146a(parcel, 11, gameEntity.isInstanceInstalled());
        C0065b.m143a(parcel, 12, gameEntity.getInstancePackageName(), false);
        C0065b.m153c(parcel, 13, gameEntity.getGameplayAclStatus());
        C0065b.m153c(parcel, 14, gameEntity.getAchievementTotalCount());
        C0065b.m153c(parcel, 15, gameEntity.getLeaderboardCount());
        C0065b.m153c(parcel, 1000, gameEntity.m191i());
        C0065b.m134C(parcel, d);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return mo364n(parcel);
    }

    public GameEntity mo364n(Parcel parcel) {
        int c = C0064a.m105c(parcel);
        int i = 0;
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        Uri uri = null;
        Uri uri2 = null;
        Uri uri3 = null;
        boolean z = false;
        boolean z2 = false;
        String str7 = null;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    str = C0064a.m116l(parcel, b);
                    break;
                case 2:
                    str2 = C0064a.m116l(parcel, b);
                    break;
                case 3:
                    str3 = C0064a.m116l(parcel, b);
                    break;
                case 4:
                    str4 = C0064a.m116l(parcel, b);
                    break;
                case 5:
                    str5 = C0064a.m116l(parcel, b);
                    break;
                case 6:
                    str6 = C0064a.m116l(parcel, b);
                    break;
                case 7:
                    uri = (Uri) C0064a.m99a(parcel, b, Uri.CREATOR);
                    break;
                case 8:
                    uri2 = (Uri) C0064a.m99a(parcel, b, Uri.CREATOR);
                    break;
                case 9:
                    uri3 = (Uri) C0064a.m99a(parcel, b, Uri.CREATOR);
                    break;
                case 10:
                    z = C0064a.m107c(parcel, b);
                    break;
                case 11:
                    z2 = C0064a.m107c(parcel, b);
                    break;
                case 12:
                    str7 = C0064a.m116l(parcel, b);
                    break;
                case 13:
                    i2 = C0064a.m110f(parcel, b);
                    break;
                case 14:
                    i3 = C0064a.m110f(parcel, b);
                    break;
                case 15:
                    i4 = C0064a.m110f(parcel, b);
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
            return new GameEntity(i, str, str2, str3, str4, str5, str6, uri, uri2, uri3, z, z2, str7, i2, i3, i4);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return m178z(i);
    }

    public GameEntity[] m178z(int i) {
        return new GameEntity[i];
    }
}
