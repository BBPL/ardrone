package com.google.android.gms.location;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class DetectedActivity implements SafeParcelable {
    public static final DetectedActivityCreator CREATOR = new DetectedActivityCreator();
    public static final int IN_VEHICLE = 0;
    public static final int ON_BICYCLE = 1;
    public static final int ON_FOOT = 2;
    public static final int STILL = 3;
    public static final int TILTING = 5;
    public static final int UNKNOWN = 4;
    private final int ab;
    int fs;
    int ft;

    public DetectedActivity(int i, int i2) {
        this.ab = 1;
        this.fs = i;
        this.ft = i2;
    }

    public DetectedActivity(int i, int i2, int i3) {
        this.ab = i;
        this.fs = i2;
        this.ft = i3;
    }

    private int m1235L(int i) {
        return i > 5 ? 4 : i;
    }

    public int describeContents() {
        return 0;
    }

    public int getConfidence() {
        return this.ft;
    }

    public int getType() {
        return m1235L(this.fs);
    }

    public int m1236i() {
        return this.ab;
    }

    public String toString() {
        return "DetectedActivity [type=" + getType() + ", confidence=" + this.ft + "]";
    }

    public void writeToParcel(Parcel parcel, int i) {
        DetectedActivityCreator.m1237a(this, parcel, i);
    }
}
