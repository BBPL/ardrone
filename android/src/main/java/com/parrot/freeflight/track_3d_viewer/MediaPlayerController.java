package com.parrot.freeflight.track_3d_viewer;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MediaPlayerController {
    private static final int SEEK_BAR_MAX = 10000;
    private final TextView allTimeView;
    private IMediaControllerCallback controllerCallback;
    private long currDelta = 0;
    private final TextView currTimeView;
    private boolean isPlaying = false;
    private long maxDelta = 0;
    private final ImageButton playButton;
    private float prevPosition = 0.0f;
    private long prevTime = 0;
    private final SeekBar seekBar;
    private final Runnable stopPlaybackRunnable = new C11852();
    private final Handler uiHandler;
    private final Thread uiThread;
    private final Runnable updateControlsRunnable = new C11841();

    class C11841 implements Runnable {
        C11841() {
        }

        public void run() {
            MediaPlayerController.this.updateSeekBarPos();
            MediaPlayerController.this.updateTimeControls();
        }
    }

    class C11852 implements Runnable {
        C11852() {
        }

        public void run() {
            MediaPlayerController.this.playButton.setImageResource(17301540);
            MediaPlayerController.this.updateSeekBarPos();
            MediaPlayerController.this.updateTimeControls();
        }
    }

    class C11863 implements OnClickListener {
        C11863() {
        }

        public void onClick(View view) {
            MediaPlayerController.this.playPauseClicked();
        }
    }

    class C11874 implements OnSeekBarChangeListener {
        C11874() {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            if (z) {
                MediaPlayerController.this.currDelta = (MediaPlayerController.this.maxDelta * ((long) i)) / ((long) seekBar.getMax());
                if (MediaPlayerController.this.controllerCallback != null) {
                    MediaPlayerController.this.controllerCallback.onPositionChanged(((float) MediaPlayerController.this.currDelta) / ((float) MediaPlayerController.this.maxDelta), true);
                }
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }

    public interface IMediaControllerCallback {
        void onPause();

        void onPlay();

        void onPositionChanged(float f, boolean z);
    }

    public MediaPlayerController(Thread thread, Handler handler, ImageButton imageButton, SeekBar seekBar, TextView textView, TextView textView2) {
        this.uiThread = thread;
        this.uiHandler = handler;
        this.playButton = imageButton;
        this.seekBar = seekBar;
        this.currTimeView = textView;
        this.allTimeView = textView2;
        seekBar.setMax(SEEK_BAR_MAX);
        imageButton.setOnClickListener(new C11863());
        seekBar.setOnSeekBarChangeListener(new C11874());
    }

    private static String formatTime(long j) {
        int i = (int) (((j / 1000) / 60) % 60);
        int i2 = (int) ((j / 1000) % 60);
        if (((int) ((((j / 1000) / 60) / 60) % 60)) != 0) {
            return String.format("%d:%d:%02d", new Object[]{Integer.valueOf((int) ((((j / 1000) / 60) / 60) % 60)), Integer.valueOf(i), Integer.valueOf(i2)});
        }
        return String.format("%d:%02d", new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
    }

    private void playPauseClicked() {
        this.isPlaying ^= 1;
        this.playButton.setImageResource(this.isPlaying ? 17301539 : 17301540);
        if (this.isPlaying) {
            this.prevTime = System.currentTimeMillis();
            if (this.controllerCallback != null) {
                this.controllerCallback.onPlay();
            }
        } else if (this.controllerCallback != null) {
            this.controllerCallback.onPause();
        }
    }

    private void runOnUiThread(Runnable runnable) {
        if (Thread.currentThread() != this.uiThread) {
            this.uiHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    private void updateSeekBarPos() {
        this.seekBar.setProgress((int) ((((float) this.currDelta) / ((float) this.maxDelta)) * ((float) this.seekBar.getMax())));
    }

    private void updateTimeControls() {
        this.currTimeView.setText(formatTime(this.currDelta));
        this.allTimeView.setText("-" + formatTime(this.maxDelta - this.currDelta));
    }

    public IMediaControllerCallback getControllerCallback() {
        return this.controllerCallback;
    }

    public float getPosition() {
        boolean z;
        float f;
        if (this.isPlaying) {
            long currentTimeMillis = System.currentTimeMillis();
            long j = this.prevTime;
            this.prevTime = currentTimeMillis;
            this.currDelta = (currentTimeMillis - j) + this.currDelta;
            if (this.currDelta > this.maxDelta) {
                this.currDelta = 0;
                z = true;
                f = ((float) this.currDelta) / ((float) this.maxDelta);
                if (z) {
                    this.isPlaying = false;
                    this.prevPosition = 0.0f;
                    if (this.controllerCallback != null) {
                        this.controllerCallback.onPause();
                        this.controllerCallback.onPositionChanged(0.0f, false);
                    }
                    runOnUiThread(this.stopPlaybackRunnable);
                } else if (Math.abs(f - this.prevPosition) > 0.001f) {
                    this.prevPosition = f;
                    if (this.controllerCallback != null) {
                        this.controllerCallback.onPositionChanged(f, false);
                    }
                    runOnUiThread(this.updateControlsRunnable);
                    return f;
                }
                return f;
            }
        }
        z = false;
        f = ((float) this.currDelta) / ((float) this.maxDelta);
        if (z) {
            this.isPlaying = false;
            this.prevPosition = 0.0f;
            if (this.controllerCallback != null) {
                this.controllerCallback.onPause();
                this.controllerCallback.onPositionChanged(0.0f, false);
            }
            runOnUiThread(this.stopPlaybackRunnable);
        } else if (Math.abs(f - this.prevPosition) > 0.001f) {
            this.prevPosition = f;
            if (this.controllerCallback != null) {
                this.controllerCallback.onPositionChanged(f, false);
            }
            runOnUiThread(this.updateControlsRunnable);
            return f;
        }
        return f;
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public void setControllerCallback(IMediaControllerCallback iMediaControllerCallback) {
        this.controllerCallback = iMediaControllerCallback;
    }

    public void setMaxTime(long j) {
        this.maxDelta = j;
    }

    public void setPosition(float f) {
        this.currDelta = (long) (((float) this.maxDelta) * f);
        if (this.controllerCallback != null) {
            this.controllerCallback.onPositionChanged(f, true);
        }
    }
}
