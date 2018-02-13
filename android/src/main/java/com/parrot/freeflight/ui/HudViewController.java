package com.parrot.freeflight.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parrot.ardronetool.ARDroneEngine.ErrorState;
import com.parrot.ardronetool.ARDroneVersion;
import com.parrot.ardronetool.academynavdata.FlyingState;
import com.parrot.ardronetool.academynavdata.MotorState;
import com.parrot.ardronetool.academynavdata.RescueMode;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.activities.ControlDroneActivity;
import com.parrot.freeflight.gestures.EnhancedGestureDetector;
import com.parrot.freeflight.settings.ApplicationSettings;
import com.parrot.freeflight.settings.ApplicationSettings.Coords;
import com.parrot.freeflight.track_3d_viewer.utils.Holder;
import com.parrot.freeflight.ui.gl.IBitmapLoader;
import com.parrot.freeflight.ui.gl.ResourceBitmapLoader;
import com.parrot.freeflight.ui.hud.Button;
import com.parrot.freeflight.ui.hud.Image;
import com.parrot.freeflight.ui.hud.Image.SizeParams;
import com.parrot.freeflight.ui.hud.Indicator;
import com.parrot.freeflight.ui.hud.JoystickBase;
import com.parrot.freeflight.ui.hud.Sprite;
import com.parrot.freeflight.ui.hud.Sprite.Align;
import com.parrot.freeflight.ui.hud.Text;
import com.parrot.freeflight.ui.hud.ToggleButton;
import com.parrot.freeflight.ui.widgets.SlideRuleView2;
import com.parrot.freeflight.ui.widgets.SlideRuleView2.ElementDataProvider;
import com.parrot.freeflight.ui.widgets.SlideRuleView2.SlideRuleValueChangeListener;
import com.parrot.freeflight.utils.FontUtils.TYPEFACE;
import com.parrot.freeflight.utils.GooglePlayServicesChecker;
import com.parrot.freeflight.utils.ImageUtils;
import com.parrot.freeflight.utils.OnPressListener;
import com.parrot.freeflight.video.VideoStageRenderer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mortbay.jetty.HttpVersions;

@SuppressLint({"ResourceAsColor"})
public class HudViewController implements OnTouchListener, OnGestureListener {
    private static final int ALERT_ID = 3;
    private static final int ALTITUDE_INDICATOR_ID = 22;
    private static final int ALTITUDE_INDICATOR_TEXT_ID = 23;
    private static final int BACK_BTN_ID = 18;
    private static final int BACK_TO_HOME_BUTTON_ID = 37;
    private static final int BATTERY_INDICATOR_ID = 11;
    private static final int BATTERY_STATUS_LABEL_ID = 14;
    private static final int BOTTOM_BAR_ID = 6;
    private static final int BUTTON_FADING_DURATION = 500;
    private static final int CAMERA_ID = 7;
    private static final float DEFAULT_ALTITUDE = 2.0f;
    private static final float DEFAULT_SPEED = 2.0f;
    private static final int EMERGENCY_LABEL_ID = 13;
    private static final int GPS_IMG_ID = 38;
    private static final int GPS_PRECISION_GRANULARITY = 5;
    private static final int GPS_TXT_ID = 39;
    private static final int JOY_ID_LEFT = 1;
    private static final int JOY_ID_RIGHT = 2;
    private static final int LAND_ID = 19;
    private static final int MAP_GO_BUTTON_ID = 40;
    private static final int MAP_ROTATE_LEFT = 41;
    private static final int MAP_ROTATE_RIGHT = 42;
    private static final double MIN_GPS_CHANGE_DISTANCE = 0.5d;
    private static final int OVER_BALANCE_BUTTON_ID = 26;
    private static final int PHOTO_ID = 9;
    private static final int PROPELLER1CROSS_IMAGE_ID = 33;
    private static final int PROPELLER1_IMAGE_ID = 29;
    private static final int PROPELLER2CROSS_IMAGE_ID = 34;
    private static final int PROPELLER2_IMAGE_ID = 30;
    private static final int PROPELLER3CROSS_IMAGE_ID = 35;
    private static final int PROPELLER3_IMAGE_ID = 31;
    private static final int PROPELLER4CROSS_IMAGE_ID = 36;
    private static final int PROPELLER4_IMAGE_ID = 32;
    private static final int PROPELLER_BLINK_DURATION = 500;
    private static final int RANDOM_SHAKE_BUTTON_ID = 25;
    private static final int RECORD_ID = 8;
    private static final int RECORD_LABEL_ID = 15;
    private static final int RESCUE_BUTTON_ID = 24;
    private static final int RESCUE_DRONE_BG_SIZE = 32;
    private static final int RESCUE_IMAGE_BASE_LAYER_ID = 27;
    private static final int RESCUE_IMAGE_ID = 28;
    private static final int SETTINGS_ID = 10;
    private static final int SPEED_INDICATOR_ID = 20;
    private static final int SPEED_INDICATOR_TEXT_ID = 21;
    private static final String TAG = "HudViewController";
    private static final int TAKE_OFF_ID = 4;
    private static final int TOP_BAR_ID = 5;
    private static final int USB_INDICATOR_ID = 16;
    private static final int USB_INDICATOR_TEXT_ID = 17;
    private static final int WIFI_INDICATOR_ID = 12;
    private final ElementDataProvider altitudeDP;
    private Image altitudeIndicator;
    private SlideRuleView2 altitudeSlide;
    private final SlideRuleValueChangeListener altitudeSlideChangeListener = new C12122();
    private Text altitudeText;
    private float autoHeadingInitialValue = GroundOverlayOptions.NO_DIMENSION;
    private Indicator batteryIndicator;
    private Image bottomBarBg;
    private Button btnBack;
    private Button btnBackToHome;
    private Button btnCameraSwitch;
    private Button btnEmergency;
    private Button btnGps;
    private Button btnLand;
    private Button btnMapGo;
    private Button btnMapLeft;
    private Button btnMapRight;
    private Button btnOverBalance;
    private Button btnPhoto;
    private Button btnRandomShake;
    private ToggleButton btnRecord;
    private Button btnRescue;
    private Button btnSelectedRescue;
    private Button btnSettings;
    private Button btnTakeOff;
    private Button[] buttons;
    private final MyCameraChangeListener cameraChangeListener = new MyCameraChangeListener();
    private final ControlDroneActivity context;
    private Marker distanceMarker;
    private Marker droneCurrentMarker;
    private Polyline dronePath;
    private final List<LatLng> dronePoints = new ArrayList();
    private float droneTargetHeading = 0.0f;
    private Marker droneTargetMarker;
    private Map<ErrorState, Integer> emergencyStringMap;
    private Integer errorStateRes;
    private boolean flyingCameraActive = false;
    private GestureDetector gestureDetector;
    private GLSurfaceView2 glView;
    private final float[] gpsLastCoordDistance = new float[1];
    private boolean isFlying = false;
    private float joypadOpacity = 1.0f;
    private JoystickBase[] joysticks;
    private boolean joysticksEnabled = true;
    private boolean locatedMe = false;
    private final OnMapClickListener mapClickListener = new C12111();
    private LinearLayout mapLayoutContainer;
    private MapView mapView;
    private boolean prevIsLanded = true;
    private double prevLatFused;
    private double prevLongFused;
    private int prevMotor1;
    private MotorState prevMotor1State;
    private int prevMotor2;
    private MotorState prevMotor2State;
    private int prevMotor3;
    private MotorState prevMotor3State;
    private int prevMotor4;
    private MotorState prevMotor4State;
    private int prevRemainingTime;
    private Image propeller1CrossImage;
    private Image propeller1Image;
    private Image propeller2CrossImage;
    private Image propeller2Image;
    private Image propeller3CrossImage;
    private Image propeller3Image;
    private Image propeller4CrossImage;
    private Image propeller4Image;
    private VideoStageRenderer renderer;
    private Image rescueImage;
    private Image rescueImageBaseLayer;
    private boolean rescueShown = false;
    private FrameLayout rootContainer;
    private boolean setTargetRotation;
    private final ApplicationSettings settings;
    private final ElementDataProvider speedDP;
    private Image speedIndicator;
    private SlideRuleView2 speedSlide;
    private final SlideRuleValueChangeListener speedSlideChangeListener = new C12133();
    private Text speedText;
    private TextView toastView;
    private Text txtAlert;
    private Text txtBatteryStatus;
    private Text txtGps;
    private Text txtRecord;
    private TextView txtSceneFps;
    private Text txtUsbRemaining;
    private Image usbIndicator;
    private Polyline waypointPath;
    private final List<LatLng> waypointsPoints = Arrays.asList(new LatLng[]{null, null});
    private Indicator wifiIndicator;
    private TextView zoomText;

    class C12111 implements OnMapClickListener {
        C12111() {
        }

        public void onMapClick(LatLng latLng) {
            HudViewController.this.setDroneTargetCoords(latLng);
        }
    }

    class C12122 implements SlideRuleValueChangeListener {
        C12122() {
        }

        public void onSlideValueChanged(float f) {
            HudViewController.this.updateToast();
            HudViewController.this.updateGoButtonState();
        }
    }

    class C12133 implements SlideRuleValueChangeListener {
        C12133() {
        }

        public void onSlideValueChanged(float f) {
            HudViewController.this.updateToast();
            HudViewController.this.updateGoButtonState();
        }
    }

    class C12144 implements IBitmapLoader {
        C12144() {
        }

        public void getBitmapDims(Holder<Integer> holder, Holder<Integer> holder2) {
            Integer valueOf = Integer.valueOf(32);
            holder2.value = valueOf;
            holder.value = valueOf;
        }

        public Bitmap loadBitmap() {
            return HudViewController.getRescueDroneBgBitmap();
        }
    }

    class C12155 implements OnMarkerClickListener {
        C12155() {
        }

        public boolean onMarkerClick(Marker marker) {
            return true;
        }
    }

    public enum JoystickType {
        NONE,
        ANALOGUE,
        ACCELERO,
        COMBINED,
        MAGNETO
    }

    public static class MapView2 extends MapView {
        private final GestureDetector gestureDetector;
        private final OnMapClickListener mapClickListener;
        private final Point point = new Point();
        private final ApplicationSettings settings;

        private class MyGestureDetector extends SimpleOnGestureListener {
            private MyGestureDetector() {
            }

            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                MapView2.this.point.set((int) motionEvent.getX(), (int) motionEvent.getY());
                MapView2.this.mapClickListener.onMapClick(MapView2.this.getMap().getProjection().fromScreenLocation(MapView2.this.point));
                return false;
            }
        }

        public MapView2(Context context, ApplicationSettings applicationSettings, OnMapClickListener onMapClickListener) {
            super(context);
            this.settings = applicationSettings;
            this.mapClickListener = onMapClickListener;
            this.gestureDetector = new GestureDetector(context, new MyGestureDetector());
        }

        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
        }

        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            CameraPosition cameraPosition = getMap().getCameraPosition();
            this.settings.setMapPilotingLastCoords((float) cameraPosition.target.latitude, (float) cameraPosition.target.longitude, cameraPosition.zoom, cameraPosition.tilt, cameraPosition.bearing);
        }

        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            this.gestureDetector.onTouchEvent(motionEvent);
            return super.onInterceptTouchEvent(motionEvent);
        }
    }

    private class MyCameraChangeListener implements OnCameraChangeListener {
        private int currentZoom;

        private MyCameraChangeListener() {
            this.currentZoom = -1;
        }

        public void init() {
            this.currentZoom = -1;
        }

        public void onCameraChange(CameraPosition cameraPosition) {
            int i = (int) cameraPosition.zoom;
            if (this.currentZoom != i) {
                this.currentZoom = i;
                if (HudViewController.this.zoomText != null) {
                    HudViewController.this.zoomText.setText(this.currentZoom + "x");
                }
            }
        }
    }

    public HudViewController(ControlDroneActivity controlDroneActivity, ElementDataProvider elementDataProvider, ElementDataProvider elementDataProvider2, ApplicationSettings applicationSettings) {
        this.context = controlDroneActivity;
        this.altitudeDP = elementDataProvider;
        this.speedDP = elementDataProvider2;
        this.settings = applicationSettings;
        this.gestureDetector = new EnhancedGestureDetector(controlDroneActivity, this);
        this.joysticks = new JoystickBase[2];
        this.rootContainer = new FrameLayout(controlDroneActivity);
        this.rootContainer.setLayoutParams(new LayoutParams(-1, -1));
        this.glView = new GLSurfaceView2(controlDroneActivity);
        this.glView.setEGLContextClientVersion(2);
        this.glView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.glView.getHolder().setFormat(-3);
        this.glView.setZOrderOnTop(false);
        this.rootContainer.addView(this.glView);
        controlDroneActivity.setContentView(this.rootContainer);
        this.renderer = new VideoStageRenderer();
        initNavdataStrings();
        initGLSurfaceView();
        Resources resources = controlDroneActivity.getResources();
        this.btnSettings = new Button(resources, (int) C0984R.drawable.btn_settings, (int) C0984R.drawable.btn_settings_pressed, Align.TOP_LEFT);
        this.btnSettings.setMargin(0, 0, 0, (int) resources.getDimension(C0984R.dimen.hud_btn_settings_margin_left));
        this.btnBack = new Button(resources, (int) C0984R.drawable.btn_back, (int) C0984R.drawable.btn_back_pressed, Align.TOP_LEFT);
        this.btnBack.setMargin(0, 0, 0, resources.getDimensionPixelOffset(C0984R.dimen.hud_btn_back_margin_left));
        this.btnEmergency = new Button(resources, (int) C0984R.drawable.btn_emergency_normal, (int) C0984R.drawable.btn_emergency_pressed, Align.TOP_CENTER);
        this.btnTakeOff = new Button(resources, (int) C0984R.drawable.btn_take_off_normal, (int) C0984R.drawable.btn_take_off_pressed, Align.BOTTOM_CENTER);
        this.btnLand = new Button(resources, (int) C0984R.drawable.btn_landing, (int) C0984R.drawable.btn_landing_pressed, Align.BOTTOM_CENTER);
        this.btnLand.setVisible(false);
        Sprite image = new Image(new ResourceBitmapLoader(resources, C0984R.drawable.barre_haut), Align.TOP_CENTER);
        image.setSizeParams(SizeParams.FILL_SCREEN, SizeParams.NONE);
        image.setAlphaEnabled(false);
        this.bottomBarBg = new Image(new ResourceBitmapLoader(resources, C0984R.drawable.barre_bas), Align.BOTTOM_CENTER);
        this.bottomBarBg.setSizeParams(SizeParams.FILL_SCREEN, SizeParams.NONE);
        this.bottomBarBg.setAlphaEnabled(false);
        this.btnPhoto = new Button(resources, (int) C0984R.drawable.btn_photo, (int) C0984R.drawable.btn_photo_pressed, Align.TOP_RIGHT);
        this.btnPhoto.setMargin(0, (int) resources.getDimension(C0984R.dimen.hud_btn_photo_margin_right), 0, 0);
        this.btnRecord = new ToggleButton(resources, C0984R.drawable.btn_record, C0984R.drawable.btn_record_pressed, C0984R.drawable.btn_record1, C0984R.drawable.btn_record1_pressed, C0984R.drawable.btn_record2, Align.TOP_RIGHT);
        this.btnRecord.setMargin(0, resources.getDimensionPixelOffset(C0984R.dimen.hud_btn_rec_margin_right), 0, 0);
        this.txtRecord = new Text(controlDroneActivity, "REC", Align.TOP_RIGHT, TYPEFACE.Helvetica(controlDroneActivity), resources.getDimensionPixelSize(C0984R.dimen.hud_rec_text_size));
        this.txtRecord.setMargin((int) resources.getDimension(C0984R.dimen.hud_rec_text_margin_top), (int) resources.getDimension(C0984R.dimen.hud_rec_text_margin_right), 0, 0);
        this.txtRecord.setTextColor(-1);
        this.btnGps = new Button(resources, (int) C0984R.drawable.ff2_0_gps_selected_grey, (int) C0984R.drawable.ff2_0_gps_not_selected, Align.TOP_RIGHT);
        this.btnGps.setEnabled(false);
        this.txtGps = new Text(controlDroneActivity, HttpVersions.HTTP_0_9, Align.TOP_RIGHT, TYPEFACE.Helvetica(controlDroneActivity), resources.getDimensionPixelSize(C0984R.dimen.hud_rec_text_size));
        this.txtGps.setMargin((int) resources.getDimension(C0984R.dimen.hud_gps_text_margin_top), (int) resources.getDimension(C0984R.dimen.hud_gps_text_margin_right), 0, 0);
        this.txtGps.setTextColor(-1);
        this.usbIndicator = new Image(new ResourceBitmapLoader(resources, C0984R.drawable.picto_usb_actif), Align.TOP_RIGHT);
        this.usbIndicator.setMargin(0, resources.getDimensionPixelOffset(C0984R.dimen.hud_usb_indicator_margin_right), 0, 0);
        this.prevRemainingTime = -1;
        this.txtUsbRemaining = new Text(controlDroneActivity, "KO", Align.TOP_RIGHT, TYPEFACE.Helvetica(controlDroneActivity), resources.getDimensionPixelSize(C0984R.dimen.hud_usb_indicator_text_size));
        this.txtUsbRemaining.setMargin(resources.getDimensionPixelOffset(C0984R.dimen.hud_usb_indicator_text_margin_top), resources.getDimensionPixelOffset(C0984R.dimen.hud_usb_indicator_text_margin_right), 0, 0);
        this.btnCameraSwitch = new Button(resources, (int) C0984R.drawable.btn_camera, (int) C0984R.drawable.btn_camera_pressed, Align.TOP_RIGHT);
        this.btnCameraSwitch.setMargin(0, resources.getDimensionPixelOffset(C0984R.dimen.hud_btn_camera_switch_margin_right), 0, 0);
        this.rescueImageBaseLayer = new Image(new C12144(), Align.CENTER);
        this.rescueImageBaseLayer.setSizeParams(SizeParams.FILL_SCREEN, SizeParams.FILL_SCREEN);
        this.rescueImageBaseLayer.setVisible(false);
        this.rescueImageBaseLayer.setFilteringMode(9728);
        this.rescueImageBaseLayer.setClampMode(33071);
        Holder holder = new Holder();
        Holder holder2 = new Holder();
        ImageUtils.getBitmapDims(resources, C0984R.drawable.ff2_0_rescue_mode_drone, holder, holder2);
        this.rescueImage = new Image(new ResourceBitmapLoader(resources, C0984R.drawable.ff2_0_rescue_mode_drone), Align.CENTER);
        this.rescueImage.setVisible(false);
        this.propeller1Image = new Image(new ResourceBitmapLoader(resources, C0984R.drawable.ff2_0_propeller), Align.CENTER);
        this.propeller1Image.setVisible(false);
        this.propeller1Image.setOffset((int) (((float) ((-((Integer) holder.value).intValue()) / 2)) * 0.428f), (int) (((float) ((-((Integer) holder2.value).intValue()) / 2)) * 0.416f));
        this.propeller2Image = new Image(new ResourceBitmapLoader(resources, C0984R.drawable.ff2_0_propeller_flipped), Align.CENTER);
        this.propeller2Image.setVisible(false);
        this.propeller2Image.setOffset((int) (((float) (((Integer) holder.value).intValue() / 2)) * 0.428f), (int) (((float) ((-((Integer) holder2.value).intValue()) / 2)) * 0.416f));
        this.propeller3Image = new Image(new ResourceBitmapLoader(resources, C0984R.drawable.ff2_0_propeller), Align.CENTER);
        this.propeller3Image.setVisible(false);
        this.propeller3Image.setOffset((int) (((float) ((-((Integer) holder.value).intValue()) / 2)) * 0.428f), (int) (((float) (((Integer) holder2.value).intValue() / 2)) * 0.436f));
        this.propeller4Image = new Image(new ResourceBitmapLoader(resources, C0984R.drawable.ff2_0_propeller_flipped), Align.CENTER);
        this.propeller4Image.setVisible(false);
        this.propeller4Image.setOffset((int) (((float) (((Integer) holder.value).intValue() / 2)) * 0.428f), (int) (((float) (((Integer) holder2.value).intValue() / 2)) * 0.436f));
        this.propeller1CrossImage = new Image(new ResourceBitmapLoader(resources, C0984R.drawable.ff2_0_red_cross), Align.CENTER);
        this.propeller1CrossImage.setVisible(false);
        this.propeller1CrossImage.setOffset(-107, -104);
        this.propeller2CrossImage = new Image(new ResourceBitmapLoader(resources, C0984R.drawable.ff2_0_red_cross), Align.CENTER);
        this.propeller2CrossImage.setVisible(false);
        this.propeller2CrossImage.setOffset(107, -104);
        this.propeller3CrossImage = new Image(new ResourceBitmapLoader(resources, C0984R.drawable.ff2_0_red_cross), Align.CENTER);
        this.propeller3CrossImage.setVisible(false);
        this.propeller3CrossImage.setOffset(-107, 109);
        this.propeller4CrossImage = new Image(new ResourceBitmapLoader(resources, C0984R.drawable.ff2_0_red_cross), Align.CENTER);
        this.propeller4CrossImage.setVisible(false);
        this.propeller4CrossImage.setOffset(107, 109);
        this.batteryIndicator = new Indicator(resources, new int[]{C0984R.drawable.btn_battery_0, C0984R.drawable.btn_battery_1, C0984R.drawable.btn_battery_2, C0984R.drawable.btn_battery_3}, Align.TOP_LEFT);
        this.batteryIndicator.setMargin(0, 0, 0, (int) resources.getDimension(C0984R.dimen.hud_battery_indicator_margin_left));
        this.txtBatteryStatus = new Text(controlDroneActivity, "0%", Align.TOP_LEFT, TYPEFACE.Helvetica(controlDroneActivity), (int) resources.getDimension(C0984R.dimen.hud_battery_text_size));
        this.txtBatteryStatus.setMargin((int) resources.getDimension(C0984R.dimen.hud_battery_text_margin_top), 0, 0, ((int) resources.getDimension(C0984R.dimen.hud_battery_indicator_margin_left)) + this.batteryIndicator.getWidth());
        this.txtBatteryStatus.setTextColor(-1);
        this.wifiIndicator = new Indicator(resources, new int[]{C0984R.drawable.btn_wifi_0, C0984R.drawable.btn_wifi_1, C0984R.drawable.btn_wifi_2, C0984R.drawable.btn_wifi_3}, Align.TOP_LEFT);
        this.wifiIndicator.setMargin(0, 0, 0, (int) resources.getDimension(C0984R.dimen.hud_wifi_indicator_margin_left));
        this.txtAlert = new Text(controlDroneActivity, HttpVersions.HTTP_0_9, Align.TOP_CENTER);
        this.txtAlert.setMargin((int) resources.getDimension(C0984R.dimen.hud_alert_text_margin_top), 0, 0, 0);
        this.txtAlert.setTextColor(-65536);
        this.txtAlert.setTextSize((int) resources.getDimension(C0984R.dimen.hud_alert_text_size));
        this.txtAlert.setBold(true);
        this.txtAlert.blink(true);
        this.altitudeIndicator = new Image(new ResourceBitmapLoader(resources, C0984R.drawable.ff2_altitude_icon_selected), Align.BOTTOM_LEFT);
        this.altitudeText = new Text(controlDroneActivity, "0.00 m", Align.BOTTOM_LEFT, TYPEFACE.Helvetica(controlDroneActivity), (int) resources.getDimension(C0984R.dimen.hud_usb_indicator_text_size));
        this.altitudeText.setTextColor(-1);
        this.altitudeIndicator.setMargin(0, 0, (this.bottomBarBg.getHeight() - this.altitudeIndicator.getHeight()) / 2, 0);
        int height = ((int) (((float) (this.bottomBarBg.getHeight() - this.altitudeText.getHeight())) - this.altitudeText.getBaseLine())) / 2;
        if (height < 0) {
            height = 0;
        }
        this.altitudeText.setMargin(0, 0, height, this.altitudeIndicator.getWidth());
        this.speedIndicator = new Image(new ResourceBitmapLoader(resources, C0984R.drawable.ff2_speed_icon_selected), Align.BOTTOM_RIGHT);
        this.speedText = new Text(controlDroneActivity, "0.00 km / h", Align.BOTTOM_RIGHT, TYPEFACE.Helvetica(controlDroneActivity), (int) resources.getDimension(C0984R.dimen.hud_usb_indicator_text_size));
        this.speedText.setTextColor(-1);
        this.speedIndicator.setMargin(0, 0, (this.bottomBarBg.getHeight() - this.speedIndicator.getHeight()) / 2, 0);
        height = ((int) (((float) (this.bottomBarBg.getHeight() - this.speedText.getHeight())) - this.speedText.getBaseLine())) / 2;
        if (height < 0) {
            height = 0;
        }
        this.speedText.setMargin(0, this.speedIndicator.getWidth(), height, 0);
        this.btnMapLeft = new Button(resources, C0984R.drawable.btn_left, Align.BOTTOM_LEFT);
        this.btnMapLeft.setMargin(0, 0, (this.bottomBarBg.getHeight() - this.btnMapLeft.getHeight()) / 2, 0);
        this.btnMapLeft.setVisible(false);
        this.btnMapLeft.setEnabled(false);
        this.btnMapRight = new Button(resources, C0984R.drawable.btn_right, Align.BOTTOM_RIGHT);
        this.btnMapRight.setMargin(0, 0, (this.bottomBarBg.getHeight() - this.btnMapLeft.getHeight()) / 2, 0);
        this.btnMapRight.setVisible(false);
        this.btnMapRight.setEnabled(false);
        this.btnBackToHome = new Button(resources, C0984R.drawable.btn_home_img, Align.BOTTOM_LEFT);
        this.btnBackToHome.setMargin(0, 0, 0, ((int) ((((float) (this.btnBackToHome.getWidth() / 3)) * resources.getDisplayMetrics().density) / 2.0f)) + (this.altitudeIndicator.getWidth() + this.altitudeText.getWidth()));
        this.btnBackToHome.setVisible(false);
        this.btnBackToHome.setFadingDuration(500);
        this.btnRescue = new Button(resources, C0984R.drawable.btn_rescue, Align.BOTTOM_LEFT);
        this.btnRescue.setMargin(0, 0, 0, ((int) ((((float) (this.btnRescue.getWidth() / 3)) * resources.getDisplayMetrics().density) / 2.0f)) + (this.altitudeIndicator.getWidth() + this.altitudeText.getWidth()));
        this.btnMapGo = new Button(resources, (int) C0984R.drawable.btn_gps_go, (int) C0984R.drawable.btn_gps_go, Align.BOTTOM_RIGHT);
        height = (int) ((((float) (this.btnMapGo.getWidth() / 3)) * resources.getDisplayMetrics().density) / 2.0f);
        this.btnMapGo.setMargin(0, this.speedIndicator.getWidth() + this.speedText.getWidth(), 0, 0);
        this.btnMapGo.setVisible(false);
        this.btnMapGo.setEnabled(false);
        this.btnRandomShake = new Button(resources, C0984R.drawable.btn_rescue_mode_random_shake, Align.BOTTOM_LEFT);
        this.btnRandomShake.setMargin(0, 0, 0, this.btnRandomShake.getWidth() / 2);
        this.btnRandomShake.setVisible(false);
        this.btnRandomShake.setFadingDuration(500);
        this.btnOverBalance = new Button(resources, C0984R.drawable.btn_rescue_mode_over_balance, Align.BOTTOM_RIGHT);
        this.btnOverBalance.setMargin(0, this.btnOverBalance.getWidth() / 2, 0, 0);
        this.btnOverBalance.setVisible(false);
        this.btnOverBalance.setFadingDuration(500);
        this.buttons = new Button[]{this.btnSettings, this.btnEmergency, this.btnTakeOff, this.btnLand, this.btnPhoto, this.btnRecord, this.btnCameraSwitch, this.btnBack, this.btnBackToHome, this.btnRescue, this.btnMapGo, this.btnMapLeft, this.btnMapRight, this.btnRandomShake, this.btnOverBalance, this.btnGps};
        this.renderer.addSprite(Integer.valueOf(27), this.rescueImageBaseLayer);
        this.renderer.addSprite(Integer.valueOf(28), this.rescueImage);
        this.renderer.addSprite(Integer.valueOf(5), image);
        this.renderer.addSprite(Integer.valueOf(6), this.bottomBarBg);
        this.renderer.addSprite(Integer.valueOf(10), this.btnSettings);
        this.renderer.addSprite(Integer.valueOf(18), this.btnBack);
        this.renderer.addSprite(Integer.valueOf(9), this.btnPhoto);
        this.renderer.addSprite(Integer.valueOf(8), this.btnRecord);
        this.renderer.addSprite(Integer.valueOf(7), this.btnCameraSwitch);
        this.renderer.addSprite(Integer.valueOf(3), this.btnEmergency);
        this.renderer.addSprite(Integer.valueOf(4), this.btnTakeOff);
        this.renderer.addSprite(Integer.valueOf(19), this.btnLand);
        this.renderer.addSprite(Integer.valueOf(11), this.batteryIndicator);
        this.renderer.addSprite(Integer.valueOf(12), this.wifiIndicator);
        this.renderer.addSprite(Integer.valueOf(13), this.txtAlert);
        this.renderer.addSprite(Integer.valueOf(14), this.txtBatteryStatus);
        this.renderer.addSprite(Integer.valueOf(15), this.txtRecord);
        this.renderer.addSprite(Integer.valueOf(38), this.btnGps);
        this.renderer.addSprite(Integer.valueOf(39), this.txtGps);
        this.renderer.addSprite(Integer.valueOf(16), this.usbIndicator);
        this.renderer.addSprite(Integer.valueOf(17), this.txtUsbRemaining);
        this.renderer.addSprite(Integer.valueOf(20), this.speedIndicator);
        this.renderer.addSprite(Integer.valueOf(21), this.speedText);
        this.renderer.addSprite(Integer.valueOf(22), this.altitudeIndicator);
        this.renderer.addSprite(Integer.valueOf(23), this.altitudeText);
        this.renderer.addSprite(Integer.valueOf(37), this.btnBackToHome);
        this.renderer.addSprite(Integer.valueOf(24), this.btnRescue);
        this.renderer.addSprite(Integer.valueOf(40), this.btnMapGo);
        this.renderer.addSprite(Integer.valueOf(41), this.btnMapLeft);
        this.renderer.addSprite(Integer.valueOf(42), this.btnMapRight);
        this.renderer.addSprite(Integer.valueOf(25), this.btnRandomShake);
        this.renderer.addSprite(Integer.valueOf(26), this.btnOverBalance);
        this.renderer.addSprite(Integer.valueOf(29), this.propeller1Image);
        this.renderer.addSprite(Integer.valueOf(30), this.propeller2Image);
        this.renderer.addSprite(Integer.valueOf(31), this.propeller3Image);
        this.renderer.addSprite(Integer.valueOf(32), this.propeller4Image);
        this.renderer.addSprite(Integer.valueOf(33), this.propeller1CrossImage);
        this.renderer.addSprite(Integer.valueOf(34), this.propeller2CrossImage);
        this.renderer.addSprite(Integer.valueOf(35), this.propeller3CrossImage);
        this.renderer.addSprite(Integer.valueOf(36), this.propeller4CrossImage);
    }

    private void createMapViewTree() {
        this.cameraChangeListener.init();
        this.mapLayoutContainer = new LinearLayout(this.context);
        this.mapLayoutContainer.setOrientation(1);
        this.mapLayoutContainer.setLayoutParams(new LayoutParams(-1, -1));
        Holder holder = new Holder();
        Holder holder2 = new Holder();
        ImageUtils.getBitmapDims(this.context.getResources(), C0984R.drawable.barre_haut, holder, holder2);
        View linearLayout = new LinearLayout(this.context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, ((Integer) holder2.value).intValue()));
        this.mapView = new MapView2(this.context, this.settings, this.mapClickListener);
        this.mapView.setLayoutParams(new LayoutParams(-1, -1));
        this.mapView.onCreate(null);
        this.mapView.onResume();
        final GoogleMap map = this.mapView.getMap();
        map.setMyLocationEnabled(true);
        map.setMapType(4);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setTiltGesturesEnabled(false);
        map.getUiSettings().setRotateGesturesEnabled(false);
        map.setOnMarkerClickListener(new C12155());
        map.setOnMapClickListener(this.mapClickListener);
        Coords mapPilotingLastCoords = this.settings.getMapPilotingLastCoords();
        if (mapPilotingLastCoords != null) {
            map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng((double) mapPilotingLastCoords.latitude, (double) mapPilotingLastCoords.longitude), mapPilotingLastCoords.zoom, mapPilotingLastCoords.tilt, mapPilotingLastCoords.bearing)));
        }
        map.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
            public void onMyLocationChange(Location location) {
                if (!HudViewController.this.locatedMe) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18.0f));
                    HudViewController.this.locatedMe = true;
                }
            }
        });
        map.setOnCameraChangeListener(this.cameraChangeListener);
        ImageUtils.getBitmapDims(this.context.getResources(), C0984R.drawable.barre_bas, holder, holder2);
        View linearLayout2 = new LinearLayout(this.context);
        linearLayout2.setLayoutParams(new LinearLayout.LayoutParams(-1, ((Integer) holder2.value).intValue()));
        View frameLayout = new FrameLayout(this.context);
        frameLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -1, 1.0f));
        View relativeLayout = new RelativeLayout(this.context);
        relativeLayout.setLayoutParams(new LayoutParams(-1, -1));
        this.altitudeSlide = new SlideRuleView2(this.context);
        this.altitudeSlide.setLayoutParams(new RelativeLayout.LayoutParams(120, -1));
        this.altitudeSlide.setElementDataProvider(this.altitudeDP);
        this.altitudeSlide.setInterpolator(new AccelerateDecelerateInterpolator());
        this.altitudeSlide.setCurrValue1Bg(this.context.getResources().getDrawable(C0984R.drawable.ff2_altitude_speed_box));
        this.altitudeSlide.setCurrValue2Bg(this.context.getResources().getDrawable(C0984R.drawable.ff2_altitude_speed_box_green));
        this.altitudeSlide.setBarMargins(15, 10, 40, 10);
        this.altitudeSlide.setBarBgColor(2130706432);
        this.altitudeSlide.setLinesColor(-34816);
        this.altitudeSlide.setTextColor(-1);
        this.altitudeSlide.setAnimationTime(500);
        this.altitudeSlide.setElementsCount(14);
        this.altitudeSlide.setChangeListener(this.altitudeSlideChangeListener);
        this.speedSlide = new SlideRuleView2(this.context);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(120, -1);
        layoutParams.addRule(11);
        this.speedSlide.setLayoutParams(layoutParams);
        this.speedSlide.setElementDataProvider(this.speedDP);
        this.speedSlide.setInterpolator(new AccelerateDecelerateInterpolator());
        this.speedSlide.setCurrValue1Bg(this.context.getResources().getDrawable(C0984R.drawable.ff2_altitude_speed_box));
        this.speedSlide.setCurrValue2Bg(this.context.getResources().getDrawable(C0984R.drawable.ff2_altitude_speed_box_green));
        this.speedSlide.setBarMargins(15, 10, 40, 10);
        this.speedSlide.setBarBgColor(2130706432);
        this.speedSlide.setLinesColor(-34816);
        this.speedSlide.setTextColor(-1);
        this.speedSlide.setAnimationTime(500);
        this.speedSlide.setElementsCount(14);
        this.speedSlide.setChangeListener(this.speedSlideChangeListener);
        View linearLayout3 = new LinearLayout(this.context);
        linearLayout3.setLayoutParams(new LayoutParams(-1, -1));
        View frameLayout2 = new FrameLayout(this.context);
        frameLayout2.setLayoutParams(new LinearLayout.LayoutParams(0, -1, 216.0f));
        View frameLayout3 = new FrameLayout(this.context);
        frameLayout3.setLayoutParams(new LinearLayout.LayoutParams(0, -1, 216.0f));
        View linearLayout4 = new LinearLayout(this.context);
        linearLayout4.setOrientation(1);
        linearLayout4.setLayoutParams(new LinearLayout.LayoutParams(0, -1, 1616.0f));
        View frameLayout4 = new FrameLayout(this.context);
        frameLayout4.setLayoutParams(new LinearLayout.LayoutParams(-1, 0, 68.0f));
        View linearLayout5 = new LinearLayout(this.context);
        linearLayout5.setLayoutParams(new LinearLayout.LayoutParams(-1, 0, 1084.0f));
        linearLayout5.setOrientation(1);
        linearLayout4.addView(frameLayout4);
        linearLayout4.addView(linearLayout5);
        this.toastView = new TextView(this.context);
        this.toastView.setLayoutParams(new LayoutParams(-1, -2));
        this.toastView.setBackgroundColor(1879048192);
        this.toastView.setTextColor(-65536);
        this.toastView.setText(C0984R.string.ae_ID000124);
        this.toastView.setGravity(17);
        this.toastView.setPadding(0, (int) TypedValue.applyDimension(1, 3.0f, this.context.getResources().getDisplayMetrics()), 0, (int) TypedValue.applyDimension(1, 3.0f, this.context.getResources().getDisplayMetrics()));
        this.zoomText = new TextView(this.context);
        LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams2.setMargins(0, (int) TypedValue.applyDimension(1, 10.0f, this.context.getResources().getDisplayMetrics()), 0, 0);
        this.zoomText.setLayoutParams(layoutParams2);
        this.zoomText.setBackgroundResource(C0984R.drawable.round_borders_layout);
        this.zoomText.setTextColor(-1);
        linearLayout5.addView(this.toastView);
        linearLayout5.addView(this.zoomText);
        linearLayout3.addView(frameLayout2);
        linearLayout3.addView(linearLayout4);
        linearLayout3.addView(frameLayout3);
        relativeLayout.addView(this.altitudeSlide);
        relativeLayout.addView(this.speedSlide);
        relativeLayout.addView(linearLayout3);
        frameLayout.addView(this.mapView);
        frameLayout.addView(relativeLayout);
        this.mapLayoutContainer.addView(linearLayout);
        this.mapLayoutContainer.addView(frameLayout);
        this.mapLayoutContainer.addView(linearLayout2);
        this.rootContainer.addView(this.mapLayoutContainer);
        this.altitudeSlide.setValue2(this.context.getAltitudeRelValue(2000.0f));
        this.speedSlide.setValue2(this.context.getSpeedRelValue(2000.0f));
    }

    private static LatLng getCenter(LatLng latLng, LatLng latLng2) {
        double min = Math.min(latLng.latitude, latLng2.latitude);
        double min2 = Math.min(latLng.longitude, latLng2.longitude);
        return new LatLng(min + ((Math.max(latLng.latitude, latLng2.latitude) - min) / 2.0d), min2 + ((Math.max(latLng.longitude, latLng2.longitude) - min2) / 2.0d));
    }

    private static Bitmap getDistanceBitmap(float f, int i, int i2) {
        String format = String.format("%.2fm", new Object[]{Float.valueOf(f)});
        Paint paint = new Paint();
        paint.setTextSize((float) i);
        paint.setColor(i2);
        paint.setTextAlign(Paint.Align.LEFT);
        float abs = (float) ((int) (Math.abs(paint.ascent()) + 0.5f));
        Bitmap createBitmap = Bitmap.createBitmap((int) (paint.measureText(format) + 0.5f), (int) ((paint.descent() + abs) + 0.5f), Config.ARGB_8888);
        new Canvas(createBitmap).drawText(format, 0.0f, abs, paint);
        return createBitmap;
    }

    private float getLastGpsCoordDistance(double d, double d2, double d3, double d4) {
        Location.distanceBetween(d, d2, d3, d4, this.gpsLastCoordDistance);
        return this.gpsLastCoordDistance[0];
    }

    private static Bitmap getRescueDroneBgBitmap() {
        Bitmap createBitmap = Bitmap.createBitmap(32, 32, Config.ARGB_8888);
        createBitmap.eraseColor(-1907998);
        return createBitmap;
    }

    private void initGLSurfaceView() {
        if (this.glView != null) {
            this.glView.setRenderer(this.renderer);
            this.glView.setOnTouchListener(this);
        }
    }

    private void initNavdataStrings() {
        this.emergencyStringMap = new HashMap(ErrorState.values().length);
        this.emergencyStringMap.put(ErrorState.NAVDATA_CONNECTION, Integer.valueOf(C0984R.string.ae_ID000002));
        this.emergencyStringMap.put(ErrorState.EMERGENCY_CUTOUT, Integer.valueOf(C0984R.string.ae_ID000004));
        this.emergencyStringMap.put(ErrorState.EMERGENCY_MOTORS, Integer.valueOf(C0984R.string.ae_ID000005));
        this.emergencyStringMap.put(ErrorState.EMERGENCY_CAMERA, Integer.valueOf(C0984R.string.ae_ID000006));
        this.emergencyStringMap.put(ErrorState.EMERGENCY_PIC_WATCHDOG, Integer.valueOf(C0984R.string.ae_ID000007));
        this.emergencyStringMap.put(ErrorState.EMERGENCY_PIC_VERSION, Integer.valueOf(C0984R.string.ae_ID000008));
        this.emergencyStringMap.put(ErrorState.EMERGENCY_ANGLE_OUT_OF_RANGE, Integer.valueOf(C0984R.string.ae_ID000009));
        this.emergencyStringMap.put(ErrorState.EMERGENCY_VBAT_LOW, Integer.valueOf(C0984R.string.ae_ID000010));
        this.emergencyStringMap.put(ErrorState.EMERGENCY_USER_EL, Integer.valueOf(C0984R.string.ae_ID000011));
        this.emergencyStringMap.put(ErrorState.EMERGENCY_ULTRASOUND, Integer.valueOf(C0984R.string.ae_ID000003));
        this.emergencyStringMap.put(ErrorState.EMERGENCY_UNKNOWN, Integer.valueOf(C0984R.string.ae_ID000012));
        this.emergencyStringMap.put(ErrorState.START_NOT_RECEIVED, Integer.valueOf(C0984R.string.ae_ID000001));
        this.emergencyStringMap.put(ErrorState.ALERT_CAMERA, Integer.valueOf(C0984R.string.ae_ID000017));
        this.emergencyStringMap.put(ErrorState.ALERT_VBAT_LOW, Integer.valueOf(C0984R.string.ae_ID000014));
        this.emergencyStringMap.put(ErrorState.ALERT_ULTRASOUND, Integer.valueOf(C0984R.string.ae_ID000015));
        this.emergencyStringMap.put(ErrorState.ALERT_VISION, Integer.valueOf(C0984R.string.ae_ID000016));
    }

    private void selectRescueButton(Button button) {
        if (button != this.btnSelectedRescue) {
            if (this.btnSelectedRescue != null) {
                this.btnSelectedRescue.setFadingEnabled(false);
            }
            this.btnSelectedRescue = button;
            if (this.btnSelectedRescue != null) {
                this.btnSelectedRescue.setFadingEnabled(true);
            }
        }
    }

    private void setDroneTargetCoords(LatLng latLng) {
        if (this.droneTargetMarker == null) {
            this.droneTargetMarker = this.mapView.getMap().addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(C0984R.drawable.ff2_0_drone_red)).anchor(0.5f, 0.5f));
        } else {
            this.droneTargetMarker.setPosition(latLng);
        }
        this.setTargetRotation = true;
        updateDronePath();
        updateGoButtonState();
        updateToast();
    }

    private static void setMotor(MotorState motorState, int i, Image image, Image image2) {
        if (motorState == MotorState.Stalled) {
            image.setRotationPerSecond(0.0f);
            image2.setVisible(true);
            image2.setBlinkingDuration(500);
            image2.setBlinkingEnabled(true);
            return;
        }
        if (motorState == MotorState.Idle) {
            image.setRotationPerSecond(0.0f);
        } else if (motorState == MotorState.Ramp || motorState == MotorState.Running) {
            if (i == 0) {
                i = 10;
            }
            image.setRotationPerSecond((float) (i * 360));
        }
        image2.setVisible(false);
        image2.setBlinkingEnabled(false);
    }

    private void showJoysticks(boolean z) {
        this.joysticksEnabled = z;
        for (JoystickBase visible : this.joysticks) {
            visible.setVisible(z);
        }
    }

    private void updateDronePath() {
        this.btnMapLeft.setEnabled(this.droneTargetMarker != null);
        this.btnMapRight.setEnabled(this.droneTargetMarker != null);
        if (this.droneCurrentMarker != null && this.droneTargetMarker != null) {
            if (this.setTargetRotation) {
                this.setTargetRotation = false;
                LatLng position = this.droneCurrentMarker.getPosition();
                LatLng position2 = this.droneTargetMarker.getPosition();
                this.autoHeadingInitialValue = (float) Math.atan((position.longitude - position2.longitude) / (position.latitude - position2.latitude));
                if (position.latitude > position2.latitude) {
                    this.autoHeadingInitialValue = (float) (((double) this.autoHeadingInitialValue) + 3.141592653589793d);
                }
                this.droneTargetMarker.setRotation((float) Math.toDegrees((double) this.autoHeadingInitialValue));
                this.droneTargetHeading = (float) Math.toDegrees((double) this.autoHeadingInitialValue);
                if (this.flyingCameraActive) {
                    this.context.sendWaypointPosition();
                }
            }
            this.waypointsPoints.set(0, this.droneCurrentMarker.getPosition());
            this.waypointsPoints.set(1, this.droneTargetMarker.getPosition());
            if (this.waypointPath == null) {
                this.waypointPath = this.mapView.getMap().addPolyline(new PolylineOptions().addAll(this.waypointsPoints).width(5.0f).color(-16711936));
            } else {
                this.waypointPath.setPoints(this.waypointsPoints);
            }
            float[] fArr = new float[1];
            Location.distanceBetween(this.droneCurrentMarker.getPosition().latitude, this.droneCurrentMarker.getPosition().longitude, this.droneTargetMarker.getPosition().latitude, this.droneTargetMarker.getPosition().longitude, fArr);
            if (this.distanceMarker == null) {
                this.distanceMarker = this.mapView.getMap().addMarker(new MarkerOptions().position(getCenter(this.droneCurrentMarker.getPosition(), this.droneTargetMarker.getPosition())).icon(BitmapDescriptorFactory.fromBitmap(getDistanceBitmap(fArr[0], 20, -65536))).anchor(0.5f, 0.5f));
                return;
            }
            this.distanceMarker.setPosition(getCenter(this.droneCurrentMarker.getPosition(), this.droneTargetMarker.getPosition()));
            this.distanceMarker.setIcon(BitmapDescriptorFactory.fromBitmap(getDistanceBitmap(fArr[0], 20, -65536)));
        } else if (this.waypointPath != null) {
            this.waypointPath.remove();
            this.waypointPath = null;
            this.distanceMarker = null;
        }
    }

    private void updateGoButtonState() {
        if (!this.flyingCameraActive) {
            boolean z = this.isFlying && this.altitudeSlide.getValue() > 0.0f && this.speedSlide.getValue() > 0.0f && this.droneCurrentMarker != null && this.droneTargetMarker != null;
            this.btnMapGo.setEnabled(z);
        }
    }

    private void updateToast() {
        if (!this.isFlying) {
            Object obj = (this.altitudeSlide.getValue() <= 0.0f || this.speedSlide.getValue() <= 0.0f || this.droneTargetMarker == null) ? null : 1;
            this.toastView.setText(obj != null ? C0984R.string.ae_ID000125 : C0984R.string.ae_ID000124);
        }
    }

    public float getAutoHeadingInitialValue() {
        return this.autoHeadingInitialValue;
    }

    public int getBottomBarHeight() {
        return Math.max(this.btnTakeOff.getHeight(), this.btnLand.getHeight());
    }

    public float getDroneTargetHeading() {
        return this.droneTargetHeading;
    }

    public int getEmergencyWidth() {
        return this.btnEmergency.getWidth();
    }

    public JoystickBase getJoystickLeft() {
        return this.joysticks[0];
    }

    public JoystickBase getJoystickRight() {
        return this.joysticks[1];
    }

    public View getRootView() {
        if (this.glView != null) {
            return this.glView;
        }
        Log.w(TAG, "Can't find root view");
        return null;
    }

    public float getSlidingAltitudeValue() {
        return this.altitudeSlide == null ? 0.0f : this.altitudeSlide.getValue();
    }

    public float getSlidingSpeedValue() {
        return this.speedSlide == null ? 0.0f : this.speedSlide.getValue();
    }

    public double getTargetPositionLatitude() {
        return this.droneTargetMarker == null ? 0.0d : this.droneTargetMarker.getPosition().latitude;
    }

    public double getTargetPositionLongitude() {
        return this.droneTargetMarker == null ? 0.0d : this.droneTargetMarker.getPosition().longitude;
    }

    public int getTopBarHeight() {
        return this.btnEmergency.getHeight();
    }

    public int getTopBarWidth() {
        return this.bottomBarBg.getWidth();
    }

    public void invalidate() {
        if (this.altitudeSlide != null) {
            this.altitudeSlide.invalidate();
        }
        if (this.speedSlide != null) {
            this.speedSlide.invalidate();
        }
    }

    public boolean isMapShown() {
        return this.mapView != null;
    }

    public boolean isMotionEventInWorkArea(MotionEvent motionEvent) {
        for (int i = 0; i < motionEvent.getPointerCount(); i++) {
            int y = (int) motionEvent.getY(i);
            if (y < this.btnEmergency.getHeight() || y > this.glView.getHeight() - this.btnLand.getHeight()) {
                return false;
            }
        }
        return true;
    }

    public boolean isRescueShown() {
        return this.rescueShown;
    }

    public void onDestroy() {
        this.renderer.clearSprites();
        if (this.mapView != null) {
            this.mapView.onDestroy();
            this.mapView = null;
            this.toastView = null;
            this.droneCurrentMarker = null;
            this.droneTargetMarker = null;
            this.waypointPath = null;
            this.distanceMarker = null;
        }
    }

    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return false;
    }

    public void onLongPress(MotionEvent motionEvent) {
    }

    public void onPause() {
        for (JoystickBase joystickBase : this.joysticks) {
            if (joystickBase != null) {
                joystickBase.clearPressedState();
            }
        }
        if (this.glView != null) {
            this.glView.onPause();
        }
        if (this.mapView != null) {
            this.mapView.onPause();
        }
    }

    public void onResume() {
        if (this.glView != null) {
            this.glView.onResume();
        }
        if (this.mapView != null) {
            this.mapView.onResume();
        }
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return false;
    }

    public void onShowPress(MotionEvent motionEvent) {
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean z;
        int i = 0;
        for (Button processTouch : this.buttons) {
            if (processTouch.processTouch(view, motionEvent)) {
                z = true;
                break;
            }
        }
        z = false;
        if (!z) {
            this.gestureDetector.onTouchEvent(motionEvent);
            while (i < this.joysticks.length) {
                JoystickBase joystickBase = this.joysticks[i];
                if (joystickBase != null && joystickBase.processTouch(view, motionEvent)) {
                    z = true;
                }
                i++;
            }
        }
        return z;
    }

    public void recheckGooglePlayServices() {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.context) == 0) {
            showMap(true);
        }
    }

    public void rotateDroneTargetLeft() {
        this.autoHeadingInitialValue = GroundOverlayOptions.NO_DIMENSION;
        this.droneTargetHeading -= 3.0f;
        this.droneTargetHeading %= 360.0f;
        this.droneTargetMarker.setRotation(this.droneTargetHeading);
    }

    public void rotateDroneTargetRight() {
        this.autoHeadingInitialValue = GroundOverlayOptions.NO_DIMENSION;
        this.droneTargetHeading += 3.0f;
        this.droneTargetHeading %= 360.0f;
        this.droneTargetMarker.setRotation(this.droneTargetHeading);
    }

    public void setAltitudeValue(int i) {
        this.altitudeText.setText(String.format("%.2f m", new Object[]{Double.valueOf(((double) i) / 1000.0d)}));
    }

    public void setBackButtonVisible(boolean z) {
        if (z) {
            this.btnBack.setEnabled(true);
            this.btnBack.setAlpha(1.0f);
            return;
        }
        this.btnBack.setEnabled(false);
        this.btnBack.setAlpha(0.0f);
    }

    public void setBackHomeBtnActive(boolean z) {
        this.btnRescue.setFadingEnabled(z);
    }

    public void setBackHomeBtnVisible(boolean z) {
        this.btnBackToHome.setVisible(z);
        this.btnRescue.setVisible(!z);
    }

    public void setBatteryValue(int i) {
        int i2 = 3;
        if (i > 100 || i < 0) {
            Log.w(TAG, "Can't set battery value. Invalid value " + i);
            return;
        }
        int round = Math.round((((float) i) / 100.0f) * 3.0f);
        this.txtBatteryStatus.setText(i + "%");
        if (round < 0) {
            round = 0;
        }
        if (round <= 3) {
            i2 = round;
        }
        if (this.batteryIndicator != null) {
            this.batteryIndicator.setValue(i2);
        }
    }

    public void setBtnBackClickListener(OnClickListener onClickListener) {
        this.btnBack.setOnClickListener(onClickListener);
    }

    public void setBtnBackToHomeClickListener(OnClickListener onClickListener) {
        this.btnBackToHome.setOnClickListener(onClickListener);
    }

    public void setBtnCameraSwitchClickListener(OnClickListener onClickListener) {
        this.btnCameraSwitch.setOnClickListener(onClickListener);
    }

    public void setBtnEmergencyClickListener(OnClickListener onClickListener) {
        this.btnEmergency.setOnClickListener(onClickListener);
    }

    public void setBtnGpsClickListener(OnClickListener onClickListener) {
        this.btnGps.setOnClickListener(onClickListener);
    }

    public void setBtnMapGoClickListener(OnClickListener onClickListener) {
        this.btnMapGo.setOnClickListener(onClickListener);
    }

    public void setBtnMapLeftClickListener(OnClickListener onClickListener, OnPressListener onPressListener) {
        this.btnMapLeft.setOnClickListener(onClickListener);
        this.btnMapLeft.setOnClickRepeatListener(onPressListener);
    }

    public void setBtnMapRightClickListener(OnClickListener onClickListener, OnPressListener onPressListener) {
        this.btnMapRight.setOnClickListener(onClickListener);
        this.btnMapRight.setOnClickRepeatListener(onPressListener);
    }

    public void setBtnOverBalanceClickListener(OnClickListener onClickListener) {
        this.btnOverBalance.setOnClickListener(onClickListener);
    }

    public void setBtnPhotoClickListener(OnClickListener onClickListener) {
        this.btnPhoto.setOnClickListener(onClickListener);
    }

    public void setBtnRandomShakeClickListener(OnClickListener onClickListener) {
        this.btnRandomShake.setOnClickListener(onClickListener);
    }

    public void setBtnRecordClickListener(OnClickListener onClickListener) {
        this.btnRecord.setOnClickListener(onClickListener);
    }

    public void setBtnRescueClickListener(OnClickListener onClickListener) {
        this.btnRescue.setOnClickListener(onClickListener);
    }

    public void setBtnTakeOffClickListener(OnClickListener onClickListener) {
        this.btnTakeOff.setOnClickListener(onClickListener);
        this.btnLand.setOnClickListener(onClickListener);
    }

    public void setCameraButtonEnabled(boolean z) {
        this.btnPhoto.setEnabled(z);
    }

    public void setDoubleTapClickListener(OnDoubleTapListener onDoubleTapListener) {
        this.gestureDetector.setOnDoubleTapListener(onDoubleTapListener);
    }

    public void setDroneHeading(float f) {
        if (this.droneCurrentMarker != null) {
            this.droneCurrentMarker.setRotation(f);
        }
    }

    public void setEmergency(ErrorState errorState) {
        this.errorStateRes = (Integer) this.emergencyStringMap.get(errorState);
        if (this.errorStateRes != null) {
            this.txtAlert.setText(this.context.getString(this.errorStateRes.intValue()));
            if (!isMapShown()) {
                this.txtAlert.setVisible(true);
            }
            this.txtAlert.blink(true);
            return;
        }
        this.txtAlert.setVisible(false);
        this.txtAlert.blink(false);
    }

    public void setEmergencyButtonEnabled(boolean z) {
        this.btnEmergency.setEnabled(z);
    }

    public void setFlyingCameraActive(boolean z) {
        if (isMapShown() && z != this.flyingCameraActive) {
            this.flyingCameraActive = z;
            if (z) {
                this.btnMapGo.setImages(this.context.getResources(), C0984R.drawable.btn_gps_stop, C0984R.drawable.btn_gps_stop);
                this.btnMapGo.setEnabled(true);
                return;
            }
            this.btnMapGo.setImages(this.context.getResources(), C0984R.drawable.btn_gps_go, C0984R.drawable.btn_gps_go);
            updateGoButtonState();
        }
    }

    public void setFpsVisible(final boolean z) {
        this.context.runOnUiThread(new Runnable() {
            public void run() {
                if (z) {
                    HudViewController.this.txtSceneFps.setVisibility(0);
                } else {
                    HudViewController.this.txtSceneFps.setVisibility(4);
                }
            }
        });
    }

    public void setGpsParams(boolean z, boolean z2, float f, boolean z3, double d, double d2) {
        Button button = this.btnGps;
        boolean z4 = z && z2;
        button.setEnabled(z4);
        if (!z) {
            this.btnGps.setImages(this.context.getResources(), C0984R.drawable.ff2_0_gps_selected_grey, C0984R.drawable.ff2_0_gps_selected_grey);
            this.txtGps.setText(HttpVersions.HTTP_0_9);
        } else if (z2) {
            int i = ((int) f) >= 5 ? ((((int) f) / 5) + 1) * 5 : (int) f;
            r4 = z3 ? C0984R.drawable.ff2_0_gps_selected_back : C0984R.drawable.ff2_0_gps_selected;
            this.btnGps.setImages(this.context.getResources(), r4, r4);
            this.txtGps.setText(String.format("%dm", new Object[]{Integer.valueOf(i)}));
        } else {
            Calendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTimeInMillis(System.currentTimeMillis());
            r4 = gregorianCalendar.get(13) % 2 != 0 ? C0984R.drawable.ff2_0_gps_not_selected : C0984R.drawable.ff2_0_gps_selected_grey;
            this.btnGps.setImages(this.context.getResources(), r4, r4);
        }
        if (this.mapView != null) {
            Object obj = null;
            if (this.droneCurrentMarker == null) {
                this.droneCurrentMarker = this.mapView.getMap().addMarker(new MarkerOptions().position(new LatLng(d, d2)).icon(BitmapDescriptorFactory.fromResource(C0984R.drawable.ff2_0_drone)).anchor(0.5f, 0.5f));
                obj = 1;
            } else if (((double) getLastGpsCoordDistance(this.prevLatFused, this.prevLongFused, d, d2)) > 0.5d) {
                this.prevLatFused = d;
                this.prevLongFused = d2;
                this.droneCurrentMarker.setPosition(new LatLng(d, d2));
                obj = 1;
            }
            if (obj != null && this.isFlying) {
                this.dronePoints.add(this.droneCurrentMarker.getPosition());
                if (this.dronePath != null) {
                    this.dronePath.remove();
                }
                this.dronePath = this.mapView.getMap().addPolyline(new PolylineOptions().addAll(this.dronePoints).width(5.0f).color(-16711936));
            }
            updateDronePath();
            updateGoButtonState();
        }
    }

    public void setInterfaceOpacity(float f) {
        if (f < 0.0f || f > 100.0f) {
            Log.w(TAG, "Can't set interface opacity. Invalid value: " + f);
            return;
        }
        this.joypadOpacity = f / 100.0f;
        Sprite sprite = this.renderer.getSprite(Integer.valueOf(1));
        if (sprite != null) {
            sprite.setAlpha(this.joypadOpacity);
        }
        sprite = this.renderer.getSprite(Integer.valueOf(2));
        if (sprite != null) {
            sprite.setAlpha(this.joypadOpacity);
        }
    }

    public void setIsFlying(boolean z) {
        boolean z2 = false;
        this.isFlying = z;
        if (isMapShown()) {
            this.toastView.setVisibility(z ? 8 : 0);
            updateToast();
        }
        if (!isRescueShown()) {
            Button button = this.btnTakeOff;
            if (!z) {
                z2 = true;
            }
            button.setVisible(z2);
            this.btnLand.setVisible(z);
        }
    }

    public void setJoystick(JoystickBase joystickBase) {
        this.joysticks[0] = joystickBase;
        if (joystickBase != null) {
            this.joysticks[0].setAlign(Align.BOTTOM_CENTER);
            this.joysticks[0].setAlpha(this.joypadOpacity);
            joystickBase.setInverseYWhenDraw(true);
            joystickBase.setMargin(0, 0, this.context.getResources().getDimensionPixelSize(C0984R.dimen.hud_joy_margin) + this.bottomBarBg.getHeight(), 0);
            this.renderer.removeSprite(Integer.valueOf(1));
            this.renderer.removeSprite(Integer.valueOf(2));
            this.renderer.addSpriteBegin(Integer.valueOf(1), joystickBase);
        }
    }

    public void setJoysticks(JoystickBase joystickBase, JoystickBase joystickBase2) {
        this.joysticks[0] = joystickBase;
        if (joystickBase != null) {
            this.joysticks[0].setAlign(Align.BOTTOM_LEFT);
            this.joysticks[0].setAlpha(this.joypadOpacity);
        }
        this.joysticks[1] = joystickBase2;
        if (joystickBase2 != null) {
            this.joysticks[1].setAlign(Align.BOTTOM_RIGHT);
            this.joysticks[1].setAlpha(this.joypadOpacity);
        }
        for (JoystickBase joystickBase3 : this.joysticks) {
            if (joystickBase3 != null) {
                joystickBase3.setInverseYWhenDraw(true);
                int dimensionPixelSize = this.context.getResources().getDimensionPixelSize(C0984R.dimen.hud_joy_margin);
                joystickBase3.setMargin(0, dimensionPixelSize, this.bottomBarBg.getHeight() + dimensionPixelSize, dimensionPixelSize);
            }
        }
        if (joystickBase == null) {
            this.renderer.removeSprite(Integer.valueOf(1));
        } else {
            joystickBase.setVisible(this.joysticksEnabled);
            if (!this.renderer.replaceSprite(1, joystickBase)) {
                this.renderer.addSpriteBegin(Integer.valueOf(1), joystickBase);
            }
        }
        if (joystickBase2 == null) {
            this.renderer.removeSprite(Integer.valueOf(2));
            return;
        }
        joystickBase2.setVisible(this.joysticksEnabled);
        if (!this.renderer.replaceSprite(2, joystickBase2)) {
            this.renderer.addSpriteBegin(Integer.valueOf(2), joystickBase2);
        }
    }

    public void setRecordButtonEnabled(boolean z) {
        this.btnRecord.setEnabled(z);
        this.txtRecord.setEnabled(z);
    }

    public void setRecording(boolean z) {
        this.btnRecord.setChecked(z);
        if (this.txtRecord == null) {
            return;
        }
        if (z) {
            this.txtRecord.setTextColor(-65536);
        } else {
            this.txtRecord.setTextColor(-1);
        }
    }

    public void setRescueButtonEnabled(boolean z) {
        this.btnRescue.setEnabled(z);
    }

    public void setRescueMode(RescueMode rescueMode) {
        Button button = null;
        if (rescueMode == RescueMode.RandomShake) {
            button = this.btnRandomShake;
        } else if (rescueMode == RescueMode.Overbalance) {
            button = this.btnOverBalance;
        }
        selectRescueButton(button);
        if (rescueMode == RescueMode.None) {
            this.propeller1Image.setRotationPerSecond(0.0f);
            this.propeller1Image.setRotation(0.0f);
            this.propeller1Image.setBlinkingEnabled(false);
            this.propeller2Image.setRotationPerSecond(0.0f);
            this.propeller2Image.setRotation(0.0f);
            this.propeller2Image.setBlinkingEnabled(false);
            this.propeller3Image.setRotationPerSecond(0.0f);
            this.propeller3Image.setRotation(0.0f);
            this.propeller3Image.setBlinkingEnabled(false);
            this.propeller4Image.setRotationPerSecond(0.0f);
            this.propeller4Image.setRotation(0.0f);
            this.propeller4Image.setBlinkingEnabled(false);
            this.propeller1CrossImage.setVisible(false);
            this.propeller2CrossImage.setVisible(false);
            this.propeller3CrossImage.setVisible(false);
            this.propeller4CrossImage.setVisible(false);
        }
    }

    public void setSettingsButtonClickListener(OnClickListener onClickListener) {
        this.btnSettings.setOnClickListener(onClickListener);
    }

    public void setSettingsButtonEnabled(boolean z) {
        this.btnSettings.setEnabled(z);
    }

    public void setSlidingAltitudeValue(float f) {
        if (this.altitudeSlide != null) {
            this.altitudeSlide.setValue(f);
        }
    }

    public void setSlidingSpeedValue(float f) {
        if (this.speedSlide != null) {
            this.speedSlide.setValue(f);
        }
    }

    public void setSpeedValue(double d) {
        this.speedText.setText(String.format("%.2f km / h", new Object[]{Double.valueOf(0.0036d * d)}));
    }

    public void setSwitchCameraButtonEnabled(boolean z) {
        this.btnCameraSwitch.setEnabled(z);
    }

    public void setUsbIndicatorColor(boolean z, int i) {
        if (z) {
            this.usbIndicator.getSprite().updateTexture(BitmapFactory.decodeResource(this.context.getResources(), C0984R.drawable.picto_usb_actif));
            if (i >= 30) {
                this.txtUsbRemaining.setTextColor(-1);
                return;
            } else {
                this.txtUsbRemaining.setTextColor(-5636096);
                return;
            }
        }
        this.usbIndicator.getSprite().updateTexture(BitmapFactory.decodeResource(this.context.getResources(), C0984R.drawable.picto_usb_inactif));
        this.txtUsbRemaining.setTextColor(-7829368);
    }

    public void setUsbIndicatorEnabled(boolean z) {
        if (z) {
            this.usbIndicator.setAlpha(1.0f);
            this.txtUsbRemaining.setAlpha(1.0f);
            return;
        }
        this.usbIndicator.setAlpha(0.0f);
        this.txtUsbRemaining.setAlpha(0.0f);
    }

    public void setUsbRemainingTime(int i) {
        if (i != this.prevRemainingTime) {
            String str;
            if (3600 < i) {
                str = "> 1h";
            } else {
                int i2;
                int i3;
                if (30 > i) {
                    i2 = i / 60;
                    i3 = i % 60;
                } else {
                    i2 = i / 60;
                    i3 = i % 60;
                }
                if (i3 == 0 && i2 == 0) {
                    str = "FULL";
                } else {
                    str = HttpVersions.HTTP_0_9 + i2 + ":" + (i3 >= 10 ? Integer.valueOf(i3) : "0" + i3);
                }
            }
            this.prevRemainingTime = i;
            this.txtUsbRemaining.setText(str);
        }
    }

    public void setWifiValue(int i) {
        if (this.wifiIndicator != null) {
            this.wifiIndicator.setValue(i);
        }
    }

    public void showMap(boolean z) {
        if (isMapShown() != z) {
            if (!z) {
                ((VideoStageRenderer) ((GLSurfaceView2) this.rootContainer.getChildAt(0)).getRenderer()).enableVideo(true);
                showJoysticks(true);
                this.btnMapGo.setVisible(false);
                this.btnMapLeft.setVisible(false);
                this.btnMapRight.setVisible(false);
                this.altitudeIndicator.setVisible(true);
                this.altitudeText.setVisible(true);
                this.speedIndicator.setVisible(true);
                this.speedText.setVisible(true);
                this.mapView.onPause();
                this.mapView.onDestroy();
                this.altitudeSlide = null;
                this.speedSlide = null;
                this.rootContainer.removeView(this.mapLayoutContainer);
                this.mapLayoutContainer = null;
                this.mapView = null;
                this.toastView = null;
                this.droneCurrentMarker = null;
                this.droneTargetMarker = null;
                this.waypointPath = null;
                this.distanceMarker = null;
                if (this.errorStateRes != null) {
                    this.txtAlert.setVisible(true);
                }
            } else if (GooglePlayServicesChecker.check(this.context, 1)) {
                showRescueScreen(false);
                ((VideoStageRenderer) ((GLSurfaceView2) this.rootContainer.getChildAt(0)).getRenderer()).enableVideo(false);
                showJoysticks(false);
                this.btnMapGo.setVisible(true);
                this.btnMapLeft.setVisible(true);
                this.btnMapRight.setVisible(true);
                this.altitudeIndicator.setVisible(false);
                this.altitudeText.setVisible(false);
                this.speedIndicator.setVisible(false);
                this.speedText.setVisible(false);
                this.txtAlert.setVisible(false);
                try {
                    MapsInitializer.initialize(this.context);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e(TAG, "Failed to initialize Google Maps. Exception: " + e.toString());
                }
                this.dronePoints.clear();
                createMapViewTree();
            }
        }
    }

    public void showRescueScreen(boolean z) {
        boolean z2 = true;
        if (isRescueShown() != z) {
            showMap(false);
            JoystickBase joystickLeft = getJoystickLeft();
            if (joystickLeft != null) {
                joystickLeft.setVisible(!z);
            }
            joystickLeft = getJoystickRight();
            if (joystickLeft != null) {
                joystickLeft.setVisible(!z);
            }
            this.altitudeIndicator.setVisible(!z);
            this.altitudeText.setVisible(!z);
            this.speedIndicator.setVisible(!z);
            this.speedText.setVisible(!z);
            this.btnRescue.setVisible(!z);
            this.btnTakeOff.setVisible(!z);
            Button button = this.btnEmergency;
            if (z) {
                z2 = false;
            }
            button.setEnabled(z2);
            this.rescueImageBaseLayer.setVisible(z);
            this.rescueImage.setVisible(z);
            this.btnRandomShake.setVisible(z);
            this.btnOverBalance.setVisible(z);
            this.propeller1Image.setVisible(z);
            this.propeller1Image.setRotationPerSecond(0.0f);
            this.propeller1Image.setRotation(0.0f);
            this.propeller2Image.setVisible(z);
            this.propeller2Image.setRotationPerSecond(0.0f);
            this.propeller2Image.setRotation(0.0f);
            this.propeller3Image.setVisible(z);
            this.propeller3Image.setRotationPerSecond(0.0f);
            this.propeller3Image.setRotation(0.0f);
            this.propeller4Image.setVisible(z);
            this.propeller4Image.setRotationPerSecond(0.0f);
            this.propeller4Image.setRotation(0.0f);
            this.propeller1CrossImage.setVisible(false);
            this.propeller2CrossImage.setVisible(false);
            this.propeller3CrossImage.setVisible(false);
            this.propeller4CrossImage.setVisible(false);
            this.rescueShown = z;
        }
    }

    public void updateFlyingState(FlyingState flyingState) {
        boolean z = false;
        boolean z2 = flyingState == FlyingState.LANDED;
        if (z2 != this.prevIsLanded) {
            if (!z2) {
                z = true;
            }
            setBackHomeBtnVisible(z);
            this.prevIsLanded = z2;
        }
    }

    public void updateMotors(int i, MotorState motorState, int i2, MotorState motorState2, int i3, MotorState motorState3, int i4, MotorState motorState4) {
        if (!ARDroneVersion.isArDrone1()) {
            if (!(this.prevMotor1 == i && this.prevMotor1State == motorState)) {
                setMotor(motorState, i, this.propeller1Image, this.propeller1CrossImage);
                this.prevMotor1 = i;
                this.prevMotor1State = motorState;
            }
            if (!(this.prevMotor2 == i2 && this.prevMotor2State == motorState2)) {
                setMotor(motorState2, i2, this.propeller2Image, this.propeller2CrossImage);
                this.prevMotor2 = i2;
                this.prevMotor2State = motorState2;
            }
            if (!(this.prevMotor3 == i3 && this.prevMotor3State == motorState3)) {
                setMotor(motorState3, i3, this.propeller3Image, this.propeller3CrossImage);
                this.prevMotor3 = i3;
                this.prevMotor3State = motorState3;
            }
            if (this.prevMotor4 != i4 || this.prevMotor4State != motorState4) {
                setMotor(motorState4, i4, this.propeller4Image, this.propeller4CrossImage);
                this.prevMotor4 = i4;
                this.prevMotor4State = motorState4;
            }
        }
    }
}
