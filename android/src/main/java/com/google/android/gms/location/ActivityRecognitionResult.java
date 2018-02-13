package com.google.android.gms.location;

import android.content.Intent;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.C0242s;
import java.util.Collections;
import java.util.List;

public class ActivityRecognitionResult implements SafeParcelable {
    public static final ActivityRecognitionResultCreator CREATOR = new ActivityRecognitionResultCreator();
    public static final String EXTRA_ACTIVITY_RESULT = "com.google.android.location.internal.EXTRA_ACTIVITY_RESULT";
    private final int ab;
    List<DetectedActivity> fp;
    long fq;
    long fr;

    public ActivityRecognitionResult(int i, List<DetectedActivity> list, long j, long j2) {
        this.ab = 1;
        this.fp = list;
        this.fq = j;
        this.fr = j2;
    }

    public ActivityRecognitionResult(DetectedActivity detectedActivity, long j, long j2) {
        this(Collections.singletonList(detectedActivity), j, j2);
    }

    public ActivityRecognitionResult(List<DetectedActivity> list, long j, long j2) {
        boolean z = list != null && list.size() > 0;
        C0242s.m1206b(z, (Object) "Must have at least 1 detected activity");
        this.ab = 1;
        this.fp = list;
        this.fq = j;
        this.fr = j2;
    }

    public static ActivityRecognitionResult extractResult(Intent intent) {
        return !hasResult(intent) ? null : (ActivityRecognitionResult) intent.getExtras().get(EXTRA_ACTIVITY_RESULT);
    }

    public static boolean hasResult(Intent intent) {
        return intent == null ? false : intent.hasExtra(EXTRA_ACTIVITY_RESULT);
    }

    public int describeContents() {
        return 0;
    }

    public int getActivityConfidence(int i) {
        for (DetectedActivity detectedActivity : this.fp) {
            if (detectedActivity.getType() == i) {
                return detectedActivity.getConfidence();
            }
        }
        return 0;
    }

    public long getElapsedRealtimeMillis() {
        return this.fr;
    }

    public DetectedActivity getMostProbableActivity() {
        return (DetectedActivity) this.fp.get(0);
    }

    public List<DetectedActivity> getProbableActivities() {
        return this.fp;
    }

    public long getTime() {
        return this.fq;
    }

    public int m1233i() {
        return this.ab;
    }

    public String toString() {
        return "ActivityRecognitionResult [probableActivities=" + this.fp + ", timeMillis=" + this.fq + ", elapsedRealtimeMillis=" + this.fr + "]";
    }

    public void writeToParcel(Parcel parcel, int i) {
        ActivityRecognitionResultCreator.m1234a(this, parcel, i);
    }
}
