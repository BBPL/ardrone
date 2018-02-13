package com.google.android.gms.location;

import android.os.SystemClock;
import com.google.android.gms.internal.bi;

public interface Geofence {
    public static final int GEOFENCE_TRANSITION_ENTER = 1;
    public static final int GEOFENCE_TRANSITION_EXIT = 2;
    public static final long NEVER_EXPIRE = -1;

    public static final class Builder {
        private float fA;
        private String fu = null;
        private int fv = 0;
        private long fw = Long.MIN_VALUE;
        private short fx = (short) -1;
        private double fy;
        private double fz;

        public Geofence build() {
            if (this.fu == null) {
                throw new IllegalArgumentException("Request ID not set.");
            } else if (this.fv == 0) {
                throw new IllegalArgumentException("Transitions types not set.");
            } else if (this.fw == Long.MIN_VALUE) {
                throw new IllegalArgumentException("Expiration not set.");
            } else if (this.fx != (short) -1) {
                return new bi(this.fu, this.fv, (short) 1, this.fy, this.fz, this.fA, this.fw);
            } else {
                throw new IllegalArgumentException("Geofence region not set.");
            }
        }

        public Builder setCircularRegion(double d, double d2, float f) {
            this.fx = (short) 1;
            this.fy = d;
            this.fz = d2;
            this.fA = f;
            return this;
        }

        public Builder setExpirationDuration(long j) {
            if (j < 0) {
                this.fw = -1;
            } else {
                this.fw = SystemClock.elapsedRealtime() + j;
            }
            return this;
        }

        public Builder setRequestId(String str) {
            this.fu = str;
            return this;
        }

        public Builder setTransitionTypes(int i) {
            this.fv = i;
            return this;
        }
    }

    String getRequestId();
}
