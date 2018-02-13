package com.parrot.freeflight.track_3d_viewer;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.text.format.DateFormat;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;
import com.google.api.client.http.ExponentialBackOffPolicy;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.model.AcademyCredentials;
import com.parrot.freeflight.academy.model.Flight;
import com.parrot.freeflight.academy.model.FlightDataItem;
import com.parrot.freeflight.academy.utils.AcademyUtils;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.dialogs.ProgressDialog;
import com.parrot.freeflight.media.LocalMediaProvider;
import com.parrot.freeflight.media.MediaProvider.MediaType;
import com.parrot.freeflight.track_3d_viewer.MediaPlayerController.IMediaControllerCallback;
import com.parrot.freeflight.track_3d_viewer.ThumbnailsView.IThumbnailClickListener;
import com.parrot.freeflight.track_3d_viewer.ThumbnailsView.ThumbnailItem;
import com.parrot.freeflight.track_3d_viewer.ios_stuff.IOsUtils;
import com.parrot.freeflight.track_3d_viewer.ios_stuff.OpenGLInterleavedVertex;
import com.parrot.freeflight.track_3d_viewer.tasks.LoadFlightDetailsTask;
import com.parrot.freeflight.track_3d_viewer.tasks.LoadFlightDetailsTask.TaskResult;
import com.parrot.freeflight.track_3d_viewer.tasks.LoadMapTask;
import com.parrot.freeflight.track_3d_viewer.tasks.LoadMapTask.TaskParams;
import com.parrot.freeflight.track_3d_viewer.utils.Holder;
import com.parrot.freeflight.track_3d_viewer.utils.MultisampleConfigChooser;
import com.parrot.freeflight.track_3d_viewer.utils.Utils;
import com.parrot.freeflight.ui.ActionBar;
import com.parrot.freeflight.ui.ActionBar.Background;
import com.parrot.freeflight.utils.SimpleDelegate;
import com.parrot.freeflight.utils.SystemUtils;
import com.parrot.freeflight.utils.TextureUtils;
import com.parrot.freeflight.vo.MediaVO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Track3DViewerActivity extends ParrotActivity {
    private static final String EXTRA_FLIGHT_OBJ = "flight_obj";
    private static final String EXTRA_TRACK_DATE = "track_date";
    private static final String EXTRA_TRACK_ID = "track_id";
    private static final String TAG = Track3DViewerActivity.class.getSimpleName();
    private LinearLayout2 container;
    private final IMediaControllerCallback controllerCallback = new C11912();
    private int currActiveMedia = -1;
    private int currMediaDurationMsec = -1;
    private final Runnable finishRunnable = new C11901();
    private Flight flight;
    private GLSurfaceView glSurfaceView;
    private final SimpleDelegate layoutDelegate = new C11978();
    private final Runnable loadActiveMediaInPlayerRunnable = new C11956();
    private MyLoadMapTask loadMapTask;
    private LoadFlightDetailsTask loadTrackTask;
    private final MapParams mapParams = new MapParams(18, 512);
    private LinearLayout mediaControlsContainer;
    private List<MediaVO> mediaList = null;
    private MediaPlayerController mediaPlayerController;
    private final OnCompletionListener playbackCompleteListener = new C11934();
    private final OnPreparedListener playbackPrepareListener = new C11945();
    private boolean playing = false;
    private ProgressDialog progressDlg;
    private SeekBar seekBar;
    private final IThumbnailClickListener thumbnailClickListener = new C11923();
    private FrameLayout thumbnailsContainer;
    private ThumbnailsView thumbnailsView;
    private long trackDate;
    private int trackId;
    private VideoView videoView;

    class C11901 implements Runnable {
        C11901() {
        }

        public void run() {
            Track3DViewerActivity.this.finish();
        }
    }

    class C11912 implements IMediaControllerCallback {
        C11912() {
        }

        public void onPause() {
            if (Track3DViewerActivity.this.videoView.getVisibility() == 0) {
                Track3DViewerActivity.this.videoView.pause();
            }
            Track3DViewerActivity.this.playing = false;
        }

        public void onPlay() {
            if (Track3DViewerActivity.this.videoView.getVisibility() == 0) {
                Track3DViewerActivity.this.videoView.start();
            }
            Track3DViewerActivity.this.playing = true;
        }

        public void onPositionChanged(float f, boolean z) {
            if (z) {
                Track3DViewerActivity.this.setMediaPos(f);
            } else {
                Track3DViewerActivity.this.checkMediaAtPosition(f);
            }
        }
    }

    class C11923 implements IThumbnailClickListener {
        C11923() {
        }

        public void onThumbnailClicked(ThumbnailItem thumbnailItem) {
            Track3DViewerActivity.this.mediaPlayerController.setPosition(thumbnailItem.pos);
        }
    }

    class C11934 implements OnCompletionListener {
        C11934() {
        }

        public void onCompletion(MediaPlayer mediaPlayer) {
            Track3DViewerActivity.this.videoView.setVisibility(4);
        }
    }

    class C11945 implements OnPreparedListener {
        C11945() {
        }

        public void onPrepared(MediaPlayer mediaPlayer) {
            Track3DViewerActivity.this.currMediaDurationMsec = mediaPlayer.getDuration();
        }
    }

    class C11956 implements Runnable {
        C11956() {
        }

        public void run() {
            Track3DViewerActivity.this.loadActiveMediaInPlayer();
        }
    }

    class C11967 implements OnCancelListener {
        C11967() {
        }

        public void onCancel(DialogInterface dialogInterface) {
            if (Track3DViewerActivity.this.loadTrackTask != null) {
                Track3DViewerActivity.this.loadTrackTask.cancel(true);
                Track3DViewerActivity.this.loadTrackTask = null;
            }
            if (Track3DViewerActivity.this.loadMapTask != null) {
                Track3DViewerActivity.this.loadMapTask.cancel(true);
                Track3DViewerActivity.this.loadMapTask = null;
            }
            Track3DViewerActivity.this.finish();
        }
    }

    class C11978 implements SimpleDelegate {
        C11978() {
        }

        public void call() {
            Track3DViewerActivity.this.thumbnailsView.setLeftRight(Track3DViewerActivity.this.seekBar.getLeft() + Track3DViewerActivity.this.seekBar.getThumbOffset(), Track3DViewerActivity.this.seekBar.getRight() - Track3DViewerActivity.this.seekBar.getThumbOffset());
        }
    }

    class C11989 implements OnClickListener {
        C11989() {
        }

        public void onClick(View view) {
            Track3DViewerActivity.this.finish();
        }
    }

    private class MyLoadFlightDetailsTask extends LoadFlightDetailsTask {
        public MyLoadFlightDetailsTask(Context context, AcademyCredentials academyCredentials) {
            super(context, academyCredentials);
        }

        protected void onPostExecute(TaskResult taskResult) {
            Track3DViewerActivity.this.loadTrackTask = null;
            if (taskResult.exception != null) {
                Utils.showAlertDialog(Track3DViewerActivity.this, Track3DViewerActivity.this.getString(C0984R.string.aa_ID000099), String.format("Failed to load track: %s", new Object[]{taskResult.exception}), Track3DViewerActivity.this.finishRunnable);
                return;
            }
            Holder holder = new Holder();
            Holder holder2 = new Holder();
            Holder holder3 = new Holder();
            Holder holder4 = new Holder();
            TrackHelper.repairBlankCoordinatesAndNegativeAltitude(taskResult.items, holder2, holder, holder4, holder3);
            if (Track3DViewerActivity.this.getMedia().size() != 0) {
                TrackHelper.synchronizeAltitudeWithPlayer(taskResult.items, Track3DViewerActivity.mediaList2PathList(Track3DViewerActivity.this.getMedia()));
            }
            Holder holder5 = new Holder();
            Holder holder6 = new Holder();
            Holder holder7 = new Holder();
            IOsUtils.safeRegion(((Float) holder4.value).floatValue(), ((Float) holder2.value).floatValue(), ((Float) holder.value).floatValue(), 1024, 768, SystemUtils.isTablet(Track3DViewerActivity.this), holder5, holder6, holder7);
            Pair prepareRoad = new TrackBuilder(taskResult.items, ((int) (((Double) holder7.value).doubleValue() + ExponentialBackOffPolicy.DEFAULT_RANDOMIZATION_FACTOR)) + 1, Track3DViewerActivity.this.mapParams.width, (double) ((Float) holder2.value).floatValue(), (double) ((Float) holder.value).floatValue()).prepareRoad();
            Track3DViewerActivity.this.loadMapTask = new MyLoadMapTask(taskResult.items, prepareRoad == null ? null : (OpenGLInterleavedVertex[]) prepareRoad.first, prepareRoad == null ? null : (short[]) prepareRoad.second);
            Track3DViewerActivity.this.loadMapTask.execute(new TaskParams[]{new TaskParams(((Float) holder.value).floatValue(), ((Float) holder2.value).floatValue(), r2, Track3DViewerActivity.this.mapParams.width)});
        }
    }

    private class MyLoadMapTask extends LoadMapTask {
        private final List<FlightDataItem> flightDetails;
        private final short[] roadIndexes;
        private final OpenGLInterleavedVertex[] roadVertexes;

        private MyLoadMapTask(List<FlightDataItem> list, OpenGLInterleavedVertex[] openGLInterleavedVertexArr, short[] sArr) {
            this.flightDetails = list;
            this.roadVertexes = openGLInterleavedVertexArr;
            this.roadIndexes = sArr;
        }

        protected void onPostExecute(LoadMapTask.TaskResult taskResult) {
            Track3DViewerActivity.this.loadMapTask = null;
            if (taskResult.exception != null) {
                Utils.showAlertDialog(Track3DViewerActivity.this, Track3DViewerActivity.this.getString(C0984R.string.aa_ID000099), String.format("Error during loading map: %s", new Object[]{taskResult.exception.toString()}), Track3DViewerActivity.this.finishRunnable);
                return;
            }
            Track3DViewerActivity.this.dismissProgressDlg();
            Track3DViewerActivity.this.init(taskResult.bitmap, this.flightDetails, this.roadVertexes, this.roadIndexes);
        }
    }

    private boolean checkMediaAtPosition(float f) {
        int mediaAtPos = getMediaAtPos(f);
        if (this.currActiveMedia == mediaAtPos) {
            return false;
        }
        this.currActiveMedia = mediaAtPos;
        this.currMediaDurationMsec = -1;
        runOnUiThread(this.loadActiveMediaInPlayerRunnable);
        return true;
    }

    private void dismissProgressDlg() {
        if (this.progressDlg != null) {
            this.progressDlg.dismiss();
            this.progressDlg = null;
        }
    }

    private List<MediaVO> getMedia() {
        if (this.mediaList == null) {
            this.mediaList = new LocalMediaProvider(this, this.flight).getMediaList(MediaType.VIDEOS);
            if (this.mediaList == null) {
                this.mediaList = Collections.emptyList();
            }
            Collections.sort(this.mediaList, MediaVO.sortByDateTakenComparator);
        }
        return this.mediaList;
    }

    private int getMediaAtPos(float f) {
        List thumbnailsData = this.thumbnailsView.getThumbnailsData();
        if (thumbnailsData != null) {
            for (int size = thumbnailsData.size() - 1; size >= 0; size--) {
                ThumbnailItem thumbnailItem = (ThumbnailItem) thumbnailsData.get(size);
                if (f >= thumbnailItem.pos) {
                    if (f < thumbnailItem.length + thumbnailItem.pos) {
                        return size;
                    }
                }
            }
        }
        return -1;
    }

    private void init(Bitmap bitmap, List<FlightDataItem> list, OpenGLInterleavedVertex[] openGLInterleavedVertexArr, short[] sArr) {
        setContentView(C0984R.layout.media_player);
        ActionBar parrotActionBar = getParrotActionBar();
        parrotActionBar.initBackButton(new C11989());
        parrotActionBar.changeBackground(Background.ACCENT_HALF_TRANSP);
        setTitle(String.format("%s %s %s", new Object[]{getString(C0984R.string.aa_ID000229), Utils.formatIntWithSpaces(this.trackId), DateFormat.getDateFormat(this).format(new Date(this.trackDate))}));
        this.glSurfaceView = (GLSurfaceView) findViewById(C0984R.id.surface_view);
        this.videoView = (VideoView) findViewById(C0984R.id.threed_video_view);
        this.container = (LinearLayout2) findViewById(C0984R.id.media_controls);
        this.mediaControlsContainer = (LinearLayout) findViewById(C0984R.id.media_controls_container);
        this.thumbnailsContainer = (FrameLayout) findViewById(C0984R.id.thumbnails_container);
        this.thumbnailsView = (ThumbnailsView) findViewById(C0984R.id.thumbnails);
        this.seekBar = (SeekBar) findViewById(C0984R.id.media_controller_progress);
        this.container.setLayoutDelegate(this.layoutDelegate);
        this.thumbnailsView.setThumbnailClickListener(this.thumbnailClickListener);
        this.videoView.setZOrderOnTop(true);
        this.videoView.setVisibility(4);
        this.videoView.setOnCompletionListener(this.playbackCompleteListener);
        this.videoView.setOnPreparedListener(this.playbackPrepareListener);
        this.mediaPlayerController = new MediaPlayerController(Thread.currentThread(), new Handler(), (ImageButton) findViewById(C0984R.id.play_pause_button), this.seekBar, (TextView) findViewById(C0984R.id.time_current), (TextView) findViewById(C0984R.id.all_time));
        this.mediaPlayerController.setControllerCallback(this.controllerCallback);
        if (((ActivityManager) getSystemService("activity")).getDeviceConfigurationInfo().reqGlEsVersion >= AccessibilityEventCompat.TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY) {
            this.glSurfaceView.setEGLContextClientVersion(2);
            this.glSurfaceView.setEGLConfigChooser(new MultisampleConfigChooser());
            Bitmap makeTexture = TextureUtils.makeTexture(bitmap, true);
            if (makeTexture != bitmap) {
                bitmap.recycle();
            }
            Object sceneRenderer = new SceneRenderer(this, this.mediaPlayerController, makeTexture, list, openGLInterleavedVertexArr, sArr);
            this.glSurfaceView.setRenderer(sceneRenderer);
            this.glSurfaceView.setOnTouchListener(sceneRenderer);
            initThumbnails(getMedia());
            return;
        }
        Utils.showAlertDialog(this, getString(C0984R.string.aa_ID000099), "Open GL ES 2.0 is required to run.", this.finishRunnable);
    }

    private void initThumbnails(java.util.List<com.parrot.freeflight.vo.MediaVO> r15) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.ssa.SSATransform.placePhi(SSATransform.java:82)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:50)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r14 = this;
        r12 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r0 = r15.size();
        if (r0 != 0) goto L_0x0009;
    L_0x0008:
        return;
    L_0x0009:
        r0 = r14.mediaControlsContainer;
        r1 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        r0.setBackgroundColor(r1);
        r0 = r14.thumbnailsContainer;
        r1 = 0;
        r0.setVisibility(r1);
        r1 = new java.util.ArrayList;
        r1.<init>();
        r2 = new com.parrot.freeflight.media.LocalMediaProvider;
        r0 = r14.flight;
        r2.<init>(r14, r0);
        r0 = r14.flight;
        r3 = r0.getTotalFlightTime();
        r0 = r14.flight;
        r0 = r0.getDateTimeAsDate();
        r4 = r0.getTime();
        r4 = r4 / r12;
        r6 = r15.iterator();
    L_0x0037:
        r0 = r6.hasNext();
        if (r0 == 0) goto L_0x0081;
    L_0x003d:
        r0 = r6.next();
        r0 = (com.parrot.freeflight.vo.MediaVO) r0;
        r8 = r0.getDateTaken();
        r8 = r8 / r12;
        r7 = com.parrot.freeflight.media.MediaProvider.ThumbSize.MICRO;
        r7 = r2.getThumbnail(r0, r7);
        r8 = r8 - r4;
        r8 = (float) r8;
        r9 = (float) r3;
        r8 = r8 / r9;
        r9 = new com.parrot.ardronetool.video.VideoPrrt;
        r10 = r0.getPath();
        r9.<init>(r10);
        r10 = r9.getFrames();	 Catch:{ all -> 0x007c }
        r10 = (float) r10;	 Catch:{ all -> 0x007c }
        r11 = r0.getPath();	 Catch:{ all -> 0x007c }
        r11 = com.parrot.ardronetool.video.VideoPrrt.getFrameRate(r11);	 Catch:{ all -> 0x007c }
        r11 = (float) r11;
        r10 = r10 / r11;
        r11 = (float) r3;
        r10 = r10 / r11;
        r9.release();
        r9 = new com.parrot.freeflight.track_3d_viewer.ThumbnailsView$ThumbnailItem;
        r7 = r7.getBitmap();
        r9.<init>(r8, r10, r7, r0);
        r1.add(r9);
        goto L_0x0037;
    L_0x007c:
        r0 = move-exception;
        r9.release();
        throw r0;
    L_0x0081:
        r0 = r14.thumbnailsView;
        r0.setThumbnailsData(r1);
        goto L_0x0008;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.parrot.freeflight.track_3d_viewer.Track3DViewerActivity.initThumbnails(java.util.List):void");
    }

    private void loadActiveMediaInPlayer() {
        if (this.currActiveMedia != -1) {
            Uri uri = ((MediaVO) getMedia().get(this.currActiveMedia)).getUri();
            if (this.videoView.getVisibility() != 0) {
                this.videoView.setVisibility(0);
            }
            this.videoView.setVideoURI(uri);
            if (this.playing) {
                this.videoView.start();
            }
        }
    }

    private static List<String> mediaList2PathList(List<MediaVO> list) {
        List arrayList = new ArrayList(list.size());
        for (MediaVO path : list) {
            arrayList.add(path.getPath());
        }
        return arrayList;
    }

    private void setMediaPos(float f) {
        if (!checkMediaAtPosition(f)) {
            if (this.currActiveMedia == -1) {
                this.videoView.setVisibility(4);
            } else if (this.currMediaDurationMsec != -1) {
                this.videoView.seekTo((int) (((float) this.currMediaDurationMsec) * f));
            }
        }
    }

    public static void startActivity(Context context, int i, long j, Flight flight) {
        Intent intent = new Intent(context, Track3DViewerActivity.class);
        intent.putExtra(EXTRA_TRACK_ID, i);
        intent.putExtra(EXTRA_TRACK_DATE, j);
        intent.putExtra(EXTRA_FLIGHT_OBJ, flight);
        context.startActivity(intent);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.trackId = getIntent().getIntExtra(EXTRA_TRACK_ID, 0);
        this.trackDate = getIntent().getLongExtra(EXTRA_TRACK_DATE, 0);
        this.flight = (Flight) getIntent().getSerializableExtra(EXTRA_FLIGHT_OBJ);
        this.progressDlg = new ProgressDialog(this, C0984R.style.FreeFlightDialog_Hotspot);
        this.progressDlg.setMessage(getString(C0984R.string.ae_ID000068).toUpperCase());
        this.progressDlg.setSubMessage(getString(C0984R.string.aa_ID000180));
        this.progressDlg.setCanceledOnTouchOutside(false);
        this.progressDlg.setOnCancelListener(new C11967());
        this.progressDlg.show();
        AcademyCredentials academyCredentials = null;
        if (this.flight.getUser() != null && this.flight.getUser().equals(AcademyUtils.profile.getUser())) {
            academyCredentials = new AcademyCredentials(AcademyUtils.login, AcademyUtils.password);
        }
        this.loadTrackTask = new MyLoadFlightDetailsTask(this, academyCredentials);
        this.loadTrackTask.execute(new Integer[]{Integer.valueOf(this.trackId)});
    }

    protected void onPause() {
        super.onPause();
        if (this.glSurfaceView != null) {
            this.glSurfaceView.onPause();
        }
    }

    protected void onResume() {
        super.onResume();
        if (this.glSurfaceView != null) {
            this.glSurfaceView.onResume();
        }
    }
}
