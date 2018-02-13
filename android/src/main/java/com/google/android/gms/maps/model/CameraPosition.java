package com.google.android.gms.maps.model;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.util.AttributeSet;
import com.google.android.gms.C0041R;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.C0241r;
import com.google.android.gms.internal.C0242s;
import com.google.android.gms.maps.internal.C0324q;

public final class CameraPosition implements SafeParcelable {
    public static final CameraPositionCreator CREATOR = new CameraPositionCreator();
    private final int ab;
    public final float bearing;
    public final LatLng target;
    public final float tilt;
    public final float zoom;

    public static final class Builder {
        private LatLng gR;
        private float gS;
        private float gT;
        private float gU;

        public Builder(CameraPosition cameraPosition) {
            this.gR = cameraPosition.target;
            this.gS = cameraPosition.zoom;
            this.gT = cameraPosition.tilt;
            this.gU = cameraPosition.bearing;
        }

        public Builder bearing(float f) {
            this.gU = f;
            return this;
        }

        public CameraPosition build() {
            return new CameraPosition(this.gR, this.gS, this.gT, this.gU);
        }

        public Builder target(LatLng latLng) {
            this.gR = latLng;
            return this;
        }

        public Builder tilt(float f) {
            this.gT = f;
            return this;
        }

        public Builder zoom(float f) {
            this.gS = f;
            return this;
        }
    }

    CameraPosition(int i, LatLng latLng, float f, float f2, float f3) {
        C0242s.m1205b((Object) latLng, (Object) "null camera target");
        boolean z = 0.0f <= f2 && f2 <= 90.0f;
        C0242s.m1206b(z, (Object) "Tilt needs to be between 0 and 90 inclusive");
        this.ab = i;
        this.target = latLng;
        this.zoom = f;
        this.tilt = f2 + 0.0f;
        if (((double) f3) <= 0.0d) {
            f3 = (f3 % 360.0f) + 360.0f;
        }
        this.bearing = f3 % 360.0f;
    }

    public CameraPosition(LatLng latLng, float f, float f2, float f3) {
        this(1, latLng, f, f2, f3);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(CameraPosition cameraPosition) {
        return new Builder(cameraPosition);
    }

    public static CameraPosition createFromAttributes(Context context, AttributeSet attributeSet) {
        if (attributeSet == null) {
            return null;
        }
        TypedArray obtainAttributes = context.getResources().obtainAttributes(attributeSet, C0041R.styleable.MapAttrs);
        LatLng latLng = new LatLng((double) (obtainAttributes.hasValue(2) ? obtainAttributes.getFloat(2, 0.0f) : 0.0f), (double) (obtainAttributes.hasValue(3) ? obtainAttributes.getFloat(3, 0.0f) : 0.0f));
        Builder builder = builder();
        builder.target(latLng);
        if (obtainAttributes.hasValue(5)) {
            builder.zoom(obtainAttributes.getFloat(5, 0.0f));
        }
        if (obtainAttributes.hasValue(1)) {
            builder.bearing(obtainAttributes.getFloat(1, 0.0f));
        }
        if (obtainAttributes.hasValue(4)) {
            builder.tilt(obtainAttributes.getFloat(4, 0.0f));
        }
        return builder.build();
    }

    public static final CameraPosition fromLatLngZoom(LatLng latLng, float f) {
        return new CameraPosition(latLng, f, 0.0f, 0.0f);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (!(obj instanceof CameraPosition)) {
                return false;
            }
            CameraPosition cameraPosition = (CameraPosition) obj;
            if (!this.target.equals(cameraPosition.target) || Float.floatToIntBits(this.zoom) != Float.floatToIntBits(cameraPosition.zoom) || Float.floatToIntBits(this.tilt) != Float.floatToIntBits(cameraPosition.tilt)) {
                return false;
            }
            if (Float.floatToIntBits(this.bearing) != Float.floatToIntBits(cameraPosition.bearing)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return C0241r.hashCode(this.target, Float.valueOf(this.zoom), Float.valueOf(this.tilt), Float.valueOf(this.bearing));
    }

    int m1318i() {
        return this.ab;
    }

    public String toString() {
        return C0241r.m1201c(this).m1199a("target", this.target).m1199a("zoom", Float.valueOf(this.zoom)).m1199a("tilt", Float.valueOf(this.tilt)).m1199a("bearing", Float.valueOf(this.bearing)).toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (C0324q.bn()) {
            C0329a.m1352a(this, parcel, i);
        } else {
            CameraPositionCreator.m1319a(this, parcel, i);
        }
    }
}
