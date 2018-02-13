package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;

public class DetectedActivityCreator implements Creator<DetectedActivity> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m1237a(DetectedActivity detectedActivity, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m153c(parcel, 1, detectedActivity.fs);
        C0065b.m153c(parcel, 1000, detectedActivity.m1236i());
        C0065b.m153c(parcel, 2, detectedActivity.ft);
        C0065b.m134C(parcel, d);
    }

    public DetectedActivity createFromParcel(Parcel parcel) {
        int i = 0;
        int c = C0064a.m105c(parcel);
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    i2 = C0064a.m110f(parcel, b);
                    break;
                case 2:
                    i = C0064a.m110f(parcel, b);
                    break;
                case 1000:
                    i3 = C0064a.m110f(parcel, b);
                    break;
                default:
                    C0064a.m103b(parcel, b);
                    break;
            }
        }
        if (parcel.dataPosition() == c) {
            return new DetectedActivity(i3, i2, i);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public DetectedActivity[] newArray(int i) {
        return new DetectedActivity[i];
    }
}
