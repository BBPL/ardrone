package com.parrot.freeflight.activities;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask.Status;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.youtube.YouTubeScopes;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.FreeFlightApplication;
import com.parrot.freeflight.academy.model.Flight;
import com.parrot.freeflight.academy.utils.AcademyUtils;
import com.parrot.freeflight.activities.base.WatchMediaActivity;
import com.parrot.freeflight.media.LocalMediaProvider;
import com.parrot.freeflight.media.MediaProvider;
import com.parrot.freeflight.media.MediaProvider.MediaType;
import com.parrot.freeflight.media.MediaProvider.ThumbSize;
import com.parrot.freeflight.media.RemoteMediaProvider;
import com.parrot.freeflight.settings.AccountChecker;
import com.parrot.freeflight.settings.ApplicationSettings;
import com.parrot.freeflight.tasks.GetMediaObjectsListTask;
import com.parrot.freeflight.ui.ActionBar;
import com.parrot.freeflight.ui.ActionBar.Background;
import com.parrot.freeflight.ui.adapters.GalleryAdapterDelegate;
import com.parrot.freeflight.utils.SystemUtils;
import com.parrot.freeflight.vo.MediaVO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class GalleryActivity extends WatchMediaActivity implements OnPageChangeListener, GalleryAdapterDelegate {
    public static String EXTRA_FLIGHT_OBJ = "flight";
    public static String EXTRA_MEDIA_FILTER = "MEDIA_FILTER";
    public static String EXTRA_SELECTED_ELEMENT = "SELECTED_ELEMENT";
    private static final int REQUEST_ACCOUNT_PICKER = 1;
    private static final int REQUEST_UPDATE_GOOGLEPLAYSERVICES = 2;
    public static final String TAG = GalleryActivity.class.getSimpleName();
    private ActionBar actionBar;
    private GalleryAdapter adapter;
    private int currentItem;
    private ExecutorService executorService;
    private Flight flight;
    private String googleAccountName;
    private GoogleAccountCredential googleCredential;
    private GetMediaObjectsListTask initMediaTask;
    private final List<MediaVO> mediaList = new ArrayList();
    private MediaProvider mediaProvider;
    private ApplicationSettings settings;

    class C10702 implements OnClickListener {
        C10702() {
        }

        public void onClick(View view) {
            GalleryActivity.this.overridePendingTransition(0, 0);
            GalleryActivity.this.finish();
        }
    }

    public class GalleryAdapter extends PagerAdapter {
        private final Context context;
        private GalleryAdapterDelegate delegate;
        private final LayoutInflater inflater;
        private final List<MediaVO> mediaList;
        private MediaProvider mediaProvider;
        private LruCache<String, Drawable> memCache;
        private boolean sync;

        public GalleryAdapter(GalleryActivity galleryActivity, Context context, MediaProvider mediaProvider, List<MediaVO> list, GalleryAdapterDelegate galleryAdapterDelegate) {
            this(context, mediaProvider, list, galleryAdapterDelegate, false);
        }

        public GalleryAdapter(Context context, MediaProvider mediaProvider, List<MediaVO> list, GalleryAdapterDelegate galleryAdapterDelegate, boolean z) {
            this.sync = z;
            this.mediaList = list;
            this.context = context;
            this.delegate = galleryAdapterDelegate;
            this.mediaProvider = mediaProvider;
            this.inflater = LayoutInflater.from(this.context);
            initMemCache();
        }

        private RelativeLayout addImage(MediaVO mediaVO) {
            RelativeLayout relativeLayout = (RelativeLayout) this.inflater.inflate(C0984R.layout.item_video, null);
            relativeLayout.setPadding(0, 0, 0, 0);
            relativeLayout.setDrawingCacheEnabled(true);
            relativeLayout.setWillNotDraw(true);
            ImageView imageView = (ImageView) relativeLayout.findViewById(C0984R.id.image);
            imageView.setDrawingCacheEnabled(true);
            ImageView imageView2 = (ImageView) relativeLayout.findViewById(C0984R.id.btn_play);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            ProgressBar progressBar = (ProgressBar) relativeLayout.findViewById(C0984R.id.progressBar);
            progressBar.setVisibility(0);
            imageView2.setVisibility(4);
            if (this.sync) {
                createLoadThumbnailRunnable(mediaVO, progressBar, imageView).run();
            } else {
                try {
                    GalleryActivity.this.executorService.execute(createLoadThumbnailRunnable(mediaVO, progressBar, imageView));
                } catch (RejectedExecutionException e) {
                    Log.d(GalleryActivity.TAG, "Skipped load of video thumbnail");
                }
            }
            return relativeLayout;
        }

        private RelativeLayout addVideo(MediaVO mediaVO, int i) {
            final RelativeLayout relativeLayout = (RelativeLayout) this.inflater.inflate(C0984R.layout.item_video, null);
            relativeLayout.setPadding(0, 0, 0, 0);
            relativeLayout.setDrawingCacheEnabled(true);
            relativeLayout.setWillNotDraw(true);
            ImageView imageView = (ImageView) relativeLayout.findViewById(C0984R.id.image);
            imageView.setDrawingCacheEnabled(true);
            ((ImageView) relativeLayout.findViewById(C0984R.id.btn_play)).setDrawingCacheEnabled(true);
            ProgressBar progressBar = (ProgressBar) relativeLayout.findViewById(C0984R.id.progressBar);
            progressBar.setVisibility(8);
            relativeLayout.setTag(mediaVO);
            imageView.setClickable(true);
            imageView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    GalleryAdapter.this.onPlayButtonClicked(relativeLayout);
                }
            });
            if (this.sync) {
                createLoadThumbnailRunnable(mediaVO, progressBar, imageView).run();
            } else {
                try {
                    GalleryActivity.this.executorService.execute(createLoadThumbnailRunnable(mediaVO, progressBar, imageView));
                } catch (RejectedExecutionException e) {
                    Log.d(GalleryActivity.TAG, "Skipped load of video thumbnail");
                }
            }
            return relativeLayout;
        }

        private Runnable createLoadThumbnailRunnable(final MediaVO mediaVO, final ProgressBar progressBar, final ImageView imageView) {
            return new Runnable() {
                public void run() {
                    try {
                        Drawable drawable = (Drawable) GalleryAdapter.this.memCache.get(mediaVO.getPath());
                        if (drawable != null) {
                            GalleryAdapter.this.onThumbnailReady(drawable, imageView, progressBar);
                            return;
                        }
                        drawable = GalleryAdapter.this.mediaProvider.getThumbnail(mediaVO, ThumbSize.MINI);
                        if (drawable != null) {
                            GalleryAdapter.this.memCache.put(mediaVO.getPath(), drawable);
                            GalleryAdapter.this.onThumbnailReady(drawable, imageView, progressBar);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
        }

        private void initMemCache() {
            this.memCache = new LruCache<String, Drawable>(((ActivityManager) this.context.getSystemService("activity")).getMemoryClass() * AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START) {
                protected int sizeOf(String str, Drawable drawable) {
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    return bitmap.getHeight() * bitmap.getRowBytes();
                }
            };
        }

        private void onThumbnailReady(final Drawable drawable, final ImageView imageView, final ProgressBar progressBar) {
            GalleryActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    if (progressBar != null) {
                        progressBar.setVisibility(8);
                    }
                    imageView.setImageDrawable(drawable);
                }
            });
        }

        public void destroyItem(View view, int i, Object obj) {
            ((ViewPager) view).removeView((RelativeLayout) obj);
        }

        public int getCount() {
            return this.mediaList.size();
        }

        public Object instantiateItem(View view, int i) {
            try {
                MediaVO mediaVO = (MediaVO) this.mediaList.get(i);
                Object addVideo = mediaVO.isVideo() ? addVideo(mediaVO, i) : addImage(mediaVO);
                ((ViewPager) view).addView(addVideo, 0);
                return addVideo;
            } finally {
                this.delegate.onGalleryReady();
            }
        }

        public boolean isViewFromObject(View view, Object obj) {
            return view == ((RelativeLayout) obj);
        }

        public void onLowMemory() {
            this.memCache.evictAll();
        }

        protected void onPlayButtonClicked(ViewGroup viewGroup) {
            if (this.delegate != null) {
                this.delegate.onPlayButtonClicked();
            } else {
                Log.w(GalleryActivity.TAG, "Play button clicked but delegate not set");
            }
        }

        public void setThumbsLoadMode(ThumbLoadMode thumbLoadMode) {
            switch (thumbLoadMode) {
                case SYNC:
                    this.sync = true;
                    return;
                case ASYNC:
                    this.sync = false;
                    return;
                default:
                    return;
            }
        }
    }

    public enum ThumbLoadMode {
        SYNC,
        ASYNC
    }

    private void hideOrShowShareButton() {
        MediaVO mediaVO = (MediaVO) this.mediaList.get(this.currentItem);
        if (SystemUtils.isNook() || mediaVO == null || mediaVO.isRemote()) {
            this.actionBar.hideShareButton();
        } else {
            this.actionBar.initShareButton(this);
        }
    }

    private void initGoogleAccount() {
        this.googleCredential = GoogleAccountCredential.usingOAuth2(this, YouTubeScopes.YOUTUBE_READONLY, YouTubeScopes.YOUTUBE_UPLOAD);
    }

    private void initMediaTask(MediaType mediaType, int i) {
        if (this.initMediaTask == null || !(this.initMediaTask == null || this.initMediaTask.getStatus() == Status.RUNNING)) {
            final int i2 = i;
            this.initMediaTask = new GetMediaObjectsListTask(this, this.mediaProvider, mediaType) {
                protected void onPostExecute(List<MediaVO> list) {
                    GalleryActivity.this.onMediaScanCompleted(i2, list);
                }
            };
            try {
                this.initMediaTask.execute(new Object[0]).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e2) {
                e2.printStackTrace();
            }
        }
    }

    private void initView(int i) {
        setTitle((i + 1) + " " + getString(C0984R.string.of) + " " + this.mediaList.size());
        ViewPager viewPager = (ViewPager) findViewById(C0984R.id.gallery);
        this.adapter = new GalleryAdapter(this, this, this.mediaProvider, this.mediaList, this);
        this.adapter.setThumbsLoadMode(this.mediaProvider instanceof RemoteMediaProvider ? ThumbLoadMode.ASYNC : ThumbLoadMode.SYNC);
        viewPager.setAdapter(this.adapter);
        viewPager.setCurrentItem(i, false);
        viewPager.setOnPageChangeListener(this);
        this.currentItem = i;
        hideOrShowShareButton();
    }

    private void onChooseGoogleAccount() {
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (isGooglePlayServicesAvailable == 0) {
            startActivityForResult(this.googleCredential.newChooseAccountIntent(), 1);
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isGooglePlayServicesAvailable)) {
            GooglePlayServicesUtil.getErrorDialog(isGooglePlayServicesAvailable, this, 2).show();
        }
    }

    private void setGoogleAccountName(String str) {
        this.settings.setGoogleAccountName(str);
        this.googleAccountName = str;
        this.googleCredential.setSelectedAccountName(str);
    }

    public static void start(Context context, int i, MediaType mediaType, Flight flight) {
        start(context, i, mediaType, flight, null);
    }

    @SuppressLint({"NewApi"})
    public static void start(Context context, int i, MediaType mediaType, Flight flight, Bundle bundle) {
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.putExtra(EXTRA_SELECTED_ELEMENT, i);
        intent.putExtra(EXTRA_MEDIA_FILTER, mediaType.ordinal());
        if (flight != null) {
            intent.putExtra(EXTRA_FLIGHT_OBJ, flight);
        }
        if (VERSION.SDK_INT >= 16) {
            context.startActivity(intent, bundle);
        } else {
            context.startActivity(intent);
        }
    }

    protected void initActionBar() {
        String string = this.extras.getString(WatchMediaActivity.EXTRA_TITLE);
        this.actionBar = getParrotActionBar();
        this.actionBar.initBackButton(new C10702());
        this.actionBar.changeBackground(Background.ACCENT_HALF_TRANSP);
        if (!SystemUtils.isNook()) {
            this.actionBar.initShareButton(this);
        }
        if (string != null) {
            this.actionBar.setTitle(string);
        }
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        MediaVO mediaVO = (MediaVO) this.mediaList.get(this.currentItem);
        switch (i) {
            case 1:
                if (i2 == -1 && intent != null && intent.getExtras() != null) {
                    String stringExtra = intent.getStringExtra("authAccount");
                    if (stringExtra != null) {
                        setGoogleAccountName(stringExtra);
                        if (mediaVO.isVideo()) {
                            VideoUploadActivity.start(this, mediaVO);
                            return;
                        } else {
                            PhotoUploadActivity.start(this, mediaVO);
                            return;
                        }
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.gallery_screen);
        this.executorService = Executors.newSingleThreadExecutor();
        Intent intent = getIntent();
        if (bundle != null) {
            this.currentItem = bundle.getInt(EXTRA_SELECTED_ELEMENT, 0);
        } else if (this.currentItem == 0) {
            this.currentItem = intent.getIntExtra(EXTRA_SELECTED_ELEMENT, 0);
        }
        if (intent.hasExtra(EXTRA_FLIGHT_OBJ)) {
            this.flight = (Flight) intent.getSerializableExtra(EXTRA_FLIGHT_OBJ);
        }
        if (this.flight == null) {
            this.mediaProvider = new LocalMediaProvider(this);
        } else if (AcademyUtils.profile.getUser().equals(this.flight.getUser())) {
            this.mediaProvider = new LocalMediaProvider(this, this.flight);
        } else {
            this.mediaProvider = new RemoteMediaProvider(this, this.flight);
        }
        initMediaTask(MediaType.values()[intent.getIntExtra(EXTRA_MEDIA_FILTER, MediaType.ALL.ordinal())], this.currentItem);
        this.settings = ((FreeFlightApplication) getApplication()).getAppSettings();
        this.googleAccountName = AccountChecker.getGoogleAccountName(this.settings, getApplicationContext());
        initGoogleAccount();
        setGoogleAccountName(this.googleAccountName);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.initMediaTask != null && this.initMediaTask.getStatus() == Status.RUNNING) {
            this.initMediaTask.cancel(false);
        }
        this.mediaProvider.abortAnyOperations();
        this.executorService.shutdownNow();
    }

    public void onGalleryReady() {
        onPageSelected(this.currentItem);
        this.adapter.setThumbsLoadMode(ThumbLoadMode.ASYNC);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() != 1) {
            return super.onKeyUp(i, keyEvent);
        }
        switch (i) {
            case 126:
                onPlayButtonClicked();
                return true;
            default:
                return super.onKeyUp(i, keyEvent);
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        this.adapter.onLowMemory();
    }

    protected void onMediaScanCompleted(int i, List<MediaVO> list) {
        this.mediaList.clear();
        this.mediaList.addAll(list);
        initView(i);
        this.initMediaTask = null;
    }

    public void onPageScrollStateChanged(int i) {
    }

    public void onPageScrolled(int i, float f, int i2) {
    }

    public void onPageSelected(int i) {
        this.currentItem = i;
        setTitle((i + 1) + " " + getString(C0984R.string.of) + " " + this.mediaList.size());
        hideOrShowShareButton();
    }

    public void onPlayButtonClicked() {
        MediaVO mediaVO = (MediaVO) this.mediaList.get(this.currentItem);
        String title = getParrotActionBar().getTitle();
        if (!mediaVO.isVideo()) {
            return;
        }
        if (mediaVO.isRemote()) {
            WatchYoutubeVideoLegacyActivity.start(this, mediaVO, title);
        } else {
            WatchVideoActivity.start((Context) this, mediaVO, "video/mp4", title, this.flight);
        }
    }

    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(EXTRA_SELECTED_ELEMENT, ((ViewPager) findViewById(C0984R.id.gallery)).getCurrentItem());
    }

    protected void onShareClicked() {
        MediaVO mediaVO = (MediaVO) this.mediaList.get(this.currentItem);
        if (mediaVO.isRemote()) {
            Toast.makeText(this, "Can't share remote media", 0).show();
        } else if (mediaVO.isVideo()) {
            if (this.googleAccountName == null) {
                onChooseGoogleAccount();
            } else {
                VideoUploadActivity.start(this, mediaVO);
            }
        } else if (this.googleAccountName == null) {
            onChooseGoogleAccount();
        } else {
            PhotoUploadActivity.start(this, mediaVO);
        }
    }
}
