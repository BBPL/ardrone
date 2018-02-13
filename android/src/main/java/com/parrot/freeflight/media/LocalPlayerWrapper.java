package com.parrot.freeflight.media;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.widget.VideoView;

public class LocalPlayerWrapper extends VideoPlayerWrapper implements OnPreparedListener, OnCompletionListener {
    private MediaPlayer mediaPlayer;
    private OnCompletionListener onCompletionListener;
    private OnPreparedListener onOprepareListener;
    private VideoView videoView;

    public LocalPlayerWrapper(VideoView videoView) {
        this.videoView = videoView;
        this.videoView.setOnPreparedListener(this);
        this.videoView.setOnCompletionListener(this);
    }

    private void notifyOnCompletion(MediaPlayer mediaPlayer) {
        if (this.onCompletionListener != null) {
            this.onCompletionListener.onCompletion(mediaPlayer);
        }
    }

    private void notifyOnPrepared(MediaPlayer mediaPlayer) {
        if (this.onOprepareListener != null) {
            this.onOprepareListener.onPrepared(mediaPlayer);
        }
    }

    public int getDuration() {
        return this.videoView.getDuration();
    }

    public MediaPlayer getMediaPlayer() {
        return this.mediaPlayer;
    }

    public int getPosition() {
        return this.videoView.getCurrentPosition();
    }

    public boolean isPlaying() {
        return this.videoView.isPlaying();
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        notifyOnStopped(this);
        notifyOnCompletion(mediaPlayer);
    }

    public void onPrepared(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        notifyOnReady(this);
        notifyOnPrepared(mediaPlayer);
    }

    public void pause() {
        this.videoView.pause();
        notifyOnPaused();
    }

    public void play() {
        this.videoView.start();
        notifyOnStarted();
    }

    public void seekTo(int i) {
        this.videoView.seekTo(i);
    }

    public void setOnCompletionListener(OnCompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }

    public void setOnPrepareListener(OnPreparedListener onPreparedListener) {
        this.onOprepareListener = onPreparedListener;
    }

    public void stop() {
        this.videoView.stopPlayback();
        notifyOnStopped(this);
    }
}
