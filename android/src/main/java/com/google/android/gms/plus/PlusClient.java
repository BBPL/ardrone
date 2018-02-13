package com.google.android.gms.plus;

import android.content.Context;
import android.net.Uri;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.internal.bt;
import com.google.android.gms.plus.model.moments.Moment;
import com.google.android.gms.plus.model.moments.MomentBuffer;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class PlusClient implements GooglePlayServicesClient {
    @Deprecated
    public static final String KEY_REQUEST_VISIBLE_ACTIVITIES = "request_visible_actions";
    final bt hU;

    public static class Builder {
        private OnConnectionFailedListener f125e;
        private String f126g;
        private ConnectionCallbacks hV;
        private ArrayList<String> hW = new ArrayList();
        private String[] hX;
        private String[] hY;
        private String hZ = this.mContext.getPackageName();
        private String ia = this.mContext.getPackageName();
        private String ib;
        private Context mContext;

        public Builder(Context context, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            this.mContext = context;
            this.hV = connectionCallbacks;
            this.f125e = onConnectionFailedListener;
            this.hW.add(Scopes.PLUS_LOGIN);
        }

        public PlusClient build() {
            if (this.f126g == null) {
                this.f126g = "<<default account>>";
            }
            return new PlusClient(new bt(this.mContext, new C0367a(this.f126g, (String[]) this.hW.toArray(new String[this.hW.size()]), this.hX, this.hY, this.hZ, this.ia, this.ib), this.hV, this.f125e));
        }

        public Builder clearScopes() {
            this.hW.clear();
            return this;
        }

        public Builder setAccountName(String str) {
            this.f126g = str;
            return this;
        }

        public Builder setActions(String... strArr) {
            this.hX = strArr;
            return this;
        }

        public Builder setScopes(String... strArr) {
            this.hW.clear();
            this.hW.addAll(Arrays.asList(strArr));
            return this;
        }

        @Deprecated
        public Builder setVisibleActivities(String... strArr) {
            setActions(strArr);
            return this;
        }
    }

    public interface OnAccessRevokedListener {
        void onAccessRevoked(ConnectionResult connectionResult);
    }

    public interface OnMomentsLoadedListener {
        void onMomentsLoaded(ConnectionResult connectionResult, MomentBuffer momentBuffer, String str, String str2);
    }

    public interface OnPeopleLoadedListener {
        void onPeopleLoaded(ConnectionResult connectionResult, PersonBuffer personBuffer, String str);
    }

    public interface OrderBy {
        public static final int ALPHABETICAL = 0;
        public static final int BEST = 1;
    }

    PlusClient(bt btVar) {
        this.hU = btVar;
    }

    bt bu() {
        return this.hU;
    }

    public void clearDefaultAccount() {
        this.hU.clearDefaultAccount();
    }

    public void connect() {
        this.hU.connect();
    }

    public void disconnect() {
        this.hU.disconnect();
    }

    public String getAccountName() {
        return this.hU.getAccountName();
    }

    public Person getCurrentPerson() {
        return this.hU.getCurrentPerson();
    }

    public boolean isConnected() {
        return this.hU.isConnected();
    }

    public boolean isConnecting() {
        return this.hU.isConnecting();
    }

    public boolean isConnectionCallbacksRegistered(ConnectionCallbacks connectionCallbacks) {
        return this.hU.isConnectionCallbacksRegistered(connectionCallbacks);
    }

    public boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener onConnectionFailedListener) {
        return this.hU.isConnectionFailedListenerRegistered(onConnectionFailedListener);
    }

    public void loadMoments(OnMomentsLoadedListener onMomentsLoadedListener) {
        this.hU.loadMoments(onMomentsLoadedListener);
    }

    public void loadMoments(OnMomentsLoadedListener onMomentsLoadedListener, int i, String str, Uri uri, String str2, String str3) {
        this.hU.loadMoments(onMomentsLoadedListener, i, str, uri, str2, str3);
    }

    public void loadPeople(OnPeopleLoadedListener onPeopleLoadedListener, Collection<String> collection) {
        this.hU.m970a(onPeopleLoadedListener, (Collection) collection);
    }

    public void loadPeople(OnPeopleLoadedListener onPeopleLoadedListener, String... strArr) {
        this.hU.m971a(onPeopleLoadedListener, strArr);
    }

    public void loadVisiblePeople(OnPeopleLoadedListener onPeopleLoadedListener, int i, String str) {
        this.hU.loadVisiblePeople(onPeopleLoadedListener, i, str);
    }

    public void loadVisiblePeople(OnPeopleLoadedListener onPeopleLoadedListener, String str) {
        this.hU.loadVisiblePeople(onPeopleLoadedListener, str);
    }

    public void registerConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        this.hU.registerConnectionCallbacks(connectionCallbacks);
    }

    public void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        this.hU.registerConnectionFailedListener(onConnectionFailedListener);
    }

    public void removeMoment(String str) {
        this.hU.removeMoment(str);
    }

    public void revokeAccessAndDisconnect(OnAccessRevokedListener onAccessRevokedListener) {
        this.hU.revokeAccessAndDisconnect(onAccessRevokedListener);
    }

    public void unregisterConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        this.hU.unregisterConnectionCallbacks(connectionCallbacks);
    }

    public void unregisterConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        this.hU.unregisterConnectionFailedListener(onConnectionFailedListener);
    }

    public void writeMoment(Moment moment) {
        this.hU.writeMoment(moment);
    }
}
