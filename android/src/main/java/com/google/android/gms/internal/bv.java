package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;

public class bv implements SafeParcelable {
    public static final bw CREATOR = new bw();
    private final int ab;
    private final String di;
    private final ArrayList<C0246x> iA;
    private final boolean iB;
    private final ArrayList<C0246x> iz;

    public bv(int i, String str, ArrayList<C0246x> arrayList, ArrayList<C0246x> arrayList2, boolean z) {
        this.ab = i;
        this.di = str;
        this.iz = arrayList;
        this.iA = arrayList2;
        this.iB = z;
    }

    public ArrayList<C0246x> bE() {
        return this.iz;
    }

    public ArrayList<C0246x> bF() {
        return this.iA;
    }

    public boolean bG() {
        return this.iB;
    }

    public int describeContents() {
        return 0;
    }

    public String getDescription() {
        return this.di;
    }

    public int m978i() {
        return this.ab;
    }

    public void writeToParcel(Parcel parcel, int i) {
        bw.m979a(this, parcel, i);
    }
}
