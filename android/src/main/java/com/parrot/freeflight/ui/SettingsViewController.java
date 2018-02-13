package com.parrot.freeflight.ui;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.parrot.ardronetool.ARDroneVersion;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.service.DroneConfig;
import com.parrot.freeflight.service.DroneControlService.DroneFlipDirection;
import com.parrot.freeflight.ui.adapters.SettingsViewAdapter;
import com.parrot.freeflight.ui.listeners.OnSeekChangedListener;
import com.parrot.freeflight.ui.widgets.ViewPagerIndicator;
import com.parrot.freeflight.utils.FontUtils;
import com.parrot.freeflight.utils.SystemUtils;
import java.util.ArrayList;
import java.util.List;
import org.mortbay.jetty.HttpVersions;

public class SettingsViewController implements OnPageChangeListener, OnClickListener {
    private static final String TAG = "SettingsViewController";
    private String ALTITUDE_DIMENSION = " m";
    private String INTERFACE_OPACITY_DIMENTION = " %";
    private String TILT_DIMENTION = " °";
    private String VERT_SPEED_MAX_DIMENTION = " mm/s";
    private String YAW_SPEED_MAX_DIMENTION = " °/s";
    private boolean accelAvailable;
    private View[] acceleroOnlyGroup;
    private OnSeekBarChangeListener altitudeLimitSeekListener;
    private View btnCalibrateMagneto;
    private View btnDefaultSettings;
    private View btnFlatTrim;
    private View btnScrollLeft;
    private View btnScrollRight;
    private View[] clickButtons;
    private boolean connected;
    private View[] connectedOnlyGroup;
    private EditText editNetworkName;
    private OnEditorActionListener editNetworkNameActionListener;
    private boolean flying;
    private View[] flyingOnlyGroup;
    private OnCheckedChangeListener globalOnCheckedChangeListener;
    private OnSeekChangedListener globalOnSeekChangedListener;
    private TextView[] gpsViews;
    private int[] gpsViewsColors;
    private InputMethodManager inputManager;
    private OnSeekBarChangeListener interfaceOpacitySeekListener;
    private View[] landedOnlyGroup;
    private boolean magnetoAvailable;
    private View[] magnetoOnlyGroup;
    private TextView[] motorHardVersion = new TextView[]{null, null, null, null};
    private TextView[] motorSoftVersion = new TextView[]{null, null, null, null};
    private TextView[] motorType = new TextView[]{null, null, null, null};
    private boolean onWifi;
    private RadioButton rbBatteryTypeHd;
    private RadioButton rbBatteryTypeStd;
    private RadioButton rbFlipBackward;
    private RadioButton rbFlipForward;
    private RadioButton rbFlipLeft;
    private RadioButton rbFlipRight;
    private RadioButton rbVideoP264;
    private RadioButton rbVideoVLIB;
    private Resources res;
    private RadioGroup rgBatteryTypeGroup;
    private RadioGroup rgFlipOrientation;
    private RadioGroup rgVideoCodec;
    private SeekBar seekAltitudeLimit;
    private SeekBar seekDeviceTiltMax;
    private SeekBar seekInterfaceOpacity;
    private SeekBar seekTilt;
    private SeekBar seekVertSpeedMax;
    private SeekBar seekYawSpeedMax;
    private List<View> settingsViews;
    private OnSeekBarChangeListener tiltMaxSeekListener;
    private OnSeekBarChangeListener tiltSeekListener;
    private List<Integer> titles;
    private CheckBox toggleAbsoluteControl;
    private CheckBox toggleAutoRecord;
    private CheckBox[] toggleButtons;
    private CheckBox toggleFlipEnabled;
    private CheckBox toggleGpsFirmwareUpdateEnabled;
    private CheckBox toggleJoypadMode;
    private CheckBox toggleLeftHanded;
    private CheckBox toggleOutdoorFlight;
    private CheckBox toggleOutdoorHull;
    private CheckBox togglePairing;
    private CheckBox toggleVideoOnUsb;
    private TextView txtAltitudeLimit;
    private TextView txtDeviceTiltMaxValue;
    private TextView txtDroneHardVersion;
    private TextView txtDroneSoftVersion;
    private TextView txtGpsHardware;
    private TextView txtGpsHardwareLabel;
    private TextView txtGpsLabel;
    private TextView txtGpsSatellites;
    private TextView txtGpsSatellitesLabel;
    private TextView txtGpsSoftware;
    private TextView txtGpsSoftwareLabel;
    private TextView txtInertialHardVersion;
    private TextView txtInertialSoftVersion;
    private TextView txtInterfaceOpacityValue;
    private TextView txtRotationSpeedMax;
    private TextView txtTilt;
    private TextView txtTitle;
    private TextView txtVerticalSpeedMax;
    private OnSeekBarChangeListener vertSpeedMaxSeekListener;
    private ViewPager viewPager;
    private View[] wifiOnlyGroup;
    private OnSeekBarChangeListener yawSpeedMaxSeekListener;

    class C12181 implements OnSeekBarChangeListener {
        C12181() {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            SettingsViewController.this.safeSetText(SettingsViewController.this.txtDeviceTiltMaxValue, HttpVersions.HTTP_0_9 + (i + 5) + SettingsViewController.this.TILT_DIMENTION);
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (SettingsViewController.this.globalOnSeekChangedListener != null) {
                SettingsViewController.this.globalOnSeekChangedListener.onChanged(seekBar, seekBar.getProgress());
            }
        }
    }

    class C12192 implements OnSeekBarChangeListener {
        C12192() {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            SettingsViewController.this.safeSetText(SettingsViewController.this.txtInterfaceOpacityValue, HttpVersions.HTTP_0_9 + (i + 0) + SettingsViewController.this.INTERFACE_OPACITY_DIMENTION);
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (SettingsViewController.this.globalOnSeekChangedListener != null) {
                SettingsViewController.this.globalOnSeekChangedListener.onChanged(seekBar, seekBar.getProgress());
            }
        }
    }

    class C12203 implements OnSeekBarChangeListener {
        C12203() {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            SettingsViewController.this.safeSetText(SettingsViewController.this.txtRotationSpeedMax, HttpVersions.HTTP_0_9 + (i + 40) + SettingsViewController.this.YAW_SPEED_MAX_DIMENTION);
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (SettingsViewController.this.globalOnSeekChangedListener != null) {
                SettingsViewController.this.globalOnSeekChangedListener.onChanged(seekBar, seekBar.getProgress());
            }
        }
    }

    class C12214 implements OnSeekBarChangeListener {
        C12214() {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            SettingsViewController.this.safeSetText(SettingsViewController.this.txtVerticalSpeedMax, HttpVersions.HTTP_0_9 + (i + 200) + SettingsViewController.this.VERT_SPEED_MAX_DIMENTION);
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (SettingsViewController.this.globalOnSeekChangedListener != null) {
                SettingsViewController.this.globalOnSeekChangedListener.onChanged(seekBar, seekBar.getProgress());
            }
        }
    }

    class C12225 implements OnSeekBarChangeListener {
        C12225() {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            SettingsViewController.this.safeSetText(SettingsViewController.this.txtTilt, HttpVersions.HTTP_0_9 + (i + 5) + SettingsViewController.this.TILT_DIMENTION);
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (SettingsViewController.this.globalOnSeekChangedListener != null) {
                SettingsViewController.this.globalOnSeekChangedListener.onChanged(seekBar, seekBar.getProgress());
            }
        }
    }

    class C12236 implements OnSeekBarChangeListener {
        C12236() {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            SettingsViewController.this.safeSetText(SettingsViewController.this.txtAltitudeLimit, HttpVersions.HTTP_0_9 + (i + 3) + SettingsViewController.this.ALTITUDE_DIMENSION);
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (SettingsViewController.this.globalOnSeekChangedListener != null) {
                SettingsViewController.this.globalOnSeekChangedListener.onChanged(seekBar, seekBar.getProgress());
            }
        }
    }

    class C12247 implements OnEditorActionListener {
        C12247() {
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            boolean z = false;
            try {
                SettingsViewController.this.inputManager.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                SettingsViewController.this.setNetworkNameFocusable(false);
                if (SettingsViewController.this.editNetworkNameActionListener != null) {
                    z = SettingsViewController.this.editNetworkNameActionListener.onEditorAction(textView, i, keyEvent);
                }
            } catch (Exception e) {
            }
            return z;
        }
    }

    class C12258 implements OnTouchListener {
        C12258() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                view.setFocusableInTouchMode(true);
            }
            return false;
        }
    }

    public SettingsViewController(Context context, LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        this.res = context.getResources();
        this.inputManager = (InputMethodManager) context.getSystemService("input_method");
        this.TILT_DIMENTION = " " + context.getString(C0984R.string.degree_sign);
        this.YAW_SPEED_MAX_DIMENTION = " " + context.getString(C0984R.string.ae_ID000036);
        this.VERT_SPEED_MAX_DIMENTION = " " + context.getString(C0984R.string.ae_ID000034);
        this.ALTITUDE_DIMENSION = " " + context.getString(C0984R.string.meters);
        int[] iArr = null;
        int i = -1;
        int i2 = -1;
        int i3 = -1;
        int i4 = -1;
        int i5 = -1;
        int i6 = -1;
        if (ARDroneVersion.isArDrone1()) {
            this.titles = new ArrayList();
            this.titles.add(Integer.valueOf(C0984R.string.ae_ID000025));
            this.titles.add(Integer.valueOf(C0984R.string.ae_ID000031));
            this.titles.add(Integer.valueOf(C0984R.string.ae_ID000040));
            this.titles.add(Integer.valueOf(C0984R.string.ae_ID000056));
            this.titles.add(Integer.valueOf(C0984R.string.ae_ID000048));
            iArr = new int[]{C0984R.layout.settings_page_ardrone, C0984R.layout.settings_page_flight, C0984R.layout.settings_page_device, C0984R.layout.settings_page_video, C0984R.layout.settings_page_about};
            i = 0;
            i2 = 1;
            i4 = 2;
            i5 = 3;
            i6 = 4;
        } else if (ARDroneVersion.isLeastArDrone2()) {
            this.titles = new ArrayList();
            this.titles.add(Integer.valueOf(C0984R.string.ae_ID000025));
            this.titles.add(Integer.valueOf(C0984R.string.ae_ID000025));
            this.titles.add(Integer.valueOf(C0984R.string.ae_ID000031));
            this.titles.add(Integer.valueOf(C0984R.string.ae_ID000040));
            this.titles.add(Integer.valueOf(C0984R.string.ae_ID000048));
            iArr = new int[5];
            iArr[0] = C0984R.layout.ff2_settings_page_ardrone;
            iArr[1] = C0984R.layout.ff2_settings_page_flip;
            iArr[2] = C0984R.layout.settings_page_flight;
            iArr[3] = z ? C0984R.layout.ff2_settings_page_device : C0984R.layout.settings_page_device;
            iArr[4] = C0984R.layout.settings_page_about;
            i = 0;
            i3 = 1;
            i2 = 2;
            i4 = 3;
            i6 = 4;
        }
        this.settingsViews = initializePages(layoutInflater, iArr);
        Object obj = null;
        if (((View) this.settingsViews.get(i)).findViewById(C0984R.id.toggleLoopingEnabled) != null) {
            obj = 1;
        }
        if (obj != null) {
            this.settingsViews.remove(i3);
            this.titles.remove(i3);
            i = 0;
            i2 = 1;
            i4 = 2;
            i6 = 3;
        }
        TextView textView = (TextView) ((View) this.settingsViews.get(i2 + 1)).findViewById(C0984R.id.labelDeviceTiltMax);
        String charSequence = textView.getText().toString();
        if (charSequence.indexOf("%s") != -1) {
            textView.setText(charSequence.replace("%s", Build.MANUFACTURER.toUpperCase()));
        }
        this.toggleJoypadMode = (CheckBox) ((View) this.settingsViews.get(i4)).findViewById(C0984R.id.toggleAcceleroDisabled);
        this.toggleAbsoluteControl = (CheckBox) ((View) this.settingsViews.get(i4)).findViewById(C0984R.id.toggleAbsoluteControl);
        this.toggleLeftHanded = (CheckBox) ((View) this.settingsViews.get(i4)).findViewById(C0984R.id.toggleLeftHanded);
        this.rgBatteryTypeGroup = (RadioGroup) ((View) this.settingsViews.get(i4)).findViewById(C0984R.id.settings_radiogroup_battery_type_ref);
        this.rbBatteryTypeStd = (RadioButton) ((View) this.settingsViews.get(i4)).findViewById(C0984R.id.settings_radioitem_battery_type_std);
        this.rbBatteryTypeHd = (RadioButton) ((View) this.settingsViews.get(i4)).findViewById(C0984R.id.settings_radioitem_battery_type_hd);
        this.toggleVideoOnUsb = (CheckBox) ((View) this.settingsViews.get(i)).findViewById(C0984R.id.toggleUsbRecord);
        this.toggleGpsFirmwareUpdateEnabled = (CheckBox) ((View) this.settingsViews.get(i)).findViewById(C0984R.id.toggleGpsFirmwareUpdateEnabled);
        if (obj != null) {
            i3 = i;
        }
        if (i3 != -1) {
            this.toggleFlipEnabled = (CheckBox) ((View) this.settingsViews.get(i3)).findViewById(C0984R.id.toggleLoopingEnabled);
            this.rgFlipOrientation = (RadioGroup) ((View) this.settingsViews.get(i3)).findViewById(C0984R.id.settings_radiogroup_flip_direction_ref);
            this.rbFlipLeft = (RadioButton) ((View) this.settingsViews.get(i3)).findViewById(C0984R.id.settings_radioitem_flip_left);
            this.rbFlipRight = (RadioButton) ((View) this.settingsViews.get(i3)).findViewById(C0984R.id.settings_radioitem_flip_right);
            this.rbFlipForward = (RadioButton) ((View) this.settingsViews.get(i3)).findViewById(C0984R.id.settings_radioitem_flip_front);
            this.rbFlipBackward = (RadioButton) ((View) this.settingsViews.get(i3)).findViewById(C0984R.id.settings_radioitem_flip_back);
        }
        this.toggleOutdoorFlight = (CheckBox) ((View) this.settingsViews.get(i2)).findViewById(C0984R.id.toggleOutdoorFlight);
        this.toggleOutdoorHull = (CheckBox) ((View) this.settingsViews.get(i2)).findViewById(C0984R.id.toggleOutdoorHull);
        this.togglePairing = (CheckBox) ((View) this.settingsViews.get(i)).findViewById(C0984R.id.togglePairing);
        this.toggleAutoRecord = (CheckBox) ((View) this.settingsViews.get(i)).findViewById(C0984R.id.toggleAutoRecord);
        this.toggleButtons = new CheckBox[]{this.toggleJoypadMode, this.toggleAbsoluteControl, this.toggleLeftHanded, this.togglePairing, this.toggleVideoOnUsb, this.toggleGpsFirmwareUpdateEnabled, this.toggleFlipEnabled, this.toggleOutdoorFlight, this.toggleOutdoorHull, this.toggleAutoRecord};
        this.btnScrollLeft = viewGroup.findViewById(C0984R.id.btnPrev);
        this.btnScrollLeft.setOnClickListener(this);
        this.btnScrollRight = viewGroup.findViewById(C0984R.id.btnNext);
        this.btnScrollRight.setOnClickListener(this);
        this.btnCalibrateMagneto = (Button) ((View) this.settingsViews.get(i4)).findViewById(C0984R.id.btnCalibration);
        this.btnDefaultSettings = viewGroup.findViewById(C0984R.id.btnDefaultSettings);
        this.btnFlatTrim = viewGroup.findViewById(C0984R.id.btnFlatTrim);
        View findViewById = viewGroup.findViewById(C0984R.id.btnBack);
        this.btnCalibrateMagneto = ((View) this.settingsViews.get(i4)).findViewById(C0984R.id.btnCalibration);
        this.clickButtons = new View[]{r2, r7, findViewById, r0};
        if (i5 != -1) {
            this.rgVideoCodec = (RadioGroup) ((View) this.settingsViews.get(i5)).findViewById(C0984R.id.rgVideoCodec);
            this.rbVideoP264 = (RadioButton) ((View) this.settingsViews.get(i5)).findViewById(C0984R.id.rbVideoP264);
            this.rbVideoVLIB = (RadioButton) ((View) this.settingsViews.get(i5)).findViewById(C0984R.id.rbVideoVLIB);
        }
        this.txtTitle = (TextView) viewGroup.findViewById(C0984R.id.txtTitle);
        this.txtDeviceTiltMaxValue = (TextView) ((View) this.settingsViews.get(i4)).findViewById(C0984R.id.txtDeviceTiltMax);
        this.txtInterfaceOpacityValue = (TextView) ((View) this.settingsViews.get(i)).findViewById(C0984R.id.textInterfaceOpacityValue);
        this.txtRotationSpeedMax = (TextView) ((View) this.settingsViews.get(i2)).findViewById(C0984R.id.txtYawSpeedMax);
        this.txtVerticalSpeedMax = (TextView) ((View) this.settingsViews.get(i2)).findViewById(C0984R.id.txtVerticalSpeedMax);
        this.txtTilt = (TextView) ((View) this.settingsViews.get(i2)).findViewById(C0984R.id.txtTiltMax);
        this.txtAltitudeLimit = (TextView) ((View) this.settingsViews.get(i2)).findViewById(C0984R.id.txtAltitudeLimit);
        this.txtDroneSoftVersion = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.textDroneSoftwareVersion);
        this.txtDroneHardVersion = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.textDroneHardwareVersion);
        this.txtInertialHardVersion = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.textInertialHardware);
        this.txtInertialSoftVersion = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.textInertialSoftwareVersion);
        this.txtGpsLabel = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.textGpsLabel);
        this.txtGpsHardwareLabel = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.textGpsHardwareLabel);
        this.txtGpsHardware = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.textGpsHardware);
        this.txtGpsSoftwareLabel = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.textGpsSoftwareLabel);
        this.txtGpsSoftware = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.textGpsSoftware);
        this.txtGpsSatellitesLabel = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.textGpsSatellitesLabel);
        this.txtGpsSatellites = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.textGpsSatellites);
        this.gpsViews = new TextView[]{this.txtGpsLabel, this.txtGpsHardwareLabel, this.txtGpsHardware, this.txtGpsSoftwareLabel, this.txtGpsSoftware, this.txtGpsSatellitesLabel, this.txtGpsSatellites};
        this.gpsViewsColors = new int[this.gpsViews.length];
        TextView[] textViewArr = this.gpsViews;
        int length = textViewArr.length;
        int i7 = 0;
        i5 = 0;
        while (i7 < length) {
            this.gpsViewsColors[i5] = textViewArr[i7].getCurrentTextColor();
            i7++;
            i5++;
        }
        TextView[] textViewArr2 = new TextView[]{null, null, null, null};
        textViewArr2[0] = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.motor1_title);
        textViewArr2[1] = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.motor2_title);
        textViewArr2[2] = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.motor3_title);
        textViewArr2[3] = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.motor4_title);
        safeSetText(textViewArr2[0], String.format(context.getString(C0984R.string.ae_ID000054), new Object[]{Integer.valueOf(1)}));
        safeSetText(textViewArr2[1], String.format(context.getString(C0984R.string.ae_ID000054), new Object[]{Integer.valueOf(2)}));
        safeSetText(textViewArr2[2], String.format(context.getString(C0984R.string.ae_ID000054), new Object[]{Integer.valueOf(3)}));
        safeSetText(textViewArr2[3], String.format(context.getString(C0984R.string.ae_ID000054), new Object[]{Integer.valueOf(4)}));
        this.motorType[0] = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.textMotor1Type);
        this.motorType[1] = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.textMotor2Type);
        this.motorType[2] = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.textMotor3Type);
        this.motorType[3] = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.textMotor4Type);
        this.motorSoftVersion[0] = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.textMotor1Software);
        this.motorSoftVersion[1] = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.textMotor2Software);
        this.motorSoftVersion[2] = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.textMotor3Software);
        this.motorSoftVersion[3] = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.textMotor4Software);
        this.motorHardVersion[0] = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.textMotor1Hardware);
        this.motorHardVersion[1] = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.textMotor2Hardware);
        this.motorHardVersion[2] = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.textMotor3Hardware);
        this.motorHardVersion[3] = (TextView) ((View) this.settingsViews.get(i6)).findViewById(C0984R.id.textMotor4Hardware);
        this.editNetworkName = (EditText) ((View) this.settingsViews.get(i)).findViewById(C0984R.id.editNetworkName);
        if (Build.MODEL.equals("Kindle Fire")) {
            this.editNetworkName.setImeOptions(2);
        }
        this.seekDeviceTiltMax = (SeekBar) ((View) this.settingsViews.get(i4)).findViewById(C0984R.id.seekDeviceTiltMax);
        this.seekInterfaceOpacity = (SeekBar) ((View) this.settingsViews.get(i)).findViewById(C0984R.id.seekInterfaceOpacity);
        this.seekYawSpeedMax = (SeekBar) ((View) this.settingsViews.get(i2)).findViewById(C0984R.id.seekYawSpeedMax);
        this.seekVertSpeedMax = (SeekBar) ((View) this.settingsViews.get(i2)).findViewById(C0984R.id.seekVerticalSpeedMax);
        this.seekTilt = (SeekBar) ((View) this.settingsViews.get(i2)).findViewById(C0984R.id.seekTiltMax);
        this.seekAltitudeLimit = (SeekBar) ((View) this.settingsViews.get(i2)).findViewById(C0984R.id.seekAltitudeLimit);
        if (this.seekDeviceTiltMax != null) {
            this.seekDeviceTiltMax.setMax(45);
        }
        if (this.seekInterfaceOpacity != null) {
            this.seekInterfaceOpacity.setMax(100);
        }
        if (this.seekYawSpeedMax != null) {
            this.seekYawSpeedMax.setMax(310);
        }
        if (this.seekVertSpeedMax != null) {
            this.seekVertSpeedMax.setMax(1800);
        }
        if (this.seekTilt != null) {
            this.seekTilt.setMax(25);
        }
        if (this.seekAltitudeLimit != null) {
            this.seekAltitudeLimit.setMax(97);
        }
        this.acceleroOnlyGroup = new View[]{this.toggleJoypadMode, this.seekDeviceTiltMax};
        this.magnetoOnlyGroup = new View[]{this.toggleAbsoluteControl};
        this.flyingOnlyGroup = new View[]{this.btnCalibrateMagneto};
        this.landedOnlyGroup = new View[]{this.btnFlatTrim};
        this.wifiOnlyGroup = new View[]{this.togglePairing};
        this.connectedOnlyGroup = new View[]{this.btnDefaultSettings, this.btnFlatTrim, this.btnCalibrateMagneto, this.togglePairing, this.toggleVideoOnUsb, this.toggleAutoRecord, this.toggleOutdoorFlight, this.toggleOutdoorHull, this.seekAltitudeLimit, this.seekTilt, this.seekVertSpeedMax, this.seekYawSpeedMax};
        initListeners();
        hideTouchScreenOnlyOptions(context, i, i4);
        this.viewPager = (ViewPager) viewGroup.findViewById(C0984R.id.viewPager);
        this.viewPager.setAdapter(new SettingsViewAdapter(this.settingsViews));
        ViewPagerIndicator viewPagerIndicator = (ViewPagerIndicator) viewGroup.findViewById(C0984R.id.pageIndicator);
        viewPagerIndicator.setViewPager(this.viewPager);
        viewPagerIndicator.setOnPageChangeListener(this);
    }

    private void hideTouchScreenOnlyOptions(Context context, int i, int i2) {
        if (!SystemUtils.hasTouchScreen(context)) {
            if (!SystemUtils.isMoverioGlasses()) {
                ((View) this.settingsViews.get(i)).findViewById(C0984R.id.settings_page_ardrone_label_interfaceOpacity).setVisibility(8);
                ((View) this.settingsViews.get(i)).findViewById(C0984R.id.textInterfaceOpacityValue).setVisibility(8);
                this.seekInterfaceOpacity.setVisibility(8);
            }
            ((View) this.settingsViews.get(i2)).findViewById(C0984R.id.settings_label_joypadMode).setVisibility(8);
            this.toggleJoypadMode.setVisibility(8);
        }
    }

    private void initListeners() {
        this.tiltMaxSeekListener = new C12181();
        this.interfaceOpacitySeekListener = new C12192();
        this.yawSpeedMaxSeekListener = new C12203();
        this.vertSpeedMaxSeekListener = new C12214();
        this.tiltSeekListener = new C12225();
        this.altitudeLimitSeekListener = new C12236();
    }

    private List<View> initializePages(LayoutInflater layoutInflater, int[] iArr) {
        List arrayList = new ArrayList(iArr.length);
        for (int inflate : iArr) {
            View inflate2 = layoutInflater.inflate(inflate, null);
            FontUtils.applyFont(layoutInflater.getContext(), (ViewGroup) inflate2);
            arrayList.add(inflate2);
        }
        return arrayList;
    }

    private void safeSetText(TextView textView, String str) {
        if (textView != null) {
            textView.setText(str);
        }
    }

    private void setGpsViewsColor(int i) {
        TextView[] textViewArr = this.gpsViews;
        int length = textViewArr.length;
        int i2 = 0;
        int i3 = 0;
        while (i3 < length) {
            textViewArr[i3].setTextColor(i == -1 ? this.gpsViewsColors[i2] : i);
            i3++;
            i2++;
        }
    }

    private void setGroupEnabled(View[] viewArr, boolean z, boolean z2) {
        for (View view : viewArr) {
            if (view != null) {
                if (z2 && !z && view.isEnabled()) {
                    view.setEnabled(z);
                } else if (!z2) {
                    view.setEnabled(z);
                }
            }
        }
    }

    private void setGroupVisible(View[] viewArr, boolean z) {
        for (View view : viewArr) {
            if (view != null) {
                view.setVisibility(z ? 0 : 8);
            }
        }
    }

    public void disableControlsThatRequireDroneConnection() {
        setGroupEnabled(this.connectedOnlyGroup, false, false);
    }

    public void enableAvailableSettings() {
        boolean z = false;
        setGroupEnabled(this.connectedOnlyGroup, this.connected, false);
        setGroupEnabled(this.wifiOnlyGroup, this.onWifi, true);
        View[] viewArr = this.landedOnlyGroup;
        if (!this.flying) {
            z = true;
        }
        setGroupEnabled(viewArr, z, true);
        setGroupEnabled(this.flyingOnlyGroup, this.flying, true);
        setGroupEnabled(this.acceleroOnlyGroup, this.accelAvailable, true);
        setGroupEnabled(this.magnetoOnlyGroup, this.magnetoAvailable, true);
        setGroupVisible(this.magnetoOnlyGroup, this.magnetoAvailable);
    }

    public int getAltitudeLimit() {
        return this.seekAltitudeLimit.getProgress() + 3;
    }

    public int getInterfaceOpacity() {
        return this.seekInterfaceOpacity.getProgress() + 0;
    }

    public String getNetworkName() {
        return this.editNetworkName.getText().toString();
    }

    public int getTilt() {
        return this.seekTilt != null ? this.seekTilt.getProgress() + 5 : 5;
    }

    public int getTiltMax() {
        return this.seekDeviceTiltMax.getProgress() + 5;
    }

    public int getVerticalSpeedMax() {
        return this.seekVertSpeedMax != null ? this.seekVertSpeedMax.getProgress() + 200 : 200;
    }

    public int getYawSpeedMax() {
        return this.seekYawSpeedMax != null ? this.seekYawSpeedMax.getProgress() + 40 : 40;
    }

    public boolean isAbsoluteControlChecked() {
        return this.toggleAbsoluteControl.isChecked();
    }

    public boolean isAcceleroDisabledChecked() {
        if (this.toggleJoypadMode != null) {
            return this.toggleJoypadMode.isChecked();
        }
        Log.w(TAG, "Toggle button toggleAccelero is null");
        return false;
    }

    public boolean isAceModeChecked() {
        return false;
    }

    public boolean isAdapriveVideoChecked() {
        return false;
    }

    public boolean isLeftHandedChecked() {
        if (this.toggleLeftHanded != null) {
            return this.toggleLeftHanded.isChecked();
        }
        Log.w(TAG, "Toggle button toggleLeftHanded is null");
        return false;
    }

    public boolean isOutdoorFlightChecked() {
        return this.toggleOutdoorFlight.isChecked();
    }

    public boolean isOutdoorHullChecked() {
        return this.toggleOutdoorHull.isChecked();
    }

    public boolean isPairingChecked() {
        return this.togglePairing.isChecked();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0984R.id.btnPrev /*2131362105*/:
                this.viewPager.setCurrentItem(this.viewPager.getCurrentItem() - 1, true);
                return;
            case C0984R.id.btnNext /*2131362106*/:
                this.viewPager.setCurrentItem(this.viewPager.getCurrentItem() + 1, true);
                return;
            default:
                return;
        }
    }

    public void onPageScrollStateChanged(int i) {
    }

    public void onPageScrolled(int i, float f, int i2) {
    }

    public void onPageSelected(int i) {
        if (i == 0 && this.btnScrollLeft.getVisibility() != 4) {
            this.btnScrollLeft.setVisibility(4);
        } else if (this.btnScrollLeft.getVisibility() != 0) {
            this.btnScrollLeft.setVisibility(0);
        }
        if (this.btnScrollRight.getVisibility() != 4 && i == this.viewPager.getAdapter().getCount() - 1) {
            this.btnScrollRight.setVisibility(4);
        } else if (this.btnScrollRight.getVisibility() != 0) {
            this.btnScrollRight.setVisibility(0);
        }
        this.txtTitle.setText(this.res.getString(((Integer) this.titles.get(i)).intValue()));
        if (this.editNetworkName != null) {
            ((InputMethodManager) this.editNetworkName.getContext().getSystemService("input_method")).hideSoftInputFromWindow(this.editNetworkName.getWindowToken(), 0);
        }
    }

    public void setAbsoluteControlChecked(boolean z) {
        if (this.toggleAbsoluteControl != null) {
            this.toggleAbsoluteControl.setChecked(z);
        }
    }

    public void setAcceleroAvailable(boolean z) {
        this.accelAvailable = z;
    }

    public void setAcceleroDisabledChecked(boolean z) {
        if (this.toggleJoypadMode != null) {
            this.toggleJoypadMode.setChecked(z);
        }
    }

    public void setAcceleroDisabledEnabled(boolean z) {
        if (this.toggleJoypadMode != null) {
            this.toggleJoypadMode.setEnabled(z);
        }
    }

    public void setAceMode(boolean z) {
    }

    public void setAceModeEnabled(boolean z) {
    }

    public void setAdaptiveVideo(boolean z) {
    }

    public void setAltitudeLimit(int i) {
        if (i < 3 || i > 100) {
            i = i < 3 ? 3 : 100;
        }
        if (this.txtAltitudeLimit != null) {
            this.txtAltitudeLimit.setText(HttpVersions.HTTP_0_9 + i + this.ALTITUDE_DIMENSION);
        }
        if (this.seekAltitudeLimit != null) {
            this.seekAltitudeLimit.setOnSeekBarChangeListener(null);
            this.seekAltitudeLimit.setProgress(i - 3);
            this.seekAltitudeLimit.setOnSeekBarChangeListener(this.altitudeLimitSeekListener);
        }
    }

    public void setAutoRecord(boolean z) {
        if (this.toggleAutoRecord != null) {
            this.toggleAutoRecord.setChecked(z);
        }
    }

    public void setBatteryType(boolean z) {
        if (z) {
            this.rbBatteryTypeHd.setChecked(true);
        } else {
            this.rbBatteryTypeStd.setChecked(true);
        }
    }

    public void setButtonsEnabled(boolean z) {
        this.editNetworkName.setEnabled(z);
        for (View view : this.clickButtons) {
            if (!(view == null || view.getId() == C0984R.id.btnCalibration)) {
                view.setEnabled(z);
            }
        }
    }

    public void setButtonsOnClickListener(OnClickListener onClickListener) {
        for (View view : this.clickButtons) {
            if (view != null) {
                view.setOnClickListener(onClickListener);
            }
        }
    }

    public void setConnected(boolean z) {
        this.connected = z;
    }

    public void setDroneVersion(String str, String str2) {
        safeSetText(this.txtDroneHardVersion, str);
        safeSetText(this.txtDroneSoftVersion, str2);
    }

    public void setFlipEnabled(boolean z) {
        if (this.toggleFlipEnabled != null) {
            this.toggleFlipEnabled.setChecked(z);
        }
    }

    public void setFlipOrientation(DroneFlipDirection droneFlipDirection) {
        switch (droneFlipDirection) {
            case LEFT:
                if (this.rbFlipLeft != null) {
                    this.rbFlipLeft.setChecked(true);
                    return;
                }
                return;
            case RIGHT:
                if (this.rbFlipRight != null) {
                    this.rbFlipRight.setChecked(true);
                    return;
                }
                return;
            case FRONT:
                if (this.rbFlipForward != null) {
                    this.rbFlipForward.setChecked(true);
                    return;
                }
                return;
            case BACK:
                if (this.rbFlipBackward != null) {
                    this.rbFlipBackward.setChecked(true);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void setFlying(boolean z) {
        this.flying = z;
    }

    public void setGpsFirmwareUpdateEnabled(boolean z) {
        if (this.toggleGpsFirmwareUpdateEnabled != null) {
            this.toggleGpsFirmwareUpdateEnabled.setChecked(z);
        }
    }

    public void setGpsPlugged(boolean z) {
        setGpsViewsColor(z ? -1 : -5592406);
    }

    public void setGpsSatellites(int i) {
        safeSetText(this.txtGpsSatellites, Integer.toString(i));
    }

    public void setGpsVersion(String str, String str2) {
        safeSetText(this.txtGpsHardware, str);
        safeSetText(this.txtGpsSoftware, str2);
    }

    public void setInertialVersion(String str, String str2) {
        if (str != null) {
            TextView textView = this.txtInertialHardVersion;
            if (str.length() <= 0) {
                str = "0.0";
            }
            safeSetText(textView, str);
        } else {
            this.txtInertialHardVersion.setText("0.0");
        }
        if (str2 != null) {
            textView = this.txtInertialSoftVersion;
            if (str2.length() <= 0) {
                str2 = "0.0";
            }
            safeSetText(textView, str2);
            return;
        }
        this.txtInertialHardVersion.setText("0.0");
    }

    public void setInterfaceOpacity(int i) {
        if (i < 0 || i > 100) {
            throw new IllegalArgumentException();
        }
        if (this.txtInterfaceOpacityValue != null) {
            this.txtInterfaceOpacityValue.setText(HttpVersions.HTTP_0_9 + i + this.INTERFACE_OPACITY_DIMENTION);
        }
        if (this.seekInterfaceOpacity != null) {
            this.seekInterfaceOpacity.setOnSeekBarChangeListener(null);
            this.seekInterfaceOpacity.setProgress(i + 0);
            this.seekInterfaceOpacity.setOnSeekBarChangeListener(this.interfaceOpacitySeekListener);
        }
    }

    public void setIsOnWifi(boolean z) {
        this.onWifi = z;
    }

    public void setLeftHandedChecked(boolean z) {
        if (this.toggleLeftHanded != null) {
            this.toggleLeftHanded.setChecked(z);
        }
    }

    public void setMagnetoAvailable(boolean z) {
        this.magnetoAvailable = z;
    }

    public void setMotorVersion(int i, String str, String str2, String str3) {
        safeSetText(this.motorType[i], str);
        safeSetText(this.motorHardVersion[i], str2);
        safeSetText(this.motorSoftVersion[i], str3);
    }

    public void setNetworkName(String str) {
        this.editNetworkName.setText(str);
    }

    public void setNetworkNameFocusable(boolean z) {
        this.editNetworkName.setFocusableInTouchMode(z);
        if (!z) {
            this.editNetworkName.clearFocus();
        }
    }

    public void setNetworkNameOnEditorActionListener(OnEditorActionListener onEditorActionListener) {
        this.editNetworkNameActionListener = onEditorActionListener;
        this.editNetworkName.setOnEditorActionListener(new C12247());
        this.editNetworkName.setOnTouchListener(new C12258());
    }

    public void setOutdoorFlight(boolean z) {
        this.toggleOutdoorFlight.setChecked(z);
    }

    public void setOutdoorFlightControlsEnabled(boolean z) {
        if (this.connected) {
            this.toggleOutdoorFlight.setEnabled(z);
            this.seekYawSpeedMax.setEnabled(z);
            this.seekVertSpeedMax.setEnabled(z);
            this.seekTilt.setEnabled(z);
        }
    }

    public void setOutdoorHull(boolean z) {
        this.toggleOutdoorHull.setChecked(z);
    }

    public void setPairing(boolean z) {
        this.togglePairing.setChecked(z);
    }

    public void setRadioButtonsCheckedListener(RadioGroup.OnCheckedChangeListener onCheckedChangeListener) {
        if (this.rgVideoCodec != null) {
            this.rgVideoCodec.setOnCheckedChangeListener(onCheckedChangeListener);
        }
        if (this.rgFlipOrientation != null) {
            this.rgFlipOrientation.setOnCheckedChangeListener(onCheckedChangeListener);
        }
        if (this.rgBatteryTypeGroup != null) {
            this.rgBatteryTypeGroup.setOnCheckedChangeListener(onCheckedChangeListener);
        }
    }

    public void setRecordOnUsb(boolean z) {
        if (this.toggleVideoOnUsb != null) {
            this.toggleVideoOnUsb.setChecked(z);
        }
    }

    public void setSeekBarsOnChangeListener(OnSeekChangedListener onSeekChangedListener) {
        this.globalOnSeekChangedListener = onSeekChangedListener;
    }

    public void setTilt(int i) {
        if (i < 5 || i > 30) {
            i = i < 5 ? 5 : 30;
        }
        safeSetText(this.txtTilt, HttpVersions.HTTP_0_9 + i + this.TILT_DIMENTION);
        if (this.seekTilt != null) {
            this.seekTilt.setOnSeekBarChangeListener(null);
            this.seekTilt.setProgress(i - 5);
            this.seekTilt.setOnSeekBarChangeListener(this.tiltSeekListener);
        }
    }

    public void setTiltMax(int i) {
        if (i < 5 || i > 50) {
        }
        if (this.txtDeviceTiltMaxValue != null) {
            this.txtDeviceTiltMaxValue.setText(HttpVersions.HTTP_0_9 + i + this.TILT_DIMENTION);
        }
        if (this.seekDeviceTiltMax != null) {
            this.seekDeviceTiltMax.setOnSeekBarChangeListener(null);
            this.seekDeviceTiltMax.setProgress(i - 5);
            this.seekDeviceTiltMax.setOnSeekBarChangeListener(this.tiltMaxSeekListener);
        }
    }

    public void setToggleButtonsCheckedListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.globalOnCheckedChangeListener = onCheckedChangeListener;
        for (CheckBox checkBox : this.toggleButtons) {
            if (checkBox != null) {
                checkBox.setOnCheckedChangeListener(this.globalOnCheckedChangeListener);
            }
        }
    }

    public void setVerticalSpeedMax(int i) {
        if (i < 200 || i > 2000) {
            i = i < 200 ? 200 : 2000;
        }
        safeSetText(this.txtVerticalSpeedMax, HttpVersions.HTTP_0_9 + i + this.VERT_SPEED_MAX_DIMENTION);
        if (this.seekVertSpeedMax != null) {
            this.seekVertSpeedMax.setOnSeekBarChangeListener(null);
            this.seekVertSpeedMax.setProgress(i - 200);
            this.seekVertSpeedMax.setOnSeekBarChangeListener(this.vertSpeedMaxSeekListener);
        }
    }

    public void setVideoP264Checked(boolean z) {
        if (this.rbVideoP264 != null) {
            this.rbVideoP264.setChecked(z);
        }
    }

    public void setVideoVLIBChecked(boolean z) {
        if (this.rbVideoVLIB != null) {
            this.rbVideoVLIB.setChecked(z);
        }
    }

    public void setYawSpeedMax(int i) {
        if (i < 40 || i > DroneConfig.YAW_MAX) {
            Log.e(TAG, "Yaw exceeds bounds. Yaw: " + i);
            i = i > DroneConfig.YAW_MAX ? DroneConfig.YAW_MAX : 40;
        }
        safeSetText(this.txtRotationSpeedMax, HttpVersions.HTTP_0_9 + i + this.YAW_SPEED_MAX_DIMENTION);
        if (this.seekYawSpeedMax != null) {
            this.seekYawSpeedMax.setOnSeekBarChangeListener(null);
            this.seekYawSpeedMax.setProgress(i - 40);
            this.seekYawSpeedMax.setOnSeekBarChangeListener(this.yawSpeedMaxSeekListener);
        }
    }
}
