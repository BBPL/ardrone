package com.google.android.gms.games;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.games.achievement.OnAchievementUpdatedListener;
import com.google.android.gms.games.achievement.OnAchievementsLoadedListener;
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer;
import com.google.android.gms.games.leaderboard.OnLeaderboardMetadataLoadedListener;
import com.google.android.gms.games.leaderboard.OnLeaderboardScoresLoadedListener;
import com.google.android.gms.games.leaderboard.OnScoreSubmittedListener;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.OnInvitationsLoadedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeReliableMessageSentListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.android.gms.internal.C0242s;
import com.google.android.gms.internal.au;
import java.util.List;

public final class GamesClient implements GooglePlayServicesClient {
    public static final String EXTRA_EXCLUSIVE_BIT_MASK = "exclusive_bit_mask";
    public static final String EXTRA_INVITATION = "invitation";
    public static final String EXTRA_MAX_AUTOMATCH_PLAYERS = "max_automatch_players";
    public static final String EXTRA_MIN_AUTOMATCH_PLAYERS = "min_automatch_players";
    public static final String EXTRA_PLAYERS = "players";
    public static final String EXTRA_ROOM = "room";
    public static final int MAX_RELIABLE_MESSAGE_LEN = 1400;
    public static final int MAX_UNRELIABLE_MESSAGE_LEN = 1168;
    public static final int NOTIFICATION_TYPES_ALL = -1;
    public static final int NOTIFICATION_TYPES_MULTIPLAYER = 1;
    public static final int NOTIFICATION_TYPE_INVITATION = 1;
    public static final int STATUS_ACHIEVEMENT_NOT_INCREMENTAL = 3002;
    public static final int STATUS_ACHIEVEMENT_UNKNOWN = 3001;
    public static final int STATUS_ACHIEVEMENT_UNLOCKED = 3003;
    public static final int STATUS_ACHIEVEMENT_UNLOCK_FAILURE = 3000;
    public static final int STATUS_CLIENT_RECONNECT_REQUIRED = 2;
    public static final int STATUS_INTERNAL_ERROR = 1;
    public static final int STATUS_INVALID_REAL_TIME_ROOM_ID = 7002;
    public static final int STATUS_LICENSE_CHECK_FAILED = 7;
    public static final int STATUS_MULTIPLAYER_ERROR_CREATION_NOT_ALLOWED = 6000;
    public static final int STATUS_MULTIPLAYER_ERROR_NOT_TRUSTED_TESTER = 6001;
    public static final int STATUS_NETWORK_ERROR_NO_DATA = 4;
    public static final int STATUS_NETWORK_ERROR_OPERATION_DEFERRED = 5;
    public static final int STATUS_NETWORK_ERROR_OPERATION_FAILED = 6;
    public static final int STATUS_NETWORK_ERROR_STALE_DATA = 3;
    public static final int STATUS_OK = 0;
    public static final int STATUS_PARTICIPANT_NOT_CONNECTED = 7003;
    public static final int STATUS_REAL_TIME_CONNECTION_FAILED = 7000;
    public static final int STATUS_REAL_TIME_INACTIVE_ROOM = 7005;
    public static final int STATUS_REAL_TIME_MESSAGE_FAILED = -1;
    public static final int STATUS_REAL_TIME_MESSAGE_SEND_FAILED = 7001;
    public static final int STATUS_REAL_TIME_ROOM_NOT_JOINED = 7004;
    private final au dt;

    public static final class Builder {
        private final ConnectionCallbacks f50d;
        private String du;
        private int dv = 49;
        private View dw;
        private final OnConnectionFailedListener f51e;
        private String[] f52f = new String[]{Scopes.GAMES};
        private String f53g = "<<default account>>";
        private final Context mContext;

        public Builder(Context context, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            this.mContext = context;
            this.du = context.getPackageName();
            this.f50d = connectionCallbacks;
            this.f51e = onConnectionFailedListener;
        }

        public GamesClient create() {
            return new GamesClient(this.mContext, this.du, this.f53g, this.f50d, this.f51e, this.f52f, this.dv, this.dw);
        }

        public Builder setAccountName(String str) {
            this.f53g = (String) C0242s.m1208d(str);
            return this;
        }

        public Builder setGravityForPopups(int i) {
            this.dv = i;
            return this;
        }

        public Builder setScopes(String... strArr) {
            this.f52f = strArr;
            return this;
        }

        public Builder setViewForPopups(View view) {
            this.dw = (View) C0242s.m1208d(view);
            return this;
        }
    }

    private GamesClient(Context context, String str, String str2, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, String[] strArr, int i, View view) {
        this.dt = new au(context, str, str2, connectionCallbacks, onConnectionFailedListener, strArr, i, view, false);
    }

    public void clearAllNotifications() {
        this.dt.clearNotifications(-1);
    }

    public void clearNotifications(int i) {
        this.dt.clearNotifications(i);
    }

    public void connect() {
        this.dt.connect();
    }

    public void createRoom(RoomConfig roomConfig) {
        this.dt.createRoom(roomConfig);
    }

    public void declineRoomInvitation(String str) {
        this.dt.m556i(str, 0);
    }

    public void disconnect() {
        this.dt.disconnect();
    }

    public void dismissRoomInvitation(String str) {
        this.dt.m555h(str, 0);
    }

    public Intent getAchievementsIntent() {
        return this.dt.getAchievementsIntent();
    }

    public Intent getAllLeaderboardsIntent() {
        return this.dt.getAllLeaderboardsIntent();
    }

    public String getAppId() {
        return this.dt.getAppId();
    }

    public String getCurrentAccountName() {
        return this.dt.getCurrentAccountName();
    }

    public Game getCurrentGame() {
        return this.dt.getCurrentGame();
    }

    public Player getCurrentPlayer() {
        return this.dt.getCurrentPlayer();
    }

    public String getCurrentPlayerId() {
        return this.dt.getCurrentPlayerId();
    }

    public Intent getInvitationInboxIntent() {
        return this.dt.getInvitationInboxIntent();
    }

    public Intent getLeaderboardIntent(String str) {
        return this.dt.getLeaderboardIntent(str);
    }

    public RealTimeSocket getRealTimeSocketForParticipant(String str, String str2) {
        return this.dt.getRealTimeSocketForParticipant(str, str2);
    }

    public Intent getRealTimeWaitingRoomIntent(Room room, int i) {
        return this.dt.getRealTimeWaitingRoomIntent(room, i);
    }

    public Intent getSelectPlayersIntent(int i, int i2) {
        return this.dt.getSelectPlayersIntent(i, i2);
    }

    public Intent getSettingsIntent() {
        return this.dt.getSettingsIntent();
    }

    public void incrementAchievement(String str, int i) {
        this.dt.m547a(null, str, i);
    }

    public void incrementAchievementImmediate(OnAchievementUpdatedListener onAchievementUpdatedListener, String str, int i) {
        this.dt.m547a(onAchievementUpdatedListener, str, i);
    }

    public boolean isConnected() {
        return this.dt.isConnected();
    }

    public boolean isConnecting() {
        return this.dt.isConnecting();
    }

    public boolean isConnectionCallbacksRegistered(ConnectionCallbacks connectionCallbacks) {
        return this.dt.isConnectionCallbacksRegistered(connectionCallbacks);
    }

    public boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener onConnectionFailedListener) {
        return this.dt.isConnectionFailedListenerRegistered(onConnectionFailedListener);
    }

    public void joinRoom(RoomConfig roomConfig) {
        this.dt.joinRoom(roomConfig);
    }

    public void leaveRoom(RoomUpdateListener roomUpdateListener, String str) {
        this.dt.leaveRoom(roomUpdateListener, str);
    }

    public void loadAchievements(OnAchievementsLoadedListener onAchievementsLoadedListener, boolean z) {
        this.dt.loadAchievements(onAchievementsLoadedListener, z);
    }

    public void loadGame(OnGamesLoadedListener onGamesLoadedListener) {
        this.dt.loadGame(onGamesLoadedListener);
    }

    public void loadInvitablePlayers(OnPlayersLoadedListener onPlayersLoadedListener, int i, boolean z) {
        this.dt.m545a(onPlayersLoadedListener, i, false, z);
    }

    public void loadInvitations(OnInvitationsLoadedListener onInvitationsLoadedListener) {
        this.dt.loadInvitations(onInvitationsLoadedListener);
    }

    @Deprecated
    public void loadLeaderboardMetadata(OnLeaderboardMetadataLoadedListener onLeaderboardMetadataLoadedListener) {
        loadLeaderboardMetadata(onLeaderboardMetadataLoadedListener, false);
    }

    @Deprecated
    public void loadLeaderboardMetadata(OnLeaderboardMetadataLoadedListener onLeaderboardMetadataLoadedListener, String str) {
        loadLeaderboardMetadata(onLeaderboardMetadataLoadedListener, str, false);
    }

    public void loadLeaderboardMetadata(OnLeaderboardMetadataLoadedListener onLeaderboardMetadataLoadedListener, String str, boolean z) {
        this.dt.loadLeaderboardMetadata(onLeaderboardMetadataLoadedListener, str, z);
    }

    public void loadLeaderboardMetadata(OnLeaderboardMetadataLoadedListener onLeaderboardMetadataLoadedListener, boolean z) {
        this.dt.loadLeaderboardMetadata(onLeaderboardMetadataLoadedListener, z);
    }

    public void loadMoreInvitablePlayers(OnPlayersLoadedListener onPlayersLoadedListener, int i) {
        this.dt.m545a(onPlayersLoadedListener, i, true, false);
    }

    public void loadMoreScores(OnLeaderboardScoresLoadedListener onLeaderboardScoresLoadedListener, LeaderboardScoreBuffer leaderboardScoreBuffer, int i, int i2) {
        this.dt.loadMoreScores(onLeaderboardScoresLoadedListener, leaderboardScoreBuffer, i, i2);
    }

    public void loadPlayer(OnPlayersLoadedListener onPlayersLoadedListener, String str) {
        this.dt.loadPlayer(onPlayersLoadedListener, str);
    }

    public void loadPlayerCenteredScores(OnLeaderboardScoresLoadedListener onLeaderboardScoresLoadedListener, String str, int i, int i2, int i3) {
        this.dt.loadPlayerCenteredScores(onLeaderboardScoresLoadedListener, str, i, i2, i3, false);
    }

    public void loadPlayerCenteredScores(OnLeaderboardScoresLoadedListener onLeaderboardScoresLoadedListener, String str, int i, int i2, int i3, boolean z) {
        this.dt.loadPlayerCenteredScores(onLeaderboardScoresLoadedListener, str, i, i2, i3, z);
    }

    public void loadTopScores(OnLeaderboardScoresLoadedListener onLeaderboardScoresLoadedListener, String str, int i, int i2, int i3) {
        this.dt.loadTopScores(onLeaderboardScoresLoadedListener, str, i, i2, i3, false);
    }

    public void loadTopScores(OnLeaderboardScoresLoadedListener onLeaderboardScoresLoadedListener, String str, int i, int i2, int i3, boolean z) {
        this.dt.loadTopScores(onLeaderboardScoresLoadedListener, str, i, i2, i3, z);
    }

    public void reconnect() {
        this.dt.disconnect();
        this.dt.connect();
    }

    public void registerConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        this.dt.registerConnectionCallbacks(connectionCallbacks);
    }

    public void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        this.dt.registerConnectionFailedListener(onConnectionFailedListener);
    }

    public void registerInvitationListener(OnInvitationReceivedListener onInvitationReceivedListener) {
        this.dt.registerInvitationListener(onInvitationReceivedListener);
    }

    public void revealAchievement(String str) {
        this.dt.m546a(null, str);
    }

    public void revealAchievementImmediate(OnAchievementUpdatedListener onAchievementUpdatedListener, String str) {
        this.dt.m546a(onAchievementUpdatedListener, str);
    }

    public int sendReliableRealTimeMessage(RealTimeReliableMessageSentListener realTimeReliableMessageSentListener, byte[] bArr, String str, String str2) {
        return this.dt.sendReliableRealTimeMessage(realTimeReliableMessageSentListener, bArr, str, str2);
    }

    public int sendUnreliableRealTimeMessage(byte[] bArr, String str, String str2) {
        return this.dt.m541a(bArr, str, new String[]{str2});
    }

    public int sendUnreliableRealTimeMessage(byte[] bArr, String str, List<String> list) {
        return this.dt.m541a(bArr, str, (String[]) list.toArray(new String[list.size()]));
    }

    public int sendUnreliableRealTimeMessageToAll(byte[] bArr, String str) {
        return this.dt.sendUnreliableRealTimeMessageToAll(bArr, str);
    }

    public void setGravityForPopups(int i) {
        this.dt.setGravityForPopups(i);
    }

    public void setUseNewPlayerNotificationsFirstParty(boolean z) {
        this.dt.setUseNewPlayerNotificationsFirstParty(z);
    }

    public void setViewForPopups(View view) {
        C0242s.m1208d(view);
        this.dt.setViewForPopups(view);
    }

    public void signOut() {
        this.dt.signOut(null);
    }

    public void signOut(OnSignOutCompleteListener onSignOutCompleteListener) {
        this.dt.signOut(onSignOutCompleteListener);
    }

    public void submitScore(String str, long j) {
        this.dt.m548a(null, str, j);
    }

    public void submitScoreImmediate(OnScoreSubmittedListener onScoreSubmittedListener, String str, long j) {
        this.dt.m548a(onScoreSubmittedListener, str, j);
    }

    public void unlockAchievement(String str) {
        this.dt.m552b(null, str);
    }

    public void unlockAchievementImmediate(OnAchievementUpdatedListener onAchievementUpdatedListener, String str) {
        this.dt.m552b(onAchievementUpdatedListener, str);
    }

    public void unregisterConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        this.dt.unregisterConnectionCallbacks(connectionCallbacks);
    }

    public void unregisterConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        this.dt.unregisterConnectionFailedListener(onConnectionFailedListener);
    }

    public void unregisterInvitationListener() {
        this.dt.unregisterInvitationListener();
    }
}
