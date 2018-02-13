package com.parrot.freeflight.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.VideoView;
import com.google.android.gms.games.GamesClient;
import com.parrot.ardronetool.video.VideoAtomRetriever;
import com.parrot.ardronetool.video.VideoPrrt;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.model.Flight;
import com.parrot.freeflight.academy.model.FlightDataItem;
import com.parrot.freeflight.academy.tasks.DownloadFlightGraphTask;
import com.parrot.freeflight.academy.utils.AcademyUtils;
import com.parrot.freeflight.activities.base.WatchMediaActivity;
import com.parrot.freeflight.dialogs.ProgressDialog;
import com.parrot.freeflight.media.LocalPlayerWrapper;
import com.parrot.freeflight.media.VideoPlayerController;
import com.parrot.freeflight.media.VideoPlayerWrapper;
import com.parrot.freeflight.ui.OnPlayStateChangedListener;
import com.parrot.freeflight.ui.widgets.SlideRuleView;
import com.parrot.freeflight.ui.widgets.graph.Axis;
import com.parrot.freeflight.ui.widgets.graph.Axis.Align;
import com.parrot.freeflight.ui.widgets.graph.Axis.TickStyle;
import com.parrot.freeflight.ui.widgets.graph.GraphView;
import com.parrot.freeflight.ui.widgets.graph.Pointer;
import com.parrot.freeflight.ui.widgets.graph.Series;
import com.parrot.freeflight.utils.AnimationUtils;
import com.parrot.freeflight.vo.MediaVO;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.mortbay.jetty.HttpVersions;

public final class WatchVideoActivity extends WatchMediaActivity implements OnClickListener, OnPlayStateChangedListener, OnErrorListener, OnCompletionListener {
    public static final String EXTRA_CLOSE_ON_FINISH = "close_on_finish";
    public static final String EXTRA_FLIGHT_OBJ = "flight";
    private static final String TAG = WatchVideoActivity.class.getSimpleName();
    private VideoAtomRetriever atomRetreiver;
    private TextView batteryIndicator;
    private Map<Long, Integer> batteryValues;
    private boolean closeOnFinish;
    private SlideRuleView currHeightIndicator;
    public long currentTimeInterval;
    private Date dateZero;
    private Flight flight;
    private Date flightStartTime;
    private int framerate;
    private boolean graphIsReady;
    private Pointer graphPointer;
    private List<Pair<Long, Double>> heightData;
    private Map<Long, Float> heightIndicatorValues;
    private Series heightSeries;
    private Runnable hideControlsAction;
    private MediaVO media;
    private TextView speedIndicator;
    private Map<Long, Double> speedValues;
    private Timer timer;
    private TimerTask updateOverlayDataTask;
    private VideoPlayerController vc;
    private VideoPrrt videoPrrt;
    private Date videoStartTime;
    private Date videoStopTime;
    private VideoView videoView;
    private boolean wasPlaying;

    class C11422 implements Runnable {
        C11422() {
        }

        public void run() {
            WatchVideoActivity.this.getParrotActionBar().hide(true);
            WatchVideoActivity.this.vc.hide();
        }
    }

    private class UpdateGraphTask extends TimerTask {
        private int currBatteryLevel;
        private float currHeight;
        private Double currSpeed;
        private GraphView graph;
        private long position = 0;
        private Integer prevBatteryLevel;
        private Float prevHeight;
        private Double prevSpeed;
        private double prrtTimestamp0 = 0.0d;
        private Runnable updateBatteryIndicatorAction = new C11453();
        private Runnable updateGraphAction = new C11431();
        private Runnable updateHeightIndicatorAction = new C11442();
        private Runnable updateSpeedIndicatorAction = new C11464();

        class C11431 implements Runnable {
            C11431() {
            }

            public void run() {
                WatchVideoActivity.this.graphPointer.moveTo(UpdateGraphTask.this.position);
            }
        }

        class C11442 implements Runnable {
            C11442() {
            }

            public void run() {
                WatchVideoActivity.this.currHeightIndicator.setValue(UpdateGraphTask.this.currHeight, true);
            }
        }

        class C11453 implements Runnable {
            C11453() {
            }

            public void run() {
                WatchVideoActivity.this.batteryIndicator.setText(String.valueOf(UpdateGraphTask.this.currBatteryLevel) + " %");
            }
        }

        class C11464 implements Runnable {
            C11464() {
            }

            public void run() {
                WatchVideoActivity.this.speedIndicator.setText(String.format("%.1f km/h", new Object[]{UpdateGraphTask.this.currSpeed}));
            }
        }

        UpdateGraphTask(GraphView graphView) {
            this.graph = graphView;
        }

        public void run() {
            if (WatchVideoActivity.this.vc.isPrepared()) {
                float videoDuration = (float) WatchVideoActivity.this.vc.getVideoDuration();
                float videoProgress = (float) WatchVideoActivity.this.vc.getVideoProgress();
                if (WatchVideoActivity.this.graphIsReady) {
                    Object obj;
                    long j;
                    if (WatchVideoActivity.this.videoPrrt.isInitialized()) {
                        if (this.prrtTimestamp0 == 0.0d) {
                            this.prrtTimestamp0 = WatchVideoActivity.this.videoPrrt.getTimestamp(0);
                        }
                        int access$1600 = (int) ((((float) WatchVideoActivity.this.framerate) * videoProgress) / 1000.0f);
                        long timestamp = (long) ((WatchVideoActivity.this.videoPrrt.getTimestamp(access$1600) - this.prrtTimestamp0) * 1000.0d);
                        long timestamp2 = (long) (WatchVideoActivity.this.videoPrrt.getTimestamp(access$1600) - this.prrtTimestamp0);
                        j = WatchVideoActivity.this.currentTimeInterval;
                        Long valueOf = Long.valueOf((timestamp2 * 1000) + WatchVideoActivity.this.currentTimeInterval);
                        this.position = Long.valueOf(timestamp + j).longValue();
                        obj = valueOf;
                    } else {
                        j = (long) (videoProgress / 1000.0f);
                        this.position = (long) ((videoProgress / videoDuration) * ((float) WatchVideoActivity.this.heightData.size()));
                        obj = null;
                    }
                    this.graph.post(this.updateGraphAction);
                    Float f = (Float) WatchVideoActivity.this.heightIndicatorValues.get(obj);
                    if (f != null && (this.prevHeight == null || !(this.prevHeight == null || this.prevHeight.floatValue() == f.floatValue()))) {
                        this.currHeight = ((float) (f.intValue() / 100)) / 10.0f;
                        this.prevHeight = f;
                        WatchVideoActivity.this.currHeightIndicator.post(this.updateHeightIndicatorAction);
                    }
                    Integer num = (Integer) WatchVideoActivity.this.batteryValues.get(obj);
                    if (num != null && (this.prevBatteryLevel == null || !(this.prevBatteryLevel == null || this.prevBatteryLevel.intValue() == num.intValue()))) {
                        this.currBatteryLevel = num.intValue();
                        this.prevBatteryLevel = num;
                        WatchVideoActivity.this.batteryIndicator.post(this.updateBatteryIndicatorAction);
                    }
                    Double d = (Double) WatchVideoActivity.this.speedValues.get(obj);
                    if (d == null) {
                        return;
                    }
                    if (this.prevSpeed == null || !(this.prevSpeed == null || this.prevSpeed.equals(d))) {
                        this.currSpeed = d;
                        this.prevSpeed = d;
                        WatchVideoActivity.this.speedIndicator.post(this.updateSpeedIndicatorAction);
                    }
                }
            }
        }
    }

    private void cancelHideBarAndVideoControllerDelayed() {
        Handler handler = findViewById(16908290).getHandler();
        if (handler != null) {
            handler.removeCallbacks(this.hideControlsAction);
        }
    }

    private void hideBarAndVideoController() {
        this.vc.hide();
        getParrotActionBar().hide(true);
    }

    private void hideBarAndVideoControllerDelayed(int i) {
        findViewById(16908290).postDelayed(this.hideControlsAction, (long) i);
    }

    private void initGraphOverlay() {
        final GraphView graphView = (GraphView) findViewById(C0984R.id.graph1);
        final ProgressDialog progressDialog = new ProgressDialog(this, C0984R.style.FreeFlightDialog_Light_Indeterminate);
        if (!getIntent().hasExtra("flight") || this.media.isRemote()) {
            makeOverlayVisible(false);
            return;
        }
        this.flight = (Flight) getIntent().getSerializableExtra("flight");
        new DownloadFlightGraphTask(AcademyUtils.login, AcademyUtils.password) {

            class C11401 implements OnCancelListener {
                C11401() {
                }

                public void onCancel(DialogInterface dialogInterface) {
                    C11411.this.cancel(true);
                    WatchVideoActivity.this.finish();
                }
            }

            protected void onPostExecute(List<FlightDataItem> list) {
                super.onPostExecute(list);
                if (list == null || list.isEmpty()) {
                    Log.w(WatchVideoActivity.TAG, "No flight data");
                    return;
                }
                WatchVideoActivity.this.currentTimeInterval = WatchVideoActivity.this.getCurrMediaTimeInterval(WatchVideoActivity.this.flight);
                WatchVideoActivity.this.prepareDataForVideoSync(list);
                WatchVideoActivity.this.heightSeries = new Series(graphView);
                WatchVideoActivity.this.heightSeries.setSortedPairs(WatchVideoActivity.this.heightData);
                graphView.addSeries(WatchVideoActivity.this.heightSeries);
                Axis axis = new Axis(graphView, WatchVideoActivity.this.heightSeries);
                axis.setAlignment(Align.LEFT);
                axis.setTickStyle(TickStyle.LINES);
                graphView.addAxis(axis);
                axis = new Axis(graphView, WatchVideoActivity.this.heightSeries);
                axis.setAlignment(Align.BOTTOM);
                axis.setTickStyle(TickStyle.LINES);
                graphView.addAxis(axis);
                WatchVideoActivity.this.graphPointer = new Pointer(graphView, WatchVideoActivity.this.heightSeries);
                graphView.setPointer(WatchVideoActivity.this.graphPointer);
                graphView.requestLayout();
                WatchVideoActivity.this.graphIsReady = true;
                progressDialog.dismiss();
                WatchVideoActivity.this.onGraphDataReady();
            }

            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setMessage(WatchVideoActivity.this.getString(C0984R.string.aa_ID000000));
                progressDialog.setSubMessage(HttpVersions.HTTP_0_9);
                progressDialog.setCancelable(true);
                progressDialog.setProgressStyle(0);
                progressDialog.showDelayed(500);
                progressDialog.setOnCancelListener(new C11401());
            }
        }.execute(new Object[]{this, this.flight});
    }

    private void initVideoController() {
        this.videoView = (VideoView) findViewById(C0984R.id.video_view);
        this.vc = new VideoPlayerController(findViewById(C0984R.id.media_controller));
        VideoPlayerWrapper localPlayerWrapper = new LocalPlayerWrapper(this.videoView);
        this.vc.attachVideo(localPlayerWrapper);
        localPlayerWrapper.setOnPlayStateChangedListener(this);
        localPlayerWrapper.setOnCompletionListener(this);
        findViewById(C0984R.id.video_holder).setOnClickListener(this);
        this.hideControlsAction = new C11422();
    }

    private boolean isVideoPlaying() {
        return this.vc.isPlaying();
    }

    private void makeOverlayVisible(boolean z) {
        int i = z ? 0 : 4;
        findViewById(C0984R.id.graph1).setVisibility(i);
        this.currHeightIndicator.setVisibility(i);
        this.speedIndicator.setVisibility(i);
        this.batteryIndicator.setVisibility(i);
    }

    private void pauseVideo() {
        this.vc.pause();
    }

    private void playVideo() {
        this.vc.play();
    }

    private void playVideoInExternalPlayer() {
        Intent intent = new Intent("android.intent.action.VIEW");
        Uri uri = ((MediaVO) this.extras.get(WatchMediaActivity.EXTRA_MEDIA_OBJ)).getUri();
        if (uri != null) {
            intent.setDataAndType(uri, "video/mp4");
            startActivity(intent);
        }
    }

    @SuppressLint({"NewApi"})
    private void prepareDataForVideoSync(List<FlightDataItem> list) {
        int size = list.size();
        this.flightStartTime = this.flight.getDateTimeAsDate();
        this.dateZero = new Date(((FlightDataItem) list.get(0)).getTime());
        this.heightIndicatorValues = new HashMap(size);
        this.batteryValues = new HashMap(size);
        this.speedValues = new HashMap(size);
        List arrayList = new ArrayList(size);
        Date date = new Date(((FlightDataItem) list.get(0)).getTime());
        Date date2 = new Date();
        int i = -1;
        int i2 = -1;
        int i3 = 0;
        while (i3 < size) {
            FlightDataItem flightDataItem = (FlightDataItem) list.get(i3);
            float altitude = (float) flightDataItem.getAltitude();
            date2.setTime(flightDataItem.getTime());
            long time = date2.getTime() - date.getTime();
            arrayList.add(new Pair(Long.valueOf(time), Double.valueOf((double) altitude)));
            this.heightIndicatorValues.put(Long.valueOf(time), Float.valueOf(altitude));
            this.batteryValues.put(Long.valueOf(time), Integer.valueOf(flightDataItem.getBatteryLevel()));
            this.speedValues.put(Long.valueOf(time), Double.valueOf((flightDataItem.getSpeed() * 3.6d) / 1000.0d));
            if (i == -1 && time >= this.videoStartTime.getTime() - this.flightStartTime.getTime()) {
                i = i3;
            }
            int i4 = (i2 != -1 || time <= this.videoStopTime.getTime() - this.flightStartTime.getTime()) ? i2 : i3;
            i3++;
            i2 = i4;
        }
        if (i == -1) {
            this.heightData = arrayList;
        } else if (i == 0 && i2 == 0) {
            Log.w(TAG, "Unable to detect part of the graph that corresponds to the video");
            this.heightData = arrayList;
        } else {
            if (i2 == -1) {
                i2 = size;
            }
            this.heightData = arrayList.subList(i, i2);
        }
    }

    private void prepareVideoPlayback() {
        this.videoView.setVideoURI(this.media.getUri());
        this.vc.invalidate();
        this.videoView.pause();
        String path = this.media.getPath();
        this.videoPrrt = new VideoPrrt(path);
        this.framerate = VideoPrrt.getFrameRate(path);
    }

    private void resumeVideo() {
        this.vc.play();
    }

    private void showBarAndVideoController() {
        this.vc.show();
        getParrotActionBar().show(true);
    }

    public static void start(Context context, MediaVO mediaVO, String str, String str2) {
        start(context, mediaVO, str, str2, null);
    }

    public static void start(Context context, MediaVO mediaVO, String str, String str2, Flight flight) {
        start(context, mediaVO, str, str2, flight, true);
    }

    public static void start(Context context, MediaVO mediaVO, String str, String str2, Flight flight, boolean z) {
        Intent intent = new Intent(context, WatchVideoActivity.class);
        intent.putExtra(WatchMediaActivity.EXTRA_MEDIA_OBJ, mediaVO);
        intent.putExtra(WatchMediaActivity.EXTRA_MIME_TYPE, str);
        intent.putExtra(WatchMediaActivity.EXTRA_TITLE, str2);
        intent.putExtra(EXTRA_CLOSE_ON_FINISH, z);
        if (flight != null) {
            intent.putExtra("flight", flight);
        }
        context.startActivity(intent);
    }

    public static void start(Context context, MediaVO mediaVO, String str, String str2, boolean z) {
        start(context, mediaVO, str, str2, null, z);
    }

    private void stopVideo() {
        this.vc.stop();
    }

    public long getCurrMediaTimeInterval(Flight flight) {
        this.flightStartTime = flight.getDateTimeAsDate();
        String path = ((MediaVO) this.extras.get(WatchMediaActivity.EXTRA_MEDIA_OBJ)).getPath();
        Log.d(TAG, "Media path: " + path);
        String name = new File(path).getName();
        try {
            this.videoStartTime = new SimpleDateFormat("'video_'yyyyMMdd'_'HHmmss.'mp4'", Locale.getDefault()).parse(name);
        } catch (ParseException e) {
            Log.w(TAG, "Unable to parse date from filename " + name + ", using current time as date taken");
            e.printStackTrace();
        }
        double timestamp = this.videoPrrt.getTimestamp(0);
        double timestamp2 = this.videoPrrt.getTimestamp(this.videoPrrt.getFrames() - 1);
        this.videoStopTime = new Date((long) (((double) this.videoStartTime.getTime()) + (Math.ceil(timestamp2 - timestamp) * 1000.0d)));
        Log.d(TAG, "Flight start time: " + SimpleDateFormat.getDateTimeInstance(3, 1).format(this.flightStartTime));
        Log.d(TAG, "Video start time: " + SimpleDateFormat.getDateTimeInstance(3, 1).format(this.videoStartTime));
        Log.d(TAG, "Video end time: " + SimpleDateFormat.getDateTimeInstance(3, 1).format(this.videoStopTime));
        Log.d(TAG, "Video duration: " + (timestamp2 - timestamp) + " ms, or " + Math.ceil(timestamp2 - timestamp) + " sec");
        return this.videoStartTime.getTime() - this.flightStartTime.getTime();
    }

    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == C0984R.id.video_holder) {
            onVideoClicked();
        }
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        if (this.closeOnFinish) {
            finish();
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.activity_watch_video);
        overridePendingTransition(0, 0);
        this.atomRetreiver = new VideoAtomRetriever();
        this.currHeightIndicator = (SlideRuleView) findViewById(C0984R.id.activity_watch_video_indicator_current_height);
        this.batteryIndicator = (TextView) findViewById(C0984R.id.activity_watch_video_text_charge);
        this.speedIndicator = (TextView) findViewById(C0984R.id.activity_watch_video_text_speed);
        this.media = (MediaVO) getIntent().getParcelableExtra(WatchMediaActivity.EXTRA_MEDIA_OBJ);
        this.closeOnFinish = this.extras.getBoolean(EXTRA_CLOSE_ON_FINISH, true);
        initVideoController();
        prepareVideoPlayback();
        initGraphOverlay();
    }

    protected void onDestroy() {
        super.onDestroy();
        stopVideo();
        this.videoPrrt.release();
    }

    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        Log.w(TAG, "Error occured while trying to play video via embedded player. Trying to play video in external player");
        playVideoInExternalPlayer();
        finish();
        return true;
    }

    protected void onGraphDataReady() {
        makeOverlayVisible(true);
        if (!isVideoPlaying()) {
            playVideo();
        }
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        switch (i) {
            case 126:
                if (!isVideoPlaying()) {
                    playVideo();
                    break;
                }
                break;
            case 127:
                if (isVideoPlaying()) {
                    pauseVideo();
                    break;
                }
                break;
            default:
                return super.onKeyUp(i, keyEvent);
        }
        return true;
    }

    protected void onPause() {
        super.onPause();
        this.wasPlaying = this.vc.isPlaying();
        pauseVideo();
        if (this.updateOverlayDataTask != null) {
            this.updateOverlayDataTask.cancel();
        }
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
    }

    public void onPlayStateChanged(boolean z) {
        if (z) {
            hideBarAndVideoControllerDelayed(GamesClient.STATUS_ACHIEVEMENT_UNLOCK_FAILURE);
            AnimationUtils.makeInvisibleAnimated(findViewById(C0984R.id.activity_watch_video_btn_play));
            return;
        }
        cancelHideBarAndVideoControllerDelayed();
        showBarAndVideoController();
        AnimationUtils.makeVisibleAnimated(findViewById(C0984R.id.activity_watch_video_btn_play));
    }

    @SuppressLint({"NewApi"})
    protected void onResume() {
        super.onResume();
        if (this.wasPlaying) {
            this.vc.resume();
        }
        this.timer = new Timer();
        this.updateOverlayDataTask = new UpdateGraphTask((GraphView) findViewById(C0984R.id.graph1));
        this.timer.scheduleAtFixedRate(this.updateOverlayDataTask, 0, 75);
    }

    protected void onStart() {
        super.onStart();
        showBarAndVideoController();
        if ((this.flight != null && !isVideoPlaying() && this.graphIsReady) || (this.flight == null && !isVideoPlaying())) {
            playVideo();
        }
    }

    protected void onStop() {
        super.onStop();
        hideBarAndVideoController();
        pauseVideo();
    }

    protected void onVideoClicked() {
        if (isVideoPlaying()) {
            pauseVideo();
        } else {
            resumeVideo();
        }
    }
}
