package com.parrot.freeflight.utils;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import java.util.List;

public final class GPSHelper implements LocationListener {
    public static boolean active = false;
    private static volatile GPSHelper instance;
    private LocationManager locationManager;

    private GPSHelper(Context context) {
        this.locationManager = (LocationManager) context.getSystemService("location");
        isGpsOn(context);
    }

    public static void addGpsStatusListener(Context context, Listener listener) {
        ((LocationManager) context.getSystemService("location")).addGpsStatusListener(listener);
    }

    public static void callGpsSettingsScreen(Context context) {
        Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
        intent.addFlags(268435456);
        context.startActivity(intent);
    }

    public static boolean deviceSupportGPS(Context context) {
        return ((LocationManager) context.getSystemService("location")).getAllProviders().size() > 1;
    }

    public static GPSHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (GPSHelper.class) {
                try {
                    if (instance == null) {
                        GPSHelper gPSHelper = new GPSHelper(context);
                        return gPSHelper;
                    }
                } finally {
                    while (true) {
                        break;
                    }
                    Object obj = GPSHelper.class;
                }
            }
        }
        return instance;
    }

    public static Location getLastKnownLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService("location");
        return locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), true));
    }

    public static boolean isAccurate(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService("location");
        return locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false)).getAccuracy() < 100.0f;
    }

    public static boolean isGpsOn(Context context) {
        active = false;
        LocationManager locationManager = (LocationManager) context.getSystemService("location");
        try {
            if (locationManager.isProviderEnabled("gps") || locationManager.isProviderEnabled("network")) {
                active = true;
            }
        } catch (IllegalArgumentException e) {
            Log.d("GPS Helper", "Looks like we do not have gps on the device");
        }
        return active;
    }

    public void onLocationChanged(Location location) {
    }

    public void onProviderDisabled(String str) {
    }

    public void onProviderEnabled(String str) {
    }

    public void onStatusChanged(String str, int i, Bundle bundle) {
        if (i == 2) {
            active = true;
        } else {
            active = false;
        }
    }

    public void startListening(LocationListener locationListener) {
        List providers = this.locationManager.getProviders(true);
        providers.remove("passive");
        for (int i = 0; i < providers.size(); i++) {
            this.locationManager.requestLocationUpdates((String) providers.get(i), 0, 0.0f, locationListener);
        }
    }

    public void stopListening(LocationListener locationListener) {
        this.locationManager.removeUpdates(locationListener);
    }
}
