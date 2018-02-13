package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.C0324q;

public final class Tile implements SafeParcelable {
    public static final TileCreator CREATOR = new TileCreator();
    private final int ab;
    public final byte[] data;
    public final int height;
    public final int width;

    Tile(int i, int i2, int i3, byte[] bArr) {
        this.ab = i;
        this.width = i2;
        this.height = i3;
        this.data = bArr;
    }

    public Tile(int i, int i2, byte[] bArr) {
        this(1, i, i2, bArr);
    }

    public int describeContents() {
        return 0;
    }

    int m1342i() {
        return this.ab;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (C0324q.bn()) {
            C0337i.m1360a(this, parcel, i);
        } else {
            TileCreator.m1343a(this, parcel, i);
        }
    }
}
