package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.C0241r;
import com.google.android.gms.maps.internal.C0324q;

public final class VisibleRegion implements SafeParcelable {
    public static final VisibleRegionCreator CREATOR = new VisibleRegionCreator();
    private final int ab;
    public final LatLng farLeft;
    public final LatLng farRight;
    public final LatLngBounds latLngBounds;
    public final LatLng nearLeft;
    public final LatLng nearRight;

    VisibleRegion(int i, LatLng latLng, LatLng latLng2, LatLng latLng3, LatLng latLng4, LatLngBounds latLngBounds) {
        this.ab = i;
        this.nearLeft = latLng;
        this.nearRight = latLng2;
        this.farLeft = latLng3;
        this.farRight = latLng4;
        this.latLngBounds = latLngBounds;
    }

    public VisibleRegion(LatLng latLng, LatLng latLng2, LatLng latLng3, LatLng latLng4, LatLngBounds latLngBounds) {
        this(1, latLng, latLng2, latLng3, latLng4, latLngBounds);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (!(obj instanceof VisibleRegion)) {
                return false;
            }
            VisibleRegion visibleRegion = (VisibleRegion) obj;
            if (!this.nearLeft.equals(visibleRegion.nearLeft) || !this.nearRight.equals(visibleRegion.nearRight) || !this.farLeft.equals(visibleRegion.farLeft) || !this.farRight.equals(visibleRegion.farRight)) {
                return false;
            }
            if (!this.latLngBounds.equals(visibleRegion.latLngBounds)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return C0241r.hashCode(this.nearLeft, this.nearRight, this.farLeft, this.farRight, this.latLngBounds);
    }

    int m1350i() {
        return this.ab;
    }

    public String toString() {
        return C0241r.m1201c(this).m1199a("nearLeft", this.nearLeft).m1199a("nearRight", this.nearRight).m1199a("farLeft", this.farLeft).m1199a("farRight", this.farRight).m1199a("latLngBounds", this.latLngBounds).toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (C0324q.bn()) {
            C0360k.m1395a(this, parcel, i);
        } else {
            VisibleRegionCreator.m1351a(this, parcel, i);
        }
    }
}
