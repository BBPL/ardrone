package com.google.android.youtube.player.internal;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.View;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.OnFullscreenListener;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayer.PlaylistEventListener;
import com.google.android.youtube.player.internal.C0393e.C0395a;
import com.google.android.youtube.player.internal.C0396f.C0398a;
import com.google.android.youtube.player.internal.C0399g.C0401a;
import com.google.android.youtube.player.internal.C0402h.C0404a;
import java.util.List;

public final class C0436s implements YouTubePlayer {
    private C0386b f258a;
    private C0390d f259b;

    public C0436s(C0386b c0386b, C0390d c0390d) {
        this.f258a = (C0386b) ac.m1483a((Object) c0386b, (Object) "connectionClient cannot be null");
        this.f259b = (C0390d) ac.m1483a((Object) c0390d, (Object) "embeddedPlayer cannot be null");
    }

    public final View m1709a() {
        try {
            return (View) C0440v.m1725a(this.f259b.mo1288s());
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void m1710a(Configuration configuration) {
        try {
            this.f259b.mo1248a(configuration);
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void m1711a(boolean z) {
        try {
            this.f259b.mo1257a(z);
            this.f258a.mo1322a(z);
            this.f258a.mo1316d();
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final boolean m1712a(int i, KeyEvent keyEvent) {
        try {
            return this.f259b.mo1258a(i, keyEvent);
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final boolean m1713a(Bundle bundle) {
        try {
            return this.f259b.mo1259a(bundle);
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void addFullscreenControlFlag(int i) {
        try {
            this.f259b.mo1270d(i);
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void m1714b() {
        try {
            this.f259b.mo1282m();
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void m1715b(boolean z) {
        try {
            this.f259b.mo1273e(z);
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final boolean m1716b(int i, KeyEvent keyEvent) {
        try {
            return this.f259b.mo1266b(i, keyEvent);
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void m1717c() {
        try {
            this.f259b.mo1283n();
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void cuePlaylist(String str) {
        cuePlaylist(str, 0, 0);
    }

    public final void cuePlaylist(String str, int i, int i2) {
        try {
            this.f259b.mo1255a(str, i, i2);
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void cueVideo(String str) {
        cueVideo(str, 0);
    }

    public final void cueVideo(String str, int i) {
        try {
            this.f259b.mo1254a(str, i);
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void cueVideos(List<String> list) {
        cueVideos(list, 0, 0);
    }

    public final void cueVideos(List<String> list, int i, int i2) {
        try {
            this.f259b.mo1256a((List) list, i, i2);
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void m1718d() {
        try {
            this.f259b.mo1284o();
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void m1719e() {
        try {
            this.f259b.mo1285p();
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void m1720f() {
        try {
            this.f259b.mo1286q();
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void m1721g() {
        try {
            this.f259b.mo1281l();
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final int getCurrentTimeMillis() {
        try {
            return this.f259b.mo1277h();
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final int getDurationMillis() {
        try {
            return this.f259b.mo1278i();
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final int getFullscreenControlFlags() {
        try {
            return this.f259b.mo1279j();
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final Bundle m1722h() {
        try {
            return this.f259b.mo1287r();
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final boolean hasNext() {
        try {
            return this.f259b.mo1272d();
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final boolean hasPrevious() {
        try {
            return this.f259b.mo1274e();
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final boolean isPlaying() {
        try {
            return this.f259b.mo1269c();
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void loadPlaylist(String str) {
        loadPlaylist(str, 0, 0);
    }

    public final void loadPlaylist(String str, int i, int i2) {
        try {
            this.f259b.mo1263b(str, i, i2);
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void loadVideo(String str) {
        loadVideo(str, 0);
    }

    public final void loadVideo(String str, int i) {
        try {
            this.f259b.mo1262b(str, i);
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void loadVideos(List<String> list) {
        loadVideos(list, 0, 0);
    }

    public final void loadVideos(List<String> list, int i, int i2) {
        try {
            this.f259b.mo1264b((List) list, i, i2);
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void next() {
        try {
            this.f259b.mo1275f();
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void pause() {
        try {
            this.f259b.mo1260b();
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void play() {
        try {
            this.f259b.mo1246a();
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void previous() {
        try {
            this.f259b.mo1276g();
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void release() {
        m1711a(true);
    }

    public final void seekRelativeMillis(int i) {
        try {
            this.f259b.mo1261b(i);
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void seekToMillis(int i) {
        try {
            this.f259b.mo1247a(i);
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void setFullscreen(boolean z) {
        try {
            this.f259b.mo1265b(z);
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void setFullscreenControlFlags(int i) {
        try {
            this.f259b.mo1267c(i);
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void setManageAudioFocus(boolean z) {
        try {
            this.f259b.mo1271d(z);
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void setOnFullscreenListener(final OnFullscreenListener onFullscreenListener) {
        try {
            this.f259b.mo1249a(new C0395a(this) {
                final /* synthetic */ C0436s f251b;

                public final void mo1289a(boolean z) {
                    onFullscreenListener.onFullscreen(z);
                }
            });
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void setPlaybackEventListener(final PlaybackEventListener playbackEventListener) {
        try {
            this.f259b.mo1250a(new C0398a(this) {
                final /* synthetic */ C0436s f257b;

                public final void mo1290a() {
                    playbackEventListener.onPlaying();
                }

                public final void mo1291a(int i) {
                    playbackEventListener.onSeekTo(i);
                }

                public final void mo1292a(boolean z) {
                    playbackEventListener.onBuffering(z);
                }

                public final void mo1293b() {
                    playbackEventListener.onPaused();
                }

                public final void mo1294c() {
                    playbackEventListener.onStopped();
                }
            });
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void setPlayerStateChangeListener(final PlayerStateChangeListener playerStateChangeListener) {
        try {
            this.f259b.mo1251a(new C0401a(this) {
                final /* synthetic */ C0436s f255b;

                public final void mo1295a() {
                    playerStateChangeListener.onLoading();
                }

                public final void mo1296a(String str) {
                    playerStateChangeListener.onLoaded(str);
                }

                public final void mo1297b() {
                    playerStateChangeListener.onAdStarted();
                }

                public final void mo1298b(String str) {
                    ErrorReason valueOf;
                    try {
                        valueOf = ErrorReason.valueOf(str);
                    } catch (IllegalArgumentException e) {
                        valueOf = ErrorReason.UNKNOWN;
                    } catch (NullPointerException e2) {
                        valueOf = ErrorReason.UNKNOWN;
                    }
                    playerStateChangeListener.onError(valueOf);
                }

                public final void mo1299c() {
                    playerStateChangeListener.onVideoStarted();
                }

                public final void mo1300d() {
                    playerStateChangeListener.onVideoEnded();
                }
            });
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void setPlayerStyle(PlayerStyle playerStyle) {
        try {
            this.f259b.mo1253a(playerStyle.name());
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void setPlaylistEventListener(final PlaylistEventListener playlistEventListener) {
        try {
            this.f259b.mo1252a(new C0404a(this) {
                final /* synthetic */ C0436s f253b;

                public final void mo1301a() {
                    playlistEventListener.onPrevious();
                }

                public final void mo1302b() {
                    playlistEventListener.onNext();
                }

                public final void mo1303c() {
                    playlistEventListener.onPlaylistEnded();
                }
            });
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }

    public final void setShowFullscreenButton(boolean z) {
        try {
            this.f259b.mo1268c(z);
        } catch (RemoteException e) {
            throw new C0425q(e);
        }
    }
}
