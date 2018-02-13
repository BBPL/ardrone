package com.parrot.freeflight.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.activities.base.WatchMediaActivity;
import com.parrot.freeflight.media.VideoPlayerController;
import com.parrot.freeflight.media.VideoPlayerWrapper;
import com.parrot.freeflight.media.YoutubePlayerWrapper;
import com.parrot.freeflight.ui.OnPlayStateChangedListener;
import com.parrot.freeflight.vo.MediaVO;
import org.mortbay.util.URIUtil;

public class WatchYoutubeVideoNativelyActivity extends WatchMediaActivity implements OnInitializedListener, OnPlayStateChangedListener {
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private Runnable hideControlsAction;
    private MediaVO media;
    private View playIcon;
    private VideoPlayerController vc;
    private View videoOverlay;

    class C11481 implements Runnable {
        C11481() {
        }

        public void run() {
            WatchYoutubeVideoNativelyActivity.this.videoOverlay.setVisibility(8);
            WatchYoutubeVideoNativelyActivity.this.getParrotActionBar().hide(true);
            WatchYoutubeVideoNativelyActivity.this.vc.hide();
        }
    }

    private void cancelHideBarAndVideoControllerDelayed() {
        Handler handler = findViewById(16908290).getHandler();
        if (handler != null) {
            handler.removeCallbacks(this.hideControlsAction);
        }
    }

    private void hideBarAndVideoController() {
        this.playIcon.setVisibility(8);
        this.videoOverlay.setVisibility(8);
        this.vc.hide();
        getParrotActionBar().hide(true);
    }

    private void initVideoController(YouTubePlayer youTubePlayer) {
        VideoPlayerWrapper youtubePlayerWrapper = new YoutubePlayerWrapper(youTubePlayer);
        youtubePlayerWrapper.setOnPlayStateChangedListener(this);
        this.vc = new VideoPlayerController(findViewById(C0984R.id.media_controller));
        this.vc.attachVideo(youtubePlayerWrapper);
        findViewById(C0984R.id.video_holder).setOnClickListener(this);
        this.vc.playDelayed(500);
    }

    private void showBarAndVideoController() {
        this.videoOverlay.setVisibility(0);
        this.videoOverlay.setEnabled(true);
        this.playIcon.setVisibility(0);
        this.vc.show();
        getParrotActionBar().show(true);
    }

    public static void start(Context context, MediaVO mediaVO, String str) {
        Intent intent = new Intent(context, WatchYoutubeVideoNativelyActivity.class);
        intent.putExtra(WatchMediaActivity.EXTRA_MEDIA_OBJ, mediaVO);
        intent.putExtra(WatchMediaActivity.EXTRA_TITLE, str);
        context.startActivity(intent);
    }

    protected Provider getYouTubePlayerProvider() {
        return (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(C0984R.id.activity_watch_youtube_video_natively_youtube_fragment);
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1) {
            getYouTubePlayerProvider().initialize(getString(C0984R.string.key_youtube_developer_key), this);
        }
    }

    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case C0984R.id.activity_watch_youtube_video_natively_videocontainer /*2131362055*/:
                this.vc.playDelayed(500);
                this.videoOverlay.setEnabled(false);
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.activity_watch_youtube_video_natively);
        this.media = (MediaVO) getIntent().getParcelableExtra(WatchMediaActivity.EXTRA_MEDIA_OBJ);
        ((YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(C0984R.id.activity_watch_youtube_video_natively_youtube_fragment)).initialize(getString(C0984R.string.key_youtube_developer_key), this);
        this.videoOverlay = findViewById(C0984R.id.activity_watch_youtube_video_natively_videocontainer);
        this.videoOverlay.setOnClickListener(this);
        this.playIcon = findViewById(C0984R.id.activity_watch_youtube_video_natively_imageview_play);
        this.playIcon.setVisibility(8);
        this.hideControlsAction = new C11481();
    }

    public void onInitializationFailure(Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, 1).show();
        } else {
            Toast.makeText(this, "Initialization failure", 1).show();
        }
    }

    public void onInitializationSuccess(Provider provider, YouTubePlayer youTubePlayer, boolean z) {
        if (!z) {
            youTubePlayer.cueVideo(this.media.getPath().substring(this.media.getPath().lastIndexOf(URIUtil.SLASH) + 1));
            youTubePlayer.setPlayerStyle(PlayerStyle.MINIMAL);
            initVideoController(youTubePlayer);
        }
    }

    public void onPlayStateChanged(boolean z) {
        if (z) {
            hideBarAndVideoController();
            this.vc.invalidate();
            return;
        }
        cancelHideBarAndVideoControllerDelayed();
        showBarAndVideoController();
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
        showBarAndVideoController();
    }
}
