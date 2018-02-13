package com.google.android.gms.games.multiplayer.realtime;

import android.os.Bundle;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.internal.C0242s;
import java.util.ArrayList;
import java.util.Arrays;

public final class RoomConfig {
    private final String eF;
    private final int eK;
    private final RoomUpdateListener eW;
    private final RoomStatusUpdateListener eX;
    private final RealTimeMessageReceivedListener eY;
    private final String[] eZ;
    private final Bundle fa;
    private final boolean fb;

    public static final class Builder {
        int eK;
        final RoomUpdateListener eW;
        RoomStatusUpdateListener eX;
        RealTimeMessageReceivedListener eY;
        Bundle fa;
        boolean fb;
        String fc;
        ArrayList<String> fd;

        private Builder(RoomUpdateListener roomUpdateListener) {
            this.fc = null;
            this.eK = -1;
            this.fd = new ArrayList();
            this.fb = false;
            this.eW = (RoomUpdateListener) C0242s.m1205b((Object) roomUpdateListener, (Object) "Must provide a RoomUpdateListener");
        }

        public Builder addPlayersToInvite(ArrayList<String> arrayList) {
            C0242s.m1208d(arrayList);
            this.fd.addAll(arrayList);
            return this;
        }

        public Builder addPlayersToInvite(String... strArr) {
            C0242s.m1208d(strArr);
            this.fd.addAll(Arrays.asList(strArr));
            return this;
        }

        public RoomConfig build() {
            return new RoomConfig();
        }

        public Builder setAutoMatchCriteria(Bundle bundle) {
            this.fa = bundle;
            return this;
        }

        public Builder setInvitationIdToAccept(String str) {
            C0242s.m1208d(str);
            this.fc = str;
            return this;
        }

        public Builder setMessageReceivedListener(RealTimeMessageReceivedListener realTimeMessageReceivedListener) {
            this.eY = realTimeMessageReceivedListener;
            return this;
        }

        public Builder setRoomStatusUpdateListener(RoomStatusUpdateListener roomStatusUpdateListener) {
            this.eX = roomStatusUpdateListener;
            return this;
        }

        public Builder setSocketCommunicationEnabled(boolean z) {
            this.fb = z;
            return this;
        }

        public Builder setVariant(int i) {
            this.eK = i;
            return this;
        }
    }

    private RoomConfig(Builder builder) {
        this.eW = builder.eW;
        this.eX = builder.eX;
        this.eY = builder.eY;
        this.eF = builder.fc;
        this.eK = builder.eK;
        this.fa = builder.fa;
        this.fb = builder.fb;
        this.eZ = (String[]) builder.fd.toArray(new String[builder.fd.size()]);
        if (this.eY == null) {
            C0242s.m1203a(this.fb, "Must either enable sockets OR specify a message listener");
        }
    }

    public static Builder builder(RoomUpdateListener roomUpdateListener) {
        return new Builder(roomUpdateListener);
    }

    public static Bundle createAutoMatchCriteria(int i, int i2, long j) {
        Bundle bundle = new Bundle();
        bundle.putInt(GamesClient.EXTRA_MIN_AUTOMATCH_PLAYERS, i);
        bundle.putInt(GamesClient.EXTRA_MAX_AUTOMATCH_PLAYERS, i2);
        bundle.putLong(GamesClient.EXTRA_EXCLUSIVE_BIT_MASK, j);
        return bundle;
    }

    public Bundle getAutoMatchCriteria() {
        return this.fa;
    }

    public String getInvitationId() {
        return this.eF;
    }

    public String[] getInvitedPlayerIds() {
        return this.eZ;
    }

    public RealTimeMessageReceivedListener getMessageReceivedListener() {
        return this.eY;
    }

    public RoomStatusUpdateListener getRoomStatusUpdateListener() {
        return this.eX;
    }

    public RoomUpdateListener getRoomUpdateListener() {
        return this.eW;
    }

    public int getVariant() {
        return this.eK;
    }

    public boolean isSocketEnabled() {
        return this.fb;
    }
}
