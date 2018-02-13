package com.google.android.gms.internal;

import android.content.Context;
import android.content.Intent;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcelable;
import android.os.RemoteException;
import android.view.View;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.GameBuffer;
import com.google.android.gms.games.GameEntity;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.OnGamesLoadedListener;
import com.google.android.gms.games.OnPlayersLoadedListener;
import com.google.android.gms.games.OnSignOutCompleteListener;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayerBuffer;
import com.google.android.gms.games.PlayerEntity;
import com.google.android.gms.games.RealTimeSocket;
import com.google.android.gms.games.achievement.AchievementBuffer;
import com.google.android.gms.games.achievement.OnAchievementUpdatedListener;
import com.google.android.gms.games.achievement.OnAchievementsLoadedListener;
import com.google.android.gms.games.leaderboard.LeaderboardBuffer;
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer;
import com.google.android.gms.games.leaderboard.OnLeaderboardMetadataLoadedListener;
import com.google.android.gms.games.leaderboard.OnLeaderboardScoresLoadedListener;
import com.google.android.gms.games.leaderboard.OnScoreSubmittedListener;
import com.google.android.gms.games.leaderboard.SubmitScoreResult;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.InvitationBuffer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.OnInvitationsLoadedListener;
import com.google.android.gms.games.multiplayer.ParticipantUtils;
import com.google.android.gms.games.multiplayer.realtime.C0105a;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeReliableMessageSentListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.android.gms.internal.C0145k.C0117b;
import com.google.android.gms.internal.C0145k.C0118c;
import com.google.android.gms.internal.C0145k.C0224d;
import com.google.android.gms.internal.az.C0148a;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class au extends C0145k<az> {
    private final Map<String, bb> dA;
    private PlayerEntity dB;
    private GameEntity dC;
    private final ba dD;
    private boolean dE = false;
    private final Binder dF;
    private final long dG;
    private final boolean dH;
    private final String dz;
    private final String f61g;

    abstract class C0119c extends C0118c<RoomStatusUpdateListener> {
        final /* synthetic */ au dJ;

        C0119c(au auVar, RoomStatusUpdateListener roomStatusUpdateListener, C0051d c0051d) {
            this.dJ = auVar;
            super(auVar, roomStatusUpdateListener, c0051d);
        }

        protected void m432a(RoomStatusUpdateListener roomStatusUpdateListener, C0051d c0051d) {
            mo529a(roomStatusUpdateListener, this.dJ.m540x(c0051d));
        }

        protected abstract void mo529a(RoomStatusUpdateListener roomStatusUpdateListener, Room room);
    }

    abstract class C0120a extends C0119c {
        private final ArrayList<String> dI = new ArrayList();
        final /* synthetic */ au dJ;

        C0120a(au auVar, RoomStatusUpdateListener roomStatusUpdateListener, C0051d c0051d, String[] strArr) {
            this.dJ = auVar;
            super(auVar, roomStatusUpdateListener, c0051d);
            for (Object add : strArr) {
                this.dI.add(add);
            }
        }

        protected void mo529a(RoomStatusUpdateListener roomStatusUpdateListener, Room room) {
            mo530a(roomStatusUpdateListener, room, this.dI);
        }

        protected abstract void mo530a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList);
    }

    final class aa extends C0120a {
        final /* synthetic */ au dJ;

        aa(au auVar, RoomStatusUpdateListener roomStatusUpdateListener, C0051d c0051d, String[] strArr) {
            this.dJ = auVar;
            super(auVar, roomStatusUpdateListener, c0051d, strArr);
        }

        protected void mo530a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeersDisconnected(room, arrayList);
        }
    }

    final class ab extends C0120a {
        final /* synthetic */ au dJ;

        ab(au auVar, RoomStatusUpdateListener roomStatusUpdateListener, C0051d c0051d, String[] strArr) {
            this.dJ = auVar;
            super(auVar, roomStatusUpdateListener, c0051d, strArr);
        }

        protected void mo530a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeerInvitedToRoom(room, arrayList);
        }
    }

    final class ac extends C0120a {
        final /* synthetic */ au dJ;

        ac(au auVar, RoomStatusUpdateListener roomStatusUpdateListener, C0051d c0051d, String[] strArr) {
            this.dJ = auVar;
            super(auVar, roomStatusUpdateListener, c0051d, strArr);
        }

        protected void mo530a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeerJoined(room, arrayList);
        }
    }

    final class ad extends C0120a {
        final /* synthetic */ au dJ;

        ad(au auVar, RoomStatusUpdateListener roomStatusUpdateListener, C0051d c0051d, String[] strArr) {
            this.dJ = auVar;
            super(auVar, roomStatusUpdateListener, c0051d, strArr);
        }

        protected void mo530a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeerLeft(room, arrayList);
        }
    }

    final class ae extends at {
        final /* synthetic */ au dJ;
        private final OnPlayersLoadedListener dY;

        ae(au auVar, OnPlayersLoadedListener onPlayersLoadedListener) {
            this.dJ = auVar;
            this.dY = (OnPlayersLoadedListener) C0242s.m1205b((Object) onPlayersLoadedListener, (Object) "Listener must not be null");
        }

        public void mo496e(C0051d c0051d) {
            this.dJ.m528a(new af(this.dJ, this.dY, c0051d));
        }
    }

    final class af extends C0118c<OnPlayersLoadedListener> {
        final /* synthetic */ au dJ;

        af(au auVar, OnPlayersLoadedListener onPlayersLoadedListener, C0051d c0051d) {
            this.dJ = auVar;
            super(auVar, onPlayersLoadedListener, c0051d);
        }

        protected void m442a(OnPlayersLoadedListener onPlayersLoadedListener, C0051d c0051d) {
            onPlayersLoadedListener.onPlayersLoaded(c0051d.getStatusCode(), new PlayerBuffer(c0051d));
        }
    }

    final class ag extends C0117b<RealTimeReliableMessageSentListener> {
        final /* synthetic */ au dJ;
        private final String dZ;
        private final int ea;
        private final int f57p;

        ag(au auVar, RealTimeReliableMessageSentListener realTimeReliableMessageSentListener, int i, int i2, String str) {
            this.dJ = auVar;
            super(auVar, realTimeReliableMessageSentListener);
            this.f57p = i;
            this.ea = i2;
            this.dZ = str;
        }

        public void m444a(RealTimeReliableMessageSentListener realTimeReliableMessageSentListener) {
            if (realTimeReliableMessageSentListener != null) {
                realTimeReliableMessageSentListener.onRealTimeMessageSent(this.f57p, this.ea, this.dZ);
            }
        }

        protected void mo526d() {
        }
    }

    final class ah extends at {
        final /* synthetic */ au dJ;
        final RealTimeReliableMessageSentListener eb;

        public ah(au auVar, RealTimeReliableMessageSentListener realTimeReliableMessageSentListener) {
            this.dJ = auVar;
            this.eb = realTimeReliableMessageSentListener;
        }

        public void mo485a(int i, int i2, String str) {
            this.dJ.m528a(new ag(this.dJ, this.eb, i, i2, str));
        }
    }

    final class ai extends C0119c {
        final /* synthetic */ au dJ;

        ai(au auVar, RoomStatusUpdateListener roomStatusUpdateListener, C0051d c0051d) {
            this.dJ = auVar;
            super(auVar, roomStatusUpdateListener, c0051d);
        }

        public void mo529a(RoomStatusUpdateListener roomStatusUpdateListener, Room room) {
            roomStatusUpdateListener.onRoomAutoMatching(room);
        }
    }

    final class aj extends at {
        final /* synthetic */ au dJ;
        private final RoomUpdateListener ec;
        private final RoomStatusUpdateListener ed;
        private final RealTimeMessageReceivedListener ee;

        public aj(au auVar, RoomUpdateListener roomUpdateListener) {
            this.dJ = auVar;
            this.ec = (RoomUpdateListener) C0242s.m1205b((Object) roomUpdateListener, (Object) "Callbacks must not be null");
            this.ed = null;
            this.ee = null;
        }

        public aj(au auVar, RoomUpdateListener roomUpdateListener, RoomStatusUpdateListener roomStatusUpdateListener, RealTimeMessageReceivedListener realTimeMessageReceivedListener) {
            this.dJ = auVar;
            this.ec = (RoomUpdateListener) C0242s.m1205b((Object) roomUpdateListener, (Object) "Callbacks must not be null");
            this.ed = roomStatusUpdateListener;
            this.ee = realTimeMessageReceivedListener;
        }

        public void mo489a(C0051d c0051d, String[] strArr) {
            this.dJ.m528a(new ab(this.dJ, this.ed, c0051d, strArr));
        }

        public void mo491b(C0051d c0051d, String[] strArr) {
            this.dJ.m528a(new ac(this.dJ, this.ed, c0051d, strArr));
        }

        public void mo493c(C0051d c0051d, String[] strArr) {
            this.dJ.m528a(new ad(this.dJ, this.ed, c0051d, strArr));
        }

        public void mo495d(C0051d c0051d, String[] strArr) {
            this.dJ.m528a(new C0144z(this.dJ, this.ed, c0051d, strArr));
        }

        public void mo497e(C0051d c0051d, String[] strArr) {
            this.dJ.m528a(new C0143y(this.dJ, this.ed, c0051d, strArr));
        }

        public void mo499f(C0051d c0051d, String[] strArr) {
            this.dJ.m528a(new aa(this.dJ, this.ed, c0051d, strArr));
        }

        public void mo507n(C0051d c0051d) {
            this.dJ.m528a(new am(this.dJ, this.ec, c0051d));
        }

        public void mo508o(C0051d c0051d) {
            this.dJ.m528a(new C0134p(this.dJ, this.ec, c0051d));
        }

        public void onLeftRoom(int i, String str) {
            this.dJ.m528a(new C0139u(this.dJ, this.ec, i, str));
        }

        public void onP2PConnected(String str) {
            this.dJ.m528a(new C0141w(this.dJ, this.ed, str));
        }

        public void onP2PDisconnected(String str) {
            this.dJ.m528a(new C0142x(this.dJ, this.ed, str));
        }

        public void onRealTimeMessageReceived(RealTimeMessage realTimeMessage) {
            ax.m561a("GamesClient", "RoomBinderCallbacks: onRealTimeMessageReceived");
            this.dJ.m528a(new C0140v(this.dJ, this.ee, realTimeMessage));
        }

        public void mo515p(C0051d c0051d) {
            this.dJ.m528a(new al(this.dJ, this.ed, c0051d));
        }

        public void mo516q(C0051d c0051d) {
            this.dJ.m528a(new ai(this.dJ, this.ed, c0051d));
        }

        public void mo517r(C0051d c0051d) {
            this.dJ.m528a(new ak(this.dJ, this.ec, c0051d));
        }

        public void mo518s(C0051d c0051d) {
            this.dJ.m528a(new C0126h(this.dJ, this.ed, c0051d));
        }

        public void mo519t(C0051d c0051d) {
            this.dJ.m528a(new C0127i(this.dJ, this.ed, c0051d));
        }
    }

    abstract class C0121b extends C0118c<RoomUpdateListener> {
        final /* synthetic */ au dJ;

        C0121b(au auVar, RoomUpdateListener roomUpdateListener, C0051d c0051d) {
            this.dJ = auVar;
            super(auVar, roomUpdateListener, c0051d);
        }

        protected void m462a(RoomUpdateListener roomUpdateListener, C0051d c0051d) {
            mo531a(roomUpdateListener, this.dJ.m540x(c0051d), c0051d.getStatusCode());
        }

        protected abstract void mo531a(RoomUpdateListener roomUpdateListener, Room room, int i);
    }

    final class ak extends C0121b {
        final /* synthetic */ au dJ;

        ak(au auVar, RoomUpdateListener roomUpdateListener, C0051d c0051d) {
            this.dJ = auVar;
            super(auVar, roomUpdateListener, c0051d);
        }

        public void mo531a(RoomUpdateListener roomUpdateListener, Room room, int i) {
            roomUpdateListener.onRoomConnected(i, room);
        }
    }

    final class al extends C0119c {
        final /* synthetic */ au dJ;

        al(au auVar, RoomStatusUpdateListener roomStatusUpdateListener, C0051d c0051d) {
            this.dJ = auVar;
            super(auVar, roomStatusUpdateListener, c0051d);
        }

        public void mo529a(RoomStatusUpdateListener roomStatusUpdateListener, Room room) {
            roomStatusUpdateListener.onRoomConnecting(room);
        }
    }

    final class am extends C0121b {
        final /* synthetic */ au dJ;

        public am(au auVar, RoomUpdateListener roomUpdateListener, C0051d c0051d) {
            this.dJ = auVar;
            super(auVar, roomUpdateListener, c0051d);
        }

        public void mo531a(RoomUpdateListener roomUpdateListener, Room room, int i) {
            roomUpdateListener.onRoomCreated(i, room);
        }
    }

    final class an extends at {
        final /* synthetic */ au dJ;
        private final OnSignOutCompleteListener ef;

        public an(au auVar, OnSignOutCompleteListener onSignOutCompleteListener) {
            this.dJ = auVar;
            this.ef = (OnSignOutCompleteListener) C0242s.m1205b((Object) onSignOutCompleteListener, (Object) "Listener must not be null");
        }

        public void onSignOutComplete() {
            this.dJ.m528a(new ao(this.dJ, this.ef));
        }
    }

    final class ao extends C0117b<OnSignOutCompleteListener> {
        final /* synthetic */ au dJ;

        public ao(au auVar, OnSignOutCompleteListener onSignOutCompleteListener) {
            this.dJ = auVar;
            super(auVar, onSignOutCompleteListener);
        }

        public void m468a(OnSignOutCompleteListener onSignOutCompleteListener) {
            onSignOutCompleteListener.onSignOutComplete();
        }

        protected void mo526d() {
        }
    }

    final class ap extends at {
        final /* synthetic */ au dJ;
        private final OnScoreSubmittedListener eg;

        public ap(au auVar, OnScoreSubmittedListener onScoreSubmittedListener) {
            this.dJ = auVar;
            this.eg = (OnScoreSubmittedListener) C0242s.m1205b((Object) onScoreSubmittedListener, (Object) "Listener must not be null");
        }

        public void mo494d(C0051d c0051d) {
            this.dJ.m528a(new aq(this.dJ, this.eg, new SubmitScoreResult(c0051d)));
        }
    }

    final class aq extends C0117b<OnScoreSubmittedListener> {
        final /* synthetic */ au dJ;
        private final SubmitScoreResult eh;

        public aq(au auVar, OnScoreSubmittedListener onScoreSubmittedListener, SubmitScoreResult submitScoreResult) {
            this.dJ = auVar;
            super(auVar, onScoreSubmittedListener);
            this.eh = submitScoreResult;
        }

        public void m472a(OnScoreSubmittedListener onScoreSubmittedListener) {
            onScoreSubmittedListener.onScoreSubmitted(this.eh.getStatusCode(), this.eh);
        }

        protected void mo526d() {
        }
    }

    final class C0122d extends at {
        final /* synthetic */ au dJ;
        private final OnAchievementUpdatedListener dK;

        C0122d(au auVar, OnAchievementUpdatedListener onAchievementUpdatedListener) {
            this.dJ = auVar;
            this.dK = (OnAchievementUpdatedListener) C0242s.m1205b((Object) onAchievementUpdatedListener, (Object) "Listener must not be null");
        }

        public void onAchievementUpdated(int i, String str) {
            this.dJ.m528a(new C0123e(this.dJ, this.dK, i, str));
        }
    }

    final class C0123e extends C0117b<OnAchievementUpdatedListener> {
        final /* synthetic */ au dJ;
        private final String dL;
        private final int f58p;

        C0123e(au auVar, OnAchievementUpdatedListener onAchievementUpdatedListener, int i, String str) {
            this.dJ = auVar;
            super(auVar, onAchievementUpdatedListener);
            this.f58p = i;
            this.dL = str;
        }

        protected void m475a(OnAchievementUpdatedListener onAchievementUpdatedListener) {
            onAchievementUpdatedListener.onAchievementUpdated(this.f58p, this.dL);
        }

        protected void mo526d() {
        }
    }

    final class C0124f extends at {
        final /* synthetic */ au dJ;
        private final OnAchievementsLoadedListener dM;

        C0124f(au auVar, OnAchievementsLoadedListener onAchievementsLoadedListener) {
            this.dJ = auVar;
            this.dM = (OnAchievementsLoadedListener) C0242s.m1205b((Object) onAchievementsLoadedListener, (Object) "Listener must not be null");
        }

        public void mo490b(C0051d c0051d) {
            this.dJ.m528a(new C0125g(this.dJ, this.dM, c0051d));
        }
    }

    final class C0125g extends C0118c<OnAchievementsLoadedListener> {
        final /* synthetic */ au dJ;

        C0125g(au auVar, OnAchievementsLoadedListener onAchievementsLoadedListener, C0051d c0051d) {
            this.dJ = auVar;
            super(auVar, onAchievementsLoadedListener, c0051d);
        }

        protected void m479a(OnAchievementsLoadedListener onAchievementsLoadedListener, C0051d c0051d) {
            onAchievementsLoadedListener.onAchievementsLoaded(c0051d.getStatusCode(), new AchievementBuffer(c0051d));
        }
    }

    final class C0126h extends C0119c {
        final /* synthetic */ au dJ;

        C0126h(au auVar, RoomStatusUpdateListener roomStatusUpdateListener, C0051d c0051d) {
            this.dJ = auVar;
            super(auVar, roomStatusUpdateListener, c0051d);
        }

        public void mo529a(RoomStatusUpdateListener roomStatusUpdateListener, Room room) {
            roomStatusUpdateListener.onConnectedToRoom(room);
        }
    }

    final class C0127i extends C0119c {
        final /* synthetic */ au dJ;

        C0127i(au auVar, RoomStatusUpdateListener roomStatusUpdateListener, C0051d c0051d) {
            this.dJ = auVar;
            super(auVar, roomStatusUpdateListener, c0051d);
        }

        public void mo529a(RoomStatusUpdateListener roomStatusUpdateListener, Room room) {
            roomStatusUpdateListener.onDisconnectedFromRoom(room);
        }
    }

    final class C0128j extends at {
        final /* synthetic */ au dJ;
        private final OnGamesLoadedListener dN;

        C0128j(au auVar, OnGamesLoadedListener onGamesLoadedListener) {
            this.dJ = auVar;
            this.dN = (OnGamesLoadedListener) C0242s.m1205b((Object) onGamesLoadedListener, (Object) "Listener must not be null");
        }

        public void mo500g(C0051d c0051d) {
            this.dJ.m528a(new C0129k(this.dJ, this.dN, c0051d));
        }
    }

    final class C0129k extends C0118c<OnGamesLoadedListener> {
        final /* synthetic */ au dJ;

        C0129k(au auVar, OnGamesLoadedListener onGamesLoadedListener, C0051d c0051d) {
            this.dJ = auVar;
            super(auVar, onGamesLoadedListener, c0051d);
        }

        protected void m484a(OnGamesLoadedListener onGamesLoadedListener, C0051d c0051d) {
            onGamesLoadedListener.onGamesLoaded(c0051d.getStatusCode(), new GameBuffer(c0051d));
        }
    }

    final class C0130l extends at {
        final /* synthetic */ au dJ;
        private final OnInvitationReceivedListener dO;

        C0130l(au auVar, OnInvitationReceivedListener onInvitationReceivedListener) {
            this.dJ = auVar;
            this.dO = onInvitationReceivedListener;
        }

        public void mo504k(C0051d c0051d) {
            InvitationBuffer invitationBuffer = new InvitationBuffer(c0051d);
            Invitation invitation = null;
            try {
                if (invitationBuffer.getCount() > 0) {
                    invitation = (Invitation) ((Invitation) invitationBuffer.get(0)).freeze();
                }
                invitationBuffer.close();
                if (invitation != null) {
                    this.dJ.m528a(new C0131m(this.dJ, this.dO, invitation));
                }
            } catch (Throwable th) {
                invitationBuffer.close();
            }
        }
    }

    final class C0131m extends C0117b<OnInvitationReceivedListener> {
        final /* synthetic */ au dJ;
        private final Invitation dP;

        C0131m(au auVar, OnInvitationReceivedListener onInvitationReceivedListener, Invitation invitation) {
            this.dJ = auVar;
            super(auVar, onInvitationReceivedListener);
            this.dP = invitation;
        }

        protected void m487a(OnInvitationReceivedListener onInvitationReceivedListener) {
            onInvitationReceivedListener.onInvitationReceived(this.dP);
        }

        protected void mo526d() {
        }
    }

    final class C0132n extends at {
        final /* synthetic */ au dJ;
        private final OnInvitationsLoadedListener dQ;

        C0132n(au auVar, OnInvitationsLoadedListener onInvitationsLoadedListener) {
            this.dJ = auVar;
            this.dQ = onInvitationsLoadedListener;
        }

        public void mo503j(C0051d c0051d) {
            this.dJ.m528a(new C0133o(this.dJ, this.dQ, c0051d));
        }
    }

    final class C0133o extends C0118c<OnInvitationsLoadedListener> {
        final /* synthetic */ au dJ;

        C0133o(au auVar, OnInvitationsLoadedListener onInvitationsLoadedListener, C0051d c0051d) {
            this.dJ = auVar;
            super(auVar, onInvitationsLoadedListener, c0051d);
        }

        protected void m491a(OnInvitationsLoadedListener onInvitationsLoadedListener, C0051d c0051d) {
            onInvitationsLoadedListener.onInvitationsLoaded(c0051d.getStatusCode(), new InvitationBuffer(c0051d));
        }
    }

    final class C0134p extends C0121b {
        final /* synthetic */ au dJ;

        public C0134p(au auVar, RoomUpdateListener roomUpdateListener, C0051d c0051d) {
            this.dJ = auVar;
            super(auVar, roomUpdateListener, c0051d);
        }

        public void mo531a(RoomUpdateListener roomUpdateListener, Room room, int i) {
            roomUpdateListener.onJoinedRoom(i, room);
        }
    }

    final class C0135q extends at {
        final /* synthetic */ au dJ;
        private final OnLeaderboardScoresLoadedListener dR;

        C0135q(au auVar, OnLeaderboardScoresLoadedListener onLeaderboardScoresLoadedListener) {
            this.dJ = auVar;
            this.dR = (OnLeaderboardScoresLoadedListener) C0242s.m1205b((Object) onLeaderboardScoresLoadedListener, (Object) "Listener must not be null");
        }

        public void mo488a(C0051d c0051d, C0051d c0051d2) {
            this.dJ.m528a(new C0136r(this.dJ, this.dR, c0051d, c0051d2));
        }
    }

    final class C0136r extends C0117b<OnLeaderboardScoresLoadedListener> {
        final /* synthetic */ au dJ;
        private final C0051d dS;
        private final C0051d dT;

        C0136r(au auVar, OnLeaderboardScoresLoadedListener onLeaderboardScoresLoadedListener, C0051d c0051d, C0051d c0051d2) {
            this.dJ = auVar;
            super(auVar, onLeaderboardScoresLoadedListener);
            this.dS = c0051d;
            this.dT = c0051d2;
        }

        protected void m495a(OnLeaderboardScoresLoadedListener onLeaderboardScoresLoadedListener) {
            C0051d c0051d = null;
            C0051d c0051d2 = this.dS;
            C0051d c0051d3 = this.dT;
            if (onLeaderboardScoresLoadedListener != null) {
                try {
                    onLeaderboardScoresLoadedListener.onLeaderboardScoresLoaded(c0051d3.getStatusCode(), new LeaderboardBuffer(c0051d2), new LeaderboardScoreBuffer(c0051d3));
                    c0051d2 = null;
                } catch (Throwable th) {
                    if (c0051d2 != null) {
                        c0051d2.close();
                    }
                    if (c0051d3 != null) {
                        c0051d3.close();
                    }
                }
            } else {
                c0051d = c0051d2;
                c0051d2 = c0051d3;
            }
            if (c0051d != null) {
                c0051d.close();
            }
            if (c0051d2 != null) {
                c0051d2.close();
            }
        }

        protected void mo526d() {
            if (this.dS != null) {
                this.dS.close();
            }
            if (this.dT != null) {
                this.dT.close();
            }
        }
    }

    final class C0137s extends at {
        final /* synthetic */ au dJ;
        private final OnLeaderboardMetadataLoadedListener dU;

        C0137s(au auVar, OnLeaderboardMetadataLoadedListener onLeaderboardMetadataLoadedListener) {
            this.dJ = auVar;
            this.dU = (OnLeaderboardMetadataLoadedListener) C0242s.m1205b((Object) onLeaderboardMetadataLoadedListener, (Object) "Listener must not be null");
        }

        public void mo492c(C0051d c0051d) {
            this.dJ.m528a(new C0138t(this.dJ, this.dU, c0051d));
        }
    }

    final class C0138t extends C0118c<OnLeaderboardMetadataLoadedListener> {
        final /* synthetic */ au dJ;

        C0138t(au auVar, OnLeaderboardMetadataLoadedListener onLeaderboardMetadataLoadedListener, C0051d c0051d) {
            this.dJ = auVar;
            super(auVar, onLeaderboardMetadataLoadedListener, c0051d);
        }

        protected void m499a(OnLeaderboardMetadataLoadedListener onLeaderboardMetadataLoadedListener, C0051d c0051d) {
            onLeaderboardMetadataLoadedListener.onLeaderboardMetadataLoaded(c0051d.getStatusCode(), new LeaderboardBuffer(c0051d));
        }
    }

    final class C0139u extends C0117b<RoomUpdateListener> {
        final /* synthetic */ au dJ;
        private final String dV;
        private final int f59p;

        C0139u(au auVar, RoomUpdateListener roomUpdateListener, int i, String str) {
            this.dJ = auVar;
            super(auVar, roomUpdateListener);
            this.f59p = i;
            this.dV = str;
        }

        public void m501a(RoomUpdateListener roomUpdateListener) {
            roomUpdateListener.onLeftRoom(this.f59p, this.dV);
        }

        protected void mo526d() {
        }
    }

    final class C0140v extends C0117b<RealTimeMessageReceivedListener> {
        final /* synthetic */ au dJ;
        private final RealTimeMessage dW;

        C0140v(au auVar, RealTimeMessageReceivedListener realTimeMessageReceivedListener, RealTimeMessage realTimeMessage) {
            this.dJ = auVar;
            super(auVar, realTimeMessageReceivedListener);
            this.dW = realTimeMessage;
        }

        public void m504a(RealTimeMessageReceivedListener realTimeMessageReceivedListener) {
            ax.m561a("GamesClient", "Deliver Message received callback");
            if (realTimeMessageReceivedListener != null) {
                realTimeMessageReceivedListener.onRealTimeMessageReceived(this.dW);
            }
        }

        protected void mo526d() {
        }
    }

    final class C0141w extends C0117b<RoomStatusUpdateListener> {
        final /* synthetic */ au dJ;
        private final String dX;

        C0141w(au auVar, RoomStatusUpdateListener roomStatusUpdateListener, String str) {
            this.dJ = auVar;
            super(auVar, roomStatusUpdateListener);
            this.dX = str;
        }

        public void m507a(RoomStatusUpdateListener roomStatusUpdateListener) {
            if (roomStatusUpdateListener != null) {
                roomStatusUpdateListener.onP2PConnected(this.dX);
            }
        }

        protected void mo526d() {
        }
    }

    final class C0142x extends C0117b<RoomStatusUpdateListener> {
        final /* synthetic */ au dJ;
        private final String dX;

        C0142x(au auVar, RoomStatusUpdateListener roomStatusUpdateListener, String str) {
            this.dJ = auVar;
            super(auVar, roomStatusUpdateListener);
            this.dX = str;
        }

        public void m510a(RoomStatusUpdateListener roomStatusUpdateListener) {
            if (roomStatusUpdateListener != null) {
                roomStatusUpdateListener.onP2PDisconnected(this.dX);
            }
        }

        protected void mo526d() {
        }
    }

    final class C0143y extends C0120a {
        final /* synthetic */ au dJ;

        C0143y(au auVar, RoomStatusUpdateListener roomStatusUpdateListener, C0051d c0051d, String[] strArr) {
            this.dJ = auVar;
            super(auVar, roomStatusUpdateListener, c0051d, strArr);
        }

        protected void mo530a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeersConnected(room, arrayList);
        }
    }

    final class C0144z extends C0120a {
        final /* synthetic */ au dJ;

        C0144z(au auVar, RoomStatusUpdateListener roomStatusUpdateListener, C0051d c0051d, String[] strArr) {
            this.dJ = auVar;
            super(auVar, roomStatusUpdateListener, c0051d, strArr);
        }

        protected void mo530a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeerDeclined(room, arrayList);
        }
    }

    public au(Context context, String str, String str2, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, String[] strArr, int i, View view, boolean z) {
        super(context, connectionCallbacks, onConnectionFailedListener, strArr);
        this.dz = str;
        this.f61g = (String) C0242s.m1208d(str2);
        this.dF = new Binder();
        this.dA = new HashMap();
        this.dD = ba.m754a(this, i);
        setViewForPopups(view);
        this.dG = (long) hashCode();
        this.dH = z;
    }

    private void av() {
        this.dB = null;
    }

    private void aw() {
        for (bb close : this.dA.values()) {
            try {
                close.close();
            } catch (Throwable e) {
                ax.m562a("GamesClient", "IOException:", e);
            }
        }
        this.dA.clear();
    }

    private bb m539t(String str) {
        try {
            String v = ((az) m525C()).mo617v(str);
            if (v == null) {
                return null;
            }
            ax.m565d("GamesClient", "Creating a socket to bind to:" + v);
            LocalSocket localSocket = new LocalSocket();
            try {
                localSocket.connect(new LocalSocketAddress(v));
                bb bbVar = new bb(localSocket, str);
                this.dA.put(str, bbVar);
                return bbVar;
            } catch (IOException e) {
                ax.m564c("GamesClient", "connect() call failed on socket: " + e.getMessage());
                return null;
            }
        } catch (RemoteException e2) {
            ax.m564c("GamesClient", "Unable to create socket. Service died.");
            return null;
        }
    }

    private Room m540x(C0051d c0051d) {
        C0105a c0105a = new C0105a(c0051d);
        Room room = null;
        try {
            if (c0105a.getCount() > 0) {
                room = (Room) ((Room) c0105a.get(0)).freeze();
            }
            c0105a.close();
            return room;
        } catch (Throwable th) {
            c0105a.close();
        }
    }

    public int m541a(byte[] bArr, String str, String[] strArr) {
        C0242s.m1205b((Object) strArr, (Object) "Participant IDs must not be null");
        try {
            return ((az) m525C()).mo569b(bArr, str, strArr);
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
            return -1;
        }
    }

    protected void mo532a(int i, IBinder iBinder, Bundle bundle) {
        if (i == 0 && bundle != null) {
            this.dE = bundle.getBoolean("show_welcome_popup");
        }
        super.mo532a(i, iBinder, bundle);
    }

    public void m543a(IBinder iBinder, Bundle bundle) {
        if (isConnected()) {
            try {
                ((az) m525C()).mo543a(iBinder, bundle);
            } catch (RemoteException e) {
                ax.m563b("GamesClient", "service died");
            }
        }
    }

    protected void mo533a(ConnectionResult connectionResult) {
        super.mo533a(connectionResult);
        this.dE = false;
    }

    public void m545a(OnPlayersLoadedListener onPlayersLoadedListener, int i, boolean z, boolean z2) {
        try {
            ((az) m525C()).mo546a(new ae(this, onPlayersLoadedListener), i, z, z2);
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    public void m546a(OnAchievementUpdatedListener onAchievementUpdatedListener, String str) {
        if (onAchievementUpdatedListener == null) {
            ay ayVar = null;
        } else {
            Object c0122d = new C0122d(this, onAchievementUpdatedListener);
        }
        try {
            ((az) m525C()).mo557a(ayVar, str, this.dD.aD(), this.dD.aC());
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    public void m547a(OnAchievementUpdatedListener onAchievementUpdatedListener, String str, int i) {
        try {
            ((az) m525C()).mo553a(onAchievementUpdatedListener == null ? null : new C0122d(this, onAchievementUpdatedListener), str, i, this.dD.aD(), this.dD.aC());
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    public void m548a(OnScoreSubmittedListener onScoreSubmittedListener, String str, long j) {
        if (onScoreSubmittedListener == null) {
            ay ayVar = null;
        } else {
            Object apVar = new ap(this, onScoreSubmittedListener);
        }
        try {
            ((az) m525C()).mo556a(ayVar, str, j);
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    protected void mo534a(C0233p c0233p, C0224d c0224d) throws RemoteException {
        String locale = getContext().getResources().getConfiguration().locale.toString();
        Bundle bundle = new Bundle();
        bundle.putBoolean("com.google.android.gms.games.key.isHeadless", this.dH);
        c0233p.mo964a(c0224d, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, getContext().getPackageName(), this.f61g, m535x(), this.dz, this.dD.aD(), locale, bundle);
    }

    protected void mo535a(String... strArr) {
        boolean z = false;
        int i = 0;
        for (String str : strArr) {
            if (str.equals(Scopes.GAMES)) {
                z = true;
            } else if (str.equals("https://www.googleapis.com/auth/games.firstparty")) {
                i = 1;
            }
        }
        if (i != 0) {
            C0242s.m1203a(!z, String.format("Cannot have both %s and %s!", new Object[]{Scopes.GAMES, "https://www.googleapis.com/auth/games.firstparty"}));
        } else {
            C0242s.m1203a(z, String.format("GamesClient requires %s to function.", new Object[]{Scopes.GAMES}));
        }
    }

    public void ax() {
        if (isConnected()) {
            try {
                ((az) m525C()).ax();
            } catch (RemoteException e) {
                ax.m563b("GamesClient", "service died");
            }
        }
    }

    protected String mo536b() {
        return "com.google.android.gms.games.service.START";
    }

    public void m552b(OnAchievementUpdatedListener onAchievementUpdatedListener, String str) {
        if (onAchievementUpdatedListener == null) {
            ay ayVar = null;
        } else {
            Object c0122d = new C0122d(this, onAchievementUpdatedListener);
        }
        try {
            ((az) m525C()).mo577b(ayVar, str, this.dD.aD(), this.dD.aC());
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    protected /* synthetic */ IInterface mo537c(IBinder iBinder) {
        return m557m(iBinder);
    }

    protected String mo538c() {
        return "com.google.android.gms.games.internal.IGamesService";
    }

    public void clearNotifications(int i) {
        try {
            ((az) m525C()).clearNotifications(i);
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    public void connect() {
        av();
        super.connect();
    }

    public void createRoom(RoomConfig roomConfig) {
        try {
            ((az) m525C()).mo549a(new aj(this, roomConfig.getRoomUpdateListener(), roomConfig.getRoomStatusUpdateListener(), roomConfig.getMessageReceivedListener()), this.dF, roomConfig.getVariant(), roomConfig.getInvitedPlayerIds(), roomConfig.getAutoMatchCriteria(), roomConfig.isSocketEnabled(), this.dG);
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    public void disconnect() {
        this.dE = false;
        if (isConnected()) {
            try {
                az azVar = (az) m525C();
                azVar.ax();
                azVar.mo571b(this.dG);
                azVar.mo542a(this.dG);
            } catch (RemoteException e) {
                ax.m563b("GamesClient", "Failed to notify client disconnect.");
            }
        }
        aw();
        super.disconnect();
    }

    public Intent getAchievementsIntent() {
        m524B();
        Intent intent = new Intent("com.google.android.gms.games.VIEW_ACHIEVEMENTS");
        intent.addFlags(67108864);
        return aw.m560b(intent);
    }

    public Intent getAllLeaderboardsIntent() {
        m524B();
        Intent intent = new Intent("com.google.android.gms.games.VIEW_LEADERBOARDS");
        intent.putExtra("com.google.android.gms.games.GAME_PACKAGE_NAME", this.dz);
        intent.addFlags(67108864);
        return aw.m560b(intent);
    }

    public String getAppId() {
        try {
            return ((az) m525C()).getAppId();
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
            return null;
        }
    }

    public String getCurrentAccountName() {
        try {
            return ((az) m525C()).getCurrentAccountName();
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
            return null;
        }
    }

    public Game getCurrentGame() {
        m524B();
        synchronized (this) {
            if (this.dC == null) {
                GameBuffer gameBuffer;
                try {
                    gameBuffer = new GameBuffer(((az) m525C()).aA());
                    if (gameBuffer.getCount() > 0) {
                        this.dC = (GameEntity) gameBuffer.get(0).freeze();
                    }
                    gameBuffer.close();
                } catch (RemoteException e) {
                    ax.m563b("GamesClient", "service died");
                } catch (Throwable th) {
                    gameBuffer.close();
                }
            }
        }
        return this.dC;
    }

    public Player getCurrentPlayer() {
        PlayerBuffer playerBuffer;
        m524B();
        synchronized (this) {
            if (this.dB == null) {
                try {
                    playerBuffer = new PlayerBuffer(((az) m525C()).ay());
                    if (playerBuffer.getCount() > 0) {
                        this.dB = (PlayerEntity) playerBuffer.get(0).freeze();
                    }
                    playerBuffer.close();
                } catch (RemoteException e) {
                    ax.m563b("GamesClient", "service died");
                } catch (Throwable th) {
                    playerBuffer.close();
                }
            }
        }
        return this.dB;
    }

    public String getCurrentPlayerId() {
        try {
            return ((az) m525C()).getCurrentPlayerId();
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
            return null;
        }
    }

    public Intent getInvitationInboxIntent() {
        m524B();
        Intent intent = new Intent("com.google.android.gms.games.SHOW_INVITATIONS");
        intent.putExtra("com.google.android.gms.games.GAME_PACKAGE_NAME", this.dz);
        return aw.m560b(intent);
    }

    public Intent getLeaderboardIntent(String str) {
        m524B();
        Intent intent = new Intent("com.google.android.gms.games.VIEW_LEADERBOARD_SCORES");
        intent.putExtra("com.google.android.gms.games.LEADERBOARD_ID", str);
        intent.addFlags(67108864);
        return aw.m560b(intent);
    }

    public RealTimeSocket getRealTimeSocketForParticipant(String str, String str2) {
        if (str2 == null || !ParticipantUtils.m228z(str2)) {
            throw new IllegalArgumentException("Bad participant ID");
        }
        bb bbVar = (bb) this.dA.get(str2);
        return (bbVar == null || bbVar.isClosed()) ? m539t(str2) : bbVar;
    }

    public Intent getRealTimeWaitingRoomIntent(Room room, int i) {
        m524B();
        Intent intent = new Intent("com.google.android.gms.games.SHOW_REAL_TIME_WAITING_ROOM");
        C0242s.m1205b((Object) room, (Object) "Room parameter must not be null");
        intent.putExtra(GamesClient.EXTRA_ROOM, (Parcelable) room.freeze());
        C0242s.m1203a(i >= 0, "minParticipantsToStart must be >= 0");
        intent.putExtra("com.google.android.gms.games.MIN_PARTICIPANTS_TO_START", i);
        return aw.m560b(intent);
    }

    public Intent getSelectPlayersIntent(int i, int i2) {
        m524B();
        Intent intent = new Intent("com.google.android.gms.games.SELECT_PLAYERS");
        intent.putExtra("com.google.android.gms.games.MIN_SELECTIONS", i);
        intent.putExtra("com.google.android.gms.games.MAX_SELECTIONS", i2);
        return aw.m560b(intent);
    }

    public Intent getSettingsIntent() {
        m524B();
        Intent intent = new Intent("com.google.android.gms.games.SHOW_SETTINGS");
        intent.putExtra("com.google.android.gms.games.GAME_PACKAGE_NAME", this.dz);
        intent.addFlags(67108864);
        return aw.m560b(intent);
    }

    public void m555h(String str, int i) {
        try {
            ((az) m525C()).mo607h(str, i);
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    public void m556i(String str, int i) {
        try {
            ((az) m525C()).mo610i(str, i);
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    public void joinRoom(RoomConfig roomConfig) {
        try {
            ((az) m525C()).mo550a(new aj(this, roomConfig.getRoomUpdateListener(), roomConfig.getRoomStatusUpdateListener(), roomConfig.getMessageReceivedListener()), this.dF, roomConfig.getInvitationId(), roomConfig.isSocketEnabled(), this.dG);
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    public void leaveRoom(RoomUpdateListener roomUpdateListener, String str) {
        try {
            ((az) m525C()).mo596e(new aj(this, roomUpdateListener), str);
            aw();
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    public void loadAchievements(OnAchievementsLoadedListener onAchievementsLoadedListener, boolean z) {
        try {
            ((az) m525C()).mo582b(new C0124f(this, onAchievementsLoadedListener), z);
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    public void loadGame(OnGamesLoadedListener onGamesLoadedListener) {
        try {
            ((az) m525C()).mo590d(new C0128j(this, onGamesLoadedListener));
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    public void loadInvitations(OnInvitationsLoadedListener onInvitationsLoadedListener) {
        try {
            ((az) m525C()).mo594e(new C0132n(this, onInvitationsLoadedListener));
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    public void loadLeaderboardMetadata(OnLeaderboardMetadataLoadedListener onLeaderboardMetadataLoadedListener, String str, boolean z) {
        try {
            ((az) m525C()).mo587c(new C0137s(this, onLeaderboardMetadataLoadedListener), str, z);
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    public void loadLeaderboardMetadata(OnLeaderboardMetadataLoadedListener onLeaderboardMetadataLoadedListener, boolean z) {
        try {
            ((az) m525C()).mo588c(new C0137s(this, onLeaderboardMetadataLoadedListener), z);
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    public void loadMoreScores(OnLeaderboardScoresLoadedListener onLeaderboardScoresLoadedListener, LeaderboardScoreBuffer leaderboardScoreBuffer, int i, int i2) {
        try {
            ((az) m525C()).mo548a(new C0135q(this, onLeaderboardScoresLoadedListener), leaderboardScoreBuffer.aF().aG(), i, i2);
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    public void loadPlayer(OnPlayersLoadedListener onPlayersLoadedListener, String str) {
        try {
            ((az) m525C()).mo585c(new ae(this, onPlayersLoadedListener), str);
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    public void loadPlayerCenteredScores(OnLeaderboardScoresLoadedListener onLeaderboardScoresLoadedListener, String str, int i, int i2, int i3, boolean z) {
        try {
            ((az) m525C()).mo575b(new C0135q(this, onLeaderboardScoresLoadedListener), str, i, i2, i3, z);
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    public void loadTopScores(OnLeaderboardScoresLoadedListener onLeaderboardScoresLoadedListener, String str, int i, int i2, int i3, boolean z) {
        try {
            ((az) m525C()).mo552a(new C0135q(this, onLeaderboardScoresLoadedListener), str, i, i2, i3, z);
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    protected az m557m(IBinder iBinder) {
        return C0148a.m746o(iBinder);
    }

    public void registerInvitationListener(OnInvitationReceivedListener onInvitationReceivedListener) {
        try {
            ((az) m525C()).mo547a(new C0130l(this, onInvitationReceivedListener), this.dG);
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    public int sendReliableRealTimeMessage(RealTimeReliableMessageSentListener realTimeReliableMessageSentListener, byte[] bArr, String str, String str2) {
        try {
            return ((az) m525C()).mo541a(new ah(this, realTimeReliableMessageSentListener), bArr, str, str2);
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
            return -1;
        }
    }

    public int sendUnreliableRealTimeMessageToAll(byte[] bArr, String str) {
        try {
            return ((az) m525C()).mo569b(bArr, str, null);
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
            return -1;
        }
    }

    public void setGravityForPopups(int i) {
        this.dD.setGravity(i);
    }

    public void setUseNewPlayerNotificationsFirstParty(boolean z) {
        try {
            ((az) m525C()).setUseNewPlayerNotificationsFirstParty(z);
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    public void setViewForPopups(View view) {
        this.dD.mo628a(view);
    }

    public void signOut(OnSignOutCompleteListener onSignOutCompleteListener) {
        if (onSignOutCompleteListener == null) {
            ay ayVar = null;
        } else {
            Object anVar = new an(this, onSignOutCompleteListener);
        }
        try {
            ((az) m525C()).mo544a(ayVar);
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    public void unregisterInvitationListener() {
        try {
            ((az) m525C()).mo571b(this.dG);
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
        }
    }

    protected void mo539y() {
        super.mo539y();
        if (this.dE) {
            this.dD.aB();
            this.dE = false;
        }
    }

    protected Bundle mo540z() {
        try {
            Bundle z = ((az) m525C()).mo621z();
            if (z == null) {
                return z;
            }
            z.setClassLoader(au.class.getClassLoader());
            return z;
        } catch (RemoteException e) {
            ax.m563b("GamesClient", "service died");
            return null;
        }
    }
}
