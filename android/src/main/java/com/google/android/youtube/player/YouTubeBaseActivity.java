package com.google.android.youtube.player;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayerView.C0369b;

public class YouTubeBaseActivity extends Activity {
    private C0370a f134a;
    private YouTubePlayerView f135b;
    private int f136c;
    private Bundle f137d;

    private final class C0370a implements C0369b {
        final /* synthetic */ YouTubeBaseActivity f133a;

        private C0370a(YouTubeBaseActivity youTubeBaseActivity) {
            this.f133a = youTubeBaseActivity;
        }

        public final void mo1221a(YouTubePlayerView youTubePlayerView) {
            if (!(this.f133a.f135b == null || this.f133a.f135b == youTubePlayerView)) {
                this.f133a.f135b.m1454b(true);
            }
            this.f133a.f135b = youTubePlayerView;
            if (this.f133a.f136c > 0) {
                youTubePlayerView.m1450a();
            }
            if (this.f133a.f136c >= 2) {
                youTubePlayerView.m1453b();
            }
        }

        public final void mo1222a(YouTubePlayerView youTubePlayerView, String str, OnInitializedListener onInitializedListener) {
            youTubePlayerView.m1451a(this.f133a, youTubePlayerView, str, onInitializedListener, this.f133a.f137d);
            this.f133a.f137d = null;
        }
    }

    final C0369b m1417a() {
        return this.f134a;
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.f134a = new C0370a();
        this.f137d = bundle != null ? bundle.getBundle("YouTubeBaseActivity.KEY_PLAYER_VIEW_STATE") : null;
    }

    protected void onDestroy() {
        if (this.f135b != null) {
            this.f135b.m1452a(isFinishing());
        }
        super.onDestroy();
    }

    protected void onPause() {
        this.f136c = 1;
        if (this.f135b != null) {
            this.f135b.m1455c();
        }
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
        this.f136c = 2;
        if (this.f135b != null) {
            this.f135b.m1453b();
        }
    }

    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBundle("YouTubeBaseActivity.KEY_PLAYER_VIEW_STATE", this.f135b != null ? this.f135b.m1457e() : this.f137d);
    }

    protected void onStart() {
        super.onStart();
        this.f136c = 1;
        if (this.f135b != null) {
            this.f135b.m1450a();
        }
    }

    protected void onStop() {
        this.f136c = 0;
        if (this.f135b != null) {
            this.f135b.m1456d();
        }
        super.onStop();
    }
}
