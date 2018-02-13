package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.dynamic.C0075b.C0077a;
import com.google.android.gms.maps.internal.C0324q;

public final class MarkerOptions implements SafeParcelable {
    public static final MarkerOptionsCreator CREATOR = new MarkerOptionsCreator();
    private final int ab;
    private boolean hc;
    private float hk;
    private float hl;
    private LatLng hr;
    private String hs;
    private String ht;
    private BitmapDescriptor hu;
    private boolean hv;
    private boolean hw;
    private float hx;
    private float hy;
    private float hz;

    public MarkerOptions() {
        this.hk = 0.5f;
        this.hl = 1.0f;
        this.hc = true;
        this.hw = false;
        this.hx = 0.0f;
        this.hy = 0.5f;
        this.hz = 0.0f;
        this.ab = 1;
    }

    MarkerOptions(int i, LatLng latLng, String str, String str2, IBinder iBinder, float f, float f2, boolean z, boolean z2, boolean z3, float f3, float f4, float f5) {
        this.hk = 0.5f;
        this.hl = 1.0f;
        this.hc = true;
        this.hw = false;
        this.hx = 0.0f;
        this.hy = 0.5f;
        this.hz = 0.0f;
        this.ab = i;
        this.hr = latLng;
        this.hs = str;
        this.ht = str2;
        this.hu = iBinder == null ? null : new BitmapDescriptor(C0077a.m171l(iBinder));
        this.hk = f;
        this.hl = f2;
        this.hv = z;
        this.hc = z2;
        this.hw = z3;
        this.hx = f3;
        this.hy = f4;
        this.hz = f5;
    }

    public MarkerOptions anchor(float f, float f2) {
        this.hk = f;
        this.hl = f2;
        return this;
    }

    IBinder bq() {
        return this.hu == null ? null : this.hu.aW().asBinder();
    }

    public int describeContents() {
        return 0;
    }

    public MarkerOptions draggable(boolean z) {
        this.hv = z;
        return this;
    }

    public MarkerOptions flat(boolean z) {
        this.hw = z;
        return this;
    }

    public float getAnchorU() {
        return this.hk;
    }

    public float getAnchorV() {
        return this.hl;
    }

    public BitmapDescriptor getIcon() {
        return this.hu;
    }

    public float getInfoWindowAnchorU() {
        return this.hy;
    }

    public float getInfoWindowAnchorV() {
        return this.hz;
    }

    public LatLng getPosition() {
        return this.hr;
    }

    public float getRotation() {
        return this.hx;
    }

    public String getSnippet() {
        return this.ht;
    }

    public String getTitle() {
        return this.hs;
    }

    int m1336i() {
        return this.ab;
    }

    public MarkerOptions icon(BitmapDescriptor bitmapDescriptor) {
        this.hu = bitmapDescriptor;
        return this;
    }

    public MarkerOptions infoWindowAnchor(float f, float f2) {
        this.hy = f;
        this.hz = f2;
        return this;
    }

    public boolean isDraggable() {
        return this.hv;
    }

    public boolean isFlat() {
        return this.hw;
    }

    public boolean isVisible() {
        return this.hc;
    }

    public MarkerOptions position(LatLng latLng) {
        this.hr = latLng;
        return this;
    }

    public MarkerOptions rotation(float f) {
        this.hx = f;
        return this;
    }

    public MarkerOptions snippet(String str) {
        this.ht = str;
        return this;
    }

    public MarkerOptions title(String str) {
        this.hs = str;
        return this;
    }

    public MarkerOptions visible(boolean z) {
        this.hc = z;
        return this;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (C0324q.bn()) {
            C0334f.m1357a(this, parcel, i);
        } else {
            MarkerOptionsCreator.m1337a(this, parcel, i);
        }
    }
}
