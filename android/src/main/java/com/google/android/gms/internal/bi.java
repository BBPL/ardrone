package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.location.Geofence;

public class bi implements SafeParcelable, Geofence {
    public static final bj CREATOR = new bj();
    private final int ab;
    private final float fA;
    private final long fU;
    private final String fu;
    private final int fv;
    private final short fx;
    private final double fy;
    private final double fz;

    public bi(int i, String str, int i2, short s, double d, double d2, float f, long j) {
        m800A(str);
        m804b(f);
        m803a(d, d2);
        int P = m801P(i2);
        this.ab = i;
        this.fx = s;
        this.fu = str;
        this.fy = d;
        this.fz = d2;
        this.fA = f;
        this.fU = j;
        this.fv = P;
    }

    public bi(String str, int i, short s, double d, double d2, float f, long j) {
        this(1, str, i, s, d, d2, f, j);
    }

    private static void m800A(String str) {
        if (str == null || str.length() > 100) {
            throw new IllegalArgumentException("requestId is null or too long: " + str);
        }
    }

    private static int m801P(int i) {
        int i2 = i & 3;
        if (i2 != 0) {
            return i2;
        }
        throw new IllegalArgumentException("No supported transition specified: " + i);
    }

    private static String m802Q(int i) {
        switch (i) {
            case 1:
                return "CIRCLE";
            default:
                return null;
        }
    }

    private static void m803a(double d, double d2) {
        if (d > 90.0d || d < -90.0d) {
            throw new IllegalArgumentException("invalid latitude: " + d);
        } else if (d2 > 180.0d || d2 < -180.0d) {
            throw new IllegalArgumentException("invalid longitude: " + d2);
        }
    }

    private static void m804b(float f) {
        if (f <= 0.0f) {
            throw new IllegalArgumentException("invalid radius: " + f);
        }
    }

    public static bi m805c(byte[] bArr) {
        Parcel obtain = Parcel.obtain();
        obtain.unmarshall(bArr, 0, bArr.length);
        obtain.setDataPosition(0);
        bi t = CREATOR.m809t(obtain);
        obtain.recycle();
        return t;
    }

    public short aT() {
        return this.fx;
    }

    public float aU() {
        return this.fA;
    }

    public int aV() {
        return this.fv;
    }

    public int describeContents() {
        bj bjVar = CREATOR;
        return 0;
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj == null || !(obj instanceof bi)) {
                return false;
            }
            bi biVar = (bi) obj;
            if (this.fA != biVar.fA || this.fy != biVar.fy || this.fz != biVar.fz) {
                return false;
            }
            if (this.fx != biVar.fx) {
                return false;
            }
        }
        return true;
    }

    public long getExpirationTime() {
        return this.fU;
    }

    public double getLatitude() {
        return this.fy;
    }

    public double getLongitude() {
        return this.fz;
    }

    public String getRequestId() {
        return this.fu;
    }

    public int hashCode() {
        long doubleToLongBits = Double.doubleToLongBits(this.fy);
        int i = (int) (doubleToLongBits ^ (doubleToLongBits >>> 32));
        long doubleToLongBits2 = Double.doubleToLongBits(this.fz);
        return ((((((((i + 31) * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)))) * 31) + Float.floatToIntBits(this.fA)) * 31) + this.fx) * 31) + this.fv;
    }

    public int m806i() {
        return this.ab;
    }

    public String toString() {
        return String.format("Geofence[%s id:%s transitions:%d %.6f, %.6f %.0fm, @%d]", new Object[]{m802Q(this.fx), this.fu, Integer.valueOf(this.fv), Double.valueOf(this.fy), Double.valueOf(this.fz), Float.valueOf(this.fA), Long.valueOf(this.fU)});
    }

    public void writeToParcel(Parcel parcel, int i) {
        bj bjVar = CREATOR;
        bj.m807a(this, parcel, i);
    }
}
