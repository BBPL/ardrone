package com.google.android.youtube.player;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView.C0369b;
import com.google.android.youtube.player.internal.ac;

public class YouTubePlayerSupportFragment extends Fragment implements Provider {
    private final C0374a f152a = new C0374a();
    private Bundle f153b;
    private YouTubePlayerView f154c;
    private String f155d;
    private OnInitializedListener f156e;

    private final class C0374a implements C0369b {
        final /* synthetic */ YouTubePlayerSupportFragment f151a;

        private C0374a(YouTubePlayerSupportFragment youTubePlayerSupportFragment) {
            this.f151a = youTubePlayerSupportFragment;
        }

        public final void mo1221a(YouTubePlayerView youTubePlayerView) {
        }

        public final void mo1222a(YouTubePlayerView youTubePlayerView, String str, OnInitializedListener onInitializedListener) {
            this.f151a.initialize(str, this.f151a.f156e);
        }
    }

    private void m1430a() {
        if (this.f154c != null && this.f156e != null) {
            this.f154c.m1451a(getActivity(), this, this.f155d, this.f156e, this.f153b);
            this.f153b = null;
            this.f156e = null;
        }
    }

    public static YouTubePlayerSupportFragment newInstance() {
        return new YouTubePlayerSupportFragment();
    }

    public void initialize(String str, OnInitializedListener onInitializedListener) {
        this.f155d = ac.m1484a(str, (Object) "Developer key cannot be null or empty");
        this.f156e = onInitializedListener;
        m1430a();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.f153b = bundle != null ? bundle.getBundle("YouTubePlayerSupportFragment.KEY_PLAYER_VIEW_STATE") : null;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.f154c = new YouTubePlayerView(getActivity(), null, 0, this.f152a);
        m1430a();
        return this.f154c;
    }

    public void onDestroy() {
        if (this.f154c != null) {
            Activity activity = getActivity();
            YouTubePlayerView youTubePlayerView = this.f154c;
            boolean z = activity == null || activity.isFinishing();
            youTubePlayerView.m1452a(z);
        }
        super.onDestroy();
    }

    public void onDestroyView() {
        this.f154c.m1454b(getActivity().isFinishing());
        this.f154c = null;
        super.onDestroyView();
    }

    public void onPause() {
        this.f154c.m1455c();
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        this.f154c.m1453b();
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBundle("YouTubePlayerSupportFragment.KEY_PLAYER_VIEW_STATE", this.f154c != null ? this.f154c.m1457e() : this.f153b);
    }

    public void onStart() {
        super.onStart();
        this.f154c.m1450a();
    }

    public void onStop() {
        this.f154c.m1456d();
        super.onStop();
    }
}
