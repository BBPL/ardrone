package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.C0324q;
import com.google.android.gms.maps.model.internal.C0326g;
import com.google.android.gms.maps.model.internal.C0326g.C0327a;

public final class TileOverlayOptions implements SafeParcelable {
    public static final TileOverlayOptionsCreator CREATOR = new TileOverlayOptionsCreator();
    private final int ab;
    private C0326g hG;
    private TileProvider hH;
    private float hb;
    private boolean hc;

    class C03251 implements TileProvider {
        private final C0326g hI = this.hJ.hG;
        final /* synthetic */ TileOverlayOptions hJ;

        C03251(TileOverlayOptions tileOverlayOptions) {
            this.hJ = tileOverlayOptions;
        }

        public Tile getTile(int i, int i2, int i3) {
            try {
                return this.hI.getTile(i, i2, i3);
            } catch (RemoteException e) {
                return null;
            }
        }
    }

    public TileOverlayOptions() {
        this.hc = true;
        this.ab = 1;
    }

    TileOverlayOptions(int i, IBinder iBinder, boolean z, float f) {
        this.hc = true;
        this.ab = i;
        this.hG = C0327a.m1344U(iBinder);
        this.hH = this.hG == null ? null : new C03251(this);
        this.hc = z;
        this.hb = f;
    }

    IBinder bs() {
        return this.hG.asBinder();
    }

    public int describeContents() {
        return 0;
    }

    public TileProvider getTileProvider() {
        return this.hH;
    }

    public float getZIndex() {
        return this.hb;
    }

    int m1346i() {
        return this.ab;
    }

    public boolean isVisible() {
        return this.hc;
    }

    public TileOverlayOptions tileProvider(final TileProvider tileProvider) {
        this.hH = tileProvider;
        this.hG = this.hH == null ? null : new C0327a(this) {
            final /* synthetic */ TileOverlayOptions hJ;

            public Tile getTile(int i, int i2, int i3) {
                return tileProvider.getTile(i, i2, i3);
            }
        };
        return this;
    }

    public TileOverlayOptions visible(boolean z) {
        this.hc = z;
        return this;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (C0324q.bn()) {
            C0359j.m1394a(this, parcel, i);
        } else {
            TileOverlayOptionsCreator.m1347a(this, parcel, i);
        }
    }

    public TileOverlayOptions zIndex(float f) {
        this.hb = f;
        return this;
    }
}
