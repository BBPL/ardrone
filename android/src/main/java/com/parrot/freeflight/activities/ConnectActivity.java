package com.parrot.freeflight.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.receivers.DroneConnectionChangeReceiverDelegate;
import com.parrot.freeflight.receivers.DroneConnectionChangedReceiver;
import com.parrot.freeflight.receivers.DroneReadyReceiver;
import com.parrot.freeflight.receivers.DroneReadyReceiverDelegate;
import com.parrot.freeflight.service.DroneControlService;
import com.parrot.freeflight.service.DroneControlService.LocalBinder;
import com.parrot.freeflight.updater.FirmwareUpdateService;
import com.parrot.freeflight.utils.SystemUtils;
import java.util.Random;

public class ConnectActivity extends ParrotActivity implements ServiceConnection, DroneReadyReceiverDelegate, DroneConnectionChangeReceiverDelegate {
    private static final String TAG = ConnectActivity.class.getSimpleName();
    private static final int[] TIPS = new int[]{C0984R.layout.hint_screen_joypad_mode, C0984R.layout.hint_screen_absolute_control, C0984R.layout.hint_screen_record, C0984R.layout.hint_screen_usb, C0984R.layout.hint_screen_switch, C0984R.layout.hint_screen_landing, C0984R.layout.hint_screen_take_off, C0984R.layout.hint_screen_emergency, C0984R.layout.hint_screen_altitude, C0984R.layout.hint_screen_hovering, C0984R.layout.hint_screen_share, C0984R.layout.hint_screen_flip};
    private String AUTO_SKIPP_KEY = "auto_skip";
    private BroadcastReceiver droneConnectionChangeReceiver;
    private BroadcastReceiver droneReadyReceiver;
    private DroneControlService mService;
    private AsyncTask<Object, Integer, Boolean> openHudTask;

    class C10521 implements OnClickListener {
        C10521() {
        }

        public void onClick(View view) {
            ConnectActivity.this.onOpenHudScreen(0);
        }
    }

    class C10532 implements OnCheckedChangeListener {
        C10532() {
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            ConnectActivity.this.setAutoSkip(z);
        }
    }

    private void onOpenHudScreen(final int i) {
        Log.d(TAG, "==>>onOpenHudScreen()");
        this.openHudTask = new AsyncTask<Object, Integer, Boolean>() {
            protected Boolean doInBackground(Object... objArr) {
                try {
                    Thread.sleep((long) i);
                    if (!isCancelled()) {
                        Intent intent = new Intent(ConnectActivity.this, ControlDroneActivity.class);
                        intent.putExtra("USE_SOFTWARE_RENDERING", false);
                        intent.putExtra("FORCE_COMBINED_CONTROL_MODE", false);
                        ConnectActivity.this.startActivity(intent);
                        ConnectActivity.this.finish();
                    }
                    return Boolean.TRUE;
                } catch (InterruptedException e) {
                    return Boolean.FALSE;
                }
            }
        };
        this.openHudTask.execute(new Object[0]);
    }

    private void prepareGoogleTVControls() {
        findViewById(C0984R.id.loading_view).setVisibility(0);
        findViewById(C0984R.id.skip_button).setVisibility(8);
        findViewById(C0984R.id.skip_button).setOnClickListener(new C10521());
        CheckBox checkBox = (CheckBox) findViewById(C0984R.id.auto_skip);
        checkBox.setChecked(getAutoSkip());
        checkBox.setOnCheckedChangeListener(new C10532());
    }

    public void finish() {
        super.finish();
        overridePendingTransition(C0984R.anim.nothing, C0984R.anim.nothing);
    }

    protected boolean getAutoSkip() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(this.AUTO_SKIPP_KEY, false);
    }

    public void onBackPressed() {
        super.onBackPressed();
        if (this.openHudTask != null) {
            this.openHudTask.cancel(true);
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (SystemUtils.isGoogleTV(this)) {
            setContentView(C0984R.layout.hint_screen_remote_instructions);
            prepareGoogleTVControls();
        } else {
            setContentView(TIPS[new Random(System.currentTimeMillis()).nextInt(TIPS.length)]);
        }
        this.droneReadyReceiver = new DroneReadyReceiver(this);
        this.droneConnectionChangeReceiver = new DroneConnectionChangedReceiver(this);
        bindService(new Intent(this, DroneControlService.class), this, 1);
    }

    protected void onDestroy() {
        Log.d(TAG, "==>>onDestroy()");
        super.onDestroy();
        unbindService(this);
        Log.d(TAG, "Connect activity destroyed");
    }

    public void onDroneConnected() {
        this.mService.requestConfigUpdate();
    }

    public void onDroneDisconnected() {
    }

    public void onDroneReady() {
        Log.d(TAG, "==>>onDroneReady()");
        if (SystemUtils.isGoogleTV(this)) {
            if (((CheckBox) findViewById(C0984R.id.auto_skip)).isChecked()) {
                onOpenHudScreen(500);
            }
            findViewById(C0984R.id.loading_view).setVisibility(8);
            findViewById(C0984R.id.skip_button).setVisibility(0);
            return;
        }
        onOpenHudScreen(500);
    }

    protected void onPause() {
        if (this.mService != null) {
            this.mService.pause();
        }
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(getApplicationContext());
        instance.unregisterReceiver(this.droneReadyReceiver);
        instance.unregisterReceiver(this.droneConnectionChangeReceiver);
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
        if (this.mService != null) {
            this.mService.resume();
        }
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(getApplicationContext());
        instance.registerReceiver(this.droneReadyReceiver, new IntentFilter(DroneControlService.ACTION_DRONE_STATE_READY));
        instance.registerReceiver(this.droneConnectionChangeReceiver, new IntentFilter(DroneControlService.ACTION_DRONE_CONNECTION_CHANGED));
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.mService = ((LocalBinder) iBinder).getService();
        this.mService.resume();
        this.mService.requestDroneStatus();
    }

    public void onServiceDisconnected(ComponentName componentName) {
    }

    protected void onStart() {
        Log.d(TAG, "==>>onStart()");
        super.onStart();
        stopService(new Intent(this, FirmwareUpdateService.class));
    }

    protected void setAutoSkip(boolean z) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(this.AUTO_SKIPP_KEY, z).commit();
    }
}
