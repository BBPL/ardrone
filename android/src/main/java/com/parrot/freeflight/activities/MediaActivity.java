package com.parrot.freeflight.activities;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.LruCache;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.parrot.ardronetool.DataTracker;
import com.parrot.ardronetool.tracking.TRACK_KEY_ENUM;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.model.Flight;
import com.parrot.freeflight.academy.utils.AcademyUtils;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.media.LocalMediaProvider;
import com.parrot.freeflight.media.MediaProvider;
import com.parrot.freeflight.media.MediaProvider.MediaType;
import com.parrot.freeflight.media.MediaProvider.ThumbSize;
import com.parrot.freeflight.media.RemoteMediaProvider;
import com.parrot.freeflight.mediauploadservice.MediaShareService;
import com.parrot.freeflight.mediauploadservice.MediaShareService.LocalBinder;
import com.parrot.freeflight.mediauploadservice.broadcastreceivers.MediaShareStateChangedReceiver;
import com.parrot.freeflight.mediauploadservice.broadcastreceivers.MediaShareStateChangedReceiverDelegate;
import com.parrot.freeflight.receivers.DroneUsbStateChangedReceiver;
import com.parrot.freeflight.receivers.DroneUsbStateChangedReceiverDelegate;
import com.parrot.freeflight.receivers.MediaReadyDelegate;
import com.parrot.freeflight.receivers.MediaReadyReceiver;
import com.parrot.freeflight.receivers.MediaStorageReceiver;
import com.parrot.freeflight.receivers.MediaStorageReceiverDelegate;
import com.parrot.freeflight.receivers.NetworkChangeReceiverDelegate;
import com.parrot.freeflight.service.DroneControlService;
import com.parrot.freeflight.tasks.CheckDroneNetworkAvailabilityTask;
import com.parrot.freeflight.tasks.GetMediaObjectsListTask;
import com.parrot.freeflight.transcodeservice.TranscodingService;
import com.parrot.freeflight.ui.ActionBar;
import com.parrot.freeflight.vo.MediaVO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import org.mortbay.jetty.HttpVersions;

public class MediaActivity extends ParrotActivity implements OnClickListener, OnCheckedChangeListener, OnItemClickListener, MediaReadyDelegate, MediaStorageReceiverDelegate, ServiceConnection, DroneUsbStateChangedReceiverDelegate, MediaShareStateChangedReceiverDelegate, NetworkChangeReceiverDelegate {
    public static final String EXTRA_FLIGHT_OBJ = "flight";
    private static final String TAG = MediaActivity.class.getSimpleName();
    private Button btnSelectClear;
    private ActionBarState currentABState = ActionBarState.BROWSE;
    private MediaType currentFilter = MediaType.ALL;
    private DroneControlService droneControlService;
    private DroneUsbStateChangedReceiver droneUsbStateChangedReceiver;
    private Flight flight;
    private GridView gridView;
    private final ArrayList<MediaVO> mediaList = new ArrayList();
    private MediaProvider mediaProvider;
    private MediaReadyReceiver mediaReadyReceiver;
    private MediaShareService mediaShareService;
    private MediaShareServiceConnection mediaShareServiceConnection;
    private MediaStorageReceiver mediaStorageReceiver;
    private final ArrayList<MediaVO> selectedItems = new ArrayList();
    private MediaThumbnailExecutorManager thumbnailWorkerThread;
    private BroadcastReceiver uploadUpdateReceiver;

    class C10831 implements CompoundButton.OnCheckedChangeListener {
        C10831() {
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            View findViewById = MediaActivity.this.findViewById(C0984R.id.activity_media_screen_button_edit);
            if (z) {
                findViewById.startAnimation(AnimationUtils.makeInAnimation(MediaActivity.this, false));
                findViewById.setVisibility(0);
                return;
            }
            findViewById.startAnimation(AnimationUtils.makeOutAnimation(MediaActivity.this, true));
            findViewById.setVisibility(4);
        }
    }

    class C10853 implements DialogInterface.OnClickListener {
        C10853() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            MediaActivity.this.onDeleteSelectedMediaItems();
        }
    }

    class C10864 implements DialogInterface.OnClickListener {
        C10864() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.cancel();
        }
    }

    class C10875 extends CheckDroneNetworkAvailabilityTask {
        C10875() {
        }

        protected void onPostExecute(Boolean bool) {
            Button button = (Button) MediaActivity.this.findViewById(C0984R.id.activity_media_button_usbkey);
            if (bool.booleanValue()) {
                button.setEnabled(MediaActivity.this.droneControlService.isUSBInserted());
            } else {
                button.setEnabled(false);
            }
        }
    }

    private enum ActionBarState {
        BROWSE,
        EDIT
    }

    private interface OnThumbnailReadyListener {
        void onThumbnailReady(ViewHolder viewHolder, String str, Drawable drawable);
    }

    private class MediaAdapter extends BaseAdapter implements OnThumbnailReadyListener {
        private int cacheSize;
        private List<MediaVO> fileList;
        private MediaThumbnailExecutorManager getThumbnailWorkerTask;
        private final LayoutInflater inflater;
        private LruCache<String, Drawable> memoryCache;

        public MediaAdapter(Context context, List<MediaVO> list, MediaProvider mediaProvider, MediaThumbnailExecutorManager mediaThumbnailExecutorManager) {
            this.fileList = list;
            this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
            this.cacheSize = ((ActivityManager) context.getSystemService("activity")).getMemoryClass() * AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START;
            this.memoryCache = new LruCache<String, Drawable>(this.cacheSize, MediaActivity.this) {
                protected int sizeOf(String str, Drawable drawable) {
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    return bitmap.getHeight() * bitmap.getRowBytes();
                }
            };
            this.getThumbnailWorkerTask = mediaThumbnailExecutorManager;
            this.getThumbnailWorkerTask.setOnThumbnailReadyListener(this);
        }

        public int getCount() {
            return this.fileList.size();
        }

        public MediaVO getItem(int i) {
            return (MediaVO) this.fileList.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            MediaVO mediaVO = (MediaVO) this.fileList.get(i);
            if (view == null) {
                viewHolder = new ViewHolder();
                view = this.inflater.inflate(C0984R.layout.view_photosvideos_tableview_item, null);
                viewHolder.selectedHolder = (LinearLayout) view.findViewById(C0984R.id.selected_holder);
                viewHolder.imageView = (ImageView) view.findViewById(C0984R.id.image);
                viewHolder.videoIndicatorView = (ImageView) view.findViewById(C0984R.id.image_indicator);
                viewHolder.imageUploadedStatus = (ImageView) view.findViewById(C0984R.id.image_updated_status);
                viewHolder.progressBar = view.findViewById(C0984R.id.view_photosvideos_tableview_item_progress);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            if (mediaVO.isVideo()) {
                if (viewHolder.videoIndicatorView.getVisibility() == 4) {
                    viewHolder.videoIndicatorView.setVisibility(0);
                }
            } else if (viewHolder.videoIndicatorView.getVisibility() == 0) {
                viewHolder.videoIndicatorView.setVisibility(4);
            }
            if (mediaVO.isSelected()) {
                viewHolder.selectedHolder.setVisibility(0);
            } else {
                viewHolder.selectedHolder.setVisibility(4);
            }
            String key = mediaVO.getKey();
            Drawable drawable = (Drawable) this.memoryCache.get(key);
            if (drawable != null) {
                viewHolder.progressBar.setVisibility(4);
                if (viewHolder.imageView.getVisibility() != 0) {
                    viewHolder.imageView.setVisibility(0);
                }
                viewHolder.imageView.setImageDrawable(drawable);
            } else {
                viewHolder.imageView.setVisibility(4);
                viewHolder.progressBar.setVisibility(0);
                viewHolder.tag = key;
                try {
                    this.getThumbnailWorkerTask.execute(mediaVO, viewHolder);
                } catch (RejectedExecutionException e) {
                    Log.d(MediaActivity.TAG, "Get thumbnail task rejected");
                }
            }
            if (MediaActivity.this.mediaShareService == null || !MediaActivity.this.mediaShareService.mediaIsUploaded(mediaVO.getPath())) {
                viewHolder.imageUploadedStatus.setVisibility(4);
            } else {
                viewHolder.imageUploadedStatus.setVisibility(0);
            }
            return view;
        }

        public void onLowMemory() {
            Log.w(MediaActivity.TAG, "Low memory warning received");
            this.memoryCache.evictAll();
        }

        public void onThumbnailReady(ViewHolder viewHolder, String str, Drawable drawable) {
            if (viewHolder.tag.equals(str)) {
                viewHolder.progressBar.setVisibility(4);
                viewHolder.imageView.setImageDrawable(drawable);
                if (viewHolder.imageView.getVisibility() != 0) {
                    viewHolder.imageView.setVisibility(0);
                }
            }
            if (this.memoryCache.get(str) == null) {
                this.memoryCache.put(str, drawable);
            }
        }

        public void setFileList(List<MediaVO> list) {
            this.fileList = list;
            notifyDataSetChanged();
        }
    }

    private class MediaShareServiceConnection implements ServiceConnection {
        private final String TAG;

        private MediaShareServiceConnection() {
            this.TAG = MediaShareServiceConnection.class.getSimpleName();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(this.TAG, "Connected to Media Share Service");
            MediaActivity.this.mediaShareService = ((LocalBinder) iBinder).getService();
            MediaActivity.this.mediaShareService.resendBroadcasts();
            MediaAdapter mediaAdapter = (MediaAdapter) MediaActivity.this.gridView.getAdapter();
            if (mediaAdapter != null) {
                mediaAdapter.notifyDataSetChanged();
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            MediaActivity.this.mediaShareService = null;
        }
    }

    private class MediaThumbnailExecutorManager {
        private ExecutorService execture;
        private OnThumbnailReadyListener listener;
        private MediaProvider mediaProvider;

        class C10901 implements ThreadFactory {
            C10901() {
            }

            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setPriority(1);
                return thread;
            }
        }

        public MediaThumbnailExecutorManager(Context context, MediaProvider mediaProvider) {
            this.mediaProvider = mediaProvider;
        }

        private Runnable getThumbnailRunnable(final MediaVO mediaVO, final ViewHolder viewHolder) {
            return new Runnable() {
                public void run() {
                    try {
                        final BitmapDrawable thumbnail = MediaThumbnailExecutorManager.this.mediaProvider.getThumbnail(mediaVO, ThumbSize.MICRO);
                        MediaActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                MediaThumbnailExecutorManager.this.onThumbnailReady(viewHolder, mediaVO.getKey(), thumbnail);
                            }
                        });
                    } catch (IOException e) {
                        Log.w("ThumbnailWorker", "Can't load thumbnail for file " + mediaVO.getPath());
                    }
                }
            };
        }

        private void onThumbnailReady(ViewHolder viewHolder, String str, Drawable drawable) {
            if (this.listener != null) {
                this.listener.onThumbnailReady(viewHolder, str, drawable);
            }
        }

        public final void execute(MediaVO mediaVO, ViewHolder viewHolder) {
            this.execture.execute(getThumbnailRunnable(mediaVO, viewHolder));
        }

        final boolean isTerminated() {
            return this.execture.isTerminated();
        }

        public final void setOnThumbnailReadyListener(OnThumbnailReadyListener onThumbnailReadyListener) {
            this.listener = onThumbnailReadyListener;
        }

        public final void start() {
            this.execture = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new C10901());
        }

        public final void stop() {
            this.mediaProvider.abortAnyOperations();
            this.execture.shutdownNow();
        }
    }

    private static final class ViewHolder {
        ImageView imageUploadedStatus;
        ImageView imageView;
        View progressBar;
        LinearLayout selectedHolder;
        String tag;
        ImageView videoIndicatorView;

        private ViewHolder() {
        }
    }

    private void initActionBar() {
        setTitle(getString(C0984R.string.ff_ID000053));
        ActionBar parrotActionBar = getParrotActionBar();
        if (this.flight == null) {
            parrotActionBar.initHomeButton();
        } else {
            parrotActionBar.initBackButton();
        }
        if (this.flight == null || this.flight.getUser().equals(AcademyUtils.profile.getUser())) {
            parrotActionBar.initPlusToggleButton(new C10831());
        }
    }

    private void initBottomBar(ActionBarState actionBarState) {
        LinearLayout linearLayout = (LinearLayout) findViewById(C0984R.id.llayBrowseBar);
        LinearLayout linearLayout2 = (LinearLayout) findViewById(C0984R.id.llayEditBar);
        switch (actionBarState) {
            case BROWSE:
                linearLayout.setVisibility(0);
                linearLayout2.setVisibility(8);
                return;
            case EDIT:
                linearLayout.setVisibility(8);
                linearLayout2.setVisibility(0);
                return;
            default:
                return;
        }
    }

    private void initBroadcastReceivers() {
        this.droneUsbStateChangedReceiver = new DroneUsbStateChangedReceiver(this);
        this.uploadUpdateReceiver = new MediaShareStateChangedReceiver(this);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(this.uploadUpdateReceiver, MediaShareStateChangedReceiver.createFilter());
        Log.d(TAG, "Receiver registered");
    }

    private void initGalleryGrid() {
        int integer = getResources().getInteger(C0984R.integer.media_gallery_columns_count);
        this.gridView = (GridView) findViewById(C0984R.id.grid);
        this.gridView.setNumColumns(integer);
        this.gridView.setOnItemClickListener(this);
    }

    private void initListeners() {
        ((RadioGroup) findViewById(C0984R.id.filter)).setOnCheckedChangeListener(this);
        Button button = (Button) findViewById(C0984R.id.activity_media_screen_button_edit);
        button.setOnClickListener(this);
        button.setVisibility(8);
        ((Button) findViewById(C0984R.id.btnCancel)).setOnClickListener(this);
        ((Button) findViewById(C0984R.id.btnDelete)).setOnClickListener(this);
        this.btnSelectClear = (Button) findViewById(C0984R.id.btnSelectClear);
        this.btnSelectClear.setOnClickListener(this);
        ((Button) findViewById(C0984R.id.activity_media_button_usbkey)).setOnClickListener(this);
    }

    private void onDeleteSelectedMediaItems() {
        int size = this.selectedItems.size();
        if (this.mediaProvider instanceof LocalMediaProvider) {
            ((LocalMediaProvider) this.mediaProvider).deleteMedia(this.selectedItems);
        }
        this.mediaList.removeAll(this.selectedItems);
        this.selectedItems.clear();
        if (size > 0) {
            switchEditBarToInitialState();
        }
    }

    private void onUsbKeyClicked() {
        startActivityForResult(new Intent(this, UsbManagerActivity.class), 101);
    }

    private void refreshUsbKeyButtonState() {
        new C10875().execute(new Context[]{this});
    }

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Flight flight) {
        Intent intent = new Intent(context, MediaActivity.class);
        if (flight != null) {
            intent.putExtra("flight", flight);
        }
        context.startActivity(intent);
    }

    private void switchAllItemState(boolean z) {
        if (z) {
            this.selectedItems.addAll(this.mediaList);
        } else {
            this.selectedItems.clear();
        }
        int size = this.mediaList.size();
        for (int i = 0; i < size; i++) {
            ((MediaVO) this.mediaList.get(i)).setSelected(z);
        }
    }

    private void switchEditBarToInitialState() {
        this.btnSelectClear.setText(getString(C0984R.string.aa_ID000032));
        switchAllItemState(false);
        MediaAdapter mediaAdapter = (MediaAdapter) this.gridView.getAdapter();
        if (mediaAdapter != null) {
            mediaAdapter.notifyDataSetChanged();
        }
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i == 101) {
            onApplyMediaFilter(this.currentFilter);
        } else {
            super.onActivityResult(i, i2, intent);
        }
    }

    @SuppressLint({"NewApi"})
    protected void onApplyMediaFilter(MediaType mediaType) {
        synchronized (this) {
            GetMediaObjectsListTask c10842 = new GetMediaObjectsListTask(this, this.mediaProvider, mediaType) {
                protected void onPostExecute(List<MediaVO> list) {
                    MediaActivity.this.mediaList.clear();
                    MediaActivity.this.mediaList.addAll(list);
                    MediaAdapter mediaAdapter = (MediaAdapter) MediaActivity.this.gridView.getAdapter();
                    if (mediaAdapter == null) {
                        MediaActivity.this.gridView.setAdapter(new MediaAdapter(MediaActivity.this, MediaActivity.this.mediaList, MediaActivity.this.mediaProvider, MediaActivity.this.thumbnailWorkerThread));
                        return;
                    }
                    mediaAdapter.setFileList(MediaActivity.this.mediaList);
                }
            };
            try {
                if (VERSION.SDK_INT < 11) {
                    c10842.execute(new Object[0]).get();
                } else {
                    c10842.executeOnExecutor(GetMediaObjectsListTask.THREAD_POOL_EXECUTOR, new Object[0]).get();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void onAutoUploadPermissionChanged() {
    }

    protected void onCancelEditClicked() {
        switchEditBarToInitialState();
        this.currentABState = ActionBarState.BROWSE;
    }

    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case C0984R.id.rbtn_images /*2131362202*/:
                this.currentFilter = MediaType.PHOTOS;
                break;
            case C0984R.id.rbtn_videos /*2131362203*/:
                this.currentFilter = MediaType.VIDEOS;
                break;
            default:
                this.currentFilter = MediaType.ALL;
                break;
        }
        onApplyMediaFilter(this.currentFilter);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0984R.id.btnSelectClear /*2131362197*/:
                onSelectAllClicked();
                break;
            case C0984R.id.btnDelete /*2131362198*/:
                onDeleteMediaClicked();
                break;
            case C0984R.id.btnCancel /*2131362199*/:
                onCancelEditClicked();
                break;
            case C0984R.id.activity_media_button_usbkey /*2131362205*/:
                onUsbKeyClicked();
                break;
            case C0984R.id.activity_media_screen_button_edit /*2131362207*/:
                onEditClicked();
                break;
        }
        initBottomBar(this.currentABState);
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.media_screen);
        this.mediaReadyReceiver = new MediaReadyReceiver(this);
        this.mediaStorageReceiver = new MediaStorageReceiver(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.flight = (Flight) extras.getSerializable("flight");
        }
        if (this.flight == null) {
            this.mediaProvider = new LocalMediaProvider(this);
        } else if (this.flight.getUser().equals(AcademyUtils.profile.getUser())) {
            this.mediaProvider = new LocalMediaProvider(this, this.flight);
        } else {
            this.mediaProvider = new RemoteMediaProvider(this, this.flight);
        }
        if (bindService(new Intent(this, DroneControlService.class), this, 1)) {
            Log.w(TAG, "Unable to bind to DroneControlService");
            this.mediaShareServiceConnection = new MediaShareServiceConnection();
        } else {
            Log.w(TAG, "Unable to bind to DroneControlService");
            this.mediaShareServiceConnection = new MediaShareServiceConnection();
        }
        if (!bindService(new Intent(this, MediaShareService.class), this.mediaShareServiceConnection, 1)) {
            Log.w(TAG, "Unable to bind to MediaShareService");
        }
        initListeners();
        initBroadcastReceivers();
        initActionBar();
        initBottomBar(this.currentABState);
        initGalleryGrid();
        this.thumbnailWorkerThread = new MediaThumbnailExecutorManager(this, this.mediaProvider);
        this.thumbnailWorkerThread.start();
        onApplyMediaFilter(this.currentFilter);
        DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__MEDIAS_ZONE_OPEN);
    }

    protected void onDeleteMediaClicked() {
        if (this.selectedItems.size() > 0) {
            Builder builder = new Builder(this);
            builder.setTitle(HttpVersions.HTTP_0_9);
            builder.setMessage(C0984R.string.delete_popup);
            builder.setPositiveButton(getString(C0984R.string.delete_media), new C10853());
            builder.setNegativeButton(getString(17039360), new C10864());
            builder.show();
        }
    }

    protected void onDestroy() {
        DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__MEDIAS_ZONE_CLOSE);
        super.onDestroy();
        this.thumbnailWorkerThread.stop();
        unbindService(this);
        unbindService(this.mediaShareServiceConnection);
    }

    public void onDroneUsbStateChanged(boolean z) {
        ((Button) findViewById(C0984R.id.activity_media_button_usbkey)).setEnabled(z);
    }

    protected void onEditClicked() {
        this.currentABState = ActionBarState.EDIT;
        ((CompoundButton) findViewById(C0984R.id.action_bar_togglebutton_plus)).setChecked(false);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        switch (this.currentABState) {
            case BROWSE:
                onPlayMediaItem(view, i);
                return;
            case EDIT:
                LinearLayout linearLayout = (LinearLayout) view.findViewById(C0984R.id.selected_holder);
                MediaVO mediaVO;
                if (linearLayout.isShown()) {
                    linearLayout.setVisibility(4);
                    mediaVO = (MediaVO) this.mediaList.get(i);
                    mediaVO.setSelected(false);
                    this.selectedItems.remove(mediaVO);
                    return;
                }
                linearLayout.setVisibility(0);
                mediaVO = (MediaVO) this.mediaList.get(i);
                if (!mediaVO.isRemote()) {
                    mediaVO.setSelected(true);
                    this.selectedItems.add(mediaVO);
                    this.btnSelectClear.setText(getString(C0984R.string.aa_ID000042));
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        Log.w(TAG, "Low memory warning received. Trying to cleanum cacne");
        ((MediaAdapter) this.gridView.getAdapter()).onLowMemory();
    }

    public void onMediaEject() {
        this.mediaProvider.abortAnyOperations();
        finish();
    }

    public void onMediaReady(File file) {
        Log.d(TAG, "New file available " + file.getAbsolutePath());
        onApplyMediaFilter(this.currentFilter);
    }

    public void onMediaShareStateChanged(MediaVO mediaVO, boolean z, double d) {
        if (d == 1.0d) {
            MediaAdapter mediaAdapter = (MediaAdapter) this.gridView.getAdapter();
            if (mediaAdapter != null) {
                mediaAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onMediaStorageMounted() {
    }

    public void onMediaStorageUnmounted() {
    }

    public void onNetworkChanged(NetworkInfo networkInfo) {
        refreshUsbKeyButtonState();
    }

    protected void onPause() {
        super.onPause();
        this.mediaStorageReceiver.unregisterFromEvents(this);
        if (this.droneControlService != null) {
            this.droneControlService.pause();
        }
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(this.droneUsbStateChangedReceiver);
    }

    @SuppressLint({"NewApi"})
    protected void onPlayMediaItem(View view, int i) {
        Bundle toBundle;
        if (VERSION.SDK_INT >= 16) {
            Bitmap bitmap;
            ImageView imageView = (ImageView) view.findViewById(C0984R.id.image);
            if (imageView != null) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                if (bitmapDrawable != null) {
                    bitmap = bitmapDrawable.getBitmap();
                    if (!(imageView == null || bitmap == null)) {
                        toBundle = ActivityOptions.makeThumbnailScaleUpAnimation(imageView, bitmap, 0, 0).toBundle();
                        GalleryActivity.start(this, i, this.currentFilter, this.flight, toBundle);
                    }
                }
            }
            bitmap = null;
            toBundle = ActivityOptions.makeThumbnailScaleUpAnimation(imageView, bitmap, 0, 0).toBundle();
            GalleryActivity.start(this, i, this.currentFilter, this.flight, toBundle);
        }
        toBundle = null;
        GalleryActivity.start(this, i, this.currentFilter, this.flight, toBundle);
    }

    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(this.droneUsbStateChangedReceiver, DroneUsbStateChangedReceiver.createFilter());
        this.mediaStorageReceiver.registerForEvents(this);
        if (this.droneControlService != null) {
            this.droneControlService.resume();
            refreshUsbKeyButtonState();
        }
    }

    protected void onSelectAllClicked() {
        if (this.selectedItems.size() > 0) {
            this.btnSelectClear.setText(getString(C0984R.string.aa_ID000032));
            switchAllItemState(false);
        } else {
            this.btnSelectClear.setText(getString(C0984R.string.aa_ID000042));
            switchAllItemState(true);
        }
        ((MediaAdapter) this.gridView.getAdapter()).notifyDataSetChanged();
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.droneControlService = ((DroneControlService.LocalBinder) iBinder).getService();
        this.droneControlService.resume();
        refreshUsbKeyButtonState();
    }

    public void onServiceDisconnected(ComponentName componentName) {
    }

    protected void onStart() {
        super.onStart();
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(getApplicationContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DroneControlService.ACTION_NEW_MEDIA_IS_AVAILABLE);
        intentFilter.addAction(TranscodingService.NEW_MEDIA_IS_AVAILABLE_ACTION);
        instance.registerReceiver(this.mediaReadyReceiver, intentFilter);
        if (this.thumbnailWorkerThread.isTerminated()) {
            this.thumbnailWorkerThread.start();
            ((MediaAdapter) this.gridView.getAdapter()).notifyDataSetChanged();
        }
    }

    protected void onStop() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(this.mediaReadyReceiver);
        if (!this.thumbnailWorkerThread.isTerminated()) {
            this.thumbnailWorkerThread.stop();
        }
        super.onStop();
    }
}
