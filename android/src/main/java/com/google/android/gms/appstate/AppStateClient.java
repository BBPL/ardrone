package com.google.android.gms.appstate;

import android.content.Context;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.internal.C0198c;
import com.google.android.gms.internal.C0242s;

public final class AppStateClient implements GooglePlayServicesClient {
    public static final int STATUS_CLIENT_RECONNECT_REQUIRED = 2;
    public static final int STATUS_DEVELOPER_ERROR = 7;
    public static final int STATUS_INTERNAL_ERROR = 1;
    public static final int STATUS_NETWORK_ERROR_NO_DATA = 4;
    public static final int STATUS_NETWORK_ERROR_OPERATION_DEFERRED = 5;
    public static final int STATUS_NETWORK_ERROR_OPERATION_FAILED = 6;
    public static final int STATUS_NETWORK_ERROR_STALE_DATA = 3;
    public static final int STATUS_OK = 0;
    public static final int STATUS_STATE_KEY_LIMIT_EXCEEDED = 2003;
    public static final int STATUS_STATE_KEY_NOT_FOUND = 2002;
    public static final int STATUS_WRITE_OUT_OF_DATE_VERSION = 2000;
    public static final int STATUS_WRITE_SIZE_EXCEEDED = 2001;
    private final C0198c f7b;

    public static final class Builder {
        private static final String[] f2c = new String[]{Scopes.APP_STATE};
        private ConnectionCallbacks f3d;
        private OnConnectionFailedListener f4e;
        private String[] f5f = f2c;
        private String f6g = "<<default account>>";
        private Context mContext;

        public Builder(Context context, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            this.mContext = context;
            this.f3d = connectionCallbacks;
            this.f4e = onConnectionFailedListener;
        }

        public AppStateClient create() {
            return new AppStateClient(this.mContext, this.f3d, this.f4e, this.f6g, this.f5f);
        }

        public Builder setAccountName(String str) {
            this.f6g = (String) C0242s.m1208d(str);
            return this;
        }

        public Builder setScopes(String... strArr) {
            this.f5f = strArr;
            return this;
        }
    }

    private AppStateClient(Context context, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, String str, String[] strArr) {
        this.f7b = new C0198c(context, connectionCallbacks, onConnectionFailedListener, str, strArr);
    }

    public void connect() {
        this.f7b.connect();
    }

    public void deleteState(OnStateDeletedListener onStateDeletedListener, int i) {
        this.f7b.deleteState(onStateDeletedListener, i);
    }

    public void disconnect() {
        this.f7b.disconnect();
    }

    public int getMaxNumKeys() {
        return this.f7b.getMaxNumKeys();
    }

    public int getMaxStateSize() {
        return this.f7b.getMaxStateSize();
    }

    public boolean isConnected() {
        return this.f7b.isConnected();
    }

    public boolean isConnecting() {
        return this.f7b.isConnecting();
    }

    public boolean isConnectionCallbacksRegistered(ConnectionCallbacks connectionCallbacks) {
        return this.f7b.isConnectionCallbacksRegistered(connectionCallbacks);
    }

    public boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener onConnectionFailedListener) {
        return this.f7b.isConnectionFailedListenerRegistered(onConnectionFailedListener);
    }

    public void listStates(OnStateListLoadedListener onStateListLoadedListener) {
        this.f7b.listStates(onStateListLoadedListener);
    }

    public void loadState(OnStateLoadedListener onStateLoadedListener, int i) {
        this.f7b.loadState(onStateLoadedListener, i);
    }

    public void reconnect() {
        this.f7b.disconnect();
        this.f7b.connect();
    }

    public void registerConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        this.f7b.registerConnectionCallbacks(connectionCallbacks);
    }

    public void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        this.f7b.registerConnectionFailedListener(onConnectionFailedListener);
    }

    public void resolveState(OnStateLoadedListener onStateLoadedListener, int i, String str, byte[] bArr) {
        this.f7b.resolveState(onStateLoadedListener, i, str, bArr);
    }

    public void signOut() {
        this.f7b.signOut(null);
    }

    public void signOut(OnSignOutCompleteListener onSignOutCompleteListener) {
        C0242s.m1205b((Object) onSignOutCompleteListener, (Object) "Must provide a valid listener");
        this.f7b.signOut(onSignOutCompleteListener);
    }

    public void unregisterConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        this.f7b.unregisterConnectionCallbacks(connectionCallbacks);
    }

    public void unregisterConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        this.f7b.unregisterConnectionFailedListener(onConnectionFailedListener);
    }

    public void updateState(int i, byte[] bArr) {
        this.f7b.m1009a(null, i, bArr);
    }

    public void updateStateImmediate(OnStateLoadedListener onStateLoadedListener, int i, byte[] bArr) {
        C0242s.m1205b((Object) onStateLoadedListener, (Object) "Must provide a valid listener");
        this.f7b.m1009a(onStateLoadedListener, i, bArr);
    }
}
