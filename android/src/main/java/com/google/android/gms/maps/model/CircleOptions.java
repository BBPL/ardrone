package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.C0324q;

public final class CircleOptions implements SafeParcelable {
    public static final CircleOptionsCreator CREATOR = new CircleOptionsCreator();
    private final int ab;
    private LatLng gW;
    private double gX;
    private float gY;
    private int gZ;
    private int ha;
    private float hb;
    private boolean hc;

    public CircleOptions() {
        this.gW = null;
        this.gX = 0.0d;
        this.gY = 10.0f;
        this.gZ = -16777216;
        this.ha = 0;
        this.hb = 0.0f;
        this.hc = true;
        this.ab = 1;
    }

    CircleOptions(int i, LatLng latLng, double d, float f, int i2, int i3, float f2, boolean z) {
        this.gW = null;
        this.gX = 0.0d;
        this.gY = 10.0f;
        this.gZ = -16777216;
        this.ha = 0;
        this.hb = 0.0f;
        this.hc = true;
        this.ab = i;
        this.gW = latLng;
        this.gX = d;
        this.gY = f;
        this.gZ = i2;
        this.ha = i3;
        this.hb = f2;
        this.hc = z;
    }

    public CircleOptions center(LatLng latLng) {
        this.gW = latLng;
        return this;
    }

    public int describeContents() {
        return 0;
    }

    public CircleOptions fillColor(int i) {
        this.ha = i;
        return this;
    }

    public LatLng getCenter() {
        return this.gW;
    }

    public int getFillColor() {
        return this.ha;
    }

    public double getRadius() {
        return this.gX;
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

    int m1320i() {
        return this.ab;
    }

    public boolean isVisible() {
        return this.hc;
    }

    public CircleOptions radius(double d) {
        this.gX = d;
        return this;
    }

    public CircleOptions strokeColor(int i) {
        this.gZ = i;
        return this;
    }

    public CircleOptions strokeWidth(float f) {
        this.gY = f;
        return this;
    }

    public CircleOptions visible(boolean z) {
        this.hc = z;
        return this;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (C0324q.bn()) {
            C0330b.m1353a(this, parcel, i);
        } else {
            CircleOptionsCreator.m1321a(this, parcel, i);
        }
    }

    public CircleOptions zIndex(float f) {
        this.hb = f;
        return this;
    }
}
