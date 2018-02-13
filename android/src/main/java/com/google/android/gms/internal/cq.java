package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;

public class cq implements SafeParcelable {
    public static final cr CREATOR = new cr();
    private final int ab;
    private final ArrayList<C0246x> kA;
    private final Bundle kB;
    private final boolean kC;
    private final int ky;
    private final ArrayList<C0246x> kz;

    public cq(int i, ArrayList<C0246x> arrayList, ArrayList<C0246x> arrayList2, Bundle bundle, boolean z, int i2) {
        this.ab = i;
        this.kz = arrayList;
        this.kA = arrayList2;
        this.kB = bundle;
        this.kC = z;
        this.ky = i2;
    }

    public int cJ() {
        return this.ky;
    }

    public ArrayList<C0246x> cK() {
        return this.kz;
    }

    public ArrayList<C0246x> cL() {
        return this.kA;
    }

    public Bundle cM() {
        return this.kB;
    }

    public boolean cN() {
        return this.kC;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj instanceof cq) {
            cq cqVar = (cq) obj;
            if (this.ab == cqVar.ab && C0241r.m1200a(this.kz, cqVar.kz) && C0241r.m1200a(this.kA, cqVar.kA) && C0241r.m1200a(this.kB, cqVar.kB) && C0241r.m1200a(Integer.valueOf(this.ky), Integer.valueOf(cqVar.ky))) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return C0241r.hashCode(Integer.valueOf(this.ab), this.kz, this.kA, this.kB, Integer.valueOf(this.ky));
    }

    public int m1106i() {
        return this.ab;
    }

    public void writeToParcel(Parcel parcel, int i) {
        cr.m1107a(this, parcel, i);
    }
}
