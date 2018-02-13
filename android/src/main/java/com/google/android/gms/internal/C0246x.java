package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class C0246x implements SafeParcelable {
    public static final C0247y CREATOR = new C0247y();
    private final int aJ;
    private final int ab;
    private final int ci;
    private final String cj;
    private final String ck;
    private final String cl;
    private final String cm;

    public C0246x(int i, int i2, int i3, String str, String str2, String str3, String str4) {
        this.ab = i;
        this.aJ = i2;
        this.ci = i3;
        this.cj = str;
        this.ck = str2;
        this.cl = str3;
        this.cm = str4;
    }

    public int m1219I() {
        return this.ci;
    }

    public String m1220J() {
        return this.cj;
    }

    public String m1221K() {
        return this.ck;
    }

    public String m1222L() {
        return this.cm;
    }

    public boolean m1223M() {
        return this.aJ == 1 && this.ci == -1;
    }

    public boolean m1224N() {
        return this.aJ == 2;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj instanceof C0246x) {
            C0246x c0246x = (C0246x) obj;
            if (this.ab == c0246x.ab && this.aJ == c0246x.aJ && this.ci == c0246x.ci && C0241r.m1200a(this.cj, c0246x.cj) && C0241r.m1200a(this.ck, c0246x.ck)) {
                return true;
            }
        }
        return false;
    }

    public String getDisplayName() {
        return this.cl;
    }

    public int getType() {
        return this.aJ;
    }

    public int hashCode() {
        return C0241r.hashCode(Integer.valueOf(this.ab), Integer.valueOf(this.aJ), Integer.valueOf(this.ci), this.cj, this.ck);
    }

    public int m1225i() {
        return this.ab;
    }

    public String toString() {
        if (m1224N()) {
            return String.format("Person [%s] %s", new Object[]{m1221K(), getDisplayName()});
        } else if (m1223M()) {
            return String.format("Circle [%s] %s", new Object[]{m1220J(), getDisplayName()});
        } else {
            return String.format("Group [%s] %s", new Object[]{m1220J(), getDisplayName()});
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        C0247y.m1226a(this, parcel, i);
    }
}
