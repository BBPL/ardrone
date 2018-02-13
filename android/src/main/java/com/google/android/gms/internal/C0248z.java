package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.ae.C0112b;

public class C0248z implements SafeParcelable {
    public static final aa CREATOR = new aa();
    private final int ab;
    private final ab cn;

    C0248z(int i, ab abVar) {
        this.ab = i;
        this.cn = abVar;
    }

    private C0248z(ab abVar) {
        this.ab = 1;
        this.cn = abVar;
    }

    public static C0248z m1229a(C0112b<?, ?> c0112b) {
        if (c0112b instanceof ab) {
            return new C0248z((ab) c0112b);
        }
        throw new IllegalArgumentException("Unsupported safe parcelable field converter class.");
    }

    ab m1230O() {
        return this.cn;
    }

    public C0112b<?, ?> m1231P() {
        if (this.cn != null) {
            return this.cn;
        }
        throw new IllegalStateException("There was no converter wrapped in this ConverterWrapper.");
    }

    public int describeContents() {
        aa aaVar = CREATOR;
        return 0;
    }

    int m1232i() {
        return this.ab;
    }

    public void writeToParcel(Parcel parcel, int i) {
        aa aaVar = CREATOR;
        aa.m249a(this, parcel, i);
    }
}
