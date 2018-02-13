package com.parrot.freeflight.media;

import android.util.Log;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;

public class YoutubePlayerWrapper extends VideoPlayerWrapper implements PlayerStateChangeListener, PlaybackEventListener {
    private static final String TAG = YoutubePlayerWrapper.class.getSimpleName();
    private YouTubePlayer videoPlayer;

    public YoutubePlayerWrapper(YouTubePlayer youTubePlayer) {
        this.videoPlayer = youTubePlayer;
        youTubePlayer.setPlayerStateChangeListener(this);
        youTubePlayer.setPlaybackEventListener(this);
    }

    public int getDuration() {
        return this.videoPlayer.getDurationMillis();
    }

    public int getPosition() {
        return this.videoPlayer.getCurrentTimeMillis();
    }

    public boolean isPlaying() {
        return this.videoPlayer.isPlaying();
    }

    public void onAdStarted() {
    }

    public void onBuffering(boolean z) {
    }

    public void onError(ErrorReason errorReason) {
        Log.w(TAG, "Error occured: " + errorReason.name());
    }

    public void onLoaded(String str) {
        notifyOnReady(this);
    }

    public void onLoading() {
    }

    public void onPaused() {
        notifyOnPaused();
        this.videoPlayer.setPlayerStyle(PlayerStyle.CHROMELESS);
    }

    public void onPlaying() {
        this.videoPlayer.setPlayerStyle(PlayerStyle.MINIMAL);
        notifyOnStarted();
    }

    public void onSeekTo(int i) {
    }

    public void onStopped() {
        notifyOnStopped(this);
        this.videoPlayer.setPlayerStyle(PlayerStyle.CHROMELESS);
    }

    public void onVideoEnded() {
    }

    public void onVideoStarted() {
    }

    public void pause() {
        this.videoPlayer.pause();
    }

    public void play() {
        this.videoPlayer.play();
        notifyOnPlayStateChanged(true);
    }

    public void seekTo(int i) {
        this.videoPlayer.seekToMillis(i);
    }

    public void stop() {
        this.videoPlayer.pause();
        this.videoPlayer.seekToMillis(0);
    }
}
