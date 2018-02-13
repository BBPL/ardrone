package com.parrot.freeflight.activities;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.parrot.ardronetool.ARDroneVersion;
import com.parrot.ardronetool.DataTracker;
import com.parrot.ardronetool.NavDataGps;
import com.parrot.ardronetool.academynavdata.BatteryType;
import com.parrot.ardronetool.tracking.TRACK_KEY_ENUM;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.FreeFlightApplication;
import com.parrot.freeflight.receivers.DroneConfigChangedReceiver;
import com.parrot.freeflight.receivers.DroneConfigChangedReceiverDelegate;
import com.parrot.freeflight.receivers.GpsParamsChangeReceiver;
import com.parrot.freeflight.receivers.GpsParamsChangeReceiverDelegate;
import com.parrot.freeflight.service.DroneConfig;
import com.parrot.freeflight.service.DroneControlService;
import com.parrot.freeflight.service.DroneControlService.DroneFlipDirection;
import com.parrot.freeflight.settings.ApplicationSettings;
import com.parrot.freeflight.settings.ApplicationSettings.ControlMode;
import com.parrot.freeflight.settings.ApplicationSettings.EAppSettingProperty;
import com.parrot.freeflight.ui.SettingsDialogDelegate;
import com.parrot.freeflight.ui.SettingsViewController;
import com.parrot.freeflight.ui.listeners.OnSeekChangedListener;
import com.parrot.freeflight.utils.FontUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsDialog extends DialogFragment implements OnCheckedChangeListener, OnSeekChangedListener, OnEditorActionListener, RadioGroup.OnCheckedChangeListener, OnClickListener, DroneConfigChangedReceiverDelegate, GpsParamsChangeReceiverDelegate {
    private static final String NULL_MAC = "00:00:00:00:00:00";
    public static final int RESULT_CLOSE_APP = 1;
    public static final int RESULT_OK = 0;
    private static final String TAG = SettingsDialog.class.getSimpleName();
    private boolean acceleroAvailable;
    private ApplicationSettings appSettings;
    private DroneConfigChangedReceiver configChangedReceiver;
    private Context context;
    private SettingsDialogDelegate delegate;
    private DroneConfig droneSettings;
    private BroadcastReceiver gpsParamsChangedReceiver;
    private AsyncTask<ApplicationSettings, Integer, Boolean> loadSettingsTask;
    private DroneControlService mService;
    private boolean magnetoAvailable;
    private String ownerMac;
    private SettingsViewController view;

    class C11051 extends AsyncTask<Integer, Integer, Boolean> {
        C11051() {
        }

        protected Boolean doInBackground(Integer... numArr) {
            SettingsDialog.this.appSettings.setInterfaceOpacity(50);
            if (SettingsDialog.this.appSettings.isCombinedControlForced()) {
                SettingsDialog.this.appSettings.setControlMode(ControlMode.ACE_MODE);
            } else if (SettingsDialog.this.acceleroAvailable) {
                SettingsDialog.this.appSettings.setControlMode(ControlMode.ACCELERO_MODE);
            } else {
                SettingsDialog.this.appSettings.setControlMode(ControlMode.NORMAL_MODE);
            }
            SettingsDialog.this.appSettings.setFlipEnabled(false);
            SettingsDialog.this.appSettings.setDroneFlipDirection(DroneFlipDirection.LEFT);
            SettingsDialog.this.appSettings.setAbsoluteControlEnabled(false);
            SettingsDialog.this.appSettings.setLeftHanded(false);
            SettingsDialog.this.appSettings.setAutorecordEnabled(true);
            SettingsDialog.this.mService.setAutoRecordEnabled(true);
            SettingsDialog.this.droneSettings.setRecordOnUsb(true);
            SettingsDialog.this.mService.resetConfigToDefaults();
            return Boolean.TRUE;
        }

        protected void onPostExecute(Boolean bool) {
            SettingsDialog.this.fillUiControls();
            SettingsDialog.this.mService.requestConfigUpdate();
        }
    }

    class C11062 implements DialogInterface.OnClickListener {
        C11062() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }

    class C11073 implements DialogInterface.OnClickListener {
        C11073() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }

    public SettingsDialog(Context context, SettingsDialogDelegate settingsDialogDelegate, DroneControlService droneControlService, boolean z) {
        this.delegate = settingsDialogDelegate;
        this.magnetoAvailable = z;
        this.mService = droneControlService;
        this.context = context;
    }

    private void initListeners() {
        this.view.setToggleButtonsCheckedListener(this);
        this.view.setSeekBarsOnChangeListener(this);
        this.view.setNetworkNameOnEditorActionListener(this);
        this.view.setRadioButtonsCheckedListener(this);
        this.view.setButtonsOnClickListener(this);
    }

    private void loadSettings() {
        this.appSettings = ((FreeFlightApplication) this.mService.getApplicationContext()).getAppSettings();
        fillUiControls();
        this.view.enableAvailableSettings();
        initListeners();
    }

    private void onCalibrate() {
        this.mService.calibrateMagneto();
    }

    public void enableAvailableSettings() {
        this.view.enableAvailableSettings();
    }

    protected void fillUiControls() {
        this.view.setLeftHandedChecked(this.appSettings.isLeftHanded());
        if (this.appSettings.isCombinedControlForced()) {
            this.view.setAceMode(true);
            this.view.setAceModeEnabled(false);
        }
        ControlMode controlMode = this.appSettings.getControlMode();
        this.view.setAceMode(controlMode == ControlMode.ACE_MODE);
        SettingsViewController settingsViewController = this.view;
        boolean z = (controlMode == ControlMode.ACCELERO_MODE || controlMode == ControlMode.ACE_MODE) ? false : true;
        settingsViewController.setAcceleroDisabledChecked(z);
        this.view.setAcceleroDisabledEnabled(controlMode != ControlMode.ACE_MODE);
        this.view.setAbsoluteControlChecked(this.appSettings.isAbsoluteControlEnabled());
        this.view.setFlipEnabled(this.appSettings.isFlipEnabled());
        this.view.setFlipOrientation(this.appSettings.getFlipDirection());
        this.view.setBatteryType(this.droneSettings.getBatteryType() == BatteryType.Type_1500MA);
        this.view.setInterfaceOpacity(this.appSettings.getInterfaceOpacity());
        this.view.setAutoRecord(this.appSettings.isAutorecordEnabled());
        this.view.setGpsFirmwareUpdateEnabled(this.appSettings.isGpsFirmwareUpdateEnabled());
        this.view.setYawSpeedMax(40);
        this.view.setVerticalSpeedMax(200);
        this.view.setTilt(5);
        if (this.mService != null) {
            this.droneSettings = this.mService.getDroneConfig();
            if (this.droneSettings != null) {
                this.view.setDroneVersion(this.droneSettings.getHardwareVersion(), this.droneSettings.getSoftwareVersion());
                this.view.setGpsVersion(this.droneSettings.getGpsHardware(), this.droneSettings.getGpsSoftware());
                NavDataGps navDataGps = this.mService.getDroneEngine().getNavData().getNavDataGps();
                this.view.setGpsPlugged(navDataGps.isGpsPlugged());
                this.view.setGpsSatellites(navDataGps.getGpsSatsUsed());
                this.view.setRecordOnUsb(this.droneSettings.isRecordOnUsb());
                this.view.setInertialVersion(this.droneSettings.getInertialHardwareVersion(), this.droneSettings.getInertialSoftwareVersion());
                this.view.setMotorVersion(0, this.droneSettings.getMotor1Vendor(), this.droneSettings.getMotor1HardVersion(), this.droneSettings.getMotor1SoftVersion());
                this.view.setMotorVersion(1, this.droneSettings.getMotor2Vendor(), this.droneSettings.getMotor2HardVersion(), this.droneSettings.getMotor2SoftVersion());
                this.view.setMotorVersion(2, this.droneSettings.getMotor3Vendor(), this.droneSettings.getMotor3HardVersion(), this.droneSettings.getMotor3SoftVersion());
                this.view.setMotorVersion(3, this.droneSettings.getMotor4Vendor(), this.droneSettings.getMotor4HardVersion(), this.droneSettings.getMotor4SoftVersion());
                Log.d(TAG, "config.ownerMac = " + this.droneSettings.getOwnerMac());
                if (this.droneSettings.getOwnerMac() == null || this.droneSettings.getOwnerMac().equalsIgnoreCase(NULL_MAC)) {
                    this.view.setPairing(false);
                } else {
                    this.view.setPairing(true);
                }
                this.view.setNetworkName(this.droneSettings.getNetworkName());
                this.view.setAltitudeLimit(this.droneSettings.getAltitudeLimit());
                this.view.setAdaptiveVideo(this.droneSettings.isAdaptiveVideo());
                this.view.setOutdoorHull(this.droneSettings.isOutdoorHull());
                if (ARDroneVersion.isArDrone1()) {
                    if (this.droneSettings.getVideoCodec() == 64) {
                        this.view.setVideoP264Checked(true);
                    } else if (this.droneSettings.getVideoCodec() == 32) {
                        this.view.setVideoVLIBChecked(true);
                    } else {
                        Log.w(TAG, "Unknown video codec " + this.droneSettings.getVideoCodec());
                    }
                }
                this.view.setOutdoorFlight(this.droneSettings.isOutdoorFlight());
                this.view.setYawSpeedMax((int) this.droneSettings.getYawSpeedMax());
                this.view.setVerticalSpeedMax((int) this.droneSettings.getVertSpeedMax());
                this.view.setTilt((int) this.droneSettings.getTilt());
                this.view.setTiltMax((int) this.droneSettings.getDeviceTiltMax());
                return;
            }
            Log.w(TAG, "Can't get drone's configuration.");
        }
    }

    public void onChanged(SeekBar seekBar, int i) {
        switch (seekBar.getId()) {
            case C0984R.id.seekInterfaceOpacity /*2131362079*/:
                this.appSettings.setInterfaceOpacity(this.view.getInterfaceOpacity());
                this.delegate.onOptionChangedApp(this, EAppSettingProperty.INTERFACE_OPACITY_PROP, Integer.valueOf(i));
                return;
            case C0984R.id.seekDeviceTiltMax /*2131362092*/:
                this.droneSettings.setDeviceTiltMax((float) this.view.getTiltMax());
                return;
            case C0984R.id.seekAltitudeLimit /*2131362280*/:
                this.droneSettings.setAltitudeLimit(this.view.getAltitudeLimit());
                return;
            case C0984R.id.seekVerticalSpeedMax /*2131362281*/:
                this.droneSettings.setVertSpeedMax((float) this.view.getVerticalSpeedMax());
                return;
            case C0984R.id.seekYawSpeedMax /*2131362282*/:
                this.droneSettings.setYawSpeedMax(this.view.getYawSpeedMax());
                return;
            case C0984R.id.seekTiltMax /*2131362283*/:
                this.droneSettings.setTilt((float) this.view.getTilt());
                return;
            default:
                return;
        }
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        switch (compoundButton.getId()) {
            case C0984R.id.togglePairing /*2131362077*/:
                this.droneSettings.setOwnerMac(z ? this.ownerMac : NULL_MAC);
                return;
            case C0984R.id.toggleUsbRecord /*2131362082*/:
                this.droneSettings.setRecordOnUsb(z);
                return;
            case C0984R.id.toggleAutoRecord /*2131362083*/:
                this.appSettings.setAutorecordEnabled(z);
                this.mService.setAutoRecordEnabled(z);
                return;
            case C0984R.id.toggleGpsFirmwareUpdateEnabled /*2131362084*/:
                this.appSettings.setGpsFirmwareUpdateEnabled(z);
                return;
            case C0984R.id.toggleLoopingEnabled /*2131362085*/:
                this.appSettings.setFlipEnabled(z);
                return;
            case C0984R.id.toggleAcceleroDisabled /*2131362088*/:
                ControlMode controlMode = z ? ControlMode.NORMAL_MODE : ControlMode.ACCELERO_MODE;
                this.appSettings.setControlMode(controlMode);
                this.delegate.onOptionChangedApp(this, EAppSettingProperty.CONTROL_MODE_PROP, controlMode);
                return;
            case C0984R.id.toggleLeftHanded /*2131362089*/:
                this.appSettings.setLeftHanded(z);
                this.delegate.onOptionChangedApp(this, EAppSettingProperty.LEFT_HANDED_PROP, Boolean.valueOf(z));
                return;
            case C0984R.id.toggleAbsoluteControl /*2131362090*/:
                this.appSettings.setAbsoluteControlEnabled(z);
                this.delegate.onOptionChangedApp(this, EAppSettingProperty.MAGNETO_ENABLED_PROP, Boolean.valueOf(z));
                return;
            case C0984R.id.toggleOutdoorHull /*2131362292*/:
                this.droneSettings.setOutdoorHull(z);
                DataTracker.trackInfoVoid(z ? TRACK_KEY_ENUM.TRACK_KEY_EVENT__IS_OUTDOOR_HULL : TRACK_KEY_ENUM.TRACK_KEY_EVENT__IS_INDOOR_HULL);
                return;
            case C0984R.id.toggleOutdoorFlight /*2131362293*/:
                this.view.setOutdoorFlightControlsEnabled(false);
                this.droneSettings.setOutdoorFlight(z);
                this.mService.triggerConfigUpdate();
                return;
            case C0984R.id.toggleAdaptiveVideo /*2131362294*/:
                this.droneSettings.setAdaptiveVideo(z);
                return;
            default:
                Log.d(TAG, "Unknown button");
                return;
        }
    }

    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case C0984R.id.rbVideoVLIB /*2131362296*/:
                this.droneSettings.setVideoCodec(32);
                return;
            case C0984R.id.rbVideoP264 /*2131362297*/:
                this.droneSettings.setVideoCodec(64);
                return;
            case C0984R.id.settings_radioitem_flip_front /*2131362330*/:
                this.appSettings.setDroneFlipDirection(DroneFlipDirection.FRONT);
                return;
            case C0984R.id.settings_radioitem_flip_back /*2131362331*/:
                this.appSettings.setDroneFlipDirection(DroneFlipDirection.BACK);
                return;
            case C0984R.id.settings_radioitem_flip_left /*2131362332*/:
                this.appSettings.setDroneFlipDirection(DroneFlipDirection.LEFT);
                return;
            case C0984R.id.settings_radioitem_flip_right /*2131362333*/:
                this.appSettings.setDroneFlipDirection(DroneFlipDirection.RIGHT);
                return;
            case C0984R.id.settings_radioitem_battery_type_std /*2131362348*/:
                this.droneSettings.setBatteryType(BatteryType.Type_1000MA);
                return;
            case C0984R.id.settings_radioitem_battery_type_hd /*2131362349*/:
                this.droneSettings.setBatteryType(BatteryType.Type_1500MA);
                return;
            default:
                return;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0984R.id.btnCalibration /*2131362098*/:
                onCalibrate();
                return;
            case C0984R.id.btnBack /*2131362100*/:
                dismiss();
                return;
            case C0984R.id.btnDefaultSettings /*2131362107*/:
                onDefaultSettingsClicked(view);
                return;
            case C0984R.id.btnFlatTrim /*2131362108*/:
                onFlatTrimClicked(view);
                return;
            default:
                return;
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.configChangedReceiver = new DroneConfigChangedReceiver(this);
        this.gpsParamsChangedReceiver = new GpsParamsChangeReceiver(this);
        setStyle(1, C0984R.style.FreeFlightTheme_SettingsScreen);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        ViewGroup viewGroup2 = (ViewGroup) layoutInflater.inflate(C0984R.layout.ff2_settings_screen, viewGroup, false);
        FontUtils.applyFont(getActivity(), viewGroup2);
        this.view = new SettingsViewController(getActivity(), layoutInflater, viewGroup2, this.magnetoAvailable);
        if (this.delegate != null) {
            this.delegate.prepareDialog(this);
        }
        getDialog().getWindow().setSoftInputMode(32);
        return viewGroup2;
    }

    public void onDefaultSettingsClicked(View view) {
        this.view.disableControlsThatRequireDroneConnection();
        new C11051().execute(new Integer[0]);
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        if (this.delegate != null) {
            this.delegate.onDismissed(this);
        }
    }

    public void onDroneConfigChanged() {
        fillUiControls();
        this.view.enableAvailableSettings();
    }

    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (textView.getId() != C0984R.id.editNetworkName) {
            return false;
        }
        if (i == 3 || i == 6 || i == 2 || i == 0 || (keyEvent != null && keyEvent.getAction() == 0 && keyEvent.getKeyCode() == 66)) {
            String charSequence = textView.getText().toString();
            if (charSequence.equals(this.droneSettings.getNetworkName())) {
                return false;
            }
            Matcher matcher = Pattern.compile("[a-zA-Z0-9_]*").matcher(charSequence);
            if (charSequence.trim().length() == 0 || charSequence.trim().length() > 32 || !matcher.matches()) {
                textView.setText(this.droneSettings.getNetworkName());
                Builder builder = new Builder(this.context);
                builder.setMessage(C0984R.string.f315x9d3860c).setTitle(C0984R.string.Bad_network_name).setCancelable(true).setIcon(17301659).setNegativeButton(17039370, new C11062());
                builder.create().show();
            } else {
                this.droneSettings.setNetworkName(textView.getText().toString());
                CharSequence format = String.format(this.context.getString(C0984R.string.ae_ID000060), new Object[]{textView.getText()});
                Builder builder2 = new Builder(this.context);
                builder2.setMessage(format).setTitle(this.context.getString(C0984R.string.your_changes_will_be_applied_after_rebooting_drone)).setCancelable(false).setIcon(17301659).setPositiveButton(17039370, new C11073());
                builder2.create().show();
            }
        }
        return true;
    }

    public void onFlatTrimClicked(View view) {
        this.mService.flatTrim();
    }

    public void onGpsParamsChanged(boolean z, boolean z2, float f, int i, double d, double d2) {
        this.view.setGpsPlugged(z);
        this.view.setGpsSatellites(i);
    }

    public void onOkClicked(View view) {
        dismiss();
    }

    public void onStart() {
        super.onStart();
        this.droneSettings = this.mService.getDroneConfig();
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(this.mService.getApplicationContext());
        instance.registerReceiver(this.configChangedReceiver, new IntentFilter(DroneControlService.ACTION_DRONE_CONFIG_STATE_CHANGED));
        instance.registerReceiver(this.gpsParamsChangedReceiver, GpsParamsChangeReceiver.createFilter());
        loadSettings();
        Context context = this.mService;
        if (((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo().getType() == 1) {
            this.ownerMac = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
        }
    }

    public void onStop() {
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(this.mService.getApplicationContext());
        instance.unregisterReceiver(this.configChangedReceiver);
        instance.unregisterReceiver(this.gpsParamsChangedReceiver);
        if (this.loadSettingsTask != null) {
            this.loadSettingsTask.cancel(true);
        }
        super.onStop();
    }

    public void setAcceleroAvailable(boolean z) {
        this.acceleroAvailable = z;
        this.view.setAcceleroAvailable(z);
    }

    public void setConnected(boolean z, boolean z2) {
        this.view.setConnected(z);
        this.view.setIsOnWifi(z2);
    }

    public void setFlying(boolean z) {
        this.view.setFlying(z);
    }

    public void setMagnetoAvailable(boolean z) {
        this.view.setMagnetoAvailable(z);
    }
}
