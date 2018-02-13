package com.parrot.freeflight.media;

import com.parrot.freeflight.ui.OnPlayStateChangedListener;

public abstract class VideoPlayerWrapper {
    private OnPlayerEventListener eventListener;
    private OnPlayStateChangedListener onPlayStateChangedListener;

    public interface OnPlayerEventListener {
        void onPaused();

        void onReady(VideoPlayerWrapper videoPlayerWrapper);

        void onStarted();

        void onStopped(VideoPlayerWrapper videoPlayerWrapper);
    }

    public abstract int getDuration();

    public abstract int getPosition();

    public abstract boolean isPlaying();

    protected void notifyOnPaused() {
        if (this.eventListener != null) {
            this.eventListener.onPaused();
        }
        notifyOnPlayStateChanged(false);
    }

    protected void notifyOnPlayStateChanged(boolean z) {
        if (this.onPlayStateChangedListener != null) {
            this.onPlayStateChangedListener.onPlayStateChanged(z);
        }
    }

    protected void notifyOnReady(VideoPlayerWrapper videoPlayerWrapper) {
        if (this.eventListener != null) {
            this.eventListener.onReady(videoPlayerWrapper);
        }
    }

    protected void notifyOnStarted() {
        if (this.eventListener != null) {
            this.eventListener.onStarted();
        }
        notifyOnPlayStateChanged(true);
    }

    protected void notifyOnStopped(VideoPlayerWrapper videoPlayerWrapper) {
        if (this.eventListener != null) {
            this.eventListener.onStopped(videoPlayerWrapper);
        }
        notifyOnPlayStateChanged(false);
    }

    public abstract void pause();

    public abstract void play();

    public abstract void seekTo(int i);

    public void setOnPlayStateChangedListener(OnPlayStateChangedListener onPlayStateChangedListener) {
        this.onPlayStateChangedListener = onPlayStateChangedListener;
    }

    public void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener) {
        this.eventListener = onPlayerEventListener;
    }

    public abstract void stop();
}
