package com.parrot.freeflight.media;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.media.VideoPlayerWrapper.OnPlayerEventListener;
import com.parrot.freeflight.utils.AnimationUtils;
import java.util.Formatter;
import java.util.Locale;

public class VideoPlayerController implements OnClickListener, OnSeekBarChangeListener, OnPlayerEventListener {
    protected static final int MSG_PLAY = 2;
    protected static final int MSG_UPDATE_PROGRESS = 1;
    private static final int UPDATE_TIME = 1000;
    private ImageButton btnPlayPause;
    @SuppressLint({"HandlerLeak"})
    private final Handler handler = new C11611();
    private int lastMsec;
    private final StringBuilder mFormatBuilder = new StringBuilder();
    private final Formatter mFormatter = new Formatter(this.mFormatBuilder, Locale.getDefault());
    private View mediaControlsView;
    private OnCompletionListener onCompletionListener;
    private OnPreparedListener onOprepareListener;
    private boolean prepared;
    private boolean prevPlaying;
    private SeekBar seekBar;
    private boolean seeking;
    private TextView textCurrentTime;
    private TextView textTotalTime;
    private VideoPlayerWrapper video;

    class C11611 extends Handler {
        C11611() {
        }

        public void handleMessage(Message message) {
            IllegalStateException e;
            boolean z = false;
            switch (message.what) {
                case 1:
                    int access$000;
                    try {
                        access$000 = VideoPlayerController.this.updateProgress();
                        try {
                            if (VideoPlayerController.this.video != null && VideoPlayerController.this.video.isPlaying()) {
                                z = true;
                            }
                            if (z != VideoPlayerController.this.prevPlaying) {
                                VideoPlayerController.this.updatePlayButtonImage();
                            }
                            VideoPlayerController.this.prevPlaying = z;
                        } catch (IllegalStateException e2) {
                            e = e2;
                            e.printStackTrace();
                            VideoPlayerController.this.video = null;
                            if (VideoPlayerController.this.video != null) {
                                return;
                            }
                            return;
                        }
                    } catch (IllegalStateException e3) {
                        e = e3;
                        access$000 = 0;
                        e.printStackTrace();
                        VideoPlayerController.this.video = null;
                        if (VideoPlayerController.this.video != null) {
                            return;
                        }
                        return;
                    }
                    if (VideoPlayerController.this.video != null && !VideoPlayerController.this.seeking && r0) {
                        sendMessageDelayed(obtainMessage(1), (long) (1000 - (access$000 % 1000)));
                        return;
                    }
                    return;
                case 2:
                    VideoPlayerController.this.play();
                    return;
                default:
                    return;
            }
        }
    }

    public VideoPlayerController(View view) {
        this.mediaControlsView = view;
        initUiControls();
        initListeners();
    }

    private void initListeners() {
        this.btnPlayPause.setOnClickListener(this);
        this.seekBar.setOnSeekBarChangeListener(this);
    }

    private void initUiControls() {
        this.btnPlayPause = (ImageButton) this.mediaControlsView.findViewById(C0984R.id.cboxPlayPause);
        this.seekBar = (SeekBar) this.mediaControlsView.findViewById(C0984R.id.barProgress);
        this.textCurrentTime = (TextView) this.mediaControlsView.findViewById(C0984R.id.textCurrentTime);
        this.textTotalTime = (TextView) this.mediaControlsView.findViewById(C0984R.id.textTotalTime);
    }

    private void notifyOnCompletion(MediaPlayer mediaPlayer) {
        if (this.onCompletionListener != null) {
            this.onCompletionListener.onCompletion(mediaPlayer);
        }
    }

    private void seek(int i) {
        this.video.seekTo(i);
    }

    private String stringForTime(int i) {
        int i2 = i / 1000;
        int i3 = i2 % 60;
        int i4 = (i2 / 60) % 60;
        i2 /= 3600;
        this.mFormatBuilder.setLength(0);
        if (i2 > 0) {
            return this.mFormatter.format("%d:%02d:%02d", new Object[]{Integer.valueOf(i2), Integer.valueOf(i4), Integer.valueOf(i3)}).toString();
        }
        return this.mFormatter.format("%02d:%02d", new Object[]{Integer.valueOf(i4), Integer.valueOf(i3)}).toString();
    }

    private int updateProgress() {
        if (this.video == null) {
            return 0;
        }
        int position = this.video.getPosition();
        int duration = this.video.getDuration();
        this.seekBar.setMax(duration);
        this.seekBar.setProgress(position);
        this.textCurrentTime.setText(stringForTime(position));
        this.textTotalTime.setText(stringForTime(duration));
        this.lastMsec = position;
        return position;
    }

    public void attachVideo(VideoPlayerWrapper videoPlayerWrapper) {
        this.video = videoPlayerWrapper;
        if (videoPlayerWrapper != null) {
            this.video.setOnPlayerEventListener(this);
        }
        this.lastMsec = 0;
    }

    public int getVideoDuration() {
        return this.video.getDuration();
    }

    public int getVideoProgress() {
        return this.video.getPosition();
    }

    public View getView() {
        return this.mediaControlsView;
    }

    public void hide() {
        if (isVisible()) {
            this.handler.removeMessages(1);
            AnimationUtils.makeInvisibleAnimated(this.mediaControlsView);
        }
    }

    public void invalidate() {
        updateProgress();
        updatePlayButtonImage();
        if (isVisible()) {
            this.handler.sendEmptyMessageDelayed(1, 500);
        }
    }

    public boolean isPlaying() {
        return this.video != null ? this.video.isPlaying() : false;
    }

    public boolean isPrepared() {
        return this.prepared;
    }

    public boolean isVisible() {
        return this.mediaControlsView.getVisibility() == 0;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0984R.id.video_view /*2131362051*/:
            case C0984R.id.cboxPlayPause /*2131362180*/:
                if (isPlaying()) {
                    pause();
                    return;
                } else if (this.video instanceof YoutubePlayerWrapper) {
                    this.video.notifyOnPlayStateChanged(true);
                    playDelayed(500);
                    return;
                } else {
                    play();
                    return;
                }
            default:
                return;
        }
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        updateProgress();
        updatePlayButtonImage();
        notifyOnCompletion(mediaPlayer);
    }

    public void onPaused() {
    }

    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        if (z && this.video != null) {
            seek(i);
        }
    }

    public void onReady(VideoPlayerWrapper videoPlayerWrapper) {
        updatePlayButtonImage();
        this.prepared = true;
        this.handler.removeMessages(1);
        this.handler.sendEmptyMessage(1);
        if (this.onOprepareListener != null) {
            this.onOprepareListener.onPrepared(null);
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        this.seeking = true;
        this.handler.removeMessages(1);
    }

    public void onStarted() {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        this.seeking = false;
        updateProgress();
        updatePlayButtonImage();
        show();
        this.handler.sendEmptyMessage(1);
    }

    public void onStopped(VideoPlayerWrapper videoPlayerWrapper) {
    }

    public void pause() {
        if (this.video != null && this.video.isPlaying()) {
            this.video.pause();
            if (isVisible()) {
                this.handler.removeMessages(1);
            }
            updatePlayButtonImage();
            updateProgress();
        }
    }

    public void play() {
        if (this.video != null && !this.video.isPlaying()) {
            this.video.play();
            this.handler.sendEmptyMessageDelayed(1, 500);
            updatePlayButtonImage();
            updateProgress();
        }
    }

    public void playDelayed(int i) {
        this.handler.sendEmptyMessageDelayed(2, (long) i);
    }

    public void resume() {
        if (this.video != null) {
            this.video.seekTo(this.lastMsec);
            play();
        }
    }

    public void show() {
        if (!isVisible()) {
            updateProgress();
            updatePlayButtonImage();
            AnimationUtils.makeVisibleAnimated(this.mediaControlsView);
        }
        this.handler.sendEmptyMessageDelayed(1, 500);
    }

    public void stop() {
        if (this.video != null) {
            if (this.video.isPlaying()) {
                this.video.stop();
            }
            updatePlayButtonImage();
            updateProgress();
        }
    }

    protected void updatePlayButtonImage() {
        if (this.video == null) {
            return;
        }
        if (this.video.isPlaying()) {
            this.btnPlayPause.setImageResource(C0984R.drawable.btn_pause);
        } else {
            this.btnPlayPause.setImageResource(C0984R.drawable.btn_play);
        }
    }
}
