package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.parrot.freeflight.service.DroneControlService;

public class GpsParamsChangeReceiver extends BroadcastReceiver {
    private final GpsParamsChangeReceiverDelegate delegate;

    public GpsParamsChangeReceiver(GpsParamsChangeReceiverDelegate gpsParamsChangeReceiverDelegate) {
        this.delegate = gpsParamsChangeReceiverDelegate;
    }

    public static IntentFilter createFilter() {
        return new IntentFilter(DroneControlService.ACTION_GPS_PARAMS_CHANGED);
    }

    public void onReceive(Context context, Intent intent) {
        this.delegate.onGpsParamsChanged(intent.getBooleanExtra(DroneControlService.EXTRA_GPS_PLUGGED, false), intent.getBooleanExtra(DroneControlService.EXTRA_GPS_ACTIVE, false), intent.getFloatExtra(DroneControlService.EXTRA_GPS_PRECISION, GroundOverlayOptions.NO_DIMENSION), intent.getIntExtra(DroneControlService.EXTRA_GPS_SATSUSED, -1), intent.getDoubleExtra(DroneControlService.EXTRA_GPS_LATFUSED, -1.0d), intent.getDoubleExtra(DroneControlService.EXTRA_GPS_LONGFUSED, -1.0d));
    }
}
