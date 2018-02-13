package com.parrot.freeflight.academy.activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckedTextView;
import android.widget.RelativeLayout;
import com.parrot.ardronetool.video.VideoAtomRetriever;
import com.parrot.ardronetool.video.VideoAtomRetriever.AtomType;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.model.Flight;
import com.parrot.freeflight.academy.model.FlightDataItem;
import com.parrot.freeflight.academy.tasks.DownloadFlightGraphTask;
import com.parrot.freeflight.academy.utils.AcademyUtils;
import com.parrot.freeflight.activities.GalleryActivity;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.media.Exif2Interface;
import com.parrot.freeflight.media.Exif2Interface.Tag;
import com.parrot.freeflight.media.LocalMediaProvider;
import com.parrot.freeflight.media.MediaProvider;
import com.parrot.freeflight.media.MediaProvider.MediaType;
import com.parrot.freeflight.media.MediaProvider.ThumbSize;
import com.parrot.freeflight.media.RemoteMediaProvider;
import com.parrot.freeflight.ui.ActionBar;
import com.parrot.freeflight.ui.widgets.graph.Axis;
import com.parrot.freeflight.ui.widgets.graph.Axis.Align;
import com.parrot.freeflight.ui.widgets.graph.Axis.AxisLabelFormatter;
import com.parrot.freeflight.ui.widgets.graph.Axis.TickStyle;
import com.parrot.freeflight.ui.widgets.graph.GraphView;
import com.parrot.freeflight.ui.widgets.graph.MediaThumbsOverlay;
import com.parrot.freeflight.ui.widgets.graph.MediaThumbsOverlay.OnMediaThumbClickedListener;
import com.parrot.freeflight.ui.widgets.graph.Series;
import com.parrot.freeflight.utils.FileUtils;
import com.parrot.freeflight.utils.ShareUtils;
import com.parrot.freeflight.vo.MediaVO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.util.URIUtil;

public class AcademyGraphActivity extends ParrotActivity implements OnClickListener, OnMediaThumbClickedListener {
    private static final String EXTRA_FLIGHT_OBJ = "academy.graph.extra.flight";
    private static final String TAG = AcademyGraphActivity.class.getSimpleName();
    private RelativeLayout altitudeLayout;
    private Axis batteryAxis;
    private RelativeLayout batteryLayout;
    private Series batterySeries;
    private CheckedTextView btnAltitude;
    private CheckedTextView btnBattery;
    private CheckedTextView btnSpeed;
    private int checkedButtonsCount;
    private DownloadFlightGraphTask downloadGraphTask;
    private Flight flight;
    private AxisLabelFormatter formatterBattery;
    private AxisLabelFormatter formatterTime;
    private GraphView graph;
    private Axis heightAxis;
    private Series heightSeries;
    private List<MediaVO> mediaForFlight;
    private MediaProvider mediaProvider;
    private Axis speedAxis;
    private RelativeLayout speedLayout;
    private Series speedSeries;

    class C09941 implements AxisLabelFormatter {
        C09941() {
        }

        public String onFormat(Series series, double d) {
            return String.valueOf((int) d);
        }

        public String onFormat(Series series, long j) {
            return HttpVersions.HTTP_0_9;
        }
    }

    class C09952 implements AxisLabelFormatter {
        private SparseArrayCompat<String> cache = new SparseArrayCompat(50);

        C09952() {
        }

        public String onFormat(Series series, double d) {
            return HttpVersions.HTTP_0_9;
        }

        public String onFormat(Series series, long j) {
            int minX = (int) ((j - series.getMinX()) / 1000);
            String str = (String) this.cache.get(minX);
            if (str == null) {
                if (minX > 60) {
                    int i = minX / 60;
                    str = String.format(Locale.US, "%dm%ds", new Object[]{Integer.valueOf(i), Integer.valueOf(minX - (i * 60))});
                } else if (minX != 0) {
                    str = String.format(Locale.US, "%ds", new Object[]{Integer.valueOf(minX)});
                } else {
                    str = "0";
                }
                this.cache.put(minX, str);
            }
            return str;
        }
    }

    private class PrepareThumbsTask extends AsyncTask<Void, Void, List<Pair<Long, Drawable>>> {
        private VideoAtomRetriever atomRetriever = new VideoAtomRetriever();
        private SimpleDateFormat flightDateFormatter = new SimpleDateFormat("'media_'yyyyMMdd'_'HHmmss", Locale.US);
        private SimpleDateFormat photoDateFormatter = new SimpleDateFormat("'picture_'yyyyMMdd'_'HHmmss", Locale.US);
        private SimpleDateFormat videoDateFormatter = new SimpleDateFormat("'video_'yyyyMMdd'_'HHmmss", Locale.US);

        class C10001 implements Comparator<Pair<Long, Drawable>> {
            C10001() {
            }

            public int compare(Pair<Long, Drawable> pair, Pair<Long, Drawable> pair2) {
                return pair.first == pair2.first ? 1 : ((Long) pair.first).longValue() < ((Long) pair2.first).longValue() ? -1 : 1;
            }
        }

        private Long getFlightTimestamp(MediaVO mediaVO) throws ParseException {
            String atomData;
            String str;
            if (mediaVO.isVideo()) {
                atomData = this.atomRetriever.getAtomData(mediaVO.getPath(), AtomType.ARDT);
                if (atomData == null || atomData.indexOf(47) == -1) {
                    return null;
                }
                String[] split = atomData.split("[|/]");
                str = split[2];
                atomData = split[1];
                if (str == null || atomData == null) {
                    return null;
                }
                Date parse = this.flightDateFormatter.parse(atomData);
                Date parse2 = this.videoDateFormatter.parse(str);
                return Long.valueOf((parse2.getTime() - parse.getTime()) + AcademyGraphActivity.this.heightSeries.getMinX());
            }
            try {
                atomData = mediaVO.getPath();
                if (mediaVO.isRemote()) {
                    atomData = atomData.replace("kPhotoThumbSize", "w128");
                }
                atomData = new Exif2Interface(atomData).getAttribute(Tag.IMAGE_DESCRIPTION);
                if (atomData == null || atomData.lastIndexOf(47) == -1) {
                    Log.i(AcademyGraphActivity.TAG, "No image description tag found");
                    return null;
                }
                split = atomData.split(URIUtil.SLASH);
                str = split[1];
                atomData = split[0];
                if (str == null || atomData == null) {
                    return null;
                }
                parse2 = this.flightDateFormatter.parse(atomData);
                Date parse3 = this.photoDateFormatter.parse(str);
                return Long.valueOf((parse3.getTime() - parse2.getTime()) + AcademyGraphActivity.this.heightSeries.getMinX());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        protected List<Pair<Long, Drawable>> doInBackground(Void... voidArr) {
            List<Pair<Long, Drawable>> arrayList = new ArrayList();
            AcademyGraphActivity.this.mediaForFlight = AcademyGraphActivity.this.mediaProvider.getMediaList(MediaType.ALL);
            int size = AcademyGraphActivity.this.mediaForFlight.size();
            for (int i = 0; i < size; i++) {
                MediaVO mediaVO = (MediaVO) AcademyGraphActivity.this.mediaForFlight.get(i);
                try {
                    BitmapDrawable thumbnail = AcademyGraphActivity.this.mediaProvider.getThumbnail(mediaVO, ThumbSize.MICRO);
                    Long flightTimestamp = getFlightTimestamp(mediaVO);
                    if (flightTimestamp != null) {
                        arrayList.add(new Pair(flightTimestamp, thumbnail));
                    }
                } catch (ParseException e) {
                    Log.w(AcademyGraphActivity.TAG, "Unable to parse flight original filename " + mediaVO.getPath());
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            Collections.sort(arrayList, new C10001());
            return arrayList;
        }
    }

    class C09974 extends PrepareThumbsTask {
        C09974() {
            super();
        }

        protected void onPostExecute(List<Pair<Long, Drawable>> list) {
            AcademyGraphActivity.this.onGraphThumbnailsReady(list);
        }
    }

    class C09985 implements DialogInterface.OnClickListener {
        C09985() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            AcademyGraphActivity.this.finish();
        }
    }

    private void cancelDataRequest() {
        if (this.downloadGraphTask != null && this.downloadGraphTask.getStatus() != Status.FINISHED) {
            this.downloadGraphTask.cancel(true);
            this.downloadGraphTask = null;
        }
    }

    private Axis createAxis(Series series, Align align, String str) {
        Axis axis = new Axis(this.graph, series);
        axis.setTickStyle(TickStyle.TICK);
        axis.setTitle(str);
        axis.showLabels(true);
        axis.setAlignment(align);
        return axis;
    }

    private MediaThumbsOverlay createMediaThumbsOverlay(Series series, List<Pair<Long, Drawable>> list) {
        MediaThumbsOverlay mediaThumbsOverlay = new MediaThumbsOverlay(this.graph, series);
        mediaThumbsOverlay.setThumbs(list);
        mediaThumbsOverlay.setOnMediaThumbClickListener(this);
        return mediaThumbsOverlay;
    }

    private Series createSeries(List<Pair<Long, Double>> list, int i) {
        Series series = new Series(this.graph);
        series.setSortedPairs(list);
        series.setColor(i);
        series.setFillEnabled(false);
        return series;
    }

    private void extractParams(Intent intent) {
        if (intent.hasExtra(EXTRA_FLIGHT_OBJ)) {
            this.flight = (Flight) intent.getSerializableExtra(EXTRA_FLIGHT_OBJ);
        }
    }

    private void initActionBar() {
        setTitle((int) C0984R.string.aa_ID000001);
        ActionBar parrotActionBar = getParrotActionBar();
        parrotActionBar.initBackButton();
        if (this.flight.getUser().equals(AcademyUtils.profile.getUser())) {
            parrotActionBar.initShareButton(this);
        }
    }

    private void initBottomBar() {
        this.btnAltitude = (CheckedTextView) findViewById(C0984R.id.activity_academy_graph_checkedtext_altitude);
        this.btnSpeed = (CheckedTextView) findViewById(C0984R.id.activity_academy_graph_checkedtext_speed);
        this.btnBattery = (CheckedTextView) findViewById(C0984R.id.activity_academy_graph_checkedtext_battery);
        this.altitudeLayout = (RelativeLayout) findViewById(C0984R.id.activity_academy_graph_layout_altitude);
        this.speedLayout = (RelativeLayout) findViewById(C0984R.id.activity_academy_graph_layout_speed);
        this.batteryLayout = (RelativeLayout) findViewById(C0984R.id.activity_academy_graph_layout_battery);
        this.btnAltitude.setChecked(true);
        this.btnAltitude.setOnClickListener(this);
        this.btnSpeed.setOnClickListener(this);
        this.btnBattery.setOnClickListener(this);
        this.altitudeLayout.setOnClickListener(this);
        this.speedLayout.setOnClickListener(this);
        this.batteryLayout.setOnClickListener(this);
        if (this.btnAltitude.isChecked()) {
            this.checkedButtonsCount++;
        }
        if (this.btnSpeed.isChecked()) {
            this.checkedButtonsCount++;
        }
        if (this.btnBattery.isChecked()) {
            this.checkedButtonsCount++;
        }
    }

    private void initGraph() {
        this.graph = (GraphView) findViewById(C0984R.id.activity_academy_graph_graphview);
        this.graph.setZoomEnabled(true);
        setGraphVisibility(4);
        setProgressVisibility(0);
    }

    private void initGraphLabelFormatters() {
        this.formatterBattery = new C09941();
        this.formatterTime = new C09952();
    }

    private void initMediaProvider() {
        if (this.flight == null) {
            this.mediaProvider = new LocalMediaProvider(this);
        } else if (this.flight.getUser().equals(AcademyUtils.profile.getUser())) {
            this.mediaProvider = new LocalMediaProvider(this, this.flight);
        } else {
            this.mediaProvider = new RemoteMediaProvider(this, this.flight);
        }
    }

    private void onErrorReceivingGraphData() {
        new Builder(this).setMessage(C0984R.string.aa_ID000099).setPositiveButton(17039370, new C09985()).create().show();
    }

    private void onPrepareGraphThumbnails() {
        new C09974().execute(new Void[0]);
    }

    private void requestData() {
        this.downloadGraphTask = new DownloadFlightGraphTask(AcademyUtils.login, AcademyUtils.password) {
            protected void onPostExecute(List<FlightDataItem> list) {
                AcademyGraphActivity.this.setProgressVisibility(4);
                if (list == null || list.isEmpty()) {
                    AcademyGraphActivity.this.onErrorReceivingGraphData();
                    Log.w(AcademyGraphActivity.TAG, "No result for graph");
                    return;
                }
                AcademyGraphActivity.this.onGraphDataReady(list);
            }
        };
        this.downloadGraphTask.execute(new Object[]{this, this.flight});
    }

    private void restoreState(Bundle bundle) {
        if (bundle != null && bundle.containsKey(EXTRA_FLIGHT_OBJ)) {
            this.flight = (Flight) bundle.getSerializable(EXTRA_FLIGHT_OBJ);
        }
    }

    private void setGraphVisibility(int i) {
        this.graph.setVisibility(i);
    }

    private void setProgressVisibility(int i) {
        findViewById(C0984R.id.activity_academy_graph_progress).setVisibility(i);
    }

    public static void start(Context context, Flight flight) {
        if (flight == null) {
            throw new IllegalArgumentException("Flight can not be null");
        }
        Intent intent = new Intent(context, AcademyGraphActivity.class);
        intent.putExtra(EXTRA_FLIGHT_OBJ, flight);
        context.startActivity(intent);
    }

    protected void onCheckedTextCheckChanged(CheckedTextView checkedTextView) {
        switch (checkedTextView.getId()) {
            case C0984R.id.activity_academy_graph_checkedtext_altitude /*2131361991*/:
                if (this.heightSeries != null && this.heightAxis != null) {
                    this.heightSeries.setVisible(checkedTextView.isChecked());
                    this.heightAxis.setVisible(checkedTextView.isChecked());
                    this.graph.requestLayout();
                    this.graph.invalidate();
                    return;
                }
                return;
            case C0984R.id.activity_academy_graph_checkedtext_speed /*2131361993*/:
                if (this.speedSeries != null) {
                    this.speedSeries.setVisible(checkedTextView.isChecked());
                    this.speedAxis.setVisible(checkedTextView.isChecked());
                    this.graph.requestLayout();
                    this.graph.invalidate();
                    return;
                }
                return;
            case C0984R.id.activity_academy_graph_checkedtext_battery /*2131361995*/:
                if (this.batterySeries != null) {
                    this.batterySeries.setVisible(checkedTextView.isChecked());
                    this.batteryAxis.setVisible(checkedTextView.isChecked());
                    this.graph.requestLayout();
                    this.graph.invalidate();
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        CheckedTextView checkedTextView = null;
        switch (id) {
            case C0984R.id.activity_academy_graph_layout_altitude /*2131361990*/:
                id = C0984R.id.activity_academy_graph_checkedtext_altitude;
                checkedTextView = this.btnAltitude;
                break;
            case C0984R.id.activity_academy_graph_layout_speed /*2131361992*/:
                id = C0984R.id.activity_academy_graph_checkedtext_speed;
                checkedTextView = this.btnSpeed;
                break;
            case C0984R.id.activity_academy_graph_layout_battery /*2131361994*/:
                id = C0984R.id.activity_academy_graph_checkedtext_battery;
                checkedTextView = this.btnBattery;
                break;
        }
        switch (id) {
            case C0984R.id.activity_academy_graph_checkedtext_altitude /*2131361991*/:
            case C0984R.id.activity_academy_graph_checkedtext_speed /*2131361993*/:
            case C0984R.id.activity_academy_graph_checkedtext_battery /*2131361995*/:
                CheckedTextView checkedTextView2 = checkedTextView == null ? (CheckedTextView) view : checkedTextView;
                if (this.checkedButtonsCount > 1 || !checkedTextView2.isChecked()) {
                    if (checkedTextView2.isChecked()) {
                        this.checkedButtonsCount--;
                    } else {
                        this.checkedButtonsCount++;
                    }
                    checkedTextView2.setChecked(!checkedTextView2.isChecked());
                    onCheckedTextCheckChanged(checkedTextView2);
                    return;
                }
                return;
            default:
                onShareClicked();
                return;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.activity_academy_graph);
        extractParams(getIntent());
        restoreState(bundle);
        initMediaProvider();
        this.checkedButtonsCount = 0;
        initActionBar();
        initGraph();
        initBottomBar();
        requestData();
    }

    protected void onDestroy() {
        super.onDestroy();
        cancelDataRequest();
    }

    protected void onGraphDataReady(List<FlightDataItem> list) {
        onPrepareGraphThumbnails();
        setGraphVisibility(0);
        int size = list.size();
        List arrayList = new ArrayList(size);
        List arrayList2 = new ArrayList(size);
        List arrayList3 = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            FlightDataItem flightDataItem = (FlightDataItem) list.get(i);
            Long valueOf = Long.valueOf(flightDataItem.getTime());
            arrayList.add(new Pair(valueOf, Double.valueOf((double) flightDataItem.getAltitude())));
            arrayList2.add(new Pair(valueOf, Double.valueOf(flightDataItem.getSpeed())));
            arrayList3.add(new Pair(valueOf, Double.valueOf((double) flightDataItem.getBatteryLevel())));
        }
        this.heightSeries = createSeries(arrayList, getResources().getColor(C0984R.color.accent));
        this.speedSeries = createSeries(arrayList2, -256);
        this.batterySeries = createSeries(arrayList3, -65536);
        this.graph.addSeries(this.heightSeries);
        this.graph.addSeries(this.speedSeries);
        this.graph.addSeries(this.batterySeries);
        this.heightAxis = createAxis(this.heightSeries, Align.LEFT, getString(C0984R.string.aa_ID000023));
        this.speedAxis = createAxis(this.speedSeries, Align.LEFT, getString(C0984R.string.aa_ID000024));
        this.batteryAxis = createAxis(this.batterySeries, Align.RIGHT, "%");
        Axis createAxis = createAxis(this.heightSeries, Align.BOTTOM, getString(C0984R.string.aa_ID000025));
        initGraphLabelFormatters();
        this.batteryAxis.setAxisLabelFormatter(this.formatterBattery);
        createAxis.setAxisLabelFormatter(this.formatterTime);
        this.graph.addAxis(this.heightAxis);
        this.graph.addAxis(this.speedAxis);
        this.graph.addAxis(this.batteryAxis);
        this.graph.addAxis(createAxis);
        this.batterySeries.setVisible(false);
        this.speedSeries.setVisible(false);
        this.speedAxis.setVisible(false);
        this.batteryAxis.setVisible(false);
    }

    protected void onGraphThumbnailsReady(List<Pair<Long, Drawable>> list) {
        this.graph.setMediaOverlay(createMediaThumbsOverlay(this.heightSeries, list));
    }

    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (this.flight != null) {
            bundle.putSerializable(EXTRA_FLIGHT_OBJ, this.flight);
        }
    }

    protected void onShareClicked() {
        FileNotFoundException e;
        IOException e2;
        Throwable th;
        FileOutputStream fileOutputStream = null;
        Bitmap createBitmap = Bitmap.createBitmap(this.graph.getWidth(), this.graph.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawColor(-1);
        this.graph.draw(canvas);
        if (createBitmap == null) {
            Log.w(TAG, "Unable to create bitmap.");
            return;
        }
        FileOutputStream fileOutputStream2;
        try {
            if (FileUtils.isExtStorgAvailable()) {
                File file = new File(getExternalCacheDir(), "graph.png");
                if (file.exists()) {
                    file.delete();
                    file.createNewFile();
                } else {
                    file.createNewFile();
                }
                fileOutputStream2 = new FileOutputStream(file);
                try {
                    if (createBitmap.compress(CompressFormat.PNG, 90, fileOutputStream2)) {
                        fileOutputStream2.flush();
                        ShareUtils.sharePhoto(this, file.getAbsolutePath());
                    } else {
                        Log.w(TAG, "Unable to save bitmap");
                    }
                } catch (FileNotFoundException e3) {
                    e = e3;
                    try {
                        Log.w(TAG, "Unable to share graph as temp file could not be created");
                        e.printStackTrace();
                        if (fileOutputStream2 != null) {
                            try {
                                fileOutputStream2.close();
                            } catch (IOException e22) {
                                e22.printStackTrace();
                            }
                        }
                        if (createBitmap != null) {
                            createBitmap.recycle();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        fileOutputStream = fileOutputStream2;
                        fileOutputStream2 = fileOutputStream;
                        if (fileOutputStream2 != null) {
                            try {
                                fileOutputStream2.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                        }
                        if (createBitmap != null) {
                            createBitmap.recycle();
                        }
                        throw th;
                    }
                } catch (IOException e5) {
                    e22 = e5;
                    Log.w(TAG, "Unable to share graph due to IOException");
                    e22.printStackTrace();
                    if (fileOutputStream2 != null) {
                        try {
                            fileOutputStream2.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    if (createBitmap != null) {
                        createBitmap.recycle();
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (fileOutputStream2 != null) {
                        fileOutputStream2.close();
                    }
                    if (createBitmap != null) {
                        createBitmap.recycle();
                    }
                    throw th;
                }
            }
            Log.w(TAG, "Unable to share because external storage is not available");
            fileOutputStream2 = null;
            if (fileOutputStream2 != null) {
                try {
                    fileOutputStream2.close();
                } catch (IOException e2222) {
                    e2222.printStackTrace();
                }
            }
            if (createBitmap != null) {
                createBitmap.recycle();
            }
        } catch (FileNotFoundException e6) {
            e = e6;
            fileOutputStream2 = null;
            Log.w(TAG, "Unable to share graph as temp file could not be created");
            e.printStackTrace();
            if (fileOutputStream2 != null) {
                fileOutputStream2.close();
            }
            if (createBitmap != null) {
                createBitmap.recycle();
            }
        } catch (IOException e7) {
            e2222 = e7;
            fileOutputStream2 = null;
            Log.w(TAG, "Unable to share graph due to IOException");
            e2222.printStackTrace();
            if (fileOutputStream2 != null) {
                fileOutputStream2.close();
            }
            if (createBitmap != null) {
                createBitmap.recycle();
            }
        } catch (Throwable th4) {
            th = th4;
            fileOutputStream2 = fileOutputStream;
            if (fileOutputStream2 != null) {
                fileOutputStream2.close();
            }
            if (createBitmap != null) {
                createBitmap.recycle();
            }
            throw th;
        }
    }

    @SuppressLint({"NewApi"})
    public void onThumbClicked(final GraphView graphView, Drawable drawable, int i) {
        if (this.mediaForFlight != null) {
            Bundle bundle = null;
            if (VERSION.SDK_INT >= 16) {
                final Rect bounds = drawable.getBounds();
                bundle = ActivityOptions.makeThumbnailScaleUpAnimation(new View(this) {
                    public void getLocationOnScreen(int[] iArr) {
                        iArr[0] = bounds.left;
                        iArr[1] = graphView.getHeight() - bounds.top;
                    }
                }, ((BitmapDrawable) drawable).getBitmap(), bounds.width() / 2, Math.abs(bounds.height()) / 2).toBundle();
            }
            GalleryActivity.start(this, i, MediaType.ALL, this.flight, bundle);
        }
    }
}
