package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.C0241r;
import com.google.android.gms.internal.C0242s;
import com.google.android.gms.maps.internal.C0324q;

public final class LatLngBounds implements SafeParcelable {
    public static final LatLngBoundsCreator CREATOR = new LatLngBoundsCreator();
    private final int ab;
    public final LatLng northeast;
    public final LatLng southwest;

    public static final class Builder {
        private double hm = Double.POSITIVE_INFINITY;
        private double hn = Double.NEGATIVE_INFINITY;
        private double ho = Double.NaN;
        private double hp = Double.NaN;

        private boolean m1326b(double d) {
            return this.ho <= this.hp ? this.ho <= d && d <= this.hp : this.ho <= d || d <= this.hp;
        }

        public LatLngBounds build() {
            C0242s.m1203a(!Double.isNaN(this.ho), "no included points");
            return new LatLngBounds(new LatLng(this.hm, this.ho), new LatLng(this.hn, this.hp));
        }

        public Builder include(LatLng latLng) {
            this.hm = Math.min(this.hm, latLng.latitude);
            this.hn = Math.max(this.hn, latLng.latitude);
            double d = latLng.longitude;
            if (Double.isNaN(this.ho)) {
                this.ho = d;
                this.hp = d;
            } else if (!m1326b(d)) {
                if (LatLngBounds.m1328b(this.ho, d) < LatLngBounds.m1330c(this.hp, d)) {
                    this.ho = d;
                } else {
                    this.hp = d;
                }
            }
            return this;
        }
    }

    LatLngBounds(int i, LatLng latLng, LatLng latLng2) {
        C0242s.m1205b((Object) latLng, (Object) "null southwest");
        C0242s.m1205b((Object) latLng2, (Object) "null northeast");
        C0242s.m1204a(latLng2.latitude >= latLng.latitude, "southern latitude exceeds northern latitude (%s > %s)", Double.valueOf(latLng.latitude), Double.valueOf(latLng2.latitude));
        this.ab = i;
        this.southwest = latLng;
        this.northeast = latLng2;
    }

    public LatLngBounds(LatLng latLng, LatLng latLng2) {
        this(1, latLng, latLng2);
    }

    private boolean m1327a(double d) {
        return this.southwest.latitude <= d && d <= this.northeast.latitude;
    }

    private static double m1328b(double d, double d2) {
        return ((d - d2) + 360.0d) % 360.0d;
    }

    private boolean m1329b(double d) {
        return this.southwest.longitude <= this.northeast.longitude ? this.southwest.longitude <= d && d <= this.northeast.longitude : this.southwest.longitude <= d || d <= this.northeast.longitude;
    }

    public static Builder builder() {
        return new Builder();
    }

    private static double m1330c(double d, double d2) {
        return ((d2 - d) + 360.0d) % 360.0d;
    }

    public boolean contains(LatLng latLng) {
        return m1327a(latLng.latitude) && m1329b(latLng.longitude);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (!(obj instanceof LatLngBounds)) {
                return false;
            }
            LatLngBounds latLngBounds = (LatLngBounds) obj;
            if (!this.southwest.equals(latLngBounds.southwest)) {
                return false;
            }
            if (!this.northeast.equals(latLngBounds.northeast)) {
                return false;
            }
        }
        return true;
    }

    public LatLng getCenter() {
        double d = (this.southwest.latitude + this.northeast.latitude) / 2.0d;
        double d2 = this.northeast.longitude;
        double d3 = this.southwest.longitude;
        return new LatLng(d, d3 <= d2 ? (d2 + d3) / 2.0d : ((d2 + 360.0d) + d3) / 2.0d);
    }

    public int hashCode() {
        return C0241r.hashCode(this.southwest, this.northeast);
    }

    int m1333i() {
        return this.ab;
    }

    public LatLngBounds including(LatLng latLng) {
        double min = Math.min(this.southwest.latitude, latLng.latitude);
        double max = Math.max(this.northeast.latitude, latLng.latitude);
        double d = this.northeast.longitude;
        double d2 = this.southwest.longitude;
        double d3 = latLng.longitude;
        if (m1329b(d3)) {
            d3 = d;
            d = d2;
        } else if (m1328b(d2, d3) < m1330c(d, d3)) {
            double d4 = d;
            d = d3;
            d3 = d4;
        } else {
            d = d2;
        }
        return new LatLngBounds(new LatLng(min, d), new LatLng(max, d3));
    }

    public String toString() {
        return C0241r.m1201c(this).m1199a("southwest", this.southwest).m1199a("northeast", this.northeast).toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (C0324q.bn()) {
            C0332d.m1355a(this, parcel, i);
        } else {
            LatLngBoundsCreator.m1334a(this, parcel, i);
        }
    }
}
