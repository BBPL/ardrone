package com.google.android.gms.location;

import android.app.PendingIntent;
import android.content.Context;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.internal.bh;

public class ActivityRecognitionClient implements GooglePlayServicesClient {
    private final bh fo;

    public ActivityRecognitionClient(Context context, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        this.fo = new bh(context, connectionCallbacks, onConnectionFailedListener, "activity_recognition");
    }

    public void connect() {
        this.fo.connect();
    }

    public void disconnect() {
        this.fo.disconnect();
    }

    public boolean isConnected() {
        return this.fo.isConnected();
    }

    public boolean isConnecting() {
        return this.fo.isConnecting();
    }

    public boolean isConnectionCallbacksRegistered(ConnectionCallbacks connectionCallbacks) {
        return this.fo.isConnectionCallbacksRegistered(connectionCallbacks);
    }

    public boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener onConnectionFailedListener) {
        return this.fo.isConnectionFailedListenerRegistered(onConnectionFailedListener);
    }

    public void registerConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        this.fo.registerConnectionCallbacks(connectionCallbacks);
    }

    public void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        this.fo.registerConnectionFailedListener(onConnectionFailedListener);
    }

    public void removeActivityUpdates(PendingIntent pendingIntent) {
        this.fo.removeActivityUpdates(pendingIntent);
    }

    public void requestActivityUpdates(long j, PendingIntent pendingIntent) {
        this.fo.requestActivityUpdates(j, pendingIntent);
    }

    public void unregisterConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        this.fo.unregisterConnectionCallbacks(connectionCallbacks);
    }

    public void unregisterConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        this.fo.unregisterConnectionFailedListener(onConnectionFailedListener);
    }
}
