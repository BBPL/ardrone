package com.google.android.gms.panorama;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.internal.bn;

public class PanoramaClient implements GooglePlayServicesClient {
    private final bn hN;

    public interface OnPanoramaInfoLoadedListener {
        void onPanoramaInfoLoaded(ConnectionResult connectionResult, Intent intent);
    }

    public interface C0361a {
        void m1396a(ConnectionResult connectionResult, int i, Intent intent);
    }

    public PanoramaClient(Context context, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        this.hN = new bn(context, connectionCallbacks, onConnectionFailedListener);
    }

    public void connect() {
        this.hN.connect();
    }

    public void disconnect() {
        this.hN.disconnect();
    }

    public boolean isConnected() {
        return this.hN.isConnected();
    }

    public boolean isConnecting() {
        return this.hN.isConnecting();
    }

    public boolean isConnectionCallbacksRegistered(ConnectionCallbacks connectionCallbacks) {
        return this.hN.isConnectionCallbacksRegistered(connectionCallbacks);
    }

    public boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener onConnectionFailedListener) {
        return this.hN.isConnectionFailedListenerRegistered(onConnectionFailedListener);
    }

    public void loadPanoramaInfo(OnPanoramaInfoLoadedListener onPanoramaInfoLoadedListener, Uri uri) {
        this.hN.m826a(onPanoramaInfoLoadedListener, uri, false);
    }

    public void loadPanoramaInfoAndGrantAccess(OnPanoramaInfoLoadedListener onPanoramaInfoLoadedListener, Uri uri) {
        this.hN.m826a(onPanoramaInfoLoadedListener, uri, true);
    }

    public void registerConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        this.hN.registerConnectionCallbacks(connectionCallbacks);
    }

    public void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        this.hN.registerConnectionFailedListener(onConnectionFailedListener);
    }

    public void unregisterConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        this.hN.unregisterConnectionCallbacks(connectionCallbacks);
    }

    public void unregisterConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        this.hN.unregisterConnectionFailedListener(onConnectionFailedListener);
    }
}
