package com.google.android.gms.location;

import android.os.Parcel;
import android.os.SystemClock;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.C0241r;

public final class LocationRequest implements SafeParcelable {
    public static final LocationRequestCreator CREATOR = new LocationRequestCreator();
    public static final int PRIORITY_BALANCED_POWER_ACCURACY = 102;
    public static final int PRIORITY_HIGH_ACCURACY = 100;
    public static final int PRIORITY_LOW_POWER = 104;
    public static final int PRIORITY_NO_POWER = 105;
    private final int ab;
    long fB;
    long fC;
    boolean fD;
    int fE;
    float fF;
    long fw;
    int mPriority;

    public LocationRequest() {
        this.ab = 1;
        this.mPriority = 102;
        this.fB = 3600000;
        this.fC = 600000;
        this.fD = false;
        this.fw = Long.MAX_VALUE;
        this.fE = Integer.MAX_VALUE;
        this.fF = 0.0f;
    }

    LocationRequest(int i, int i2, long j, long j2, boolean z, long j3, int i3, float f) {
        this.ab = i;
        this.mPriority = i2;
        this.fB = j;
        this.fC = j2;
        this.fD = z;
        this.fw = j3;
        this.fE = i3;
        this.fF = f;
    }

    private static void m1238M(int i) {
        switch (i) {
            case 100:
            case 102:
            case PRIORITY_LOW_POWER /*104*/:
            case PRIORITY_NO_POWER /*105*/:
                return;
            default:
                throw new IllegalArgumentException("invalid quality: " + i);
        }
    }

    public static String m1239N(int i) {
        switch (i) {
            case 100:
                return "PRIORITY_HIGH_ACCURACY";
            case 102:
                return "PRIORITY_BALANCED_POWER_ACCURACY";
            case PRIORITY_LOW_POWER /*104*/:
                return "PRIORITY_LOW_POWER";
            case PRIORITY_NO_POWER /*105*/:
                return "PRIORITY_NO_POWER";
            default:
                return "???";
        }
    }

    private static void m1240a(float f) {
        if (f < 0.0f) {
            throw new IllegalArgumentException("invalid displacement: " + f);
        }
    }

    private static void m1241c(long j) {
        if (j < 0) {
            throw new IllegalArgumentException("invalid interval: " + j);
        }
    }

    public static LocationRequest create() {
        return new LocationRequest();
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (!(obj instanceof LocationRequest)) {
                return false;
            }
            LocationRequest locationRequest = (LocationRequest) obj;
            if (this.mPriority != locationRequest.mPriority || this.fB != locationRequest.fB || this.fC != locationRequest.fC || this.fD != locationRequest.fD || this.fw != locationRequest.fw || this.fE != locationRequest.fE) {
                return false;
            }
            if (this.fF != locationRequest.fF) {
                return false;
            }
        }
        return true;
    }

    public long getExpirationTime() {
        return this.fw;
    }

    public long getFastestInterval() {
        return this.fC;
    }

    public long getInterval() {
        return this.fB;
    }

    public int getNumUpdates() {
        return this.fE;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public float getSmallestDisplacement() {
        return this.fF;
    }

    public int hashCode() {
        return C0241r.hashCode(Integer.valueOf(this.mPriority), Long.valueOf(this.fB), Long.valueOf(this.fC), Boolean.valueOf(this.fD), Long.valueOf(this.fw), Integer.valueOf(this.fE), Float.valueOf(this.fF));
    }

    int m1242i() {
        return this.ab;
    }

    public LocationRequest setExpirationDuration(long j) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (j > Long.MAX_VALUE - elapsedRealtime) {
            this.fw = Long.MAX_VALUE;
        } else {
            this.fw = elapsedRealtime + j;
        }
        if (this.fw < 0) {
            this.fw = 0;
        }
        return this;
    }

    public LocationRequest setExpirationTime(long j) {
        this.fw = j;
        if (this.fw < 0) {
            this.fw = 0;
        }
        return this;
    }

    public LocationRequest setFastestInterval(long j) {
        m1241c(j);
        this.fD = true;
        this.fC = j;
        return this;
    }

    public LocationRequest setInterval(long j) {
        m1241c(j);
        this.fB = j;
        if (!this.fD) {
            this.fC = (long) (((double) this.fB) / 6.0d);
        }
        return this;
    }

    public LocationRequest setNumUpdates(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("invalid numUpdates: " + i);
        }
        this.fE = i;
        return this;
    }

    public LocationRequest setPriority(int i) {
        m1238M(i);
        this.mPriority = i;
        return this;
    }

    public LocationRequest setSmallestDisplacement(float f) {
        m1240a(f);
        this.fF = f;
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Request[").append(m1239N(this.mPriority));
        if (this.mPriority != PRIORITY_NO_POWER) {
            stringBuilder.append(" requested=");
            stringBuilder.append(this.fB + "ms");
        }
        stringBuilder.append(" fastest=");
        stringBuilder.append(this.fC + "ms");
        if (this.fw != Long.MAX_VALUE) {
            long j = this.fw;
            long elapsedRealtime = SystemClock.elapsedRealtime();
            stringBuilder.append(" expireIn=");
            stringBuilder.append((j - elapsedRealtime) + "ms");
        }
        if (this.fE != Integer.MAX_VALUE) {
            stringBuilder.append(" num=").append(this.fE);
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        LocationRequestCreator.m1243a(this, parcel, i);
    }
}
