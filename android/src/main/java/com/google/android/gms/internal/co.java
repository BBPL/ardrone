package com.google.android.gms.internal;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.List;

public class co implements SafeParcelable {
    public static final cp CREATOR = new cp();
    private final int ab;
    private final String jh;
    private final List<C0246x> kq;
    private final List<Uri> kr;
    private final Uri ks;
    private final String kt;
    private final String ku;
    private final String kv;
    private final Bundle kw;
    private final Bundle kx;
    private final int ky;

    public co(int i, String str, List<C0246x> list, List<Uri> list2, Uri uri, String str2, String str3, String str4, Bundle bundle, Bundle bundle2, int i2) {
        this.ab = i;
        this.jh = str;
        this.kq = list;
        this.kr = list2;
        this.ks = uri;
        this.kt = str2;
        this.ku = str3;
        this.kv = str4;
        this.kw = bundle;
        this.kx = bundle2;
        this.ky = i2;
    }

    public List<C0246x> cB() {
        return this.kq;
    }

    public List<Uri> cC() {
        return this.kr;
    }

    public Uri cD() {
        return this.ks;
    }

    public String cE() {
        return this.kt;
    }

    public String cF() {
        return this.ku;
    }

    public String cG() {
        return this.kv;
    }

    public Bundle cH() {
        return this.kw;
    }

    public Bundle cI() {
        return this.kx;
    }

    public int cJ() {
        return this.ky;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj instanceof co) {
            co coVar = (co) obj;
            if (this.ab == coVar.ab && C0241r.m1200a(this.kq, coVar.kq) && C0241r.m1200a(this.kr, coVar.kr) && C0241r.m1200a(this.ks, coVar.ks) && C0241r.m1200a(this.kt, coVar.kt) && C0241r.m1200a(this.ku, coVar.ku) && C0241r.m1200a(this.kv, coVar.kv)) {
                return true;
            }
        }
        return false;
    }

    public String getId() {
        return this.jh;
    }

    public int hashCode() {
        return C0241r.hashCode(Integer.valueOf(this.ab), this.kq, this.kr, this.ks, this.kt, this.ku, this.kv);
    }

    public int m1103i() {
        return this.ab;
    }

    public void writeToParcel(Parcel parcel, int i) {
        cp.m1104a(this, parcel, i);
    }
}
