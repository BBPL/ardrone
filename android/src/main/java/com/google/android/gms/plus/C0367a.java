package com.google.android.gms.plus;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.C0241r;

public class C0367a implements SafeParcelable {
    public static final C0368b CREATOR = new C0368b();
    private final int ab;
    private final String f131g;
    private final String[] hY;
    private final String hZ;
    private final String ia;
    private final String ib;
    private final String[] ik;
    private final String[] il;

    public C0367a(int i, String str, String[] strArr, String[] strArr2, String[] strArr3, String str2, String str3, String str4) {
        this.ab = i;
        this.f131g = str;
        this.ik = strArr;
        this.il = strArr2;
        this.hY = strArr3;
        this.hZ = str2;
        this.ia = str3;
        this.ib = str4;
    }

    public C0367a(String str, String[] strArr, String[] strArr2, String[] strArr3, String str2, String str3, String str4) {
        this.ab = 1;
        this.f131g = str;
        this.ik = strArr;
        this.il = strArr2;
        this.hY = strArr3;
        this.hZ = str2;
        this.ia = str3;
        this.ib = str4;
    }

    public String[] bA() {
        return this.hY;
    }

    public String bB() {
        return this.hZ;
    }

    public String bC() {
        return this.ia;
    }

    public String bD() {
        return this.ib;
    }

    public String[] by() {
        return this.ik;
    }

    public String[] bz() {
        return this.il;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj instanceof C0367a) {
            C0367a c0367a = (C0367a) obj;
            if (this.ab == c0367a.ab && C0241r.m1200a(this.f131g, c0367a.f131g) && C0241r.m1200a(this.ik, c0367a.ik) && C0241r.m1200a(this.il, c0367a.il) && C0241r.m1200a(this.hY, c0367a.hY) && C0241r.m1200a(this.hZ, c0367a.hZ) && C0241r.m1200a(this.ia, c0367a.ia) && C0241r.m1200a(this.ib, c0367a.ib)) {
                return true;
            }
        }
        return false;
    }

    public String getAccountName() {
        return this.f131g;
    }

    public int hashCode() {
        return C0241r.hashCode(Integer.valueOf(this.ab), this.f131g, this.ik, this.il, this.hY, this.hZ, this.ia, this.ib);
    }

    public int m1403i() {
        return this.ab;
    }

    public String toString() {
        return C0241r.m1201c(this).m1199a("versionCode", Integer.valueOf(this.ab)).m1199a("accountName", this.f131g).m1199a("requestedScopes", this.ik).m1199a("visibleActivities", this.il).m1199a("requiredFeatures", this.hY).m1199a("packageNameForAuth", this.hZ).m1199a("callingPackageName", this.ia).m1199a("applicationName", this.ib).toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        C0368b.m1404a(this, parcel, i);
    }
}
