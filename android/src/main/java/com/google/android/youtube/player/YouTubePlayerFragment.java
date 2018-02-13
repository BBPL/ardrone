package com.google.android.youtube.player;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView.C0369b;
import com.google.android.youtube.player.internal.ac;

public class YouTubePlayerFragment extends Fragment implements Provider {
    private final C0373a f146a = new C0373a();
    private Bundle f147b;
    private YouTubePlayerView f148c;
    private String f149d;
    private OnInitializedListener f150e;

    private final class C0373a implements C0369b {
        final /* synthetic */ YouTubePlayerFragment f145a;

        private C0373a(YouTubePlayerFragment youTubePlayerFragment) {
            this.f145a = youTubePlayerFragment;
        }

        public final void mo1221a(YouTubePlayerView youTubePlayerView) {
        }

        public final void mo1222a(YouTubePlayerView youTubePlayerView, String str, OnInitializedListener onInitializedListener) {
            this.f145a.initialize(str, this.f145a.f150e);
        }
    }

    private void m1426a() {
        if (this.f148c != null && this.f150e != null) {
            this.f148c.m1451a(getActivity(), this, this.f149d, this.f150e, this.f147b);
            this.f147b = null;
            this.f150e = null;
        }
    }

    public static YouTubePlayerFragment newInstance() {
        return new YouTubePlayerFragment();
    }

    public void initialize(String str, OnInitializedListener onInitializedListener) {
        this.f149d = ac.m1484a(str, (Object) "Developer key cannot be null or empty");
        this.f150e = onInitializedListener;
        m1426a();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.f147b = bundle != null ? bundle.getBundle("YouTubePlayerFragment.KEY_PLAYER_VIEW_STATE") : null;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.f148c = new YouTubePlayerView(getActivity(), null, 0, this.f146a);
        m1426a();
        return this.f148c;
    }

    public void onDestroy() {
        if (this.f148c != null) {
            Activity activity = getActivity();
            YouTubePlayerView youTubePlayerView = this.f148c;
            boolean z = activity == null || activity.isFinishing();
            youTubePlayerView.m1452a(z);
        }
        super.onDestroy();
    }

    public void onDestroyView() {
        this.f148c.m1454b(getActivity().isFinishing());
        this.f148c = null;
        super.onDestroyView();
    }

    public void onPause() {
        this.f148c.m1455c();
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        this.f148c.m1453b();
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBundle("YouTubePlayerFragment.KEY_PLAYER_VIEW_STATE", this.f148c != null ? this.f148c.m1457e() : this.f147b);
    }

    public void onStart() {
        super.onStart();
        this.f148c.m1450a();
    }

    public void onStop() {
        this.f148c.m1456d();
        super.onStop();
    }
}
