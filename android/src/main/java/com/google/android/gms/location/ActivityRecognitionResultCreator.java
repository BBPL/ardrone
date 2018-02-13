package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.C0064a;
import com.google.android.gms.common.internal.safeparcel.C0064a.C0063a;
import com.google.android.gms.common.internal.safeparcel.C0065b;
import java.util.List;

public class ActivityRecognitionResultCreator implements Creator<ActivityRecognitionResult> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void m1234a(ActivityRecognitionResult activityRecognitionResult, Parcel parcel, int i) {
        int d = C0065b.m155d(parcel);
        C0065b.m152b(parcel, 1, activityRecognitionResult.fp, false);
        C0065b.m153c(parcel, 1000, activityRecognitionResult.m1233i());
        C0065b.m138a(parcel, 2, activityRecognitionResult.fq);
        C0065b.m138a(parcel, 3, activityRecognitionResult.fr);
        C0065b.m134C(parcel, d);
    }

    public ActivityRecognitionResult createFromParcel(Parcel parcel) {
        long j = 0;
        int c = C0064a.m105c(parcel);
        int i = 0;
        List list = null;
        long j2 = 0;
        while (parcel.dataPosition() < c) {
            int b = C0064a.m102b(parcel);
            switch (C0064a.m117m(b)) {
                case 1:
                    list = C0064a.m106c(parcel, b, DetectedActivity.CREATOR);
                    break;
                case 2:
                    j = C0064a.m111g(parcel, b);
                    break;
                case 3:
                    j2 = C0064a.m111g(parcel, b);
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
            return new ActivityRecognitionResult(i, list, j, j2);
        }
        throw new C0063a("Overread allowed size end=" + c, parcel);
    }

    public ActivityRecognitionResult[] newArray(int i) {
        return new ActivityRecognitionResult[i];
    }
}
