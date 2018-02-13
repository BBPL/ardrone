package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.dynamic.C0075b.C0077a;
import com.google.android.gms.internal.C0242s;
import com.google.android.gms.maps.internal.C0324q;

public final class GroundOverlayOptions implements SafeParcelable {
    public static final GroundOverlayOptionsCreator CREATOR = new GroundOverlayOptionsCreator();
    public static final float NO_DIMENSION = -1.0f;
    private final int ab;
    private float gU;
    private float hb;
    private boolean hc;
    private BitmapDescriptor he;
    private LatLng hf;
    private float hg;
    private float hh;
    private LatLngBounds hi;
    private float hj;
    private float hk;
    private float hl;

    public GroundOverlayOptions() {
        this.hc = true;
        this.hj = 0.0f;
        this.hk = 0.5f;
        this.hl = 0.5f;
        this.ab = 1;
    }

    GroundOverlayOptions(int i, IBinder iBinder, LatLng latLng, float f, float f2, LatLngBounds latLngBounds, float f3, float f4, boolean z, float f5, float f6, float f7) {
        this.hc = true;
        this.hj = 0.0f;
        this.hk = 0.5f;
        this.hl = 0.5f;
        this.ab = i;
        this.he = new BitmapDescriptor(C0077a.m171l(iBinder));
        this.hf = latLng;
        this.hg = f;
        this.hh = f2;
        this.hi = latLngBounds;
        this.gU = f3;
        this.hb = f4;
        this.hc = z;
        this.hj = f5;
        this.hk = f6;
        this.hl = f7;
    }

    private GroundOverlayOptions m1322a(LatLng latLng, float f, float f2) {
        this.hf = latLng;
        this.hg = f;
        this.hh = f2;
        return this;
    }

    public GroundOverlayOptions anchor(float f, float f2) {
        this.hk = f;
        this.hl = f2;
        return this;
    }

    public GroundOverlayOptions bearing(float f) {
        this.gU = ((f % 360.0f) + 360.0f) % 360.0f;
        return this;
    }

    IBinder bp() {
        return this.he.aW().asBinder();
    }

    public int describeContents() {
        return 0;
    }

    public float getAnchorU() {
        return this.hk;
    }

    public float getAnchorV() {
        return this.hl;
    }

    public float getBearing() {
        return this.gU;
    }

    public LatLngBounds getBounds() {
        return this.hi;
    }

    public float getHeight() {
        return this.hh;
    }

    public BitmapDescriptor getImage() {
        return this.he;
    }

    public LatLng getLocation() {
        return this.hf;
    }

    public float getTransparency() {
        return this.hj;
    }

    public float getWidth() {
        return this.hg;
    }

    public float getZIndex() {
        return this.hb;
    }

    int m1323i() {
        return this.ab;
    }

    public GroundOverlayOptions image(BitmapDescriptor bitmapDescriptor) {
        this.he = bitmapDescriptor;
        return this;
    }

    public boolean isVisible() {
        return this.hc;
    }

    public GroundOverlayOptions position(LatLng latLng, float f) {
        boolean z = true;
        C0242s.m1203a(this.hi == null, "Position has already been set using positionFromBounds");
        C0242s.m1206b(latLng != null, (Object) "Location must be specified");
        if (f < 0.0f) {
            z = false;
        }
        C0242s.m1206b(z, (Object) "Width must be non-negative");
        return m1322a(latLng, f, NO_DIMENSION);
    }

    public GroundOverlayOptions position(LatLng latLng, float f, float f2) {
        boolean z = true;
        C0242s.m1203a(this.hi == null, "Position has already been set using positionFromBounds");
        C0242s.m1206b(latLng != null, (Object) "Location must be specified");
        C0242s.m1206b(f >= 0.0f, (Object) "Width must be non-negative");
        if (f2 < 0.0f) {
            z = false;
        }
        C0242s.m1206b(z, (Object) "Height must be non-negative");
        return m1322a(latLng, f, f2);
    }

    public GroundOverlayOptions positionFromBounds(LatLngBounds latLngBounds) {
        C0242s.m1203a(this.hf == null, "Position has already been set using position: " + this.hf);
        this.hi = latLngBounds;
        return this;
    }

    public GroundOverlayOptions transparency(float f) {
        boolean z = f >= 0.0f && f <= 1.0f;
        C0242s.m1206b(z, (Object) "Transparency must be in the range [0..1]");
        this.hj = f;
        return this;
    }

    public GroundOverlayOptions visible(boolean z) {
        this.hc = z;
        return this;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (C0324q.bn()) {
            C0331c.m1354a(this, parcel, i);
        } else {
            GroundOverlayOptionsCreator.m1324a(this, parcel, i);
        }
    }

    public GroundOverlayOptions zIndex(float f) {
        this.hb = f;
        return this;
    }
}
