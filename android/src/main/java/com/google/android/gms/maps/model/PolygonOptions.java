package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.C0324q;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PolygonOptions implements SafeParcelable {
    public static final PolygonOptionsCreator CREATOR = new PolygonOptionsCreator();
    private final int ab;
    private float gY;
    private int gZ;
    private final List<LatLng> hB;
    private final List<List<LatLng>> hC;
    private boolean hD;
    private int ha;
    private float hb;
    private boolean hc;

    public PolygonOptions() {
        this.gY = 10.0f;
        this.gZ = -16777216;
        this.ha = 0;
        this.hb = 0.0f;
        this.hc = true;
        this.hD = false;
        this.ab = 1;
        this.hB = new ArrayList();
        this.hC = new ArrayList();
    }

    PolygonOptions(int i, List<LatLng> list, List list2, float f, int i2, int i3, float f2, boolean z, boolean z2) {
        this.gY = 10.0f;
        this.gZ = -16777216;
        this.ha = 0;
        this.hb = 0.0f;
        this.hc = true;
        this.hD = false;
        this.ab = i;
        this.hB = list;
        this.hC = list2;
        this.gY = f;
        this.gZ = i2;
        this.ha = i3;
        this.hb = f2;
        this.hc = z;
        this.hD = z2;
    }

    public PolygonOptions add(LatLng latLng) {
        this.hB.add(latLng);
        return this;
    }

    public PolygonOptions add(LatLng... latLngArr) {
        this.hB.addAll(Arrays.asList(latLngArr));
        return this;
    }

    public PolygonOptions addAll(Iterable<LatLng> iterable) {
        for (LatLng add : iterable) {
            this.hB.add(add);
        }
        return this;
    }

    public PolygonOptions addHole(Iterable<LatLng> iterable) {
        ArrayList arrayList = new ArrayList();
        for (LatLng add : iterable) {
            arrayList.add(add);
        }
        this.hC.add(arrayList);
        return this;
    }

    List br() {
        return this.hC;
    }

    public int describeContents() {
        return 0;
    }

    public PolygonOptions fillColor(int i) {
        this.ha = i;
        return this;
    }

    public PolygonOptions geodesic(boolean z) {
        this.hD = z;
        return this;
    }

    public int getFillColor() {
        return this.ha;
    }

    public List<List<LatLng>> getHoles() {
        return this.hC;
    }

    public List<LatLng> getPoints() {
        return this.hB;
    }

    public int getStrokeColor() {
        return this.gZ;
    }

    public float getStrokeWidth() {
        return this.gY;
    }

    public float getZIndex() {
        return this.hb;
    }

    int m1338i() {
        return this.ab;
    }

    public boolean isGeodesic() {
        return this.hD;
    }

    public boolean isVisible() {
        return this.hc;
    }

    public PolygonOptions strokeColor(int i) {
        this.gZ = i;
        return this;
    }

    public PolygonOptions strokeWidth(float f) {
        this.gY = f;
        return this;
    }

    public PolygonOptions visible(boolean z) {
        this.hc = z;
        return this;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (C0324q.bn()) {
            C0335g.m1358a(this, parcel, i);
        } else {
            PolygonOptionsCreator.m1339a(this, parcel, i);
        }
    }

    public PolygonOptions zIndex(float f) {
        this.hb = f;
        return this;
    }
}
