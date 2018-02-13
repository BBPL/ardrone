package com.google.android.gms.maps;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.util.AttributeSet;
import com.google.android.gms.C0041R;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.C0304a;
import com.google.android.gms.maps.internal.C0324q;
import com.google.android.gms.maps.model.CameraPosition;

public final class GoogleMapOptions implements SafeParcelable {
    public static final GoogleMapOptionsCreator CREATOR = new GoogleMapOptionsCreator();
    private final int ab;
    private Boolean go;
    private Boolean gp;
    private int gq;
    private CameraPosition gr;
    private Boolean gs;
    private Boolean gt;
    private Boolean gu;
    private Boolean gv;
    private Boolean gw;
    private Boolean gx;

    public GoogleMapOptions() {
        this.gq = -1;
        this.ab = 1;
    }

    GoogleMapOptions(int i, byte b, byte b2, int i2, CameraPosition cameraPosition, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8) {
        this.gq = -1;
        this.ab = i;
        this.go = C0304a.m1288a(b);
        this.gp = C0304a.m1288a(b2);
        this.gq = i2;
        this.gr = cameraPosition;
        this.gs = C0304a.m1288a(b3);
        this.gt = C0304a.m1288a(b4);
        this.gu = C0304a.m1288a(b5);
        this.gv = C0304a.m1288a(b6);
        this.gw = C0304a.m1288a(b7);
        this.gx = C0304a.m1288a(b8);
    }

    public static GoogleMapOptions createFromAttributes(Context context, AttributeSet attributeSet) {
        if (attributeSet == null) {
            return null;
        }
        TypedArray obtainAttributes = context.getResources().obtainAttributes(attributeSet, C0041R.styleable.MapAttrs);
        GoogleMapOptions googleMapOptions = new GoogleMapOptions();
        if (obtainAttributes.hasValue(0)) {
            googleMapOptions.mapType(obtainAttributes.getInt(0, -1));
        }
        if (obtainAttributes.hasValue(13)) {
            googleMapOptions.zOrderOnTop(obtainAttributes.getBoolean(13, false));
        }
        if (obtainAttributes.hasValue(12)) {
            googleMapOptions.useViewLifecycleInFragment(obtainAttributes.getBoolean(12, false));
        }
        if (obtainAttributes.hasValue(6)) {
            googleMapOptions.compassEnabled(obtainAttributes.getBoolean(6, true));
        }
        if (obtainAttributes.hasValue(7)) {
            googleMapOptions.rotateGesturesEnabled(obtainAttributes.getBoolean(7, true));
        }
        if (obtainAttributes.hasValue(8)) {
            googleMapOptions.scrollGesturesEnabled(obtainAttributes.getBoolean(8, true));
        }
        if (obtainAttributes.hasValue(9)) {
            googleMapOptions.tiltGesturesEnabled(obtainAttributes.getBoolean(9, true));
        }
        if (obtainAttributes.hasValue(11)) {
            googleMapOptions.zoomGesturesEnabled(obtainAttributes.getBoolean(11, true));
        }
        if (obtainAttributes.hasValue(10)) {
            googleMapOptions.zoomControlsEnabled(obtainAttributes.getBoolean(10, true));
        }
        googleMapOptions.camera(CameraPosition.createFromAttributes(context, attributeSet));
        obtainAttributes.recycle();
        return googleMapOptions;
    }

    byte aZ() {
        return C0304a.m1289b(this.go);
    }

    byte ba() {
        return C0304a.m1289b(this.gp);
    }

    byte bb() {
        return C0304a.m1289b(this.gs);
    }

    byte bc() {
        return C0304a.m1289b(this.gt);
    }

    byte bd() {
        return C0304a.m1289b(this.gu);
    }

    byte be() {
        return C0304a.m1289b(this.gv);
    }

    byte bf() {
        return C0304a.m1289b(this.gw);
    }

    byte bg() {
        return C0304a.m1289b(this.gx);
    }

    public GoogleMapOptions camera(CameraPosition cameraPosition) {
        this.gr = cameraPosition;
        return this;
    }

    public GoogleMapOptions compassEnabled(boolean z) {
        this.gt = Boolean.valueOf(z);
        return this;
    }

    public int describeContents() {
        return 0;
    }

    public CameraPosition getCamera() {
        return this.gr;
    }

    public Boolean getCompassEnabled() {
        return this.gt;
    }

    public int getMapType() {
        return this.gq;
    }

    public Boolean getRotateGesturesEnabled() {
        return this.gx;
    }

    public Boolean getScrollGesturesEnabled() {
        return this.gu;
    }

    public Boolean getTiltGesturesEnabled() {
        return this.gw;
    }

    public Boolean getUseViewLifecycleInFragment() {
        return this.gp;
    }

    public Boolean getZOrderOnTop() {
        return this.go;
    }

    public Boolean getZoomControlsEnabled() {
        return this.gs;
    }

    public Boolean getZoomGesturesEnabled() {
        return this.gv;
    }

    int m1274i() {
        return this.ab;
    }

    public GoogleMapOptions mapType(int i) {
        this.gq = i;
        return this;
    }

    public GoogleMapOptions rotateGesturesEnabled(boolean z) {
        this.gx = Boolean.valueOf(z);
        return this;
    }

    public GoogleMapOptions scrollGesturesEnabled(boolean z) {
        this.gu = Boolean.valueOf(z);
        return this;
    }

    public GoogleMapOptions tiltGesturesEnabled(boolean z) {
        this.gw = Boolean.valueOf(z);
        return this;
    }

    public GoogleMapOptions useViewLifecycleInFragment(boolean z) {
        this.gp = Boolean.valueOf(z);
        return this;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (C0324q.bn()) {
            C0290a.m1281a(this, parcel, i);
        } else {
            GoogleMapOptionsCreator.m1275a(this, parcel, i);
        }
    }

    public GoogleMapOptions zOrderOnTop(boolean z) {
        this.go = Boolean.valueOf(z);
        return this;
    }

    public GoogleMapOptions zoomControlsEnabled(boolean z) {
        this.gs = Boolean.valueOf(z);
        return this;
    }

    public GoogleMapOptions zoomGesturesEnabled(boolean z) {
        this.gv = Boolean.valueOf(z);
        return this;
    }
}
