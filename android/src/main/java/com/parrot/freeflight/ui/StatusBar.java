package com.parrot.freeflight.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.parrot.ardronetool.DataTracker;
import com.parrot.ardronetool.tracking.TRACK_KEY_ENUM;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.activities.PreferencesActivity;
import com.parrot.freeflight.mediauploadservice.MediaShareService;
import com.parrot.freeflight.mediauploadservice.MediaShareService.State;
import com.parrot.freeflight.mediauploadservice.broadcastreceivers.MediaUploadStateChangedReceiver;
import com.parrot.freeflight.mediauploadservice.broadcastreceivers.MediaUploadStateChangedReceiverDelegate;
import com.parrot.freeflight.receivers.FlightUploadStatusReceiver;
import com.parrot.freeflight.receivers.FlightUploadStatusReceiverDelegate;
import com.parrot.freeflight.service.FlightUploadService;
import com.parrot.freeflight.utils.GPSHelper;
import java.util.Date;
import org.mortbay.jetty.HttpVersions;

public final class StatusBar implements OnClickListener, MediaUploadStateChangedReceiverDelegate, FlightUploadStatusReceiverDelegate {
    private final Activity activity;
    private final int[] batteryIndicatorIds = new int[]{C0984R.drawable.ff2_battery_000, C0984R.drawable.ff2_battery_020, C0984R.drawable.ff2_battery_040, C0984R.drawable.ff2_battery_060, C0984R.drawable.ff2_battery_080, C0984R.drawable.ff2_battery_100};
    private boolean flightUploadInProgress;
    private BroadcastReceiver flightUploadStateChangedReceiver;
    private final View headerView;
    private BroadcastReceiver mBatInfoReceiver;
    private BroadcastReceiver mTimeInfoReceiver;
    private boolean mediaUploadInProgress;
    private BroadcastReceiver mediaUploadStateChangedReceiver;
    private TextView settingsBtn;
    private TextView txtUploadState;

    class C12271 extends BroadcastReceiver {
        C12271() {
        }

        public void onReceive(Context context, Intent intent) {
            StatusBar.this.processBatteryEvent(intent);
        }
    }

    class C12282 extends BroadcastReceiver {
        C12282() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.TIME_TICK")) {
                StatusBar.this.updateTime();
            }
        }
    }

    public StatusBar(Activity activity, View view) {
        this.activity = activity;
        this.headerView = view;
        initUi();
        initListeners();
        initBroadcastReceivers();
        updateTime();
        initGpsTextColor();
    }

    private void initBroadcastReceivers() {
        this.mBatInfoReceiver = new C12271();
        this.mTimeInfoReceiver = new C12282();
        this.mediaUploadStateChangedReceiver = new MediaUploadStateChangedReceiver(this);
        this.flightUploadStateChangedReceiver = new FlightUploadStatusReceiver(this);
    }

    private void initGpsTextColor() {
        TextView textView = (TextView) this.activity.findViewById(C0984R.id.txtGPS);
        if (GPSHelper.deviceSupportGPS(this.activity) && GPSHelper.isGpsOn(this.activity)) {
            textView.setTextColor(this.activity.getResources().getColor(C0984R.color.accent));
        } else {
            textView.setTextColor(this.activity.getResources().getColor(C0984R.color.text_color));
        }
    }

    private void initListeners() {
        this.settingsBtn.setOnClickListener(this);
    }

    private void initUi() {
        this.settingsBtn = (TextView) this.activity.findViewById(C0984R.id.txtPreferences);
        this.txtUploadState = (TextView) this.activity.findViewById(C0984R.id.view_toppanel_textview_uploadstatus);
        this.txtUploadState.setVisibility(4);
    }

    private void processBatteryEvent(Intent intent) {
        TextView textView = (TextView) this.headerView.findViewById(C0984R.id.txtBatteryStatus);
        ImageView imageView = (ImageView) this.headerView.findViewById(C0984R.id.imgBattery);
        if (intent.getAction().equals("android.intent.action.BATTERY_CHANGED")) {
            int intExtra = intent.getIntExtra("level", 0);
            textView.setText(HttpVersions.HTTP_0_9 + intExtra + "%");
            Resources resources = this.activity.getResources();
            int i = intExtra <= 20 ? 1 : intExtra <= 40 ? 2 : intExtra <= 60 ? 3 : intExtra <= 80 ? 4 : intExtra <= 100 ? 5 : 0;
            imageView.setImageDrawable(resources.getDrawable(this.batteryIndicatorIds[i]));
        }
    }

    private void updateTime() {
        ((TextView) this.headerView.findViewById(C0984R.id.txtTime)).setText(DateFormat.getTimeFormat(this.activity).format(new Date(System.currentTimeMillis())));
    }

    private void updateUploadText() {
        if (this.mediaUploadInProgress || this.flightUploadInProgress) {
            this.txtUploadState.setVisibility(0);
        } else {
            this.txtUploadState.setVisibility(4);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0984R.id.txtPreferences /*2131362352*/:
                DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__CLICK_HOME_PREFERENCES);
                this.activity.startActivity(new Intent(this.activity, PreferencesActivity.class));
                return;
            default:
                return;
        }
    }

    public void onFlightUploadStatusChanged(boolean z) {
        this.flightUploadInProgress = z;
        updateUploadText();
    }

    public void onMediaUploadStateChanged(boolean z) {
        this.mediaUploadInProgress = z;
        updateUploadText();
    }

    public void startUpdating() {
        updateTime();
        this.activity.registerReceiver(this.mBatInfoReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        this.activity.registerReceiver(this.mTimeInfoReceiver, new IntentFilter("android.intent.action.TIME_TICK"));
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(this.activity.getApplicationContext());
        instance.registerReceiver(this.mediaUploadStateChangedReceiver, MediaUploadStateChangedReceiver.createFilter());
        instance.registerReceiver(this.flightUploadStateChangedReceiver, FlightUploadStatusReceiver.createFilter());
        onMediaUploadStateChanged(MediaShareService.getState() == State.UPLOADING);
        onFlightUploadStatusChanged(FlightUploadService.getUploadStatus());
    }

    public void stopUpdating() {
        this.activity.unregisterReceiver(this.mBatInfoReceiver);
        this.activity.unregisterReceiver(this.mTimeInfoReceiver);
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(this.activity.getApplicationContext());
        instance.unregisterReceiver(this.mediaUploadStateChangedReceiver);
        instance.unregisterReceiver(this.flightUploadStateChangedReceiver);
    }
}
