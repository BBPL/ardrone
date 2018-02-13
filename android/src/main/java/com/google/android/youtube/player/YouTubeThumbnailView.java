package com.google.android.youtube.player;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.google.android.youtube.player.internal.C0385t.C0375a;
import com.google.android.youtube.player.internal.C0385t.C0377b;
import com.google.android.youtube.player.internal.C0386b;
import com.google.android.youtube.player.internal.ab;

public final class YouTubeThumbnailView extends ImageView {
    private C0386b f177a;

    public interface OnInitializedListener {
        void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult);

        void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader);
    }

    public YouTubeThumbnailView(Context context) {
        this(context, null);
    }

    public YouTubeThumbnailView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public YouTubeThumbnailView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public final void initialize(String str, final OnInitializedListener onInitializedListener) {
        this.f177a = ab.m1477a().mo1243a(getContext(), str, new C0375a(this) {
            final /* synthetic */ YouTubeThumbnailView f174b;

            public final void mo1224a() {
                if (this.f174b.f177a != null) {
                    YouTubeThumbnailView youTubeThumbnailView = this.f174b;
                    onInitializedListener.onInitializationSuccess(youTubeThumbnailView, ab.m1477a().mo1242a(this.f174b.f177a, youTubeThumbnailView));
                    this.f174b.f177a = null;
                }
            }

            public final void mo1225b() {
                this.f174b.f177a = null;
            }
        }, new C0377b(this) {
            final /* synthetic */ YouTubeThumbnailView f176b;

            public final void mo1226a(YouTubeInitializationResult youTubeInitializationResult) {
                onInitializedListener.onInitializationFailure(this.f176b, youTubeInitializationResult);
                this.f176b.f177a = null;
            }
        });
        this.f177a.mo1317e();
    }
}
