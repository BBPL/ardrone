package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.C0324q;

public final class LatLng implements SafeParcelable {
    public static final LatLngCreator CREATOR = new LatLngCreator();
    private final int ab;
    public final double latitude;
    public final double longitude;

    public LatLng(double d, double d2) {
        this(1, d, d2);
    }

    LatLng(int i, double d, double d2) {
        this.ab = i;
        if (-180.0d > d2 || d2 >= 180.0d) {
            this.longitude = ((((d2 - 180.0d) % 360.0d) + 360.0d) % 360.0d) - 180.0d;
        } else {
            this.longitude = d2;
        }
        this.latitude = Math.max(-90.0d, Math.min(90.0d, d));
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (!(obj instanceof LatLng)) {
                return false;
            }
            LatLng latLng = (LatLng) obj;
            if (Double.doubleToLongBits(this.latitude) != Double.doubleToLongBits(latLng.latitude)) {
                return false;
            }
            if (Double.doubleToLongBits(this.longitude) != Double.doubleToLongBits(latLng.longitude)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        long doubleToLongBits = Double.doubleToLongBits(this.latitude);
        int i = (int) (doubleToLongBits ^ (doubleToLongBits >>> 32));
        long doubleToLongBits2 = Double.doubleToLongBits(this.longitude);
        return ((i + 31) * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)));
    }

    int m1325i() {
        return this.ab;
    }

    public String toString() {
        return "lat/lng: (" + this.latitude + "," + this.longitude + ")";
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (C0324q.bn()) {
            C0333e.m1356a(this, parcel, i);
        } else {
            LatLngCreator.m1335a(this, parcel, i);
        }
    }
}
