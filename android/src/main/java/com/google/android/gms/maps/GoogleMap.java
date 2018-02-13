package com.google.android.gms.maps;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.RemoteException;
import android.view.View;
import com.google.android.gms.dynamic.C0075b;
import com.google.android.gms.dynamic.C0078c;
import com.google.android.gms.internal.C0242s;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.internal.C0251d.C0252a;
import com.google.android.gms.maps.internal.C0253m.C0254a;
import com.google.android.gms.maps.internal.C0257l.C0258a;
import com.google.android.gms.maps.internal.C0260n.C0261a;
import com.google.android.gms.maps.internal.C0263e.C0264a;
import com.google.android.gms.maps.internal.C0266h.C0267a;
import com.google.android.gms.maps.internal.C0269i.C0270a;
import com.google.android.gms.maps.internal.C0272j.C0273a;
import com.google.android.gms.maps.internal.C0275k.C0276a;
import com.google.android.gms.maps.internal.C0278f.C0279a;
import com.google.android.gms.maps.internal.C0281b.C0282a;
import com.google.android.gms.maps.internal.C0312g;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.internal.ILocationSourceDelegate.C0255a;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.internal.C0346c;
import com.google.android.gms.maps.model.internal.C0349d;
import com.google.android.gms.maps.model.internal.C0355f;

public final class GoogleMap {
    public static final int MAP_TYPE_HYBRID = 4;
    public static final int MAP_TYPE_NONE = 0;
    public static final int MAP_TYPE_NORMAL = 1;
    public static final int MAP_TYPE_SATELLITE = 2;
    public static final int MAP_TYPE_TERRAIN = 3;
    private final IGoogleMapDelegate fX;
    private UiSettings fY;

    public interface CancelableCallback {
        void onCancel();

        void onFinish();
    }

    public interface InfoWindowAdapter {
        View getInfoContents(Marker marker);

        View getInfoWindow(Marker marker);
    }

    public interface OnCameraChangeListener {
        void onCameraChange(CameraPosition cameraPosition);
    }

    public interface OnInfoWindowClickListener {
        void onInfoWindowClick(Marker marker);
    }

    public interface OnMapClickListener {
        void onMapClick(LatLng latLng);
    }

    public interface OnMapLongClickListener {
        void onMapLongClick(LatLng latLng);
    }

    public interface OnMarkerClickListener {
        boolean onMarkerClick(Marker marker);
    }

    public interface OnMarkerDragListener {
        void onMarkerDrag(Marker marker);

        void onMarkerDragEnd(Marker marker);

        void onMarkerDragStart(Marker marker);
    }

    public interface OnMyLocationButtonClickListener {
        boolean onMyLocationButtonClick();
    }

    @Deprecated
    public interface OnMyLocationChangeListener {
        void onMyLocationChange(Location location);
    }

    public interface SnapshotReadyCallback {
        void onSnapshotReady(Bitmap bitmap);
    }

    private static final class C0283a extends C0282a {
        private final CancelableCallback gn;

        C0283a(CancelableCallback cancelableCallback) {
            this.gn = cancelableCallback;
        }

        public void onCancel() {
            this.gn.onCancel();
        }

        public void onFinish() {
            this.gn.onFinish();
        }
    }

    protected GoogleMap(IGoogleMapDelegate iGoogleMapDelegate) {
        this.fX = (IGoogleMapDelegate) C0242s.m1208d(iGoogleMapDelegate);
    }

    IGoogleMapDelegate aY() {
        return this.fX;
    }

    public final Circle addCircle(CircleOptions circleOptions) {
        try {
            return new Circle(this.fX.addCircle(circleOptions));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final GroundOverlay addGroundOverlay(GroundOverlayOptions groundOverlayOptions) {
        try {
            C0346c addGroundOverlay = this.fX.addGroundOverlay(groundOverlayOptions);
            return addGroundOverlay != null ? new GroundOverlay(addGroundOverlay) : null;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Marker addMarker(MarkerOptions markerOptions) {
        try {
            C0349d addMarker = this.fX.addMarker(markerOptions);
            return addMarker != null ? new Marker(addMarker) : null;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Polygon addPolygon(PolygonOptions polygonOptions) {
        try {
            return new Polygon(this.fX.addPolygon(polygonOptions));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Polyline addPolyline(PolylineOptions polylineOptions) {
        try {
            return new Polyline(this.fX.addPolyline(polylineOptions));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final TileOverlay addTileOverlay(TileOverlayOptions tileOverlayOptions) {
        try {
            C0355f addTileOverlay = this.fX.addTileOverlay(tileOverlayOptions);
            return addTileOverlay != null ? new TileOverlay(addTileOverlay) : null;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void animateCamera(CameraUpdate cameraUpdate) {
        try {
            this.fX.animateCamera(cameraUpdate.aW());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void animateCamera(CameraUpdate cameraUpdate, int i, CancelableCallback cancelableCallback) {
        try {
            this.fX.animateCameraWithDurationAndCallback(cameraUpdate.aW(), i, cancelableCallback == null ? null : new C0283a(cancelableCallback));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void animateCamera(CameraUpdate cameraUpdate, CancelableCallback cancelableCallback) {
        try {
            this.fX.animateCameraWithCallback(cameraUpdate.aW(), cancelableCallback == null ? null : new C0283a(cancelableCallback));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void clear() {
        try {
            this.fX.clear();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final CameraPosition getCameraPosition() {
        try {
            return this.fX.getCameraPosition();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final int getMapType() {
        try {
            return this.fX.getMapType();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getMaxZoomLevel() {
        try {
            return this.fX.getMaxZoomLevel();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getMinZoomLevel() {
        try {
            return this.fX.getMinZoomLevel();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    @Deprecated
    public final Location getMyLocation() {
        try {
            return this.fX.getMyLocation();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Projection getProjection() {
        try {
            return new Projection(this.fX.getProjection());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final UiSettings getUiSettings() {
        try {
            if (this.fY == null) {
                this.fY = new UiSettings(this.fX.getUiSettings());
            }
            return this.fY;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isIndoorEnabled() {
        try {
            return this.fX.isIndoorEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isMyLocationEnabled() {
        try {
            return this.fX.isMyLocationEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isTrafficEnabled() {
        try {
            return this.fX.isTrafficEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void moveCamera(CameraUpdate cameraUpdate) {
        try {
            this.fX.moveCamera(cameraUpdate.aW());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean setIndoorEnabled(boolean z) {
        try {
            return this.fX.setIndoorEnabled(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setInfoWindowAdapter(final InfoWindowAdapter infoWindowAdapter) {
        if (infoWindowAdapter == null) {
            try {
                this.fX.setInfoWindowAdapter(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.fX.setInfoWindowAdapter(new C0252a(this) {
            final /* synthetic */ GoogleMap ga;

            public C0075b mo975f(C0349d c0349d) {
                return C0078c.m173f(infoWindowAdapter.getInfoWindow(new Marker(c0349d)));
            }

            public C0075b mo976g(C0349d c0349d) {
                return C0078c.m173f(infoWindowAdapter.getInfoContents(new Marker(c0349d)));
            }
        });
    }

    public final void setLocationSource(final LocationSource locationSource) {
        if (locationSource == null) {
            try {
                this.fX.setLocationSource(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.fX.setLocationSource(new C0255a(this) {
            final /* synthetic */ GoogleMap ga;

            public void activate(final C0312g c0312g) {
                locationSource.activate(new OnLocationChangedListener(this) {
                    final /* synthetic */ C02561 gc;

                    public void onLocationChanged(Location location) {
                        try {
                            c0312g.mo1101e(C0078c.m173f(location));
                        } catch (RemoteException e) {
                            throw new RuntimeRemoteException(e);
                        }
                    }
                });
            }

            public void deactivate() {
                locationSource.deactivate();
            }
        });
    }

    public final void setMapType(int i) {
        try {
            this.fX.setMapType(i);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setMyLocationEnabled(boolean z) {
        try {
            this.fX.setMyLocationEnabled(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnCameraChangeListener(final OnCameraChangeListener onCameraChangeListener) {
        if (onCameraChangeListener == null) {
            try {
                this.fX.setOnCameraChangeListener(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.fX.setOnCameraChangeListener(new C0264a(this) {
            final /* synthetic */ GoogleMap ga;

            public void onCameraChange(CameraPosition cameraPosition) {
                onCameraChangeListener.onCameraChange(cameraPosition);
            }
        });
    }

    public final void setOnInfoWindowClickListener(final OnInfoWindowClickListener onInfoWindowClickListener) {
        if (onInfoWindowClickListener == null) {
            try {
                this.fX.setOnInfoWindowClickListener(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.fX.setOnInfoWindowClickListener(new C0279a(this) {
            final /* synthetic */ GoogleMap ga;

            public void mo989e(C0349d c0349d) {
                onInfoWindowClickListener.onInfoWindowClick(new Marker(c0349d));
            }
        });
    }

    public final void setOnMapClickListener(final OnMapClickListener onMapClickListener) {
        if (onMapClickListener == null) {
            try {
                this.fX.setOnMapClickListener(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.fX.setOnMapClickListener(new C0267a(this) {
            final /* synthetic */ GoogleMap ga;

            public void onMapClick(LatLng latLng) {
                onMapClickListener.onMapClick(latLng);
            }
        });
    }

    public final void setOnMapLongClickListener(final OnMapLongClickListener onMapLongClickListener) {
        if (onMapLongClickListener == null) {
            try {
                this.fX.setOnMapLongClickListener(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.fX.setOnMapLongClickListener(new C0270a(this) {
            final /* synthetic */ GoogleMap ga;

            public void onMapLongClick(LatLng latLng) {
                onMapLongClickListener.onMapLongClick(latLng);
            }
        });
    }

    public final void setOnMarkerClickListener(final OnMarkerClickListener onMarkerClickListener) {
        if (onMarkerClickListener == null) {
            try {
                this.fX.setOnMarkerClickListener(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.fX.setOnMarkerClickListener(new C0273a(this) {
            final /* synthetic */ GoogleMap ga;

            public boolean mo985a(C0349d c0349d) {
                return onMarkerClickListener.onMarkerClick(new Marker(c0349d));
            }
        });
    }

    public final void setOnMarkerDragListener(final OnMarkerDragListener onMarkerDragListener) {
        if (onMarkerDragListener == null) {
            try {
                this.fX.setOnMarkerDragListener(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.fX.setOnMarkerDragListener(new C0276a(this) {
            final /* synthetic */ GoogleMap ga;

            public void mo986b(C0349d c0349d) {
                onMarkerDragListener.onMarkerDragStart(new Marker(c0349d));
            }

            public void mo987c(C0349d c0349d) {
                onMarkerDragListener.onMarkerDragEnd(new Marker(c0349d));
            }

            public void mo988d(C0349d c0349d) {
                onMarkerDragListener.onMarkerDrag(new Marker(c0349d));
            }
        });
    }

    public final void setOnMyLocationButtonClickListener(final OnMyLocationButtonClickListener onMyLocationButtonClickListener) {
        if (onMyLocationButtonClickListener == null) {
            try {
                this.fX.setOnMyLocationButtonClickListener(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.fX.setOnMyLocationButtonClickListener(new C0258a(this) {
            final /* synthetic */ GoogleMap ga;

            public boolean onMyLocationButtonClick() throws RemoteException {
                return onMyLocationButtonClickListener.onMyLocationButtonClick();
            }
        });
    }

    @Deprecated
    public final void setOnMyLocationChangeListener(final OnMyLocationChangeListener onMyLocationChangeListener) {
        if (onMyLocationChangeListener == null) {
            try {
                this.fX.setOnMyLocationChangeListener(null);
                return;
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        this.fX.setOnMyLocationChangeListener(new C0254a(this) {
            final /* synthetic */ GoogleMap ga;

            public void mo977b(C0075b c0075b) {
                onMyLocationChangeListener.onMyLocationChange((Location) C0078c.m172a(c0075b));
            }
        });
    }

    public final void setPadding(int i, int i2, int i3, int i4) {
        try {
            this.fX.setPadding(i, i2, i3, i4);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setTrafficEnabled(boolean z) {
        try {
            this.fX.setTrafficEnabled(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void snapshot(SnapshotReadyCallback snapshotReadyCallback) {
        snapshot(snapshotReadyCallback, null);
    }

    public final void snapshot(final SnapshotReadyCallback snapshotReadyCallback, Bitmap bitmap) {
        try {
            this.fX.snapshot(new C0261a(this) {
                final /* synthetic */ GoogleMap ga;

                public void onSnapshotReady(Bitmap bitmap) throws RemoteException {
                    snapshotReadyCallback.onSnapshotReady(bitmap);
                }
            }, (C0078c) (bitmap != null ? C0078c.m173f(bitmap) : null));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void stopAnimation() {
        try {
            this.fX.stopAnimation();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}
