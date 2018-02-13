package com.parrot.freeflight.activities;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.FreeFlightApplication;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.dialogs.ProgressDialog;
import com.parrot.freeflight.settings.ApplicationSettings;
import com.parrot.freeflight.settings.ApplicationSettings.Coords;
import com.parrot.freeflight.ui.ActionBar;
import com.parrot.freeflight.utils.SystemUtils;
import org.mortbay.jetty.HttpVersions;

public class MapLoadingActivity extends ParrotActivity {
    private static final int LOAD_DELAY = 1500;
    private static final String TAG = MapLoadingActivity.class.getSimpleName();
    private ActionBar actionBar;
    private final OnCameraChangeListener cameraChangeListener = new C10791();
    private Button centerButton;
    private int currentIndex;
    private int currentZoomLevel;
    private boolean firstLocation = false;
    private final Handler handler = new Handler();
    private TextView hintText;
    private final Runnable loadNextMapPartRunnable = new C10802();
    private GoogleMap map;
    private MapView mapView;
    private int maxZoomLevel;
    private int minZoomLevel;
    private LatLng orgLatLng;
    private double orgSpanLatDelta;
    private double orgSpanLongDelta;
    private float orgZoom;
    private int originalZoomLevel;
    private ProgressDialog progressDlg;
    private ApplicationSettings settings;
    private double startLatitude;
    private double startLongitude;
    private Button storeButton;
    private boolean storingIsStarted = false;
    private boolean userLocationDidUpdateOneTime = false;
    private TextView zoomText;

    class C10791 implements OnCameraChangeListener {
        private int currentZoom = -1;

        C10791() {
        }

        public void onCameraChange(CameraPosition cameraPosition) {
            float f = (float) cameraPosition.target.latitude;
            float f2 = (float) cameraPosition.target.longitude;
            float f3 = cameraPosition.zoom;
            if (!MapLoadingActivity.this.storingIsStarted) {
                if (f3 > ((float) MapLoadingActivity.this.maxZoomLevel)) {
                    f3 = (float) MapLoadingActivity.this.maxZoomLevel;
                    MapLoadingActivity.this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition.target, (float) MapLoadingActivity.this.maxZoomLevel));
                }
                MapLoadingActivity.this.settings.setMapStoringLastCoords(f, f2, f3, cameraPosition.tilt, cameraPosition.bearing);
            }
            int i = (int) f3;
            if (this.currentZoom != i) {
                this.currentZoom = i;
                MapLoadingActivity.this.onZoomChanged(this.currentZoom);
            }
        }
    }

    class C10802 implements Runnable {
        C10802() {
        }

        public void run() {
            MapLoadingActivity.this.loadNextMapPart();
        }
    }

    class C10813 implements OnMyLocationChangeListener {
        C10813() {
        }

        public void onMyLocationChange(Location location) {
            if (!MapLoadingActivity.this.storingIsStarted && !MapLoadingActivity.this.userLocationDidUpdateOneTime) {
                MapLoadingActivity.this.userLocationDidUpdateOneTime = true;
                MapLoadingActivity.this.centerButton.setEnabled(true);
                if (MapLoadingActivity.this.firstLocation) {
                    MapLoadingActivity.this.map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(location.getLatitude(), location.getLongitude()), (float) MapLoadingActivity.this.maxZoomLevel, 0.0f, 0.0f)));
                }
            }
        }
    }

    class C10824 implements OnCancelListener {
        C10824() {
        }

        public void onCancel(DialogInterface dialogInterface) {
            MapLoadingActivity.this.finishLoading();
            MapLoadingActivity.this.handler.removeCallbacks(MapLoadingActivity.this.loadNextMapPartRunnable);
        }
    }

    private void centerClicked() {
        Location myLocation = this.map.getMyLocation();
        this.map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(myLocation.getLatitude(), myLocation.getLongitude())));
    }

    private void dismissProgressDlg() {
        if (this.progressDlg != null) {
            this.progressDlg.dismiss();
            this.progressDlg = null;
        }
    }

    private void finishLoading() {
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(this.orgLatLng, this.orgZoom));
        this.storingIsStarted = false;
        this.storeButton.setEnabled(true);
        this.centerButton.setEnabled(true);
        dismissProgressDlg();
    }

    private void initActionBar() {
        this.actionBar = new ActionBar(this, findViewById(C0984R.id.navigation_bar));
        this.actionBar.setTitle(getString(C0984R.string.ff_ID000222));
        this.actionBar.initBackButton();
    }

    private void initUI(Bundle bundle) {
        setContentView(C0984R.layout.map_load);
        this.mapView = (MapView) findViewById(C0984R.id.gmap);
        this.mapView.onCreate(bundle);
        this.storeButton = (Button) findViewById(C0984R.id.storeButton);
        this.centerButton = (Button) findViewById(C0984R.id.centerButton);
        this.hintText = (TextView) findViewById(C0984R.id.hintText);
        this.zoomText = (TextView) findViewById(C0984R.id.zoomText);
        this.map = this.mapView.getMap();
    }

    private void loadNextMapPart() {
        if (this.currentZoomLevel > this.maxZoomLevel) {
            finishLoading();
            return;
        }
        int i = 1 << ((this.currentZoomLevel - this.originalZoomLevel) + 1);
        int i2 = this.currentIndex / i;
        double d = this.orgSpanLatDelta / ((double) i);
        double d2 = this.orgSpanLongDelta / ((double) i);
        double d3 = this.startLatitude + (((double) i2) * d);
        double d4 = (((double) (this.currentIndex % i)) * d2) + this.startLongitude;
        this.map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(d3, d4), new LatLng(d + d3, d4 + d2)), 0));
        i2 = this.currentIndex + 1;
        this.currentIndex = i2;
        if (i2 >= i * i) {
            this.currentZoomLevel++;
            this.currentIndex = 0;
        }
        this.handler.postDelayed(this.loadNextMapPartRunnable, 1500);
    }

    private void onZoomChanged(int i) {
        this.zoomText.setText(i + "x");
    }

    private void showProgressDlg() {
        if (this.progressDlg == null) {
            this.progressDlg = new ProgressDialog(this, C0984R.style.FreeFlightDialog_Hotspot);
            this.progressDlg.setMessage(getString(C0984R.string.ff_ID000222).toUpperCase());
            this.progressDlg.setSubMessage(HttpVersions.HTTP_0_9);
            this.progressDlg.setCanceledOnTouchOutside(false);
            this.progressDlg.setOnCancelListener(new C10824());
            this.progressDlg.show();
        }
    }

    private void storeClicked() {
        int i = (int) this.map.getCameraPosition().zoom;
        if (i <= this.maxZoomLevel) {
            if (i >= this.minZoomLevel) {
                this.storeButton.setEnabled(false);
                this.centerButton.setEnabled(false);
                showProgressDlg();
                LatLngBounds latLngBounds = this.map.getProjection().getVisibleRegion().latLngBounds;
                this.orgSpanLatDelta = (latLngBounds.northeast.latitude - latLngBounds.southwest.latitude) * 2.0d;
                this.orgSpanLongDelta = (latLngBounds.northeast.longitude - latLngBounds.southwest.longitude) * 2.0d;
                this.startLatitude = latLngBounds.southwest.latitude - (this.orgSpanLatDelta / 4.0d);
                this.startLongitude = latLngBounds.southwest.longitude - (this.orgSpanLongDelta / 4.0d);
                this.orgLatLng = new LatLng(this.map.getCameraPosition().target.latitude, this.map.getCameraPosition().target.longitude);
                this.orgZoom = this.map.getCameraPosition().zoom;
                this.originalZoomLevel = i;
                this.currentZoomLevel = this.originalZoomLevel;
                this.storingIsStarted = true;
                this.currentIndex = 0;
                loadNextMapPart();
                return;
            }
            Toast.makeText(this, getString(C0984R.string.ff_ID000226, new Object[]{Integer.valueOf(this.minZoomLevel)}), 0).show();
        }
    }

    public void onButtonClicked(View view) {
        switch (view.getId()) {
            case C0984R.id.storeButton /*2131362176*/:
                storeClicked();
                return;
            case C0984R.id.centerButton /*2131362179*/:
                centerClicked();
                return;
            default:
                return;
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.maxZoomLevel = SystemUtils.isTablet(this) ? 16 : 17;
        this.minZoomLevel = this.maxZoomLevel - 4;
        this.settings = ((FreeFlightApplication) getApplication()).getAppSettings();
        try {
            MapsInitializer.initialize(this);
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, "Failed to initialize Google Maps. Exception: " + e.toString());
        }
        initUI(bundle);
        initActionBar();
        this.map.setMyLocationEnabled(true);
        this.map.setMapType(4);
        this.map.getUiSettings().setMyLocationButtonEnabled(false);
        this.map.getUiSettings().setZoomControlsEnabled(false);
        this.map.getUiSettings().setTiltGesturesEnabled(false);
        this.map.getUiSettings().setRotateGesturesEnabled(false);
        Coords mapStoringLastCoords = this.settings.getMapStoringLastCoords();
        if (mapStoringLastCoords != null) {
            this.map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng((double) mapStoringLastCoords.latitude, (double) mapStoringLastCoords.longitude), mapStoringLastCoords.zoom, mapStoringLastCoords.tilt, mapStoringLastCoords.bearing)));
        } else {
            this.firstLocation = true;
        }
        this.map.setOnMyLocationChangeListener(new C10813());
        this.map.setOnCameraChangeListener(this.cameraChangeListener);
    }

    protected void onDestroy() {
        if (this.storingIsStarted) {
            this.storingIsStarted = false;
            this.handler.removeCallbacks(this.loadNextMapPartRunnable);
        }
        this.mapView.onDestroy();
        super.onDestroy();
    }

    public void onLowMemory() {
        this.mapView.onLowMemory();
        super.onLowMemory();
    }

    protected void onResume() {
        super.onResume();
        this.mapView.onResume();
    }
}
