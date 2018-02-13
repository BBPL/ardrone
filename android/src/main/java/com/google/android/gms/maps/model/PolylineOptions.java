package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.C0324q;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PolylineOptions implements SafeParcelable {
    public static final PolylineOptionsCreator CREATOR = new PolylineOptionsCreator();
    private int f116P;
    private final int ab;
    private final List<LatLng> hB;
    private boolean hD;
    private float hb;
    private boolean hc;
    private float hg;

    public PolylineOptions() {
        this.hg = 10.0f;
        this.f116P = -16777216;
        this.hb = 0.0f;
        this.hc = true;
        this.hD = false;
        this.ab = 1;
        this.hB = new ArrayList();
    }

    PolylineOptions(int i, List list, float f, int i2, float f2, boolean z, boolean z2) {
        this.hg = 10.0f;
        this.f116P = -16777216;
        this.hb = 0.0f;
        this.hc = true;
        this.hD = false;
        this.ab = i;
        this.hB = list;
        this.hg = f;
        this.f116P = i2;
        this.hb = f2;
        this.hc = z;
        this.hD = z2;
    }

    public PolylineOptions add(LatLng latLng) {
        this.hB.add(latLng);
        return this;
    }

    public PolylineOptions add(LatLng... latLngArr) {
        this.hB.addAll(Arrays.asList(latLngArr));
        return this;
    }

    public PolylineOptions addAll(Iterable<LatLng> iterable) {
        for (LatLng add : iterable) {
            this.hB.add(add);
        }
        return this;
    }

    public PolylineOptions color(int i) {
        this.f116P = i;
        return this;
    }

    public int describeContents() {
        return 0;
    }

    public PolylineOptions geodesic(boolean z) {
        this.hD = z;
        return this;
    }

    public int getColor() {
        return this.f116P;
    }

    public List<LatLng> getPoints() {
        return this.hB;
    }

    public float getWidth() {
        return this.hg;
    }

    public float getZIndex() {
        return this.hb;
    }

    int m1340i() {
        return this.ab;
    }

    public boolean isGeodesic() {
        return this.hD;
    }

    public boolean isVisible() {
        return this.hc;
    }

    public PolylineOptions visible(boolean z) {
        this.hc = z;
        return this;
    }

    public PolylineOptions width(float f) {
        this.hg = f;
        return this;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (C0324q.bn()) {
            C0336h.m1359a(this, parcel, i);
        } else {
            PolylineOptionsCreator.m1341a(this, parcel, i);
        }
    }

    public PolylineOptions zIndex(float f) {
        this.hb = f;
        return this;
    }
}
